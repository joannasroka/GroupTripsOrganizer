package com.sroka.grouptripsorganizer.repository.note;

import com.sroka.grouptripsorganizer.entity.group.Group;
import com.sroka.grouptripsorganizer.entity.note.Note;
import com.sroka.grouptripsorganizer.exception.DatabaseEntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoteRepository extends JpaRepository<Note, Long> {
    default Note getById(Long id) {
        return findById(id).orElseThrow(DatabaseEntityNotFoundException::new);
    }

    Page<Note> findAllByGroup(Group group, Pageable pageable);
}
