package com.sroka.grouptripsorganizer.service.user;

import com.sroka.grouptripsorganizer.dto.user.GroupCreateDto;
import com.sroka.grouptripsorganizer.dto.user.GroupDto;
import com.sroka.grouptripsorganizer.entity.user.Group;
import com.sroka.grouptripsorganizer.entity.user.User;
import com.sroka.grouptripsorganizer.exception.ValidationException;
import com.sroka.grouptripsorganizer.mapper.GroupMapper;
import com.sroka.grouptripsorganizer.repository.user.GroupRepository;
import com.sroka.grouptripsorganizer.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;

import javax.transaction.Transactional;
import java.util.HashSet;

@Service
@RequiredArgsConstructor
@Transactional
public class GroupService {
    private final GroupMapper groupMapper;

    private final GroupRepository groupRepository;
    private final UserRepository userRepository;

    public GroupDto create(GroupCreateDto groupCreateDto, Long ownerId, Errors errors) {
        User owner = userRepository.getById(ownerId);

        validateFields(groupCreateDto.getName(), owner, errors);

        Group newGroup = groupMapper.convertToEntity(groupCreateDto);
        newGroup.setOwner(owner);
        newGroup.setParticipants(new HashSet<>());
        newGroup.addParticipant(owner);

        Group savedGroup = groupRepository.save(newGroup);
        return groupMapper.convertToDto(savedGroup);
    }

    public void addParticipant(Long groupId, Long userId) {
        Group group = groupRepository.getById(groupId);
        User user = userRepository.getById(userId);

        group.addParticipant(user);
    }

    public void removeParticipant(Long groupId, Long userId) {
        Group group = groupRepository.getById(groupId);
        User user = userRepository.getById(userId);

        group.removeParticipant(user);
    }

    public Page<GroupDto> getAllByUser(Long userId, Pageable pageable) {
        Page<Group> groups = groupRepository.findAllByUserId(userId, pageable);

        return groups.map(groupMapper::convertToDto);
    }

    private void validateFields(String groupName, User owner, Errors errors) {
        if (!groupName.isBlank() && !isNameUnique(groupName, owner)) {
            errors.rejectValue("name", "error.groupNameExists", "error.groupNameExists");
        }

        if (errors.hasErrors()) {
            throw new ValidationException(errors);
        }
    }

    private boolean isNameUnique(String groupName, User owner) {
        return !groupRepository.existsByOwnerAndNameIgnoreCase(owner, groupName);
    }

}
