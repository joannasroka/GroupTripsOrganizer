package com.sroka.grouptripsorganizer.service.event;

import com.sroka.grouptripsorganizer.dto.event.EventCreateDto;
import com.sroka.grouptripsorganizer.dto.event.EventDto;
import com.sroka.grouptripsorganizer.dto.event.EventUpdateDto;
import com.sroka.grouptripsorganizer.entity.event.Event;
import com.sroka.grouptripsorganizer.entity.group.Group;
import com.sroka.grouptripsorganizer.entity.user.User;
import com.sroka.grouptripsorganizer.exception.DatabaseEntityNotFoundException;
import com.sroka.grouptripsorganizer.exception.ValidationException;
import com.sroka.grouptripsorganizer.mapper.EventMapper;
import com.sroka.grouptripsorganizer.repository.event.EventRepository;
import com.sroka.grouptripsorganizer.repository.group.GroupRepository;
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
    private final GroupRepository groupRepository;
    private final UserRepository userRepository;

    private final EventMapper eventMapper;

    public EventDto create(EventCreateDto eventCreateDto, Long executorId, Errors errors) {
        Group group = groupRepository.getById(eventCreateDto.getGroupId());
        User executor = userRepository.getById(executorId);

        validate(executor, group);
        validateEventDate(eventCreateDto.getStartDateTime(), eventCreateDto.getEndDateTime(), errors);

        Event newEvent = eventMapper.convertToEntity(eventCreateDto);
        newEvent.setGroup(group);
        newEvent = eventRepository.save(newEvent);

        return eventMapper.convertToDto(newEvent);
    }

    public EventDto update(EventUpdateDto eventUpdateDto, Long eventId, Long executorId, Errors errors) {
        Event eventToUpdate = eventRepository.getById(eventId);
        Group group = eventToUpdate.getGroup();
        User executor = userRepository.getById(executorId);

        validate(executor, group);
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
        Group group = eventToDelete.getGroup();

        validate(executor, group);

        eventRepository.delete(eventToDelete);
    }

    public Page<EventDto> getByGroup(Long groupId, Long executorId, Pageable pageable) {
        User executor = userRepository.getById(executorId);
        Group group = groupRepository.getById(groupId);
        validate(executor, group);

        Page<Event> events = eventRepository.findAllByGroup(group, pageable);
        return events.map(eventMapper::convertToDto);
    }

    public EventDto getById(Long eventId, Long executorId) {
        User executor = userRepository.getById(executorId);
        Event event = eventRepository.getById(eventId);
        Group group = event.getGroup();

        validate(executor, group);

        return eventMapper.convertToDto(event);
    }

    private void validate(User executor, Group group) {
        if (!group.getParticipants().contains(executor)) {
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
