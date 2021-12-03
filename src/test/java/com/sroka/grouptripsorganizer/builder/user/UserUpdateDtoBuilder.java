package com.sroka.grouptripsorganizer.builder.user;

import com.sroka.grouptripsorganizer.dto.user.UserUpdateDto;

public class UserUpdateDtoBuilder {
    private UserUpdateDto userUpdateDto;

    private UserUpdateDtoBuilder() {
        String firstName = "John";
        String lastName = "Smith";
        String phoneNumber = "796257321";

        userUpdateDto = new UserUpdateDto(firstName, lastName, phoneNumber);
    }

    public UserUpdateDtoBuilder withFirstName(String firstName) {
        userUpdateDto.setFirstName(firstName);
        return this;
    }

    public UserUpdateDtoBuilder withLastName(String lastName) {
        userUpdateDto.setLastName(lastName);
        return this;
    }

    public UserUpdateDtoBuilder withPhoneNumber(String phoneNumber) {
        userUpdateDto.setPhoneNumber(phoneNumber);
        return this;
    }

    public UserUpdateDto build() {
        return userUpdateDto;
    }

    public static UserUpdateDtoBuilder get() {
        return new UserUpdateDtoBuilder();
    }
}
