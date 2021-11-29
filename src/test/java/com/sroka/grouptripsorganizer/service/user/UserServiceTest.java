package com.sroka.grouptripsorganizer.service.user;

import com.sroka.grouptripsorganizer.builder.user.UserBuilder;
import com.sroka.grouptripsorganizer.builder.user.UserCreateDtoBuilder;
import com.sroka.grouptripsorganizer.builder.user.UserDtoBuilder;
import com.sroka.grouptripsorganizer.dto.user.UserCreateDto;
import com.sroka.grouptripsorganizer.dto.user.UserDto;
import com.sroka.grouptripsorganizer.entity.account_activation.AccountStatus;
import com.sroka.grouptripsorganizer.entity.user.User;
import com.sroka.grouptripsorganizer.mapper.UserMapper;
import com.sroka.grouptripsorganizer.repository.user.UserRepository;
import com.sroka.grouptripsorganizer.service.account_activation.AccountActivationService;
import com.sroka.grouptripsorganizer.validate.UserEmailValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;

import java.time.LocalDate;

import static com.sroka.grouptripsorganizer.entity.account_activation.AccountStatus.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class UserServiceTest {
    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private AccountActivationService accountActivationService;

    @Mock
    UserEmailValidator userEmailValidator;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Should create User")
    void shouldCreateUser() {
        //given
        UserCreateDto userCreateDto = UserCreateDtoBuilder
                .get()
                .build();

        User user = UserBuilder
                .get()
                .withFirstName("John")
                .withLastName("Smith")
                .withDateOfBirth(LocalDate.of(1980, 1, 1))
                .withEmail("johnsmith@mail.com")
                .withPassword("Test123!")
                .withPhoneNumber("123456789")
                .withAccountStatus(REGISTERED)
                .build();

        UserDto expectedUserDto = UserDtoBuilder
                .get()
                .build();

        BindingResult bindingResult = new BeanPropertyBindingResult(userCreateDto, "userCreateDto");

        doNothing().when(userEmailValidator).validateEmail(userCreateDto.getEmail(), bindingResult);
        when(userMapper.convertToEntity(userCreateDto)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);
        doNothing().when(accountActivationService).sendActivationMail(any(User.class));
        when(userMapper.convertToDto(user)).thenReturn(expectedUserDto);

        //when
        UserDto actualUserDto = userService.create(userCreateDto, bindingResult);

        //then
        assertNotNull(actualUserDto);
        assertEquals(expectedUserDto, actualUserDto);
        verify(userRepository, times(1)).save(any(User.class));
    }
}
