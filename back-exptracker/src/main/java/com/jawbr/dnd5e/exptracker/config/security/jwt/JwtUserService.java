package com.jawbr.dnd5e.exptracker.config.security.jwt;

import com.jawbr.dnd5e.exptracker.dto.request.LoginRequestDTO;
import com.jawbr.dnd5e.exptracker.dto.response.TokenDTO;
import com.jawbr.dnd5e.exptracker.entity.User;
import com.jawbr.dnd5e.exptracker.exception.InvalidPasswordException;
import com.jawbr.dnd5e.exptracker.exception.UserAccountDeactivatedException;
import com.jawbr.dnd5e.exptracker.exception.UserNotFoundException;
import com.jawbr.dnd5e.exptracker.repository.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

@Service
public class JwtUserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public JwtUserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = Optional.ofNullable(userRepository.findByEmail(email))
                .orElseThrow(() -> new UserNotFoundException(String.format("Email %s not found.", email)));

        Optional.of(user).filter(User::isActive)
                .orElseThrow(() ->
                        new UserAccountDeactivatedException(String.format("The account '%s' was deactivated.", email)));

        Collection<SimpleGrantedAuthority> authorities = Collections.singleton(new SimpleGrantedAuthority(user.getRole().name()));

        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), authorities);
    }

    public TokenDTO generateToken(LoginRequestDTO loginRequestDTO) {
        verifyUserCredentials(loginRequestDTO);
        return jwtService.generateToken(loginRequestDTO.email());
    }

    private void verifyUserCredentials(LoginRequestDTO loginRequestDTO) {
        UserDetails user = loadUserByUsername(loginRequestDTO.email());

        boolean isPasswordMatches = passwordEncoder.matches(loginRequestDTO.password(), user.getPassword());

        if(!isPasswordMatches) {
            throw new InvalidPasswordException("Incorrect password!");
        }
    }
}
