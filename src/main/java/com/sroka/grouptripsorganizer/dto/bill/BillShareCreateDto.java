package com.sroka.grouptripsorganizer.dto.bill;

import com.sroka.grouptripsorganizer.entity.bill.Bill;
import com.sroka.grouptripsorganizer.entity.user.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class BillShareCreateDto {
    @NotNull(message = "error.cannotBeBlank")
    @Schema(required = true)
    private User payer;

    @NotNull(message = "error.cannotBeBlank")
    @Schema(required = true)
    private User debtor;

    @NotNull(message = "error.cannotBeBlank")
    @Schema(required = true)
    private Bill bill;
}
