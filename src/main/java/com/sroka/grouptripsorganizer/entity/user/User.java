package com.sroka.grouptripsorganizer.entity.user;


import com.sroka.grouptripsorganizer.entity.BaseEntity;
import com.sroka.grouptripsorganizer.entity.account_activation.AccountStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import static com.sroka.grouptripsorganizer.entity.account_activation.AccountStatus.REGISTERED;
import static javax.persistence.EnumType.STRING;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User extends BaseEntity {
    public static final String FIRST_NAME_FIELD_NAME = "firstName";
    public static final String LAST_NAME_FIELD_NAME = "lastName";
    public static final String EMAIL_FIELD_NAME = "email";

    @Column(name = "email", length = 250, nullable = false, unique = true)
    public String email;

    @Column(name = "password", length = 60)
    public String password;

    @Column(name = "first_name", nullable = false, length = 100)
    public String firstName;

    @Column(name = "last_name", nullable = false, length = 100)
    public String lastName;

    @Column(name = "phone_number", nullable = false, length = 16)
    private String phoneNumber;

    @Column(name = "account_status", nullable = false, length = 10)
    @Enumerated(STRING)
    private AccountStatus accountStatus;

    public User() {
        this.accountStatus = REGISTERED;
    }

    public User(String email, String password, String firstName, String lastName, String phoneNumber) {
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.accountStatus = REGISTERED;
    }
}