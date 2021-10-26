package com.sroka.grouptripsorganizer.entity.note;

import com.sroka.grouptripsorganizer.entity.BaseEntity;
import com.sroka.grouptripsorganizer.entity.group.Group;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "notes")
@Getter
@Setter
@NoArgsConstructor
public class Note extends BaseEntity {
    public static final String TITLE_FIELD_NAME = "title";

    @Column(name = "title", nullable = false, length = 30)
    private String title;

    @Column(name = "content", nullable = false, length = 500)
    private String content;

    @ManyToOne
    @JoinColumn(name = "group_id", nullable = false)
    private Group group;
}
