package com.sroka.grouptripsorganizer.dto.bill;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class BillShareCreateDto {
    @NotNull(message = "error.cannotBeBlank")
    @NotEmpty
    @Schema(required = true)
    private List<Long> debtorsIds;

    @NotNull(message = "error.cannotBeBlank")
    @Schema(required = true)
    private Long billId;

    private List<Integer> percentages;

    private List<Integer> shares;

    private List<BigDecimal> exactAmounts;
}
