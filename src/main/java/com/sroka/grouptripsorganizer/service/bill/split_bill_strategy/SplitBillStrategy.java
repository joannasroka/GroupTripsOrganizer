package com.sroka.grouptripsorganizer.service.bill.split_bill_strategy;

import com.sroka.grouptripsorganizer.dto.bill.BillShareDto;
import com.sroka.grouptripsorganizer.entity.bill.Bill;
import com.sroka.grouptripsorganizer.entity.trip.Trip;
import com.sroka.grouptripsorganizer.entity.user.User;
import com.sroka.grouptripsorganizer.exception.BillShareForThisUserAlreadyExistsException;
import com.sroka.grouptripsorganizer.exception.DatabaseEntityNotFoundException;

import java.util.List;

public interface SplitBillStrategy {
    List<BillShareDto> splitBill(Bill bill, SplitBillData splitBillData);

    default void validateBillShareUser(User executor, Trip trip) {
        if (!trip.getParticipants().contains(executor)) {
            throw new DatabaseEntityNotFoundException();
        }
    }

    default void validateBillShareDebtor(User user, Bill bill) {
        if (bill.getBillShares().stream().anyMatch(billShare -> billShare.getDebtor().equals(user))) {
            throw new BillShareForThisUserAlreadyExistsException();
        }
    }
}
