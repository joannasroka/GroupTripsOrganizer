package com.sroka.grouptripsorganizer.service.note;

import com.sroka.grouptripsorganizer.dto.note.NoteCreateDto;
import com.sroka.grouptripsorganizer.dto.note.NoteDto;
import com.sroka.grouptripsorganizer.dto.note.NoteUpdateDto;
import com.sroka.grouptripsorganizer.entity.group.Group;
import com.sroka.grouptripsorganizer.entity.note.Note;
import com.sroka.grouptripsorganizer.entity.user.User;
import com.sroka.grouptripsorganizer.exception.DatabaseEntityNotFoundException;
import com.sroka.grouptripsorganizer.exception.UserNotInThisGroupException;
import com.sroka.grouptripsorganizer.mapper.NoteMapper;
import com.sroka.grouptripsorganizer.repository.group.GroupRepository;
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
    private final GroupRepository groupRepository;
    private final NoteMapper noteMapper;
    private final PolicyFactory policy;

    public NoteDto create(NoteCreateDto noteCreateDto, Long executorId) {
        User executor = userRepository.getById(executorId);
        Group group = groupRepository.getById(noteCreateDto.getGroupId());

        noteCreateDto.setContent(sanitizeNote(noteCreateDto.getContent()));
        validate(executor, group);

        Note note = noteMapper.convertToEntity(noteCreateDto);
        note.setGroup(group);
        note = noteRepository.save(note);

        return noteMapper.convertToDto(note);
    }

    public NoteDto update(NoteUpdateDto noteUpdateDto, Long noteId, Long executorId) {
        User executor = userRepository.getById(executorId);
        Note noteToUpdate = noteRepository.getById(noteId);
        Group group = noteToUpdate.getGroup();

        noteUpdateDto.setContent(sanitizeNote(noteUpdateDto.getContent()));
        validate(executor, group);

        updateNoteFields(noteToUpdate, noteUpdateDto);

        return noteMapper.convertToDto(noteToUpdate);
    }

    public void delete(Long noteId, Long executorId) {
        User executor = userRepository.getById(executorId);
        Note noteToDelete = noteRepository.getById(noteId);
        Group group = noteToDelete.getGroup();

        validate(executor, group);

        noteRepository.delete(noteToDelete);
    }

    public Page<NoteDto> getByGroup(Long groupId, Long executorId, Pageable pageable) {
        User executor = userRepository.getById(executorId);
        Group group = groupRepository.getById(groupId);
        validate(executor, group);

        Page<Note> notes = noteRepository.findAllByGroup(group, pageable);
        return notes.map(noteMapper::convertToDto);
    }

    public NoteDto getById(Long noteId, Long executorId) {
        User executor = userRepository.getById(executorId);
        Note note = noteRepository.getById(noteId);
        Group group = note.getGroup();

        validate(executor, group);

        return noteMapper.convertToDto(note);
    }

    private String sanitizeNote(String untrustedNote) {
        return policy.sanitize(untrustedNote);
    }

    private void validate(User executor, Group group) {
        if (!group.getParticipants().contains(executor)) {
            throw new DatabaseEntityNotFoundException();
        }
    }

    private void updateNoteFields(Note note, NoteUpdateDto noteUpdateDto) {
        note.setTitle(noteUpdateDto.getTitle());
        note.setContent(noteUpdateDto.getContent());
    }
}
