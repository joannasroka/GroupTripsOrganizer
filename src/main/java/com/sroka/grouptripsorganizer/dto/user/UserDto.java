package com.sroka.grouptripsorganizer.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    @Schema(required = true)
    private String email;

    @Schema(required = true)
    private String firstName;

    @Schema(required = true)
    private String lastName;

    @Schema(required = true)
    private String phoneNumber;
}