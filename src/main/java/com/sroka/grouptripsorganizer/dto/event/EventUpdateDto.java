package com.sroka.grouptripsorganizer.dto.event;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventUpdateDto {
    @NotBlank(message = "error.cannotBeBlank")
    @Length(min = 1, max = 50, message = "error.invalidFieldLength")
    @Schema(required = true)
    private String name;

    @NotBlank(message = "error.cannotBeBlank")
    @Length(min = 1, max = 150, message = "error.invalidFieldLength")
    @Schema(required = true)
    private String description;

    @NotNull(message = "error.cannotBeBlank")
    @Schema(required = true)
    private LocalDateTime startDateTime;

    private LocalDateTime endDateTime;
}
