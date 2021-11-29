package com.sroka.grouptripsorganizer.builder.user;

import com.sroka.grouptripsorganizer.dto.user.UserCreateDto;

import java.time.LocalDate;

public class UserCreateDtoBuilder {
    private UserCreateDto userCreateDto;

    private UserCreateDtoBuilder() {
        String firstName = "John";
        String lastName = "Smith";
        String email = "john.smith@mail.com";
        String phoneNumber = "796257321";
        LocalDate dateOfBirth = LocalDate.of(1973, 7, 23);

        userCreateDto = new UserCreateDto(email, firstName, lastName, phoneNumber, dateOfBirth);
    }

    public UserCreateDtoBuilder withFirstName(String firstName) {
        userCreateDto.setFirstName(firstName);
        return this;
    }

    public UserCreateDtoBuilder withLastName(String lastName) {
        userCreateDto.setLastName(lastName);
        return this;
    }

    public UserCreateDtoBuilder withEmail(String email) {
        userCreateDto.setEmail(email);
        return this;
    }

    public UserCreateDtoBuilder withPhoneNumber(String phoneNumber) {
        userCreateDto.setPhoneNumber(phoneNumber);
        return this;
    }

    public UserCreateDtoBuilder withDateOfBirth(LocalDate dateOfBirth) {
        userCreateDto.setDateOfBirth(dateOfBirth);
        return this;
    }

    public UserCreateDto build() {
        return userCreateDto;
    }

    public static UserCreateDtoBuilder get() {
        return new UserCreateDtoBuilder();
    }
}
