package com.r2s.structure_sample.ServiceTest;

import com.r2s.structure_sample.common.enums.Role;
import com.r2s.structure_sample.common.event.UserRegisteredEvent;
import com.r2s.structure_sample.common.response.ResponseObject;
import com.r2s.structure_sample.dto.AuthRequest;
import com.r2s.structure_sample.entity.User;
import com.r2s.structure_sample.exception.ResourceConflictException;
import com.r2s.structure_sample.mapper.AuthMapper;
import com.r2s.structure_sample.repository.UserRepository;
import com.r2s.structure_sample.service.impl.AuthServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthMapper authMapper;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @InjectMocks
    private AuthServiceImpl authService;

    @Test
    void register_validAuthRequest_userCreatedAndEventPublished() {
        // Arrange
        AuthRequest validAuth = AuthRequest.builder()
                .email("test@example.com")
                .password("password")
                .firstName("John")
                .lastName("Doe")
                .build();
        User mappedUser = new User();
        mappedUser.setEmail("test@example.com");
        mappedUser.setPassword("encodedPassword");
        mappedUser.setRole(Role.USER);

        when(userRepository.existsByEmail(validAuth.getEmail())).thenReturn(false);
        when(authMapper.toUser(validAuth)).thenReturn(mappedUser);
        when(passwordEncoder.encode(validAuth.getPassword())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(mappedUser);

        // Act
        ResponseObject<Void> result = authService.register(validAuth);

        // Assert
        Assertions.assertEquals(HttpStatus.CREATED, result.getStatus());
        Assertions.assertEquals("Success registry", result.getMessage());
        Assertions.assertNull(result.getData());

        verify(userRepository, times(1)).existsByEmail(validAuth.getEmail());
        verify(authMapper, times(1)).toUser(validAuth);
        verify(passwordEncoder, times(1)).encode(validAuth.getPassword());
        verify(userRepository, times(1)).save(any(User.class));
        verify(eventPublisher, times(1)).publishEvent(any(UserRegisteredEvent.class));
    }

    @Test
    void register_existingEmail_throwsResourceConflictException() {
        // Arrange
        AuthRequest existingAuth = AuthRequest.builder()
                .email("existing@example.com")
                .password("password")
                .firstName("John")
                .lastName("Doe")
                .build();

        when(userRepository.existsByEmail(existingAuth.getEmail())).thenReturn(true);

        // Act & Assert
        Assertions.assertThrows(ResourceConflictException.class, () -> authService.register(existingAuth));

        // Verify interactions
        verify(userRepository, times(1)).existsByEmail(existingAuth.getEmail());
        verify(authMapper, never()).toUser(any(AuthRequest.class));
        verify(passwordEncoder, never()).encode(anyString());
        verify(userRepository, never()).save(any(User.class));
        verify(eventPublisher, never()).publishEvent(any(UserRegisteredEvent.class));
    }
}