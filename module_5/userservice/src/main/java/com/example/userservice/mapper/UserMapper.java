package com.example.userservice.mapper;

import com.example.userservice.dto.UserDto;
import com.example.userservice.model.User;

public class UserMapper {
    public static UserDto toDto(User user) {
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setAge(user.getAge());
        return dto;
    }

    public static User toEntity(UserDto dto) {
        return new User(dto.getName(), dto.getEmail(), dto.getAge());
    }
}
