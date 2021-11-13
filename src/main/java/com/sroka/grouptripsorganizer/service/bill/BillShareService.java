package com.sroka.grouptripsorganizer.service.bill;

import com.google.common.collect.Streams;
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
import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static java.math.BigDecimal.ZERO;
import static java.math.RoundingMode.UP;
import static java.util.stream.Collectors.toList;

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
        List<Integer> percentages = billShareCreateDto.getPercentages();
        List<Integer> shares = billShareCreateDto.getShares();
        List<BigDecimal> exactAmounts = billShareCreateDto.getExactAmounts();

        Long billId = billShareCreateDto.getBillId();
        Bill bill = billRepository.getById(billId);

        validateBillShareUser(executor, bill.getTrip());

        List<BillShareDto> billShareDtoList = new ArrayList<>();

        switch (bill.getSplitCategory()) {
            case EQUALLY -> billShareDtoList = splitBillEqually(bill, debtorsIds);
            case BY_PERCENTAGES -> {
                validateBillShareByPercentages(billShareCreateDto);
                billShareDtoList = splitBillByPercentages(bill, debtorsIds, percentages);
            }
            case BY_SHARES -> {
                validateBillShareByShares(billShareCreateDto);
                billShareDtoList = splitBillByShares(bill, debtorsIds, shares);
            }
            case BY_EXACT_AMOUNTS -> {
                validateBillShareByExactAmounts(billShareCreateDto);
                billShareDtoList = splitBillByExactAmounts(bill, debtorsIds, exactAmounts);
            }
        }

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

    private List<BillShareDto> splitBillEqually(Bill bill, List<Long> debtorsIds) {
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

    private List<BillShareDto> splitBillByPercentages(Bill bill, List<Long> debtorsIds, List<Integer> percentages) {
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

    private List<BillShareDto> splitBillByShares(Bill bill, List<Long> debtorsIds, List<Integer> shares) {
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

    private List<BillShareDto> splitBillByExactAmounts(Bill bill, List<Long> debtorsIds, List<BigDecimal> exactAmounts) {
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

    private void validateBillShareUser(User executor, Trip trip) {
        if (!trip.getParticipants().contains(executor)) {
            throw new DatabaseEntityNotFoundException();
        }
    }

    private void validateBillShareDebtor(User user, Bill bill) {
        if (bill.getBillShares().stream().anyMatch(billShare -> billShare.getDebtor().equals(user))) {
            throw new BillShareForThisUserAlreadyExistsException();
        }
    }

    private void validateBillShareByPercentages(BillShareCreateDto billShareCreateDto) {
        if (billShareCreateDto.getPercentages() == null
                || (billShareCreateDto.getPercentages().size() != billShareCreateDto.getDebtorsIds().size())) {
            throw new InvalidPercentageBillShareException();
        }
    }

    private void validateBillShareByShares(BillShareCreateDto billShareCreateDto) {
        if (billShareCreateDto.getShares() == null
                || (billShareCreateDto.getShares().size() != billShareCreateDto.getDebtorsIds().size())) {
            throw new InvalidShareBillShareException();
        }
    }

    private void validateBillShareByExactAmounts(BillShareCreateDto billShareCreateDto) {
        if (billShareCreateDto.getExactAmounts() == null
                || (billShareCreateDto.getExactAmounts().size() != billShareCreateDto.getDebtorsIds().size())) {
            throw new InvalidExactAmountBillShareException();
        }
    }
}
