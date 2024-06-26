package com.jawbr.dnd5e.exptracker.dto.mapper;

import com.jawbr.dnd5e.exptracker.dto.request.UserRequestDTO;
import com.jawbr.dnd5e.exptracker.dto.response.UserDTO;
import com.jawbr.dnd5e.exptracker.entity.User;
import com.jawbr.dnd5e.exptracker.util.Mapper;

import java.time.format.DateTimeFormatter;
import java.util.function.Function;

@Mapper
public class UserDTOMapper implements Function<User, UserDTO> {

    @Override
    public UserDTO apply(User user) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'hh:mm:ss a");

        return UserDTO.builder()
                .username(user.getUsername())
                .email(user.getEmail())
                .created_at(user.getCreatedAt() != null ? user.getCreatedAt().format(formatter) + " UTC" : null)
                .role(user.getRole().name().substring(5).substring(0, 1).toUpperCase() +
                        user.getRole().name().substring(5).substring(1).toLowerCase())
                .public_uuid(user.getUuid().toString())
                .is_active(null)
                .build();
    }

    public User mapToEntityFromRequest(UserRequestDTO userRequestDTO) {
        return User.builder()
                .username(userRequestDTO.username())
                .email(userRequestDTO.email())
                .password(userRequestDTO.password())
                .build();
    }

    public UserDTO mapEntityProfileToDto(User user) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'hh:mm:ss a");

        return UserDTO.builder()
                .username(user.getUsername())
                .created_at(user.getCreatedAt() != null ? user.getCreatedAt().format(formatter) + " UTC" : null)
                .public_uuid(user.getUuid().toString())
                .role(user.getRole().name().substring(5).substring(0, 1).toUpperCase() +
                        user.getRole().name().substring(5).substring(1).toLowerCase())
                .is_active(null)
                .build();
    }

    public UserDTO mapProfileForAdmin(User user) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'hh:mm:ss a");

        return UserDTO.builder()
                .username(user.getUsername())
                .is_active(user.isActive())
                .email(user.getEmail())
                .created_at(user.getCreatedAt() != null ? user.getCreatedAt().format(formatter) + " UTC" : null)
                .role(user.getRole().name().substring(5).substring(0, 1).toUpperCase() +
                        user.getRole().name().substring(5).substring(1).toLowerCase())
                .public_uuid(user.getUuid().toString())
                .deletion_date(user.getDeactivationExpirationDate() != null ? user.getDeactivationExpirationDate().format(formatter) + " UTC" : null)
                .build();
    }
}
