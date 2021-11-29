package com.sroka.grouptripsorganizer.builder.user;

import com.sroka.grouptripsorganizer.dto.user.UserDto;

public class UserDtoBuilder {
    private UserDto userDto;

    private UserDtoBuilder() {
        Long id = 1L;
        String firstName = "John";
        String lastName = "Smith";
        String email = "john.smith@mail.com";
        String phoneNumber = "796257321";

        userDto = new UserDto(id, email, firstName, lastName, phoneNumber);
    }

    public UserDtoBuilder withFirstName(String firstName) {
        userDto.setFirstName(firstName);
        return this;
    }

    public UserDtoBuilder withLastName(String lastName) {
        userDto.setLastName(lastName);
        return this;
    }

    public UserDtoBuilder withEmail(String email) {
        userDto.setEmail(email);
        return this;
    }

    public UserDtoBuilder withPhoneNumber(String phoneNumber) {
        userDto.setPhoneNumber(phoneNumber);
        return this;
    }

    public UserDto build() {
        return userDto;
    }

    public static UserDtoBuilder get() {
        return new UserDtoBuilder();
    }
}
