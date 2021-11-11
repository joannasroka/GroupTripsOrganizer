package com.sroka.grouptripsorganizer.dto.note;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class NoteCreateDto {
    @NotBlank(message = "error.cannotBeBlank")
    @Length(min = 1, max = 30, message = "error.invalidFieldLength")
    @Schema(required = true)
    private String title;

    @NotBlank(message = "error.cannotBeBlank")
    @Length(min = 1, max = 500, message = "error.invalidFieldLength")
    @Schema(required = true)
    private String content;

    @NotNull(message = "error.cannotBeBlank")
    @Schema(required = true)
    private Long tripId;
}
