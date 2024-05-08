package com.jawbr.dnd5e.exptracker.service;

import com.jawbr.dnd5e.exptracker.dto.mapper.UserDTOMapper;
import com.jawbr.dnd5e.exptracker.dto.request.UserRequestDTO;
import com.jawbr.dnd5e.exptracker.dto.response.UserCreationDTO;
import com.jawbr.dnd5e.exptracker.dto.response.UserDTO;
import com.jawbr.dnd5e.exptracker.entity.User;
import com.jawbr.dnd5e.exptracker.exception.IntegrityConstraintViolationException;
import com.jawbr.dnd5e.exptracker.exception.UserNotFoundException;
import com.jawbr.dnd5e.exptracker.repository.UserRepository;
import com.jawbr.dnd5e.exptracker.util.UserRole;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.UUID;

import static com.jawbr.dnd5e.exptracker.util.CheckRequestConfirmation.checkConfirmation;

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

    public UserDTO checkUserProfile(UUID userUuid) {
        User current = currentAuthUser.getCurrentAuthUser();
        // Incase the user input own uuid it will map correctly
        if(current.getUuid().equals(userUuid))
            return userDTOMapper.apply(current);
        return userDTOMapper.mapEntityProfileToDto(Optional.ofNullable(userRepository.findByUuid(userUuid))
                .orElseThrow(() -> new UserNotFoundException("User not found")));
    }

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

    public void deleteUser(boolean isConfirmed) {
        checkConfirmation(isConfirmed);
        User userToBeDeleted = currentAuthUser.getCurrentAuthUser();
        userToBeDeleted.getJoinedCampaigns().forEach(c -> c.getPlayers().remove(userToBeDeleted));
        userRepository.delete(userToBeDeleted);
    }

    public UserDTO updateUser(UserRequestDTO userRequestDTO) {
        User user = currentAuthUser.getCurrentAuthUser();

        final String username = StringUtils.hasText(userRequestDTO.username()) ? userRequestDTO.username() : user.getUsername();
        String password = user.getPassword();
        if(StringUtils.hasText(userRequestDTO.password()) && !passwordEncoder.matches(userRequestDTO.password(), user.getPassword())) {
            password = passwordEncoder.encode(userRequestDTO.password());
        }

        User updatedUser = user;
        updatedUser.setUsername(username);
        updatedUser.setPassword(password);

        updatedUser = userRepository.save(updatedUser);

        return userDTOMapper.apply(updatedUser);
    }

    public void adminDeleteUser(boolean isConfirmed, UUID userUuid) {
        checkConfirmation(isConfirmed);
        User userToBeDeleted = Optional.ofNullable(userRepository.findByUuid(userUuid))
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        userToBeDeleted.getJoinedCampaigns().forEach(c -> c.getPlayers().remove(userToBeDeleted));
        userRepository.delete(userToBeDeleted);
    }

    /*
     * When an account is deactivated all the user information is not deleted i.e. created characters, joined campaigns and created campaigns
     * But it will be deleted if the account is deactivated for long enough
     */
    public void adminDeactivateUser(boolean isConfirmed, UUID userUuid) {
        checkConfirmation(isConfirmed);
        User userToBeDeactivated = Optional.ofNullable(userRepository.findByUuid(userUuid))
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        userToBeDeactivated.setActive(false);
        userRepository.save(userToBeDeactivated);
    }

    public void adminReactivateUser(boolean isConfirmed, UUID userUuid) {
        checkConfirmation(isConfirmed);
        User userToBeReactivated = Optional.ofNullable(userRepository.findByUuid(userUuid))
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        userToBeReactivated.setActive(true);
        userRepository.save(userToBeReactivated);
    }

    public UserDTO adminCheckUserProfile(UUID userUuid) {
        return userDTOMapper.mapProfileForAdmin(Optional.ofNullable(userRepository.findByUuid(userUuid))
                .orElseThrow(() -> new UserNotFoundException("User not found")));
    }

    // TODO - Method for admin to see all users not including admins
    // TODO - Method for admin to see all admins
}
