package com.jawbr.dnd5e.exptracker.service;

import com.jawbr.dnd5e.exptracker.dto.mapper.UserDTOMapper;
import com.jawbr.dnd5e.exptracker.dto.request.UserRequestDTO;
import com.jawbr.dnd5e.exptracker.dto.response.UserCampaignsDTO;
import com.jawbr.dnd5e.exptracker.dto.response.UserCreationDTO;
import com.jawbr.dnd5e.exptracker.dto.response.UserDTO;
import com.jawbr.dnd5e.exptracker.entity.Campaign;
import com.jawbr.dnd5e.exptracker.entity.User;
import com.jawbr.dnd5e.exptracker.exception.IllegalParameterException;
import com.jawbr.dnd5e.exptracker.exception.IntegrityConstraintViolationException;
import com.jawbr.dnd5e.exptracker.repository.UserRepository;
import com.jawbr.dnd5e.exptracker.util.UserRole;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.UUID;

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

    // User get joined campaigns
    public Page<UserCampaignsDTO> getJoinedCampaigns(Integer page, Integer pageSize, String sortBy) {
        User user = currentAuthUser.getCurrentAuthUser();

        page = Optional.ofNullable(page).orElse(0);
        pageSize = Math.min(Optional.ofNullable(pageSize).orElse(6), 15);
        String sortByField = Optional.ofNullable(sortBy)
                .filter(s -> !s.isEmpty())
                .map(s -> switch(s) {
                    case "creator", "creator_username" -> "creator";
                    case "name" -> "name";
                    default -> throw new IllegalParameterException(String.format("Parameter '%s' is illegal.", sortBy));
                })
                .orElse("id");

        Pageable pageable = PageRequest.of(page, pageSize, Sort.by(sortByField));

        Page<Campaign> campaigns = userRepository.findJoinedCampaignsByUserId(user.getId(), pageable);

        return campaigns.map(userDTOMapper::mapJoinedCampaigns);
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
