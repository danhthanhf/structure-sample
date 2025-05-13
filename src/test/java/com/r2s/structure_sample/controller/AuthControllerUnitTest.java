package com.r2s.structure_sample.controller;

import com.r2s.structure_sample.common.response.ApiResponse;
import com.r2s.structure_sample.common.response.LoginResponse;
import com.r2s.structure_sample.dto.LoginRequest;
import com.r2s.structure_sample.dto.RegisterRequest;
import com.r2s.structure_sample.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthControllerUnitTest {

    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthController authController;

    private RegisterRequest registerRequest;
    private LoginRequest loginRequest;

    @BeforeEach
    void setup() {
        registerRequest = RegisterRequest.builder()
                .email("test@email.com")
                .password("password")
                .firstName("Nguyen Van")
                .lastName("An")
                .build();

        loginRequest = LoginRequest.builder()
                .email("test@email.com")
                .password("password")
                .build();
    }

    @Test
    void testRegister_Success() {
        // Arrange
        ApiResponse<Void> expectedResponse = ApiResponse.success(null, "Success registry");
        when(authService.register(any(RegisterRequest.class))).thenReturn(expectedResponse);

        // Act
        ResponseEntity<ApiResponse<?>> responseEntity = authController.register(registerRequest);

        // Assert
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertTrue(Objects.requireNonNull(responseEntity.getBody()).isSuccess());
        assertEquals("Success registry", responseEntity.getBody().getMessage());
        assertNull(responseEntity.getBody().getData());
    }

    @Test
    void testRegister_Failure() {
        // Arrange
        ApiResponse<Void> expectedResponse = ApiResponse.failure("Error Registry");
        when(authService.register(any(RegisterRequest.class))).thenReturn(expectedResponse);

        // Act
        ResponseEntity<ApiResponse<?>> responseEntity = authController.register(registerRequest);

        // Assert
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertFalse(Objects.requireNonNull(responseEntity.getBody()).isSuccess());
        assertEquals("Error Registry", responseEntity.getBody().getMessage());
    }

    @Test
    void testLogin_Success() {
        // Arrange
        LoginResponse loginResponseData = new LoginResponse();
        ApiResponse<LoginResponse> expectedResponse = ApiResponse.success(loginResponseData, "Login successfully");
        when(authService.login(any(LoginRequest.class))).thenReturn(expectedResponse);

        // Act
        ResponseEntity<ApiResponse<?>> responseEntity = authController.login(loginRequest);

        // Assert
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertTrue(Objects.requireNonNull(responseEntity.getBody()).isSuccess());
        assertEquals("Login successfully", responseEntity.getBody().getMessage());
        assertNotNull(responseEntity.getBody().getData());
    }

    @Test
    void testLogin_Failure() {
        // Arrange
        ApiResponse<LoginResponse> expectedResponse = ApiResponse.failure("Invalid credentials");
        when(authService.login(any(LoginRequest.class))).thenReturn(expectedResponse);

        // Act
        ResponseEntity<ApiResponse<?>> responseEntity = authController.login(loginRequest);

        // Assert
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertFalse(Objects.requireNonNull(responseEntity.getBody()).isSuccess());
        assertEquals("Invalid credentials", responseEntity.getBody().getMessage());
        assertNull(responseEntity.getBody().getData());
    }
}