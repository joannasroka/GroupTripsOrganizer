package com.sroka.grouptripsorganizer.dto.bill;

import com.sroka.grouptripsorganizer.dto.user.UserDto;
import com.sroka.grouptripsorganizer.entity.user.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BillShareDto {
    @Schema(required = true)
    private Long id;

    @Schema(required = true)
    private UserDto payer;

    @Schema(required = true)
    private UserDto debtor;

    @Schema(required = true)
    private BigDecimal amount;

    @Schema(required = true)
    private boolean paid;
}
