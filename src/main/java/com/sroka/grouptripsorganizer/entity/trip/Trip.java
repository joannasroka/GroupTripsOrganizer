package com.sroka.grouptripsorganizer.entity.trip;

import com.sroka.grouptripsorganizer.entity.BaseEntity;
import com.sroka.grouptripsorganizer.entity.bill.Bill;
import com.sroka.grouptripsorganizer.entity.note.Note;
import com.sroka.grouptripsorganizer.entity.user.User;
import com.sroka.grouptripsorganizer.exception.UserAlreadyInThisTripException;
import com.sroka.grouptripsorganizer.exception.UserNotInThisTripException;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

import static javax.persistence.CascadeType.ALL;

@Entity
@Table(name = "trips")
@Getter
@Setter
@NoArgsConstructor
public class Trip extends BaseEntity {
    public static final String NAME_FIELD_NAME = "name";

    @Column(name = "name", nullable = false, length = 30)
    private String name;

    @ManyToMany
    @JoinTable(name = "user_trip", joinColumns = @JoinColumn(name = "trip_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<User> participants;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;

    @OneToMany(mappedBy = "trip", cascade = ALL)
    private Set<Bill> bills;

    @OneToMany(mappedBy = "trip", cascade = ALL)
    private Set<Note> notes;

    public Trip(String name, User owner) {
        this.name = name;
        this.owner = owner;
        participants = new HashSet<>();
        bills = new HashSet<>();
        addParticipant(owner);
    }

    public void addParticipant(User user) {
        if (participants.contains(user)) {
            throw new UserAlreadyInThisTripException();
        }
        participants.add(user);
        user.getTrips().add(this);
    }

    public void removeParticipant(User user) {
        if (!participants.contains(user)) {
            throw new UserNotInThisTripException();
        }
        participants.remove(user);
        if (owner == user) {
            owner = null;
        }
        user.getTrips().remove(this);
    }
}
