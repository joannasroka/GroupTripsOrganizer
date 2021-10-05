package com.sroka.grouptripsorganizer.controller.user;

import com.sroka.grouptripsorganizer.controller.BaseController;
import com.sroka.grouptripsorganizer.dto.user.GroupCreateDto;
import com.sroka.grouptripsorganizer.dto.user.GroupDto;
import com.sroka.grouptripsorganizer.security.AuthenticationContextService;
import com.sroka.grouptripsorganizer.service.user.GroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/api/groups")
@RequiredArgsConstructor
public class GroupController extends BaseController {
    private final GroupService groupService;

    private final AuthenticationContextService authenticationContextService;

    @PreAuthorize("permitAll()")
    @PostMapping
    @ResponseStatus(CREATED)
    public GroupDto createGroup(@Valid @RequestBody GroupCreateDto groupCreateDto, Errors errors) {
        Long currentUserId = authenticationContextService.getCurrentUserId();

        return groupService.create(groupCreateDto, currentUserId, errors);
    }

    @PostMapping("/{groupId}/users/{userId}")
    public void addParticipant(@PathVariable long groupId,
                               @PathVariable long userId) {
        groupService.addParticipant(groupId, userId);
    }

    @DeleteMapping("/{groupId}/users/{userId}")
    public void removeParticipant(@PathVariable long groupId,
                                  @PathVariable long userId) {
        groupService.removeParticipant(groupId, userId);
    }
}
