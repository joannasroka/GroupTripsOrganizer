package com.sroka.grouptripsorganizer.service;

import com.sroka.grouptripsorganizer.dto.user.UserCreateDto;
import com.sroka.grouptripsorganizer.dto.user.UserDto;
import com.sroka.grouptripsorganizer.entity.user.User;
import com.sroka.grouptripsorganizer.mapper.UserMapper;
import com.sroka.grouptripsorganizer.repository.user.UserRepository;
import com.sroka.grouptripsorganizer.service.account_activation.AccountActivationService;
import com.sroka.grouptripsorganizer.validate.UserEmailValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;

import javax.transaction.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final AccountActivationService accountActivationService;
    private final UserEmailValidator emailValidator;

    public UserDto create(UserCreateDto userCreateDto, Errors errors) {
        emailValidator.validateEmail(userCreateDto.getEmail(), errors);

        User user = userMapper.convertToEntity(userCreateDto);
        user = userRepository.save(user);

        accountActivationService.sendActivationMail(user);

        return userMapper.convertToDto(user);
    }
}
