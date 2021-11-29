package com.sroka.grouptripsorganizer.controller.trip;

import com.sroka.grouptripsorganizer.controller.BaseController;
import com.sroka.grouptripsorganizer.dto.trip.TripCreateDto;
import com.sroka.grouptripsorganizer.dto.trip.TripDto;
import com.sroka.grouptripsorganizer.security.AuthenticationContextService;
import com.sroka.grouptripsorganizer.service.trip.TripService;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.Set;

import static com.sroka.grouptripsorganizer.entity.trip.Trip.NAME_FIELD_NAME;
import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/api/trips")
@RequiredArgsConstructor
public class TripController extends BaseController {
    private static final int MAX_ALLOWED_TRIPS_PER_PAGE = 10;
    private static final Set<String> ALLOWED_TRIPS_SORTING_PARAMS = Set.of(NAME_FIELD_NAME);

    private final TripService tripService;

    private final AuthenticationContextService authenticationContextService;

    @PreAuthorize("permitAll()")
    @PostMapping
    @ResponseStatus(CREATED)
    public TripDto createTrip(@Valid @RequestBody TripCreateDto tripCreateDto,
                              @RequestPart("picture") MultipartFile picture,
                              Errors errors) throws IOException {
        Long currentUserId = authenticationContextService.getCurrentUserId();

        return tripService.create(tripCreateDto, currentUserId, picture, errors);
    }

    @PreAuthorize("permitAll()")
    @PostMapping("/{tripId}/users/{userEmail}")
    public void addParticipant(@PathVariable Long tripId,
                               @PathVariable String userEmail) {
        tripService.addParticipant(tripId, userEmail);
    }

    @PreAuthorize("permitAll()")
    @DeleteMapping("/{tripId}/users/{userId}")
    public void removeParticipant(@PathVariable Long tripId,
                                  @PathVariable Long userId) {
        tripService.removeParticipant(tripId, userId);
    }

    @PreAuthorize("permitAll()")
    @GetMapping
    public Page<TripDto> getAllOwnTrips(@PageableDefault(sort = NAME_FIELD_NAME) @Parameter(hidden = true) Pageable pageable) {
        pageSizeValidation(pageable, MAX_ALLOWED_TRIPS_PER_PAGE);
        sortParametersValidation(pageable, ALLOWED_TRIPS_SORTING_PARAMS);

        Long userId = authenticationContextService.getCurrentUserId();

        return tripService.getAllByUser(userId, pageable);
    }

    @PreAuthorize("permitAll()")
    @GetMapping("/{tripId}")
    public TripDto getTripById(@PathVariable Long tripId) {
        Long userId = authenticationContextService.getCurrentUserId();

        return tripService.getTripById(tripId, userId);
    }
}
