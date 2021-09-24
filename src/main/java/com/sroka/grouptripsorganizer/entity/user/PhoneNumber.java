package com.sroka.grouptripsorganizer.entity.user;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PhoneNumber {
    @Column(name = "phone_number", nullable = false, length = 12)
    private String phoneNumber;

    @Column(name = "phone_number_prefix", nullable = false, length = 4)
    private String phoneNumberPrefix;
}
