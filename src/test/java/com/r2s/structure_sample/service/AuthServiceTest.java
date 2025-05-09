package com.r2s.structure_sample.service;

import com.r2s.structure_sample.common.event.UserRegisteredEvent;
import com.r2s.structure_sample.common.response.ApiResponse;
import com.r2s.structure_sample.common.util.JwtUtil;
import com.r2s.structure_sample.dto.LoginRequest;
import com.r2s.structure_sample.dto.RegisterRequest;
import com.r2s.structure_sample.entity.User;
import com.r2s.structure_sample.exception.type.ResourceConflictException;
import com.r2s.structure_sample.mapper.UserMapper;
import com.r2s.structure_sample.repository.UserRepository;
import com.r2s.structure_sample.service.impl.AuthServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @InjectMocks
    private AuthServiceImpl authService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @Test
    public void testRegister_Success() {
        RegisterRequest mockRequest = RegisterRequest.builder().email("test@gmail.com").password("password@123").lastName("lastTest").firstName("firstTest").build();

        User user = new User();
        user.setPassword(mockRequest.getPassword());
        user.setEmail(mockRequest.getEmail());

        when(userMapper.toUser(mockRequest)).thenReturn(user);

        // Act
        ApiResponse<Void> result = authService.register(mockRequest);

        // Assert
        ArgumentCaptor<UserRegisteredEvent> eventCaptor = ArgumentCaptor.forClass(UserRegisteredEvent.class);
        verify(eventPublisher, times(1)).publishEvent(eventCaptor.capture());

        UserRegisteredEvent userRegisteredEvent = eventCaptor.getValue();

        assertEquals(user, userRegisteredEvent.getUser());
        assertNotNull(result);
        assertNull(result.getData());
        assertNull(result.getErrors());
        assertEquals("Registration successfully", result.getMessage());

        // Verify interactions
        verify(userRepository, times(1)).save(user);
        verify(passwordEncoder, times(1)).encode(anyString());
        verify(userRepository, times(1)).existsByEmail(anyString());
    }


    // test email exist
    @Test
    void testRegister_Failed_Email_Exist() {
        RegisterRequest authRequest = RegisterRequest.builder()
                .email("admin@gmail.com")
                .password("12345")
                .build();
        when(userRepository.existsByEmail(authRequest.getEmail())).thenReturn(true);

        assertThrows(ResourceConflictException.class, () -> authService.register(authRequest));
    }

    @Test
    void testLogin_Successfully() {
        LoginRequest authRequest = LoginRequest.builder()
                .email("test@gmail.com")
                .password("test@123")
                .build();


        User mockUser = User.builder().email(authRequest.getEmail())
                        .password(authRequest.getPassword())
                                .build();

        doReturn(mockUser).when(userMapper).toUser(any(LoginRequest.class));
        when(userRepository.existsByEmail(authRequest.getEmail())).thenReturn(true);
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(mockUser));

        User user = userMapper.toUser(authRequest);

        when(authService.checkEmailExists(authRequest.getEmail())).thenReturn(true);
//        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));

        var response = authService.login(authRequest);

        assertEquals(response.getMessage(), "Login successfully");
    }

    @Test
    void testLogin_Failed_Email_Not_Exist() {
        LoginRequest authRequest = LoginRequest.builder()
                .email("test@gmail.com")
                .password("test@123")
                .build();

        when(authService.checkEmailExists(anyString())).thenReturn(false);

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> authService.login(authRequest));
        assertEquals("Email not found", exception.getMessage());
    }

    @Test
    void testLogin_ShouldAuthenticateWithCorrectCredentials() {
        LoginRequest authRequest = LoginRequest.builder()
                .email("test@gmail.com")
                .password("test@123")
                .build();

        User mockUser = User.builder()
                .email(authRequest.getEmail())
                .password("wrongPass")
                .build();

        when(userRepository.existsByEmail(anyString())).thenReturn(true);
        when(userMapper.toUser(authRequest)).thenReturn(mockUser);

        doThrow(new BadCredentialsException("Invalid credentials")).when(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));

        assertThrows(BadCredentialsException.class, () -> authService.login(authRequest));
        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }


}
