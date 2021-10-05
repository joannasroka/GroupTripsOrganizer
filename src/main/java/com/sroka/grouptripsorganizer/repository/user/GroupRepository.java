package com.sroka.grouptripsorganizer.repository.user;

import com.sroka.grouptripsorganizer.entity.user.Group;
import com.sroka.grouptripsorganizer.entity.user.User;
import com.sroka.grouptripsorganizer.exception.DatabaseEntityNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupRepository extends JpaRepository<Group, Long> {
    default Group getById(Long id) {
        return findById(id).orElseThrow(DatabaseEntityNotFoundException::new);
    }

    boolean existsByOwnerAndNameIgnoreCase(User owner, String name);
}
