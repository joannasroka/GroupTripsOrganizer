package com.sroka.grouptripsorganizer.service.bill.split_bill_strategy;

import com.google.common.collect.Streams;
import com.sroka.grouptripsorganizer.dto.bill.BillShareDto;
import com.sroka.grouptripsorganizer.entity.bill.Bill;
import com.sroka.grouptripsorganizer.entity.bill.BillShare;
import com.sroka.grouptripsorganizer.entity.user.User;
import com.sroka.grouptripsorganizer.exception.InvalidExactAmountBillShareException;
import com.sroka.grouptripsorganizer.mapper.BillShareMapper;
import com.sroka.grouptripsorganizer.repository.bill.BillShareRepository;
import com.sroka.grouptripsorganizer.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static java.math.BigDecimal.ZERO;
import static java.util.stream.Collectors.toList;

@Component
@Transactional
@RequiredArgsConstructor
public class SplitBillByExactAmountsImpl implements SplitBillStrategy {
    private final BillShareMapper billShareMapper;
    private final BillShareRepository billShareRepository;
    private final UserRepository userRepository;

    @Override
    public List<BillShareDto> splitBill(Bill bill, SplitBillData splitBillData) {
        List<Long> debtorsIds = splitBillData.getDebtorsIds();
        List<BigDecimal> exactAmounts = splitBillData.getExactAmounts();

        List<BillShare> billShares = new ArrayList<>();

        BigDecimal exactAmountSum = exactAmounts.stream().reduce(ZERO, BigDecimal::add);

        if (exactAmountSum.compareTo(bill.getTotalAmount()) != 0) {
            throw new InvalidExactAmountBillShareException();
        }

        List<Pair<Long, BigDecimal>> debtorsAmounts = Streams.zip(debtorsIds.stream(), exactAmounts.stream(),
                Pair::of).toList();

        debtorsAmounts.forEach(debtorAmount -> {
            User debtor = userRepository.getById(debtorAmount.getFirst());

            validateBillShareUser(debtor, bill.getTrip());
            validateBillShareDebtor(debtor, bill);

            BigDecimal amount = debtorAmount.getSecond();
            if (amount.compareTo(ZERO) <= 0) {
                throw new InvalidExactAmountBillShareException();
            }


            BillShare billShare = new BillShare(bill.getPayer(), debtor, amount, bill);
            if (debtor.equals(bill.getPayer())) {
                billShare.setPaid(true);
            }

            billShares.add(billShare);
        });

        billShares.forEach(billShareRepository::save);
        return billShares.stream().map(billShareMapper::convertToDto).collect(toList());
    }
}
