package com.sroka.grouptripsorganizer.mapper;

import com.sroka.grouptripsorganizer.dto.group.GroupCreateDto;
import com.sroka.grouptripsorganizer.dto.group.GroupDto;
import com.sroka.grouptripsorganizer.dto.group.GroupWithParticipantsDto;
import com.sroka.grouptripsorganizer.entity.group.Group;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GroupMapper {
    private final ModelMapper modelMapper;

    public GroupWithParticipantsDto convertWithParticipantsToDto(Group group) {
        return modelMapper.map(group, GroupWithParticipantsDto.class);
    }

    public GroupDto convertToDto(Group group) {
        return modelMapper.map(group, GroupDto.class);
    }

    public Group convertToEntity(GroupCreateDto groupCreateDto) {
        return modelMapper.map(groupCreateDto, Group.class);
    }
}
