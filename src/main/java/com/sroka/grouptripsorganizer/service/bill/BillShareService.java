package com.sroka.grouptripsorganizer.service.bill;

import com.sroka.grouptripsorganizer.dto.bill.BillShareCreateDto;
import com.sroka.grouptripsorganizer.dto.bill.BillShareDto;
import com.sroka.grouptripsorganizer.entity.bill.Bill;
import com.sroka.grouptripsorganizer.entity.bill.BillShare;
import com.sroka.grouptripsorganizer.entity.trip.Trip;
import com.sroka.grouptripsorganizer.entity.user.User;
import com.sroka.grouptripsorganizer.exception.*;
import com.sroka.grouptripsorganizer.mapper.BillShareMapper;
import com.sroka.grouptripsorganizer.repository.bill.BillRepository;
import com.sroka.grouptripsorganizer.repository.bill.BillShareRepository;
import com.sroka.grouptripsorganizer.repository.user.UserRepository;
import com.sroka.grouptripsorganizer.service.bill.split_bill_strategy.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
@Transactional
@RequiredArgsConstructor
public class BillShareService {
    private final BillShareMapper billShareMapper;

    private final UserRepository userRepository;
    private final BillShareRepository billShareRepository;
    private final BillRepository billRepository;

    private final SplitBillEquallyImpl splitBillEqually;
    private final SplitBillByPercentagesImpl splitBillByPercentages;
    private final SplitBillBySharesImpl splitBillByShares;
    private final SplitBillByExactAmountsImpl splitBillByExactAmounts;

    public List<BillShareDto> splitBill(BillShareCreateDto billShareCreateDto, Long executorId) {
        User executor = userRepository.getById(executorId);

        List<Long> debtorsIds = billShareCreateDto.getDebtorsIds();

        if (debtorsIds.isEmpty()) {
            throw new InvalidBillShareException();
        }

        List<Integer> percentages = billShareCreateDto.getPercentages();
        List<Integer> shares = billShareCreateDto.getShares();
        List<BigDecimal> exactAmounts = billShareCreateDto.getExactAmounts();
        SplitBillStrategy splitBillStrategy = splitBillEqually;

        Long billId = billShareCreateDto.getBillId();
        Bill bill = billRepository.getById(billId);
        SplitBillData splitBillData = new SplitBillData(bill, debtorsIds, percentages, shares, exactAmounts);

        validateBillShareUser(executor, bill.getTrip());
        
        switch (bill.getSplitCategory()) {
            case EQUALLY -> splitBillStrategy = splitBillEqually;
            case BY_PERCENTAGES -> {
                validateBillShareByPercentages(billShareCreateDto);
                splitBillStrategy = splitBillByPercentages;
            }
            case BY_SHARES -> {
                validateBillShareByShares(billShareCreateDto);
                splitBillStrategy = splitBillByShares;
            }
            case BY_EXACT_AMOUNTS -> {
                validateBillShareByExactAmounts(billShareCreateDto);
                splitBillStrategy = splitBillByExactAmounts;
            }
        }

        if(bill.getBillShares().stream().allMatch(billShare -> billShare.getDebtor().equals(bill.getPayer()))) {
            bill.setPaid(true);
        }

        List<BillShareDto> billShareDtoList = splitBillStrategy.splitBill(bill, splitBillData);

        return billShareDtoList;
    }

    public List<BillShareDto> markBillShareAsPaid(Long billShareId, Long executorId) {
        BillShare billShare = billShareRepository.getById(billShareId);
        Bill bill = billShare.getBill();
        User executor = userRepository.getById(executorId);

        validateBillShareUser(executor, bill.getTrip());

        billShare.setPaid(true);

        List<BillShare> billShares = bill.getBillShares();
        if (billShares.stream().allMatch(BillShare::isPaid)) {
            bill.setPaid(true);
        }
        return billShareMapper.convertToDtos(billShares);
    }

    public List<BillShareDto> markBillShareAsUnpaid(Long billShareId, Long executorId) {
        BillShare billShare = billShareRepository.getById(billShareId);
        Bill bill = billShare.getBill();
        User executor = userRepository.getById(executorId);

        validateBillShareUser(executor, bill.getTrip());

        billShare.setPaid(false);
        bill.setPaid(false);

        List<BillShare> billShares = bill.getBillShares();

        return billShareMapper.convertToDtos(billShares);
    }

    @Transactional(readOnly = true)
    public List<BillShareDto> getByBillId(Long billId, Long executorId) {
        User executor = userRepository.getById(executorId);
        Bill bill = billRepository.getById(billId);
        Trip trip = bill.getTrip();

        validate(executor, trip);

        List<BillShare> billShares = bill.getBillShares();

        return billShareMapper.convertToDtos(billShares);
    }

    public void delete(Long billShareId, Long executorId) {
        User executor = userRepository.getById(executorId);
        BillShare billShareToDelete = billShareRepository.getById(billShareId);
        Bill bill = billShareToDelete.getBill();
        Trip trip = bill.getTrip();

        validate(executor, trip);

        bill.getBillShares().remove(billShareToDelete);
        billShareRepository.delete(billShareToDelete);

        List<BillShare> billShares = bill.getBillShares();
        if (!billShares.isEmpty() && billShares.stream().allMatch(BillShare::isPaid)) {
            bill.setPaid(true);
        }
    }

    private void validateBillShareUser(User executor, Trip trip) {
        if (!trip.getParticipants().contains(executor)) {
            throw new DatabaseEntityNotFoundException();
        }
    }

    private void validateBillShareByPercentages(BillShareCreateDto billShareCreateDto) {
        if (billShareCreateDto.getPercentages() == null
                || (billShareCreateDto.getPercentages().size() != billShareCreateDto.getDebtorsIds().size())) {
            throw new InvalidPercentageBillShareException();
        }
        if(billShareCreateDto.getPercentages().stream().anyMatch(Objects::isNull)) {
            throw new InvalidBillShareException();
        }
    }

    private void validateBillShareByShares(BillShareCreateDto billShareCreateDto) {
        if (billShareCreateDto.getShares() == null
                || (billShareCreateDto.getShares().size() != billShareCreateDto.getDebtorsIds().size())) {
            throw new InvalidShareBillShareException();
        }
        if(billShareCreateDto.getShares().stream().anyMatch(Objects::isNull)) {
            throw new InvalidBillShareException();
        }
    }

    private void validateBillShareByExactAmounts(BillShareCreateDto billShareCreateDto) {
        if (billShareCreateDto.getExactAmounts() == null
                || (billShareCreateDto.getExactAmounts().size() != billShareCreateDto.getDebtorsIds().size())) {
            throw new InvalidExactAmountBillShareException();
        }
        if(billShareCreateDto.getExactAmounts().stream().anyMatch(Objects::isNull)) {
            throw new InvalidBillShareException();
        }
    }

    private void validate(User user, Trip trip) {
        if (!trip.getParticipants().contains(user)) {
            throw new DatabaseEntityNotFoundException();
        }
    }
}
