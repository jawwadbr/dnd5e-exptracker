package com.jawbr.dnd5e.exptracker.service;

import com.jawbr.dnd5e.exptracker.dto.mapper.UserDTOMapper;
import com.jawbr.dnd5e.exptracker.dto.request.UserRequestDTO;
import com.jawbr.dnd5e.exptracker.dto.response.UserCampaignsDTO;
import com.jawbr.dnd5e.exptracker.dto.response.UserCreationDTO;
import com.jawbr.dnd5e.exptracker.dto.response.UserDTO;
import com.jawbr.dnd5e.exptracker.entity.User;
import com.jawbr.dnd5e.exptracker.exception.CampaignNotFoundException;
import com.jawbr.dnd5e.exptracker.exception.IntegrityConstraintViolationException;
import com.jawbr.dnd5e.exptracker.repository.UserRepository;
import com.jawbr.dnd5e.exptracker.util.UserRole;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final CurrentAuthUserService currentAuthUser;
    private final UserDTOMapper userDTOMapper;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,
                       CurrentAuthUserService currentAuthUser,
                       UserDTOMapper userDTOMapper,
                       BCryptPasswordEncoder passwordEncoder)
    {
        this.userRepository = userRepository;
        this.currentAuthUser = currentAuthUser;
        this.userDTOMapper = userDTOMapper;
        this.passwordEncoder = passwordEncoder;
    }

    public UserDTO userCheckInfo() {
        return userDTOMapper.apply(currentAuthUser.getCurrentAuthUser());
    }

    // User get joined campaigns - TODO - Don't like how this look, might change
    public Page<UserCampaignsDTO> getJoinedCampaigns(Integer page, Integer pageSize) {
        // Set default values
        int currentPage = Optional.ofNullable(page).orElse(0);
        int currentPageSize = Optional.ofNullable(pageSize).orElse(6);
        // Limit page size to 15
        currentPageSize = Math.min(currentPageSize, 15);
        List<UserCampaignsDTO> campaignList = currentAuthUser.getCurrentAuthUser().getJoinedCampaigns()
                .stream().map(userDTOMapper::mapJoinedCampaigns).collect(Collectors.toList());
        if(campaignList.isEmpty()) {
            throw new CampaignNotFoundException("You have not joined any campaigns!");
        }

        // Calculate the total number of pages
        int totalPages = (int) Math.ceil((double) campaignList.size() / currentPageSize);

        // Adjust the page number if necessary
        if(currentPage >= totalPages) {
            currentPage = totalPages - 1;
        }

        // Calculate the start and end
        int start = currentPage * currentPageSize;
        int end = Math.min(start + currentPageSize, campaignList.size());

        return new PageImpl<>(campaignList.subList(start, end), PageRequest.of(currentPage, currentPageSize), campaignList.size());
    }

    // User get created campaigns

    // User check a specific campaign that he joined

    // User check a specific campaign that he created

    public UserCreationDTO registerUser(UserRequestDTO userRequestDTO) {
        if(Optional.ofNullable(userRepository.findByEmail(userRequestDTO.email())).isPresent()) {
            throw new IntegrityConstraintViolationException(
                    String.format(
                            "Email '%s' already registered.", userRequestDTO.email()
                    ));
        }

        User user = userDTOMapper.mapToEntityFromRequest(userRequestDTO);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        // By default, all new users are ROLE_USER
        user.setRole(UserRole.ROLE_USER);

        // In case of duplicated UUID - Chances are extremely low but still checking
        boolean saved = false;
        while(!saved) {
            try {
                user = userRepository.save(user);
                saved = true;
            } catch(DataIntegrityViolationException exc) {
                user.setUuid(UUID.randomUUID());
            }
        }

        return UserCreationDTO.builder()
                .username(user.getUsername())
                .email(user.getEmail())
                .creation_time(user.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'hh:mm:ss a z")))
                .public_uuid(user.getUuid().toString())
                .message("User created.")
                .build();
    }
}
