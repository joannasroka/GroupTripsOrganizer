package com.sroka.grouptripsorganizer.dto.document;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.util.MimeType;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DocumentCreateDto {
    @NotBlank(message = "error.cannotBeBlank")
    @Length(min = 1, max = 50, message = "error.invalidFieldLength")
    @Schema(required = true)
    private String name;

    @NotNull(message = "error.cannotBeBlank")
    @Schema(required = true)
    private Long tripId;
}
