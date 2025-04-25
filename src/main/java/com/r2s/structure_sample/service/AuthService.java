package com.r2s.structure_sample.service;

import com.r2s.structure_sample.common.response.ResponseObject;
import com.r2s.structure_sample.dto.AuthRequest;

public interface AuthService {
     ResponseObject<Void> register(AuthRequest auth);
     ResponseObject<String> login(AuthRequest auth);
     boolean checkEmailExists(String email);
}
