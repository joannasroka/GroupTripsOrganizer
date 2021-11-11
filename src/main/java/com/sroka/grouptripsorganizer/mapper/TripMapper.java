package com.sroka.grouptripsorganizer.mapper;

import com.sroka.grouptripsorganizer.dto.trip.TripCreateDto;
import com.sroka.grouptripsorganizer.dto.trip.TripDto;
import com.sroka.grouptripsorganizer.dto.trip.TripWithParticipantsDto;
import com.sroka.grouptripsorganizer.entity.trip.Trip;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TripMapper {
    private final ModelMapper modelMapper;

    public TripWithParticipantsDto convertWithParticipantsToDto(Trip trip) {
        return modelMapper.map(trip, TripWithParticipantsDto.class);
    }

    public TripDto convertToDto(Trip trip) {
        return modelMapper.map(trip, TripDto.class);
    }

    public Trip convertToEntity(TripCreateDto tripCreateDto) {
        return modelMapper.map(tripCreateDto, Trip.class);
    }
}
