package com.r2s.structure_sample.service;

import com.r2s.structure_sample.common.util.JwtUtil;
import com.r2s.structure_sample.dto.RegisterRequest;
import com.r2s.structure_sample.entity.User;
import com.r2s.structure_sample.exception.ResourceConflictException;
import com.r2s.structure_sample.mapper.UserMapper;
import com.r2s.structure_sample.repository.UserRepository;
import com.r2s.structure_sample.service.impl.AuthServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @InjectMocks
    private AuthServiceImpl authService;

    @Mock
    private UserRepository authRepository;

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
    void testRegister_Success() {
        // Given
        RegisterRequest authRequest = new RegisterRequest();
        authRequest.setEmail("testuser");
        authRequest.setLastName("Test");
        authRequest.setFirstName("Test");
        authRequest.setPassword("testpass");

        User user = new User();

        when(userMapper.toUser(authRequest)).thenReturn(user);

        var response = authService.register(authRequest);

        verify(authRepository, times(1)).save(any(User.class));
        verify(eventPublisher, times(1)).publishEvent(any());
        assertEquals("Register success", response.getMessage());
    }


    // test email exist
    @Test
    void testRegister_Failed_Email_Exist() {
        RegisterRequest authRequest = RegisterRequest.builder()
                .email("admin@gmail.com")
                .password("12345")
                .build();
        when(authRepository.existsByEmail(authRequest.getEmail())).thenReturn(true);

        assertThrows(ResourceConflictException.class, () -> authService.register(authRequest));
    }

    @Test
    void testLogin_Successfully() {
        RegisterRequest authRequest = RegisterRequest.builder()
                .email("test@gmail.com")
                .password("test@123")
                .build();


        User mockUser = User.builder().email(authRequest.getEmail())
                        .password(authRequest.getPassword())
                                .build();

        doReturn(mockUser).when(userMapper).toUser(any(RegisterRequest.class));
        when(authRepository.existsByEmail(authRequest.getEmail())).thenReturn(true);
        when(authRepository.findByEmail(anyString())).thenReturn(Optional.of(mockUser));

        User user = userMapper.toUser(authRequest);

        when(authService.checkEmailExists(authRequest.getEmail())).thenReturn(true);
        when(authRepository.findByEmail(anyString())).thenReturn(Optional.of(user));

        var response = authService.login(authRequest);

        assertEquals(response.getMessage(), "Login successfully");
    }

    @Test
    void testLogin_Failed_Email_Not_Exist() {
        RegisterRequest authRequest = RegisterRequest.builder()
                .email("test@gmail.com")
                .password("test@123")
                .build();

        when(authService.checkEmailExists(anyString())).thenReturn(false);

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> authService.login(authRequest));
        assertEquals("Email not found", exception.getMessage());
    }

    @Test
    void testLogin_ShouldAuthenticateWithCorrectCredentials() {
        RegisterRequest authRequest = RegisterRequest.builder()
                .email("test@gmail.com")
                .password("test@123")
                .build();

        User mockUser = User.builder()
                .email(authRequest.getEmail())
                .password("wrongPass")
                .build();

        when(authRepository.existsByEmail(anyString())).thenReturn(true);
        when(userMapper.toUser(any())).thenReturn(mockUser);

        doThrow(new BadCredentialsException("Invalid credentials")).when(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));

        assertThrows(BadCredentialsException.class, () -> authService.login(authRequest));
        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }


}
