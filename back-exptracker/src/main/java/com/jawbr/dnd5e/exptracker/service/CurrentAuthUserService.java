package com.jawbr.dnd5e.exptracker.service;

import com.jawbr.dnd5e.exptracker.entity.User;
import com.jawbr.dnd5e.exptracker.exception.UserNotFoundException;
import com.jawbr.dnd5e.exptracker.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CurrentAuthUserService {

    private final UserRepository userRepository;

    public CurrentAuthUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getCurrentAuthUser() {
        return Optional.of(userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName()))
                .orElseThrow(() -> new UserNotFoundException("Could not find user information."));
    }
}
