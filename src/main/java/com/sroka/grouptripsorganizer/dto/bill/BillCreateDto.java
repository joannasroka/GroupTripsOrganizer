package com.sroka.grouptripsorganizer.dto.bill;

import com.sroka.grouptripsorganizer.entity.bill.BillCategory;
import com.sroka.grouptripsorganizer.entity.bill.Currency;
import com.sroka.grouptripsorganizer.entity.bill.SplitCategory;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class BillCreateDto {
    @NotBlank(message = "error.cannotBeBlank")
    @Length(min = 1, max = 30, message = "error.invalidFieldLength")
    @Schema(required = true)
    private String title;

    @NotNull(message = "error.cannotBeBlank")
    @Schema(required = true)
    private BillCategory category;

    @NotNull(message = "error.cannotBeBlank")
    @Schema(required = true)
    private Long payerId;

    @NotNull(message = "error.cannotBeBlank")
    @Schema(required = true)
    private LocalDate date;

    @NotNull(message = "error.cannotBeBlank")
    @Schema(required = true)
    private Long tripId;

    @NotNull(message = "error.cannotBeBlank")
    @Schema(required = true)
    @Min(value = 0, message = "error.cannotBeNegativeNumber")
    private BigDecimal totalAmount;

    @NotNull(message = "error.cannotBeBlank")
    @Schema(required = true)
    private Currency originalCurrency;

    private Currency selectedCurrency;

    @NotNull(message = "error.cannotBeBlank")
    @Schema(required = true)
    private SplitCategory splitCategory;
}
