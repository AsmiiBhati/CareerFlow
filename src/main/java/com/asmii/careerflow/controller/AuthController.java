package com.asmii.careerflow.controller;

import com.asmii.careerflow.dto.AuthDtos.LoginRequest;
import com.asmii.careerflow.dto.AuthDtos.RegisterRequest;
import com.asmii.careerflow.dto.AuthDtos.UserResponse;
import com.asmii.careerflow.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService service;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse register(@Valid @RequestBody RegisterRequest req)
    {
        return service.register(req);
    }

    @PostMapping("/login")
    public UserResponse login(@Valid @RequestBody LoginRequest req)
    {
        return service.login(req);
    }
}
