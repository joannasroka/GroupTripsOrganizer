package com.sroka.grouptripsorganizer.service.bill.split_bill_strategy;

import com.google.common.collect.Streams;
import com.sroka.grouptripsorganizer.dto.bill.BillShareDto;
import com.sroka.grouptripsorganizer.entity.bill.Bill;
import com.sroka.grouptripsorganizer.entity.bill.BillShare;
import com.sroka.grouptripsorganizer.entity.user.User;
import com.sroka.grouptripsorganizer.exception.InvalidPercentageBillShareException;
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
import java.util.concurrent.atomic.AtomicInteger;

import static java.math.RoundingMode.UP;
import static java.util.stream.Collectors.toList;

@Component
@Transactional
@RequiredArgsConstructor
public class SplitBillByPercentagesImpl implements SplitBillStrategy {
    private final BillShareMapper billShareMapper;
    private final BillShareRepository billShareRepository;
    private final UserRepository userRepository;

    @Override
    public List<BillShareDto> splitBill(Bill bill, SplitBillData splitBillData) {
        List<Long> debtorsIds = splitBillData.getDebtorsIds();
        List<Integer> percentages = splitBillData.getPercentages();
        List<BillShare> billShares = new ArrayList<>();

        AtomicInteger percentagesSum = new AtomicInteger();

        List<Pair<Long, Integer>> debtorsPercentages = Streams.zip(debtorsIds.stream(), percentages.stream(),
                Pair::of).toList();

        debtorsPercentages.forEach(debtorPercentage -> {
            User debtor = userRepository.getById(debtorPercentage.getFirst());

            validateBillShareUser(debtor, bill.getTrip());
            validateBillShareDebtor(debtor, bill);

            int percentage = debtorPercentage.getSecond();
            if (percentage <= 0) {
                throw new InvalidPercentageBillShareException();
            }

            percentagesSum.set(percentagesSum.get() + percentage);
            if (percentagesSum.get() > 100) {
                throw new InvalidPercentageBillShareException();
            }

            BigDecimal multiplier = new BigDecimal(percentage).divide(new BigDecimal(100), 2, UP);
            BigDecimal amountToSplit = bill.getTotalAmount().multiply(multiplier);
            amountToSplit = amountToSplit.setScale(2, UP);

            BillShare billShare = new BillShare(bill.getPayer(), debtor, amountToSplit, bill);
            if (debtor.equals(bill.getPayer())) {
                billShare.setPaid(true);
            }

            billShares.add(billShare);
        });

        if (percentagesSum.get() != 100) {
            throw new InvalidPercentageBillShareException();
        }

        billShares.forEach(billShareRepository::save);
        return billShares.stream().map(billShareMapper::convertToDto).collect(toList());
    }
}
