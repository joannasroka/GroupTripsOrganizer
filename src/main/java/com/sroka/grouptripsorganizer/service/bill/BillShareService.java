package com.sroka.grouptripsorganizer.service.bill;

import com.sroka.grouptripsorganizer.dto.bill.BillShareCreateDto;
import com.sroka.grouptripsorganizer.dto.bill.BillShareDto;
import com.sroka.grouptripsorganizer.entity.bill.Bill;
import com.sroka.grouptripsorganizer.entity.bill.BillShare;
import com.sroka.grouptripsorganizer.entity.group.Group;
import com.sroka.grouptripsorganizer.entity.user.User;
import com.sroka.grouptripsorganizer.exception.BillShareForThisUserAlreadyExistsException;
import com.sroka.grouptripsorganizer.exception.UserNotInThisGroupException;
import com.sroka.grouptripsorganizer.mapper.BillShareMapper;
import com.sroka.grouptripsorganizer.repository.bill.BillRepository;
import com.sroka.grouptripsorganizer.repository.bill.BillShareRepository;
import com.sroka.grouptripsorganizer.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static java.math.RoundingMode.UP;

@Service
@Transactional
@RequiredArgsConstructor
public class BillShareService {
    private final BillShareMapper billShareMapper;

    private final UserRepository userRepository;
    private final BillShareRepository billShareRepository;
    private final BillRepository billRepository;

    public List<BillShareDto> splitBill(BillShareCreateDto billShareCreateDto, Long executorId) {
        User executor = userRepository.getById(executorId);
        List<Long> debtorsIds = billShareCreateDto.getDebtorsIds();
        Long billId = billShareCreateDto.getBillId();

        Bill bill = billRepository.getById(billId);
        validateBillShareUser(executor, bill.getGroup());

        List<BillShareDto> billShareDtoList = new ArrayList<>();

        switch (bill.getSplitCategory()) {
            case EQUALLY -> billShareDtoList = splitBillEqually(bill, debtorsIds);
        }

        return billShareDtoList;
    }

    public List<BillShareDto> markBillShareAsPaid(Long billShareId, Long executorId) {
        BillShare billShare = billShareRepository.getById(billShareId);
        Bill bill = billShare.getBill();
        User executor = userRepository.getById(executorId);

        validateBillShareUser(executor, bill.getGroup());

        billShare.setPaid(true);

        List <BillShare> billShares = bill.getBillShares();
        if (billShares.stream().allMatch(BillShare::isPaid)) {
            bill.setPaid(true);
        }
        return billShareMapper.convertToDtos(billShares);
    }

    private List<BillShareDto> splitBillEqually(Bill bill, List<Long> debtorsIds) {
        int numberOfDebtors = debtorsIds.size();

        BigDecimal amountToSplit = bill.getTotalAmount().divide(new BigDecimal(numberOfDebtors), 2, UP);
        List<BillShareDto> billShareDtoList = new ArrayList<>();

        debtorsIds.forEach(debtorId -> {
            User debtor = userRepository.getById(debtorId);

            validateBillShareUser(debtor, bill.getGroup());
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

    private void validateBillShareUser(User executor, Group group) {
        if (!group.getParticipants().contains(executor)) {
            throw new UserNotInThisGroupException();
        }
    }

    private void validateBillShareDebtor(User user, Bill bill) {
        if (bill.getBillShares().stream().anyMatch(billShare -> billShare.getDebtor().equals(user))) {
            throw new BillShareForThisUserAlreadyExistsException();
        }
    }
}
