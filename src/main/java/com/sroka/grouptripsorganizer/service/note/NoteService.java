package com.sroka.grouptripsorganizer.service.note;

import com.sroka.grouptripsorganizer.dto.note.NoteCreateDto;
import com.sroka.grouptripsorganizer.dto.note.NoteDto;
import com.sroka.grouptripsorganizer.dto.note.NoteUpdateDto;
import com.sroka.grouptripsorganizer.entity.trip.Trip;
import com.sroka.grouptripsorganizer.entity.note.Note;
import com.sroka.grouptripsorganizer.entity.user.User;
import com.sroka.grouptripsorganizer.exception.DatabaseEntityNotFoundException;
import com.sroka.grouptripsorganizer.mapper.NoteMapper;
import com.sroka.grouptripsorganizer.repository.trip.TripRepository;
import com.sroka.grouptripsorganizer.repository.note.NoteRepository;
import com.sroka.grouptripsorganizer.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.owasp.html.PolicyFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class NoteService {
    private final NoteRepository noteRepository;
    private final UserRepository userRepository;
    private final TripRepository tripRepository;
    private final NoteMapper noteMapper;
    private final PolicyFactory policy;

    public NoteDto create(NoteCreateDto noteCreateDto, Long executorId) {
        User executor = userRepository.getById(executorId);
        Trip trip = tripRepository.getById(noteCreateDto.getTripId());

        noteCreateDto.setContent(sanitizeNote(noteCreateDto.getContent()));
        validate(executor, trip);

        Note note = noteMapper.convertToEntity(noteCreateDto);
        note.setTrip(trip);
        note = noteRepository.save(note);

        return noteMapper.convertToDto(note);
    }

    public NoteDto update(NoteUpdateDto noteUpdateDto, Long noteId, Long executorId) {
        User executor = userRepository.getById(executorId);
        Note noteToUpdate = noteRepository.getById(noteId);
        Trip trip = noteToUpdate.getTrip();

        noteUpdateDto.setContent(sanitizeNote(noteUpdateDto.getContent()));
        validate(executor, trip);

        updateNoteFields(noteToUpdate, noteUpdateDto);

        return noteMapper.convertToDto(noteToUpdate);
    }

    public void delete(Long noteId, Long executorId) {
        User executor = userRepository.getById(executorId);
        Note noteToDelete = noteRepository.getById(noteId);
        Trip trip = noteToDelete.getTrip();

        validate(executor, trip);

        noteRepository.delete(noteToDelete);
    }

    public Page<NoteDto> getByTrip(Long tripId, Long executorId, Pageable pageable) {
        User executor = userRepository.getById(executorId);
        Trip trip = tripRepository.getById(tripId);
        validate(executor, trip);

        Page<Note> notes = noteRepository.findAllByTrip(trip, pageable);
        return notes.map(noteMapper::convertToDto);
    }

    public NoteDto getById(Long noteId, Long executorId) {
        User executor = userRepository.getById(executorId);
        Note note = noteRepository.getById(noteId);
        Trip trip = note.getTrip();

        validate(executor, trip);

        return noteMapper.convertToDto(note);
    }

    private String sanitizeNote(String untrustedNote) {
        return policy.sanitize(untrustedNote);
    }

    private void validate(User executor, Trip trip) {
        if (!trip.getParticipants().contains(executor)) {
            throw new DatabaseEntityNotFoundException();
        }
    }

    private void updateNoteFields(Note note, NoteUpdateDto noteUpdateDto) {
        note.setTitle(noteUpdateDto.getTitle());
        note.setContent(noteUpdateDto.getContent());
    }
}
