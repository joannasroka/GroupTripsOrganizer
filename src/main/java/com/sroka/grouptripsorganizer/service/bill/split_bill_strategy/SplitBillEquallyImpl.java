package com.sroka.grouptripsorganizer.service.bill.split_bill_strategy;

import com.sroka.grouptripsorganizer.dto.bill.BillShareDto;
import com.sroka.grouptripsorganizer.entity.bill.Bill;
import com.sroka.grouptripsorganizer.entity.bill.BillShare;
import com.sroka.grouptripsorganizer.entity.user.User;
import com.sroka.grouptripsorganizer.mapper.BillShareMapper;
import com.sroka.grouptripsorganizer.repository.bill.BillShareRepository;
import com.sroka.grouptripsorganizer.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static java.math.RoundingMode.UP;

@Component
@Transactional
@RequiredArgsConstructor
public class SplitBillEquallyImpl implements SplitBillStrategy {
    private final BillShareMapper billShareMapper;
    private final BillShareRepository billShareRepository;
    private final UserRepository userRepository;

    @Override
    public List<BillShareDto> splitBill(Bill bill, SplitBillData splitBillData) {
        List<Long> debtorsIds = splitBillData.getDebtorsIds();
        int numberOfDebtors = debtorsIds.size();

        BigDecimal amountToSplit = bill.getTotalAmount().divide(new BigDecimal(numberOfDebtors), 2, UP);
        List<BillShareDto> billShareDtoList = new ArrayList<>();

        debtorsIds.forEach(debtorId -> {
            User debtor = userRepository.getById(debtorId);

            validateBillShareUser(debtor, bill.getTrip());
            validateBillShareDebtor(debtor, bill);

            BillShare billShare = new BillShare(bill.getPayer(), debtor, amountToSplit, bill);
            if (debtor.equals(bill.getPayer())) {
                billShare.setPaid(true);
            }

            billShare = billShareRepository.save(billShare);
            billShareDtoList.add(billShareMapper.convertToDto(billShare));
        });
        return billShareDtoList;
    }
}
