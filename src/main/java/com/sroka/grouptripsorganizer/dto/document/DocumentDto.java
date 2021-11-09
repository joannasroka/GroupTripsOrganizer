package com.sroka.grouptripsorganizer.dto.document;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DocumentDto {
    @Schema(required = true)
    private Long id;

    @Schema(required = true)
    private String name;

    @Schema(required = true)
    private byte[] file;

    @Schema(required = true)
    private Long groupId;
}
