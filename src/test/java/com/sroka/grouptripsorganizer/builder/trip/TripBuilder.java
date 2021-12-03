package com.sroka.grouptripsorganizer.builder.trip;

import com.sroka.grouptripsorganizer.builder.user.UserBuilder;
import com.sroka.grouptripsorganizer.entity.trip.Trip;
import com.sroka.grouptripsorganizer.entity.user.User;

import java.util.Set;

public class TripBuilder {
    private Trip trip;

    private TripBuilder() {
        String name = "Bill Title";
        User owner = UserBuilder.get().build();
        byte[] picture = new byte[]{};

        trip = new Trip(name, owner, picture);
    }

    public TripBuilder withName(String name) {
        trip.setName(name);
        return this;
    }

    public TripBuilder withOwner(User owner) {
        trip.setOwner(owner);
        return this;
    }

    public TripBuilder withPicture(byte[] picture) {
        trip.setPicture(picture);
        return this;
    }

    public TripBuilder withParticipants(Set<User> participants) {
        trip.setParticipants(participants);
        return this;
    }

    public Trip build() {
        return trip;
    }

    public static TripBuilder get() {
        return new TripBuilder();
    }

}
