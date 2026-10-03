package com.asmii.careerflow.dto;

import com.asmii.careerflow.entity.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.UUID;

public final class AuthDtos {
    private AuthDtos() {}

    public record RegisterRequest(@NotBlank @Size(max = 100) String name, @NotBlank @Email @Size(max = 255) String email, @NotBlank @Size(min = 8, max = 72) String password) {}

    public record LoginRequest(@NotBlank @Email String email, @NotBlank String password) {}

    public record UserResponse(UUID id, String name, String email, LocalDateTime createdAt) {
        public static UserResponse from(User u)
        {
            return new UserResponse(u.getId(), u.getName(), u.getEmail(), u.getCreatedAt());
        }
    }
}
