package com.r2s.structure_sample.ControllerTest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.r2s.structure_sample.common.response.ResponseObject;
import com.r2s.structure_sample.common.util.JwtUtil;
import com.r2s.structure_sample.controller.AuthController;
import com.r2s.structure_sample.dto.AuthRequest;
import com.r2s.structure_sample.repository.UserRepository;
import com.r2s.structure_sample.service.impl.AuthServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
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

    private final ObjectMapper objectMapper = new ObjectMapper();


    @Test
    void testRegister_Success() throws Exception {
        AuthRequest auth = AuthRequest.builder().email("test@gmail.com").password("test@123").firstName("Nguyen Van").lastName("An").build();

        ResponseObject<Void> res = ResponseObject.<Void>builder().status(HttpStatus.CREATED).message("Registration successfully").build();

        when(authService.register(any(AuthRequest.class))).thenReturn(res);

        mockMvc.perform(post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(auth))
        ).andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("Registration successfully"));
    }

    @Test
    void testLogin_Success() throws Exception {
        AuthRequest auth = AuthRequest.builder()
                .email("test@gmail.com")
                .password("test@123")
                .build();

        ResponseObject<String> object = ResponseObject.<String>builder()
                .status(HttpStatus.OK)
                .message("Login successfully")
                .data("mocked-jwt-token")
                .build();

        when(authService.login(any(AuthRequest.class))).thenReturn(object);

        mockMvc.perform(post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(auth))
        ).andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Login successfully"))
                .andExpect(jsonPath("$.data").value("mocked-jwt-token"));
    }

    @Test
    void testLogin_Failed() {
        AuthRequest auth = AuthRequest.builder().email("test@gmail.com")
                .password("test@123").build();
    }

}