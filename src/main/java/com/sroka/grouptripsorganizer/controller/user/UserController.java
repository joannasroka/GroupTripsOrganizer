package com.sroka.grouptripsorganizer.controller.user;

import com.sroka.grouptripsorganizer.controller.BaseController;
import com.sroka.grouptripsorganizer.dto.user.UserCreateDto;
import com.sroka.grouptripsorganizer.dto.user.UserDto;
import com.sroka.grouptripsorganizer.dto.user.UserUpdateDto;
import com.sroka.grouptripsorganizer.security.AuthenticationContextService;
import com.sroka.grouptripsorganizer.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/users")
public class UserController extends BaseController {
    private final UserService userService;

    private final AuthenticationContextService authenticationContextService;

    @PostMapping
    @ResponseStatus(CREATED)
    public UserDto createUser(@RequestBody @Valid UserCreateDto userCreateDto, Errors errors) {
        return userService.create(userCreateDto, errors);
    }

    @PreAuthorize("permitAll()")
    @PutMapping("/me")
    public UserDto updateUser(@RequestBody @Valid UserUpdateDto userUpdateDto) {
        Long userId = authenticationContextService.getCurrentUserId();

        return userService.update(userUpdateDto, userId);
    }
}
