package com.sroka.grouptripsorganizer.mapper;

import com.sroka.grouptripsorganizer.dto.note.NoteCreateDto;
import com.sroka.grouptripsorganizer.dto.note.NoteDto;
import com.sroka.grouptripsorganizer.entity.note.Note;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class NoteMapper {
    private final ModelMapper modelMapper;

    public NoteDto convertToDto(Note note) {
        return modelMapper.map(note, NoteDto.class);
    }

    public List<NoteDto> convertToDto(List<Note> notes) {
        return notes
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public Note convertToEntity(NoteCreateDto noteCreateDto) {
        return modelMapper.map(noteCreateDto, Note.class);
    }
}
