package com.r2s.structure_sample.ControllerTest;

import com.r2s.structure_sample.common.response.ResponseObject;
import com.r2s.structure_sample.controller.AuthController;
import com.r2s.structure_sample.dto.AuthRequest;
import com.r2s.structure_sample.service.AuthService;
import com.r2s.structure_sample.service.impl.AuthServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(AuthController.class)
@Import(AuthService.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private AuthService authService;

    @Test
    void testRegisterSuccess() throws Exception {
        // Giả lập Response từ Service
        ResponseObject<Void> mockResponse = ResponseObject.<Void>builder()
                .status(HttpStatus.CREATED)
                .build();
        when(authService.register(Mockito.any(AuthRequest.class))).thenReturn(mockResponse);

        // Gửi POST Request
        mockMvc.perform(post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                            "username": "testuser",
                            "password": "123456"
                        }
                        """))
                .andExpect(status().isCreated())  // 201 CREATED
                .andExpect(jsonPath("$.message").value("Đăng ký thành công"));
    }
}
