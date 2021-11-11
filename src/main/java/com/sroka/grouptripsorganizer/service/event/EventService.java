package com.sroka.grouptripsorganizer.service.event;

import com.sroka.grouptripsorganizer.dto.event.EventCreateDto;
import com.sroka.grouptripsorganizer.dto.event.EventDto;
import com.sroka.grouptripsorganizer.dto.event.EventUpdateDto;
import com.sroka.grouptripsorganizer.entity.event.Event;
import com.sroka.grouptripsorganizer.entity.trip.Trip;
import com.sroka.grouptripsorganizer.entity.user.User;
import com.sroka.grouptripsorganizer.exception.DatabaseEntityNotFoundException;
import com.sroka.grouptripsorganizer.exception.ValidationException;
import com.sroka.grouptripsorganizer.mapper.EventMapper;
import com.sroka.grouptripsorganizer.repository.event.EventRepository;
import com.sroka.grouptripsorganizer.repository.trip.TripRepository;
import com.sroka.grouptripsorganizer.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

@Service
@Transactional
@RequiredArgsConstructor
public class EventService {
    private final EventRepository eventRepository;
    private final TripRepository tripRepository;
    private final UserRepository userRepository;

    private final EventMapper eventMapper;

    public EventDto create(EventCreateDto eventCreateDto, Long executorId, Errors errors) {
        Trip trip = tripRepository.getById(eventCreateDto.getTripId());
        User executor = userRepository.getById(executorId);

        validate(executor, trip);
        validateEventDate(eventCreateDto.getStartDateTime(), eventCreateDto.getEndDateTime(), errors);

        Event newEvent = eventMapper.convertToEntity(eventCreateDto);
        newEvent.setTrip(trip);
        newEvent = eventRepository.save(newEvent);

        return eventMapper.convertToDto(newEvent);
    }

    public EventDto update(EventUpdateDto eventUpdateDto, Long eventId, Long executorId, Errors errors) {
        Event eventToUpdate = eventRepository.getById(eventId);
        Trip trip = eventToUpdate.getTrip();
        User executor = userRepository.getById(executorId);

        validate(executor, trip);
        validateEventDate(eventUpdateDto.getStartDateTime(), eventUpdateDto.getEndDateTime(), errors);

        eventToUpdate.setName(eventUpdateDto.getName());
        eventToUpdate.setDescription(eventUpdateDto.getDescription());
        eventToUpdate.setStartDateTime(eventUpdateDto.getStartDateTime());
        eventToUpdate.setEndDateTime(eventUpdateDto.getEndDateTime());

        return eventMapper.convertToDto(eventToUpdate);
    }

    public void delete(Long eventId, Long executorId) {
        User executor = userRepository.getById(executorId);
        Event eventToDelete = eventRepository.getById(eventId);
        Trip trip = eventToDelete.getTrip();

        validate(executor, trip);

        eventRepository.delete(eventToDelete);
    }

    public Page<EventDto> getByTrip(Long tripId, Long executorId, Pageable pageable) {
        User executor = userRepository.getById(executorId);
        Trip trip = tripRepository.getById(tripId);
        validate(executor, trip);

        Page<Event> events = eventRepository.findAllByTrip(trip, pageable);
        return events.map(eventMapper::convertToDto);
    }

    public EventDto getById(Long eventId, Long executorId) {
        User executor = userRepository.getById(executorId);
        Event event = eventRepository.getById(eventId);
        Trip trip = event.getTrip();

        validate(executor, trip);

        return eventMapper.convertToDto(event);
    }

    private void validate(User executor, Trip trip) {
        if (!trip.getParticipants().contains(executor)) {
            throw new DatabaseEntityNotFoundException();
        }
    }

    private void validateEventDate(LocalDateTime startDateTime, LocalDateTime endDateTime, Errors errors) {
        if (!startDateTime.isAfter(LocalDateTime.now())) {
            errors.rejectValue("startDateTime", "error.onlyFutureDates", "error.onlyFutureDates");
        }
        if (endDateTime != null && !endDateTime.isAfter(LocalDateTime.now())) {
            errors.rejectValue("endDateTime", "error.onlyFutureDates", "error.onlyFutureDates");
        }

        if (endDateTime != null && startDateTime.isAfter(endDateTime)) {
            errors.rejectValue("startDateTime", "error.startDateAfterEndDate", "error.startDateAfterEndDate");
            errors.rejectValue("endDateTime", "error.startDateAfterEndDate", "error.startDateAfterEndDate");
        }

        if (errors.hasErrors()) {
            throw new ValidationException(errors);
        }
    }
}
