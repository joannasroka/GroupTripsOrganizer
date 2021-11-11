package com.sroka.grouptripsorganizer.controller.note;

import com.sroka.grouptripsorganizer.controller.BaseController;
import com.sroka.grouptripsorganizer.controller.sorting_pagination.CustomPageable;
import com.sroka.grouptripsorganizer.dto.note.NoteCreateDto;
import com.sroka.grouptripsorganizer.dto.note.NoteDto;
import com.sroka.grouptripsorganizer.dto.note.NoteUpdateDto;
import com.sroka.grouptripsorganizer.security.AuthenticationContextService;
import com.sroka.grouptripsorganizer.service.note.NoteService;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Set;

import static com.sroka.grouptripsorganizer.entity.note.Note.TITLE_FIELD_NAME;
import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/api/notes")
@RequiredArgsConstructor
public class NoteController extends BaseController {
    private final NoteService noteService;
    private final AuthenticationContextService authenticationContextService;

    private static final int MAX_ALLOWED_NOTES_PER_PAGE = 25;
    private static final Set<String> ALLOWED_NOTE_SORTING_PARAMS = Set.of(TITLE_FIELD_NAME);

    @PreAuthorize("permitAll()")
    @PostMapping
    @ResponseStatus(CREATED)
    public NoteDto createNote(@Valid @RequestBody NoteCreateDto noteCreateDto) {
        Long currentUserId = authenticationContextService.getCurrentUserId();

        return noteService.create(noteCreateDto, currentUserId);
    }

    @PreAuthorize("permitAll()")
    @PutMapping("/{noteId}")
    public NoteDto updateNote(@PathVariable Long noteId,
                              @RequestBody @Valid NoteUpdateDto noteUpdateDto) {
        Long currentUserId = authenticationContextService.getCurrentUserId();

        return noteService.update(noteUpdateDto, noteId, currentUserId);
    }

    @PreAuthorize("permitAll()")
    @DeleteMapping("/{noteId}")
    public void deleteNote(@PathVariable Long noteId) {
        Long currentUserId = authenticationContextService.getCurrentUserId();
        noteService.delete(noteId, currentUserId);
    }

    @PreAuthorize("permitAll()")
    @GetMapping("/trips/{tripId}")
    @CustomPageable
    public Page<NoteDto> getNotesByTrip(@PathVariable Long tripId,
                                        @PageableDefault(sort = TITLE_FIELD_NAME) @Parameter(hidden = true) Pageable pageable) {
        pageSizeValidation(pageable, MAX_ALLOWED_NOTES_PER_PAGE);
        sortParametersValidation(pageable, ALLOWED_NOTE_SORTING_PARAMS);

        Long currentUserId = authenticationContextService.getCurrentUserId();

        return noteService.getByTrip(tripId, currentUserId, pageable);
    }

    @PreAuthorize("permitAll()")
    @GetMapping("/{noteId}")
    public NoteDto getNoteById(@PathVariable Long noteId) {
        Long currentUserId = authenticationContextService.getCurrentUserId();

        return noteService.getById(noteId, currentUserId);
    }
}
