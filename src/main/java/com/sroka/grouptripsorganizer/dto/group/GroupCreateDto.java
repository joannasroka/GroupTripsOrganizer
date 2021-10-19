package com.sroka.grouptripsorganizer.dto.group;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class GroupCreateDto {

    @NotBlank(message = "error.cannotBeBlank")
    @Length(min = 1, max = 30, message = "error.invalidFieldLength")
    @Schema(required = true)
    private String name;
}
