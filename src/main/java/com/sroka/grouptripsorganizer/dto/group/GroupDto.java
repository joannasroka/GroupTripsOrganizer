package com.sroka.grouptripsorganizer.dto.group;

import com.sroka.grouptripsorganizer.dto.user.UserDto;
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
    private Long id;

    @Schema(required = true)
    private String name;
}
