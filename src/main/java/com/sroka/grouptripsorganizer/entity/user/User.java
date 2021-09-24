package com.sroka.grouptripsorganizer.entity.user;


import com.sroka.grouptripsorganizer.entity.BaseEntity;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
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

    @Embedded
    private PhoneNumber phoneNumber;

    @Column(name = "account_status", nullable = false, length = 10)
    @Enumerated(EnumType.STRING)
    private AccountStatus accountStatus;
}