package com.sroka.grouptripsorganizer.service.bill;

import com.sroka.grouptripsorganizer.dto.bill.BillCreateDto;
import com.sroka.grouptripsorganizer.dto.bill.BillDto;
import com.sroka.grouptripsorganizer.dto.bill.BillUpdateDto;
import com.sroka.grouptripsorganizer.entity.bill.Bill;
import com.sroka.grouptripsorganizer.entity.trip.Trip;
import com.sroka.grouptripsorganizer.entity.user.User;
import com.sroka.grouptripsorganizer.exception.BillIsNotSettledException;
import com.sroka.grouptripsorganizer.exception.DatabaseEntityNotFoundException;
import com.sroka.grouptripsorganizer.mapper.BillMapper;
import com.sroka.grouptripsorganizer.repository.bill.BillRepository;
import com.sroka.grouptripsorganizer.repository.trip.TripRepository;
import com.sroka.grouptripsorganizer.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
@RequiredArgsConstructor
public class BillService {
    private final BillMapper billMapper;

    private final BillRepository billRepository;
    private final UserRepository userRepository;
    private final TripRepository tripRepository;

    public BillDto create(BillCreateDto billCreateDto, Long executorId) {
        Trip trip = tripRepository.getById(billCreateDto.getTripId());
        User executor = userRepository.getById(executorId);
        User payer = userRepository.getById(billCreateDto.getPayerId());

        validate(executor, trip);
        validate(payer, trip);

        Bill newBill = billMapper.convertToEntity(billCreateDto);
        newBill.setTrip(trip);
        newBill.setPayer(payer);
        newBill.setPaid(false);

        Bill savedBill = billRepository.save(newBill);

        return billMapper.convertToDto(savedBill);
    }

    public BillDto update(BillUpdateDto billUpdateDto, Long billId, Long executorId) {
        Bill bill = billRepository.getById(billId);

        Trip trip = bill.getTrip();
        User executor = userRepository.getById(executorId);

        validate(executor, trip);

        bill.setCategory(billUpdateDto.getCategory());
        bill.setDate(billUpdateDto.getDate());
        bill.setTitle(billUpdateDto.getTitle());

        return billMapper.convertToDto(bill);
    }


    public void delete(Long billId, Long executorId) {
        User executor = userRepository.getById(executorId);
        Bill billToDelete = billRepository.getById(billId);
        Trip trip = billToDelete.getTrip();

        validate(executor, trip);
        validateBillToDelete(billToDelete);

        billRepository.delete(billToDelete);
    }

    @Transactional(readOnly = true)
    public Page<BillDto> getByTrip(Long tripId, Long executorId, Pageable pageable) {
        User executor = userRepository.getById(executorId);
        Trip trip = tripRepository.getById(tripId);
        validate(executor, trip);

        Page<Bill> bills = billRepository.findAllByTripOrderByDate(trip, pageable);
        return bills.map(billMapper::convertToDto);
    }

    @Transactional(readOnly = true)
    public BillDto getById(Long billId, Long executorId) {
        User executor = userRepository.getById(executorId);
        Bill bill = billRepository.getById(billId);
        Trip trip = bill.getTrip();

        validate(executor, trip);

        return billMapper.convertToDto(bill);
    }

    private void validate(User user, Trip trip) {
        if (!trip.getParticipants().contains(user)) {
            throw new DatabaseEntityNotFoundException();
        }
    }

    private void validateBillToDelete(Bill bill) {
        if (!bill.isPaid()) {
            throw new BillIsNotSettledException();
        }
    }
}
