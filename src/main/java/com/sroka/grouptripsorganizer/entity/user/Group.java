package com.sroka.grouptripsorganizer.entity.user;

import com.sroka.grouptripsorganizer.entity.BaseEntity;
import com.sroka.grouptripsorganizer.entity.bill.Bill;
import com.sroka.grouptripsorganizer.exception.UserAlreadyInThisGroupException;
import com.sroka.grouptripsorganizer.exception.UserNotInThisGroupException;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

import static javax.persistence.CascadeType.ALL;

@Entity
@Table(name = "groups")
@Getter
@Setter
@NoArgsConstructor
public class Group extends BaseEntity {
    @Column(name = "name", nullable = false, length = 30)
    private String name;

    @ManyToMany
    @JoinTable(name = "user_group", joinColumns = @JoinColumn(name = "group_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<User> participants;

    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false, updatable = false)
    private User owner;

    @OneToMany(mappedBy = "group", cascade = ALL)
    private Set<Bill> bills;

    public Group(String name, User owner) {
        this.name = name;
        this.owner = owner;
        participants = new HashSet<>();
        bills = new HashSet<>();
        addParticipant(owner);
    }

    public void addParticipant(User user) {
        if (participants.contains(user)) {
            throw new UserAlreadyInThisGroupException();
        }
        participants.add(user);
        user.getGroups().add(this);
    }

    public void removeParticipant(User user) {
        if (!participants.contains(user)) {
            throw new UserNotInThisGroupException();
        }
        participants.remove(user);
        user.getGroups().remove(this);
    }
}
