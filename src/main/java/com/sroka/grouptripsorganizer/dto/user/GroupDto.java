package com.sroka.grouptripsorganizer.dto.user;

import com.sroka.grouptripsorganizer.entity.user.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroupDto {
    @Schema(required = true)
    private String name;

    @Schema(required = true)
    private User owner;

    @Schema(required = true)
    private Set<User> participants;
}
