package com.sroka.grouptripsorganizer.dto.account_activation;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
@AllArgsConstructor
public class AccountActivationDto {
    @NotBlank(message = "error.cannotBeBlank")
    @Schema(required = true)
    private String token;

    @NotBlank(message = "error.cannotBeBlank")
    @Schema(required = true)
    private String password;
}
