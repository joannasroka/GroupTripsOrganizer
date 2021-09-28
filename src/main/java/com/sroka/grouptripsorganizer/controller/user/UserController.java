package com.sroka.grouptripsorganizer.controller.user;

import com.sroka.grouptripsorganizer.controller.BaseController;
import com.sroka.grouptripsorganizer.dto.user.UserCreateDto;
import com.sroka.grouptripsorganizer.dto.user.UserDto;
import com.sroka.grouptripsorganizer.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/users")
public class UserController extends BaseController {
    private final UserService userService;

    @PostMapping
    @ResponseStatus(CREATED)
    public UserDto create(@RequestBody @Valid UserCreateDto userCreateDto, Errors errors) {
        return userService.create(userCreateDto, errors);
    }
}
