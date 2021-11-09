package com.sroka.grouptripsorganizer.dto.event;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventDto {
    @Schema(required = true)
    private Long id;

    @Schema(required = true)
    private String name;

    @Schema(required = true)
    private String description;

    @Schema(required = true)
    private LocalDateTime startDateTime;

    @Schema(required = true)
    private LocalDateTime endDateTime;

    @Schema(required = true)
    Long groupId;
}
