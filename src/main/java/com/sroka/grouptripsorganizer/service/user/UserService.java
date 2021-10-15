package com.sroka.grouptripsorganizer.service.user;

import com.sroka.grouptripsorganizer.dto.user.UserCreateDto;
import com.sroka.grouptripsorganizer.dto.user.UserDto;
import com.sroka.grouptripsorganizer.dto.user.UserUpdateDto;
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

    public UserDto getById(Long id) {
        User user = userRepository.getById(id);
        return userMapper.convertToDto(user);
    }

    public UserDto create(UserCreateDto userCreateDto, Errors errors) {
        emailValidator.validateEmail(userCreateDto.getEmail(), errors);

        User user = userMapper.convertToEntity(userCreateDto);
        user = userRepository.save(user);

        accountActivationService.sendActivationMail(user);

        return userMapper.convertToDto(user);
    }

    public UserDto update(UserUpdateDto userUpdateDto, Long userId) {
        User user = userRepository.getById(userId);

        updateUserFields(user, userUpdateDto);

        return userMapper.convertToDto(user);
    }

    private void updateUserFields(User user, UserUpdateDto userUpdateDto) {
        user.setFirstName(userUpdateDto.getFirstName());
        user.setLastName(userUpdateDto.getLastName());
        user.setPhoneNumber(userUpdateDto.getPhoneNumber());
    }
}
