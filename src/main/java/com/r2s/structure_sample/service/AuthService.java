package com.r2s.structure_sample.service;

import com.r2s.structure_sample.common.response.ApiResponse;
import com.r2s.structure_sample.common.response.LoginResponse;
import com.r2s.structure_sample.dto.LoginRequest;
import com.r2s.structure_sample.dto.RegisterRequest;
import com.r2s.structure_sample.entity.User;

public interface AuthService {
     ApiResponse<Void> register(RegisterRequest auth);
     ApiResponse<LoginResponse> login(LoginRequest auth);
     LoginResponse getLoginResponse(User user, String token);
     boolean checkEmailExists(String email);
}
