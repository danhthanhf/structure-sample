package com.r2s.structure_sample.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.r2s.structure_sample.common.response.ApiResponse;
import com.r2s.structure_sample.common.response.LoginResponse;
import com.r2s.structure_sample.common.util.JwtUtil;
import com.r2s.structure_sample.dto.LoginRequest;
import com.r2s.structure_sample.dto.RegisterRequest;
import com.r2s.structure_sample.repository.UserRepository;
import com.r2s.structure_sample.service.impl.AuthServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
public class AuthControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AuthServiceImpl authService;

    @MockitoBean
    private JwtUtil jwtUtil;

    @MockitoBean
    private PasswordEncoder passwordEncoder;

    @MockitoBean
    private AuthenticationManager authenticationManager;

    @MockitoBean
    private UserRepository userRepository;

    @MockitoBean
    private LoginResponse loginResponse;

    private final ObjectMapper objectMapper = new ObjectMapper();


    @Test
    void testRegister_Success() throws Exception {
        RegisterRequest auth = RegisterRequest.builder().email("test@gmail.com").password("test@123").firstName("Nguyen Van").lastName("An").build();

        ApiResponse<Void> res = ApiResponse.success(null, "Success registry");

        when(authService.register(any(RegisterRequest.class))).thenReturn(res);

        mockMvc.perform(post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(auth))
        ).andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("Success registry"));
    }

    @Test
    void testLogin_Success() throws Exception {
        LoginRequest auth = LoginRequest.builder()
                .email("test@gmail.com")
                .password("test@123")
                .build();

        LoginResponse loginResponse = new LoginResponse();

        ApiResponse<LoginResponse> response = ApiResponse.success(loginResponse, "Login successfully");

        when(authService.login(any(LoginRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(auth))
        ).andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Login successfully"));
    }

    @Test
    void testLogin_Failed() {
        RegisterRequest auth = RegisterRequest.builder().email("test@gmail.com")
                .password("test@123").build();
    }

}