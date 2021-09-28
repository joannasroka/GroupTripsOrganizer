package com.sroka.grouptripsorganizer.mapper;

import com.sroka.grouptripsorganizer.dto.user.UserCreateDto;
import com.sroka.grouptripsorganizer.dto.user.UserDto;
import com.sroka.grouptripsorganizer.entity.user.User;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserMapper {
    private final ModelMapper modelMapper;

    public UserDto convertToDto(User user) {
        return modelMapper.map(user, UserDto.class);
    }

    public User convertToEntity(UserCreateDto userCreateDto) {
        return modelMapper.map(userCreateDto, User.class);
    }
}
