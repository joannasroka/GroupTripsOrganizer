package com.sroka.grouptripsorganizer.service.bill;

import com.sroka.grouptripsorganizer.dto.bill.BillCreateDto;
import com.sroka.grouptripsorganizer.dto.bill.BillDto;
import com.sroka.grouptripsorganizer.entity.bill.Bill;
import com.sroka.grouptripsorganizer.entity.group.Group;
import com.sroka.grouptripsorganizer.entity.user.User;
import com.sroka.grouptripsorganizer.exception.UserNotInThisGroupException;
import com.sroka.grouptripsorganizer.mapper.BillMapper;
import com.sroka.grouptripsorganizer.repository.bill.BillRepository;
import com.sroka.grouptripsorganizer.repository.group.GroupRepository;
import com.sroka.grouptripsorganizer.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class BillService {
    private final BillMapper billMapper;

    private final BillRepository billRepository;
    private final UserRepository userRepository;
    private final GroupRepository groupRepository;

    public BillDto create(BillCreateDto billCreateDto, Long executorId) {
        Group group = groupRepository.getById(billCreateDto.getGroupId());
        User executor = userRepository.getById(executorId);
        User payer = userRepository.getById(billCreateDto.getPayerId());

        validate(executor, payer, group);

        Bill newBill = billMapper.convertToEntity(billCreateDto);
        newBill.setGroup(group);
        newBill.setPayer(payer);
        newBill.setPaid(false);

        Bill savedBill = billRepository.save(newBill);

        return billMapper.convertToDto(savedBill);
    }

    private void validate(User executor, User payer, Group group) {
        if (!group.getParticipants().contains(executor) || !group.getParticipants().contains(payer)) {
            throw new UserNotInThisGroupException();
        }
    }
}
