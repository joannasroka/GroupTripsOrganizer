package com.sroka.grouptripsorganizer.service.trip;

import com.sroka.grouptripsorganizer.dto.trip.TripCreateDto;
import com.sroka.grouptripsorganizer.dto.trip.TripDto;
import com.sroka.grouptripsorganizer.entity.trip.Trip;
import com.sroka.grouptripsorganizer.entity.user.User;
import com.sroka.grouptripsorganizer.exception.DatabaseEntityNotFoundException;
import com.sroka.grouptripsorganizer.exception.NotAllowedFileTypeException;
import com.sroka.grouptripsorganizer.exception.ValidationException;
import com.sroka.grouptripsorganizer.mapper.TripMapper;
import com.sroka.grouptripsorganizer.repository.trip.TripRepository;
import com.sroka.grouptripsorganizer.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional
public class TripService {
    private static final Set<String> ALLOWED_MIME_TYPES = Set.of("image/jpeg", "image/png");

    private final TripMapper tripMapper;

    private final TripRepository tripRepository;
    private final UserRepository userRepository;

    public TripDto create(TripCreateDto tripCreateDto, Long ownerId, MultipartFile file, Errors errors) throws IOException {
        User owner = userRepository.getById(ownerId);

        validateFields(tripCreateDto.getName(), owner, errors);

        String fileType = file.getContentType();
        validateFileType(fileType);

        Trip newTrip = tripMapper.convertToEntity(tripCreateDto);
        newTrip.setOwner(owner);
        newTrip.setParticipants(new HashSet<>());
        newTrip.addParticipant(owner);
        newTrip.setPicture(file.getBytes());

        Trip savedTrip = tripRepository.save(newTrip);
        return tripMapper.convertToDto(savedTrip);
    }

    public TripDto getTripById(Long tripId, Long executorId) {
        User user = userRepository.getById(executorId);
        Trip trip = tripRepository.getById(tripId);

        validate(user, trip);

        return tripMapper.convertToDto(trip);
    }

    public void addParticipant(Long tripId, String userEmail) {
        Trip trip = tripRepository.getById(tripId);
        User user = userRepository.getByEmailIgnoreCase(userEmail);
        trip.addParticipant(user);
    }

    public void removeParticipant(Long tripId, Long userId) {
        Trip trip = tripRepository.getById(tripId);
        User user = userRepository.getById(userId);

        trip.removeParticipant(user);
    }

    public Page<TripDto> getAllByUser(Long userId, Pageable pageable) {
        Page<Trip> trips = tripRepository.findAllByUserId(userId, pageable);

        return trips.map(tripMapper::convertToDto);
    }

    private void validateFields(String tripName, User owner, Errors errors) {
        if (!tripName.isBlank() && !isNameUnique(tripName, owner)) {
            errors.rejectValue("name", "error.tripNameExists", "error.tripNameExists");
        }

        if (errors.hasErrors()) {
            throw new ValidationException(errors);
        }
    }

    private boolean isNameUnique(String tripName, User owner) {
        return !tripRepository.existsByOwnerAndNameIgnoreCase(owner, tripName);
    }

    private void validate(User user, Trip trip) {
        if (!trip.getParticipants().contains(user)) {
            throw new DatabaseEntityNotFoundException();
        }
    }

    private void validateFileType(String type) {
        if (!ALLOWED_MIME_TYPES.contains(type)) {
            throw new NotAllowedFileTypeException();
        }
    }
}
