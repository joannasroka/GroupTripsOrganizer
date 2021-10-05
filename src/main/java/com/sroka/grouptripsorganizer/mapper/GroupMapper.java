package com.sroka.grouptripsorganizer.mapper;

import com.sroka.grouptripsorganizer.dto.user.GroupCreateDto;
import com.sroka.grouptripsorganizer.dto.user.GroupDto;
import com.sroka.grouptripsorganizer.entity.user.Group;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GroupMapper {
    private final ModelMapper modelMapper;

    public GroupDto convertToDto(Group group) {
        return modelMapper.map(group, GroupDto.class);
    }

    public Group convertToEntity(GroupCreateDto groupCreateDto) {
        return modelMapper.map(groupCreateDto, Group.class);
    }
}
