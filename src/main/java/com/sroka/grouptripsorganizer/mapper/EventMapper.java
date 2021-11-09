package com.sroka.grouptripsorganizer.mapper;

import com.sroka.grouptripsorganizer.dto.event.EventCreateDto;
import com.sroka.grouptripsorganizer.dto.event.EventDto;
import com.sroka.grouptripsorganizer.entity.event.Event;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EventMapper {
    private final ModelMapper modelMapper;

    public EventDto convertToDto(Event event) {
        return modelMapper.map(event, EventDto.class);
    }

    public Event convertToEntity(EventCreateDto eventCreateDto) {
        return modelMapper.map(eventCreateDto, Event.class);
    }
}
