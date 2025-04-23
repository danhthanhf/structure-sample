package com.r2s.structure_sample.mapper;

import com.r2s.structure_sample.dto.AuthRequest;
import com.r2s.structure_sample.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AuthMapper {
    AuthRequest toAuthRequest(User user);

    User toUser(AuthRequest authRequest);
}
