package com.sroka.grouptripsorganizer.controller.group;

import com.sroka.grouptripsorganizer.controller.BaseController;
import com.sroka.grouptripsorganizer.dto.group.GroupCreateDto;
import com.sroka.grouptripsorganizer.dto.group.GroupDto;
import com.sroka.grouptripsorganizer.security.AuthenticationContextService;
import com.sroka.grouptripsorganizer.service.group.GroupService;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Set;

import static com.sroka.grouptripsorganizer.entity.group.Group.NAME_FIELD_NAME;
import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/api/groups")
@RequiredArgsConstructor
public class GroupController extends BaseController {
    private static final int MAX_ALLOWED_BLOOD_GROUPS_PER_PAGE = 10;
    private static final Set<String> ALLOWED_GROUPS_SORTING_PARAMS = Set.of(NAME_FIELD_NAME);

    private final GroupService groupService;

    private final AuthenticationContextService authenticationContextService;

    @PreAuthorize("permitAll()")
    @PostMapping
    @ResponseStatus(CREATED)
    public GroupDto createGroup(@Valid @RequestBody GroupCreateDto groupCreateDto, Errors errors) {
        Long currentUserId = authenticationContextService.getCurrentUserId();

        return groupService.create(groupCreateDto, currentUserId, errors);
    }

    @PreAuthorize("permitAll()")
    @PostMapping("/{groupId}/users/{userId}")
    public void addParticipant(@PathVariable Long groupId,
                               @PathVariable Long userId) {
        groupService.addParticipant(groupId, userId);
    }

    @PreAuthorize("permitAll()")
    @DeleteMapping("/{groupId}/users/{userId}")
    public void removeParticipant(@PathVariable Long groupId,
                                  @PathVariable Long userId) {
        groupService.removeParticipant(groupId, userId);
    }

    @PreAuthorize("permitAll()")
    @GetMapping
    public Page<GroupDto> getAllOwnGroups(@PageableDefault(sort = NAME_FIELD_NAME) @Parameter(hidden = true) Pageable pageable) {
        pageSizeValidation(pageable, MAX_ALLOWED_BLOOD_GROUPS_PER_PAGE);
        sortParametersValidation(pageable, ALLOWED_GROUPS_SORTING_PARAMS);

        Long userId = authenticationContextService.getCurrentUserId();

        return groupService.getAllByUser(userId, pageable);
    }
}
