package com.r2s.structure_sample.service.impl;

import com.r2s.structure_sample.common.enums.Role;
import com.r2s.structure_sample.common.enums.SendNotificationType;
import com.r2s.structure_sample.common.event.UserRegisteredEvent;
import com.r2s.structure_sample.common.response.ResponseObject;
import com.r2s.structure_sample.common.util.JwtUtil;
import com.r2s.structure_sample.dto.AuthRequest;
import com.r2s.structure_sample.exception.ResourceConflictException;
import com.r2s.structure_sample.mapper.AuthMapper;
import com.r2s.structure_sample.repository.UserRepository;
import com.r2s.structure_sample.service.AuthService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthServiceImpl implements AuthService {
    private final UserRepository authRepository;
    private final AuthMapper authMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final ApplicationEventPublisher eventPublisher;
    private final JwtUtil jwtUtil;

    @Override
    public ResponseObject<Void> register(AuthRequest auth) {
        if(checkEmailExists(auth.getEmail())) {
            throw new ResourceConflictException("Email already exists");
        }
        var user = authMapper.toUser(auth);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(Role.USER);
        authRepository.save(user);

        // publish user registration successful event
        eventPublisher.publishEvent(new UserRegisteredEvent(this, user));
        return ResponseObject.<Void>builder().message("Success registry").status(HttpStatus.CREATED).build();
    }

    @Override
    public ResponseObject<String> login(AuthRequest auth) {
        if(!checkEmailExists(auth.getEmail())) {
            throw new EntityNotFoundException("Email not found");
        }
        var user = authMapper.toUser(auth);

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));

        var userDetails = authRepository.findByEmail(user.getEmail()).orElseThrow(
                () -> new EntityNotFoundException("User not found")
        );

        String token = jwtUtil.generateToken(userDetails.getEmail());

        return ResponseObject.<String>builder().message("Login successfully").data(token).status(HttpStatus.OK).build();
    }


    @Override
    public boolean checkEmailExists(String email) {
        return authRepository.existsByEmail(email);
    }
}