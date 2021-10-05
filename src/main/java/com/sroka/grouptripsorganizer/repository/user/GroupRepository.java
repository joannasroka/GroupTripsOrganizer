package com.sroka.grouptripsorganizer.repository.user;

import com.sroka.grouptripsorganizer.entity.user.Group;
import com.sroka.grouptripsorganizer.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupRepository extends JpaRepository<Group, Long> {
    boolean existsByOwnerAndNameIgnoreCase(User owner, String name);
}
