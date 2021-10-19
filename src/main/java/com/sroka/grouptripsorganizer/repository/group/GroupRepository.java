package com.sroka.grouptripsorganizer.repository.group;

import com.sroka.grouptripsorganizer.entity.group.Group;
import com.sroka.grouptripsorganizer.entity.user.User;
import com.sroka.grouptripsorganizer.exception.DatabaseEntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface GroupRepository extends JpaRepository<Group, Long> {
    default Group getById(Long id) {
        return findById(id).orElseThrow(DatabaseEntityNotFoundException::new);
    }

    boolean existsByOwnerAndNameIgnoreCase(User owner, String name);

    @Query(value = "SELECT G.ID, G.VERSION, NAME, OWNER_ID " +
            "FROM GROUPS G JOIN USER_GROUP UG ON G.ID = UG.GROUP_ID " +
            "WHERE USER_ID = :userId", nativeQuery = true)
    Page<Group> findAllByUserId(Long userId, Pageable pageable);
}
