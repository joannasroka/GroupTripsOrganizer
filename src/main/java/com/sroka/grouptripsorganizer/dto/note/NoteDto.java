package com.sroka.grouptripsorganizer.dto.note;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NoteDto {
    @Schema(required = true)
    private Long id;

    @Schema(required = true)
    private String title;

    @Schema(required = true)
    private String content;
}
