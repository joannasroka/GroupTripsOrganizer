package com.sroka.grouptripsorganizer.dto.bill;

import com.sroka.grouptripsorganizer.entity.user.User;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

public class BillShareDto {
    @Schema(required = true)
    private Long id;

    @Schema(required = true)
    private User payer;

    @Schema(required = true)
    private User debtor;

    @Schema(required = true)
    private BigDecimal amount;

    @Schema(required = true)
    private boolean paid;
}
