package com.sroka.grouptripsorganizer.repository.user;

import com.sroka.grouptripsorganizer.entity.account_activation.AccountStatus;
import com.sroka.grouptripsorganizer.entity.user.User;
import com.sroka.grouptripsorganizer.exception.DatabaseEntityNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmailAndAccountStatus(String email, AccountStatus accountStatus);

    boolean existsByEmailIgnoreCase(String email);

    default User getById(Long id) {
        return findById(id).orElseThrow(DatabaseEntityNotFoundException::new);
    }

    Optional<User> findByEmailIgnoreCase(String email);

    List<User> findAllByAccountStatus(AccountStatus accountStatus);
}
