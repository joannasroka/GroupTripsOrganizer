package com.sroka.grouptripsorganizer.repository;

import com.sroka.grouptripsorganizer.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
