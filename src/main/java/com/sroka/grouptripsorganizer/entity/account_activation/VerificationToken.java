package com.sroka.grouptripsorganizer.entity.account_activation;


import com.sroka.grouptripsorganizer.entity.BaseEntity;
import com.sroka.grouptripsorganizer.entity.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import java.time.LocalDateTime;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = PROTECTED)
public class VerificationToken extends BaseEntity {
    private static final int EXPIRATION_TIME_IN_MINUTES = 10;

    @Column(nullable = false, name = "token", unique = true)
    private String token;

    @OneToOne
    @JoinColumn(nullable = false, name = "user_id")
    private User user;

    @Column(nullable = false, name = "expiry_date")
    private LocalDateTime expiryDate;

    public VerificationToken(User user, String token) {
        this.user = user;
        this.token = token;
        expiryDate = calculateExpiryDate();
    }

    private LocalDateTime calculateExpiryDate() {
        return LocalDateTime.now()
                .plusMinutes(EXPIRATION_TIME_IN_MINUTES);
    }
}
