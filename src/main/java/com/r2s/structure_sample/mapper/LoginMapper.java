package com.r2s.structure_sample.mapper;

import com.r2s.structure_sample.common.response.LoginResponse;
import com.r2s.structure_sample.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LoginMapper {
    LoginResponse toLoginResponse(User user);
}
