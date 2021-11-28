package com.sroka.grouptripsorganizer.service.bill.split_bill_strategy;

import com.sroka.grouptripsorganizer.entity.bill.Bill;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class SplitBillData {
    private Bill bill;

    private List<Long> debtorsIds;

    private List<Integer> percentages;

    private List<Integer> shares;

    private List<BigDecimal> exactAmounts;
}
