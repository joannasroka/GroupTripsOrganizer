package com.sroka.grouptripsorganizer.service.bill.split_bill_strategy;

import com.google.common.collect.Streams;
import com.sroka.grouptripsorganizer.dto.bill.BillShareDto;
import com.sroka.grouptripsorganizer.entity.bill.Bill;
import com.sroka.grouptripsorganizer.entity.bill.BillShare;
import com.sroka.grouptripsorganizer.entity.user.User;
import com.sroka.grouptripsorganizer.exception.InvalidShareBillShareException;
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

import static java.math.RoundingMode.UP;
import static java.util.stream.Collectors.toList;

@Component
@Transactional
@RequiredArgsConstructor
public class SplitBillBySharesImpl implements SplitBillStrategy {
    private final BillShareMapper billShareMapper;
    private final BillShareRepository billShareRepository;
    private final UserRepository userRepository;

    @Override
    public List<BillShareDto> splitBill(Bill bill, SplitBillData splitBillData) {
        List<Long> debtorsIds = splitBillData.getDebtorsIds();
        List<Integer> shares = splitBillData.getShares();
        List<BillShare> billShares = new ArrayList<>();

        int sharesSum = shares.stream().mapToInt(Integer::intValue).sum();

        List<Pair<Long, Integer>> debtorsShares = Streams.zip(debtorsIds.stream(), shares.stream(),
                Pair::of).toList();

        debtorsShares.forEach(debtorShare -> {
            User debtor = userRepository.getById(debtorShare.getFirst());

            validateBillShareUser(debtor, bill.getTrip());
            validateBillShareDebtor(debtor, bill);

            int share = debtorShare.getSecond();
            if (share <= 0) {
                throw new InvalidShareBillShareException();
            }

            BigDecimal amountToSplit = bill.getTotalAmount().divide(new BigDecimal(sharesSum), 2, UP);
            amountToSplit = amountToSplit.multiply(new BigDecimal(share));
            amountToSplit = amountToSplit.setScale(2, UP);

            BillShare billShare = new BillShare(bill.getPayer(), debtor, amountToSplit, bill);
            if (debtor.equals(bill.getPayer())) {
                billShare.setPaid(true);
            }

            billShares.add(billShare);
        });

        billShares.forEach(billShareRepository::save);
        return billShares.stream().map(billShareMapper::convertToDto).collect(toList());
    }
}
