package com.sroka.grouptripsorganizer.repository.trip;

import com.sroka.grouptripsorganizer.entity.trip.Trip;
import com.sroka.grouptripsorganizer.entity.user.User;
import com.sroka.grouptripsorganizer.exception.DatabaseEntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TripRepository extends JpaRepository<Trip, Long> {
    default Trip getById(Long id) {
        return findById(id).orElseThrow(DatabaseEntityNotFoundException::new);
    }

    boolean existsByOwnerAndNameIgnoreCase(User owner, String name);

    @Query(value = "SELECT G.ID, G.VERSION, NAME, OWNER_ID " +
            "FROM TRIPS G JOIN USER_TRIP UG ON G.ID = UG.TRIP_ID " +
            "WHERE USER_ID = :userId", nativeQuery = true)
    Page<Trip> findAllByUserId(Long userId, Pageable pageable);
}
