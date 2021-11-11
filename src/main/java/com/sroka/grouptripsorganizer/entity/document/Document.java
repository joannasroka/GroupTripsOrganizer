package com.sroka.grouptripsorganizer.entity.document;

import com.sroka.grouptripsorganizer.entity.BaseEntity;
import com.sroka.grouptripsorganizer.entity.trip.Trip;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "documents")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Document extends BaseEntity {
    public static final String NAME_FIELD_NAME = "name";

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Lob
    @Column(name = "file", columnDefinition = "BLOB")
    private byte[] file;

    @ManyToOne
    @JoinColumn(name = "trip_id", nullable = false)
    private Trip trip;

    @Column(name = "type", nullable = false, length = 50)
    private String type;
}
