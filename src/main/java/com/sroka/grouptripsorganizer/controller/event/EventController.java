package com.sroka.grouptripsorganizer.controller.event;

import com.sroka.grouptripsorganizer.controller.BaseController;
import com.sroka.grouptripsorganizer.controller.sorting_pagination.CustomPageable;
import com.sroka.grouptripsorganizer.dto.event.EventCreateDto;
import com.sroka.grouptripsorganizer.dto.event.EventDto;
import com.sroka.grouptripsorganizer.dto.event.EventUpdateDto;
import com.sroka.grouptripsorganizer.security.AuthenticationContextService;
import com.sroka.grouptripsorganizer.service.event.EventService;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Set;

import static com.sroka.grouptripsorganizer.entity.event.Event.NAME_FIELD_NAME;
import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
public class EventController extends BaseController {
    private final EventService eventService;
    private final AuthenticationContextService authenticationContextService;

    private static final int MAX_ALLOWED_EVENTS_PER_PAGE = 10;
    private static final Set<String> ALLOWED_EVENT_SORTING_PARAMS = Set.of(NAME_FIELD_NAME);

    @PreAuthorize("permitAll()")
    @PostMapping
    @ResponseStatus(CREATED)
    public EventDto createEvent(@Valid @RequestBody EventCreateDto eventCreateDto, Errors errors) {
        Long currentUserId = authenticationContextService.getCurrentUserId();

        return eventService.create(eventCreateDto, currentUserId, errors);
    }

    @PreAuthorize("permitAll()")
    @PutMapping("/{eventId}")
    public EventDto updateEvent(@PathVariable Long eventId,
                                @RequestBody @Valid EventUpdateDto eventUpdateDto,
                                Errors errors) {
        Long currentUserId = authenticationContextService.getCurrentUserId();

        return eventService.update(eventUpdateDto, eventId, currentUserId, errors);
    }

    @PreAuthorize("permitAll()")
    @DeleteMapping("/{eventId}")
    public void deleteEvent(@PathVariable Long eventId) {
        Long currentUserId = authenticationContextService.getCurrentUserId();
        eventService.delete(eventId, currentUserId);
    }

    @PreAuthorize("permitAll()")
    @GetMapping("/trips/{tripId}")
    @CustomPageable
    public Page<EventDto> getEventsByTrip(@PathVariable Long tripId,
                                          @PageableDefault(sort = NAME_FIELD_NAME) @Parameter(hidden = true) Pageable pageable) {
        pageSizeValidation(pageable, MAX_ALLOWED_EVENTS_PER_PAGE);
        sortParametersValidation(pageable, ALLOWED_EVENT_SORTING_PARAMS);

        Long currentUserId = authenticationContextService.getCurrentUserId();

        return eventService.getByTrip(tripId, currentUserId, pageable);
    }

    @PreAuthorize("permitAll()")
    @GetMapping("/{eventId}")
    public EventDto getEventById(@PathVariable Long eventId) {
        Long currentUserId = authenticationContextService.getCurrentUserId();

        return eventService.getById(eventId, currentUserId);
    }
}
