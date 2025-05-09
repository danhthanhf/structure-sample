package com.r2s.structure_sample.mapper;

import com.r2s.structure_sample.common.response.LoginResponse;
import com.r2s.structure_sample.dto.LoginRequest;
import com.r2s.structure_sample.dto.RegisterRequest;
import com.r2s.structure_sample.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    RegisterRequest toAuthRequest(User user);

    User toUser(RegisterRequest authRequest);

    User toUser(LoginRequest loginRequest);

    LoginResponse toLoginResponse(User user, String accessToken);
}
