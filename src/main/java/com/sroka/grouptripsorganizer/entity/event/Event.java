package com.sroka.grouptripsorganizer.entity.event;

import com.sroka.grouptripsorganizer.entity.BaseEntity;
import com.sroka.grouptripsorganizer.entity.trip.Trip;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "events")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Event extends BaseEntity {
    public static final String NAME_FIELD_NAME = "name";

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Column(name = "description", nullable = false, length = 150)
    private String description;

    @Column(name = "start_date_time", nullable = false)
    private LocalDateTime startDateTime;

    @Column(name = "end_date_time")
    private LocalDateTime endDateTime;

    @ManyToOne
    @JoinColumn(name = "trip_id", nullable = false)
    private Trip trip;
}
