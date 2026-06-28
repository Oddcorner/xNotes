package net.oddcorner.auth_service.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Set;

import net.oddcorner.auth_service.domain.User;
import net.oddcorner.auth_service.dto.RegisterRequest;
import net.oddcorner.auth_service.dto.UserSummaryResponse;
import net.oddcorner.auth_service.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // Constructor with dependency injection cause of @Service annotation
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserSummaryResponse registerUser(RegisterRequest registerRequest) {

        if (userRepository.existsByUsername(registerRequest.username())) {
            throw new IllegalArgumentException("Username already taken");
        }

        if (userRepository.existsByEmail(registerRequest.email())) {
            throw new IllegalArgumentException("Email already registered");
        }

        String hashedPassword = passwordEncoder.encode(registerRequest.password());

        User newUser = new User(
                registerRequest.username(),
                registerRequest.email(),
                hashedPassword,
                Set.of("ROLE_USER")
                );

        User savedUser = userRepository.save(newUser);

        return new UserSummaryResponse(
                savedUser.getId(),
                savedUser.getUsername(),
                savedUser.getEmail(),
                savedUser.getRoles()
                );
    }


}

