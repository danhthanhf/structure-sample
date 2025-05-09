package com.r2s.structure_sample.demoTest;

import com.r2s.structure_sample.common.response.ApiResponse;
import com.r2s.structure_sample.dto.RegisterRequest;
import com.r2s.structure_sample.entity.User;
import com.r2s.structure_sample.mapper.UserMapper;
import com.r2s.structure_sample.service.impl.AuthServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    private UserMapper userMapper;

    @Mock
    private AuthServiceImpl authService;

    @Test
    public void testRegister_Success() {
        RegisterRequest mockRequest = RegisterRequest.builder().email("test@gmail.com").password("password@123").lastName("lastTest").firstName("firstTest").build();

        User user = new User();

        when(userMapper.toUser(mockRequest)).thenReturn(user);

        ApiResponse<Void> result = authService.register(mockRequest);

        // Assertions
        assertEquals("Success registry", result.getMessage());
        assertEquals(0, result.getErrors().size());
    }
}
