package com.sroka.grouptripsorganizer.builder.user;

import com.sroka.grouptripsorganizer.entity.account_activation.AccountStatus;
import com.sroka.grouptripsorganizer.entity.user.User;

import java.time.LocalDate;

public class UserBuilder {
    private User user;

    private UserBuilder() {
        String firstName = "John";
        String lastName = "Smith";
        String email = "john.smith@mail.com";
        String password = "$2a$10$Mes5ittAuXnVLRIapDMgM.YHzjS30kreBBf40o78pNJK8Zu3.LqeG";
        String phoneNumber = "796257321";
        LocalDate dateOfBirth = LocalDate.of(1973, 7, 23);

        user = new User(email, password, firstName, lastName, phoneNumber, dateOfBirth);
    }

    public UserBuilder withFirstName(String firstName) {
        user.setFirstName(firstName);
        return this;
    }

    public UserBuilder withLastName(String lastName) {
        user.setLastName(lastName);
        return this;
    }

    public UserBuilder withEmail(String email) {
        user.setEmail(email);
        return this;
    }

    public UserBuilder withPhoneNumber(String phoneNumber) {
        user.setPhoneNumber(phoneNumber);
        return this;
    }

    public UserBuilder withPassword(String password) {
        user.setPassword(password);
        return this;
    }

    public UserBuilder withAccountStatus(AccountStatus accountStatus) {
        user.setAccountStatus(accountStatus);
        return this;
    }


    public UserBuilder withDateOfBirth(LocalDate dateOfBirth) {
        user.setDateOfBirth(dateOfBirth);
        return this;
    }

    public User build() {
        return user;
    }

    public static UserBuilder get() {
        return new UserBuilder();
    }
}
