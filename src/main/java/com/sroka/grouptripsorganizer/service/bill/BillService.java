package com.sroka.grouptripsorganizer.service.bill;

import com.sroka.grouptripsorganizer.dto.bill.BillCreateDto;
import com.sroka.grouptripsorganizer.dto.bill.BillDto;
import com.sroka.grouptripsorganizer.entity.bill.Bill;
import com.sroka.grouptripsorganizer.entity.bill.BillShare;
import com.sroka.grouptripsorganizer.entity.group.Group;
import com.sroka.grouptripsorganizer.entity.user.User;
import com.sroka.grouptripsorganizer.exception.UserNotInThisGroupException;
import com.sroka.grouptripsorganizer.mapper.BillMapper;
import com.sroka.grouptripsorganizer.repository.bill.BillRepository;
import com.sroka.grouptripsorganizer.repository.bill.BillShareRepository;
import com.sroka.grouptripsorganizer.repository.group.GroupRepository;
import com.sroka.grouptripsorganizer.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;

import static java.math.RoundingMode.DOWN;

@Service
@Transactional
@RequiredArgsConstructor
public class BillService {
    private final BillMapper billMapper;

    private final BillRepository billRepository;
    private final UserRepository userRepository;
    private final GroupRepository groupRepository;
    private final BillShareRepository billShareRepository;

    public BillDto create(BillCreateDto billCreateDto, Long creatorId) {
        Group group = groupRepository.getById(billCreateDto.getGroupId());
        User creator = userRepository.getById(creatorId);
        User payer = userRepository.getById(billCreateDto.getPayerId());

        validate(creator, payer, group);

        Bill newBill = billMapper.convertToEntity(billCreateDto);
        newBill.setGroup(group);
        newBill.setPayer(payer);
        Bill savedBill = billRepository.save(newBill);

        return billMapper.convertToDto(savedBill);
    }

    private void splitBill(Bill bill, List<User> debtors) {
        int numberOfDebtors = debtors.size();
        BigDecimal amountToSplit = bill.getTotalAmount().divide(new BigDecimal(numberOfDebtors), 2, DOWN);

        debtors.forEach(debtor -> {
            BillShare billShare = new BillShare(bill.getPayer(), debtor, amountToSplit, bill);
            billShareRepository.save(billShare);
        });
    }

    private void validate(User creator, User payer, Group group) {
        if (!group.getParticipants().contains(creator) || !group.getParticipants().contains(payer)) {
            throw new UserNotInThisGroupException();
        }
    }
}
