package com.r2s.structure_sample.service.impl;

import com.r2s.structure_sample.common.enums.Role;
import com.r2s.structure_sample.common.event.UserRegisteredEvent;
import com.r2s.structure_sample.common.response.ApiResponse;
import com.r2s.structure_sample.common.response.LoginResponse;
import com.r2s.structure_sample.common.util.JwtUtil;
import com.r2s.structure_sample.dto.LoginRequest;
import com.r2s.structure_sample.dto.RegisterRequest;
import com.r2s.structure_sample.entity.User;
import com.r2s.structure_sample.exception.type.ResourceConflictException;
import com.r2s.structure_sample.mapper.UserMapper;
import com.r2s.structure_sample.repository.UserRepository;
import com.r2s.structure_sample.service.AuthService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthServiceImpl implements AuthService {
    private final UserRepository authRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final ApplicationEventPublisher eventPublisher;
    private final JwtUtil jwtUtil;

    @Override
    public ApiResponse<Void> register(RegisterRequest auth) {
        if(checkEmailExists(auth.getEmail())) {
            throw new ResourceConflictException("Email already exists");
        }
        var user = userMapper.toUser(auth);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(Role.USER);
        authRepository.save(user);

        // publish user registration successful event
        eventPublisher.publishEvent(new UserRegisteredEvent(this, user));

        return ApiResponse.success(null, "Registration successfully");
    }

    @Override
    public ApiResponse<LoginResponse> login(LoginRequest loginRequest) {
        if(!checkEmailExists(loginRequest.getEmail())) {
            throw new EntityNotFoundException("Email not found");
        }
        var user = userMapper.toUser(loginRequest);

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));

        var userDetails = authRepository.findByEmail(user.getEmail()).orElseThrow(
                () -> new EntityNotFoundException("User not found")
        );

        // generate JWT token
        String token = jwtUtil.generateToken(userDetails.getEmail());
        // set token to user and return response
        LoginResponse loginResponse = getLoginResponse(userDetails, token);

        return ApiResponse.success(loginResponse, "Login successfully");
    }

    @Override
    public LoginResponse getLoginResponse(User user, String token) {
        return userMapper.toLoginResponse(user, token);
    }


    @Override
    public boolean checkEmailExists(String email) {
        return authRepository.existsByEmail(email);
    }
}