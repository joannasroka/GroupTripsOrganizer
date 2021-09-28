package com.sroka.grouptripsorganizer.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserUpdateDto {
    @NotBlank(message = "error.cannotBeBlank")
    @Length(min = 1, max = 100, message = "error.invalidFieldLength")
    @Schema(required = true)
    private String firstName;

    @NotBlank(message = "error.cannotBeBlank")
    @Length(min = 1, max = 100, message = "error.invalidFieldLength")
    @Schema(required = true)
    private String lastName;

    @NotBlank(message = "error.cannotBeBlank")
    @Length(min = 3, max = 16, message = "error.invalidFieldLength")
    @Pattern(regexp = "^\\+?\\d{2,100}", message = "error.invalidPhoneNumber")
    @Schema(required = true)
    private String phoneNumber;
}
