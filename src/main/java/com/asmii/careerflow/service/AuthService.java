package com.asmii.careerflow.service;

import com.asmii.careerflow.dto.AuthDtos.LoginRequest;
import com.asmii.careerflow.dto.AuthDtos.RegisterRequest;
import com.asmii.careerflow.dto.AuthDtos.UserResponse;
import com.asmii.careerflow.entity.User;
import com.asmii.careerflow.exception.ConflictException;
import com.asmii.careerflow.exception.UnauthorizedException;
import com.asmii.careerflow.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository users;
    private final PasswordEncoder encoder;

    @Transactional
    public UserResponse register(RegisterRequest req) {
        String email = req.email().trim().toLowerCase();
        if (users.existsByEmail(email))
        {
            throw new ConflictException("Email already registered");
        }
        User user = new User();
        user.setName(req.name().trim());
        user.setEmail(email);
        user.setPasswordHash(encoder.encode(req.password()));
        return UserResponse.from(users.save(user));
    }

    @Transactional(readOnly = true)
    public UserResponse login(LoginRequest req) {
        User user = users.findByEmail(req.email().trim().toLowerCase()).filter(u -> encoder.matches(req.password(),u.getPasswordHash())).orElseThrow(() -> new UnauthorizedException("Invalid email or password"));
        return UserResponse.from(user);
    }
}
