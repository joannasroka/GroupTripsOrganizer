package com.sroka.grouptripsorganizer.entity.user;

import com.sroka.grouptripsorganizer.entity.BaseEntity;
import com.sroka.grouptripsorganizer.exception.UserAlreadyInThisGroupException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "groups")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Group extends BaseEntity {
    @Column(name = "name", nullable = false, length = 30)
    private String name;

    @ManyToMany
    @JoinTable(name = "user_group", joinColumns = @JoinColumn(name = "group_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<User> participants;

    public void addParticipant(User user) {
        if (participants.contains(user)) {
            throw new UserAlreadyInThisGroupException();
        }
        participants.add(user);
        user.getGroups().add(this);
    }

    public void removeParticipant(User user) {
        participants.remove(user);
        user.getGroups().remove(this);
    }
}
