package com.sroka.grouptripsorganizer.service.bill;

import com.sroka.grouptripsorganizer.dto.bill.BillCreateDto;
import com.sroka.grouptripsorganizer.dto.bill.BillDto;
import com.sroka.grouptripsorganizer.entity.bill.Bill;
import com.sroka.grouptripsorganizer.entity.trip.Trip;
import com.sroka.grouptripsorganizer.entity.user.User;
import com.sroka.grouptripsorganizer.exception.DatabaseEntityNotFoundException;
import com.sroka.grouptripsorganizer.mapper.BillMapper;
import com.sroka.grouptripsorganizer.repository.bill.BillRepository;
import com.sroka.grouptripsorganizer.repository.trip.TripRepository;
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
    private final TripRepository tripRepository;

    public BillDto create(BillCreateDto billCreateDto, Long executorId) {
        Trip trip = tripRepository.getById(billCreateDto.getTripId());
        User executor = userRepository.getById(executorId);
        User payer = userRepository.getById(billCreateDto.getPayerId());

        validate(executor, payer, trip);

        Bill newBill = billMapper.convertToEntity(billCreateDto);
        newBill.setTrip(trip);
        newBill.setPayer(payer);
        newBill.setPaid(false);

        Bill savedBill = billRepository.save(newBill);

        return billMapper.convertToDto(savedBill);
    }

    private void validate(User executor, User payer, Trip trip) {
        if (!trip.getParticipants().contains(executor) || !trip.getParticipants().contains(payer)) {
            throw new DatabaseEntityNotFoundException();
        }
    }
}
