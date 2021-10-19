package com.sroka.grouptripsorganizer.dto.bill;

import com.sroka.grouptripsorganizer.dto.group.GroupDto;
import com.sroka.grouptripsorganizer.dto.user.UserDto;
import com.sroka.grouptripsorganizer.entity.bill.BillCategory;
import com.sroka.grouptripsorganizer.entity.bill.Currency;
import com.sroka.grouptripsorganizer.entity.bill.SplitCategory;
import com.sroka.grouptripsorganizer.entity.group.Group;
import com.sroka.grouptripsorganizer.entity.user.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BillDto {
    @Schema(required = true)
    private Long id;

    @Schema(required = true)
    private String title;

    @Schema(required = true)
    private BillCategory category;

    @Schema(required = true)
    private UserDto payer;

    @Schema(required = true)
    private GroupDto group;

    @Schema(required = true)
    private LocalDate date;

    @Schema(required = true)
    private BigDecimal totalAmount;

    @Schema(required = true)
    private Currency currency;

    @Schema(required = true)
    private SplitCategory splitCategory;
}
