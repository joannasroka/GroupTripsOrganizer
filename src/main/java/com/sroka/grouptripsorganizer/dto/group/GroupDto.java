package com.sroka.grouptripsorganizer.dto.group;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroupDto {
    @Schema(required = true)
    private Long id;

    @Schema(required = true)
    private String name;
}
