package com.sroka.grouptripsorganizer.entity.user;


import com.sroka.grouptripsorganizer.entity.BaseEntity;
import com.sroka.grouptripsorganizer.entity.account_activation.AccountStatus;
import com.sroka.grouptripsorganizer.entity.bill.Bill;
import com.sroka.grouptripsorganizer.entity.bill.BillShare;
import com.sroka.grouptripsorganizer.entity.trip.Trip;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import static com.sroka.grouptripsorganizer.entity.account_activation.AccountStatus.REGISTERED;
import static javax.persistence.CascadeType.ALL;
import static javax.persistence.EnumType.STRING;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User extends BaseEntity {
    public static final String FIRST_NAME_FIELD_NAME = "firstName";
    public static final String LAST_NAME_FIELD_NAME = "lastName";
    public static final String EMAIL_FIELD_NAME = "email";

    @Column(name = "email", length = 250, nullable = false, unique = true)
    private String email;

    @Column(name = "password", length = 60)
    private String password;

    @Column(name = "first_name", nullable = false, length = 100)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 100)
    private String lastName;

    @Column(name = "phone_number", nullable = false, length = 16)
    private String phoneNumber;

    @Column(name = "date_of_birth", nullable = false)
    private LocalDate dateOfBirth;

    @Column(name = "account_status", nullable = false, length = 10)
    @Enumerated(STRING)
    private AccountStatus accountStatus;
    
    @ManyToMany(mappedBy = "participants")
    private Set<Trip> trips;

    @OneToMany(mappedBy = "payer", cascade = ALL)
    private Set<Bill> paid;

    @OneToMany(mappedBy = "debtor")
    private Set<BillShare> owe;

    public User() {
        this.accountStatus = REGISTERED;
    }

    public User(String email, String password, String firstName, String lastName, String phoneNumber, LocalDate dateOfBirth) {
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.dateOfBirth = dateOfBirth;
        this.accountStatus = REGISTERED;
        trips = new HashSet<>();
    }
}