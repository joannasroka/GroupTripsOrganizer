package com.sroka.grouptripsorganizer.repository.document;

import com.sroka.grouptripsorganizer.entity.document.Document;
import com.sroka.grouptripsorganizer.entity.trip.Trip;
import com.sroka.grouptripsorganizer.exception.DatabaseEntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentRepository extends JpaRepository<Document, Long> {
    default Document getById(Long id) {
        return findById(id).orElseThrow(DatabaseEntityNotFoundException::new);
    }

    Page<Document> findAllByTrip(Trip trip, Pageable pageable);
}
