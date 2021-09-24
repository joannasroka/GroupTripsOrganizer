package com.sroka.grouptripsorganizer.mapper;

import com.sroka.grouptripsorganizer.dto.UserDto;
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
}
