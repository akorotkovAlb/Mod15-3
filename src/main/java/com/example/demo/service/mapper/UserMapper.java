package com.example.demo.service.mapper;

import com.example.demo.controller.response.UserResponse;
import com.example.demo.data.entity.UserEntity;
import com.example.demo.service.dto.UserDto;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserDto toUserDto(UserEntity user) {
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setLastUpdatedDate(user.getLastUpdatedDate());
        dto.setCreatedDate(user.getCreatedDate());
        return dto;
    }

    public UserResponse toUserResponse(UserDto dto) {
        UserResponse response = new UserResponse();
        response.setId(dto.getId());
        response.setUsername(dto.getUsername());
        response.setLastUpdatedDate(dto.getLastUpdatedDate());
        response.setCreatedDate(dto.getCreatedDate());
        return response;
    }
}
