package com.sroka.grouptripsorganizer.builder.bill;

import com.sroka.grouptripsorganizer.builder.trip.TripBuilder;
import com.sroka.grouptripsorganizer.builder.user.UserBuilder;
import com.sroka.grouptripsorganizer.entity.bill.*;
import com.sroka.grouptripsorganizer.entity.trip.Trip;
import com.sroka.grouptripsorganizer.entity.user.User;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.sroka.grouptripsorganizer.entity.bill.BillCategory.SOUVENIRS;
import static com.sroka.grouptripsorganizer.entity.bill.Currency.EUR;
import static com.sroka.grouptripsorganizer.entity.bill.SplitCategory.EQUALLY;
import static java.math.BigDecimal.valueOf;

public class BillBuilder {
    private Bill bill;

    private BillBuilder() {
        String title = "Bill Title";
        BillCategory billCategory = SOUVENIRS;
        User payer = UserBuilder.get().build();
        LocalDate date = LocalDate.now();
        BigDecimal totalAmount = valueOf(100.00);
        Currency currency = EUR;
        SplitCategory splitCategory = EQUALLY;
        Trip trip = TripBuilder.get().withOwner(payer).build();
        boolean paid = false;

        bill = new Bill(title, billCategory, payer, date, totalAmount, currency, splitCategory, trip, new ArrayList<>(), paid);
    }

    public BillBuilder withTitle(String title) {
        bill.setTitle(title);
        return this;
    }

    public BillBuilder withBillCategory(BillCategory billCategory) {
        bill.setCategory(billCategory);
        return this;
    }

    public BillBuilder withPayer(User payer) {
        bill.setPayer(payer);
        return this;
    }

    public BillBuilder withDate(LocalDate date) {
        bill.setDate(date);
        return this;
    }

    public BillBuilder withTotalAmount(BigDecimal totalAmount) {
        bill.setTotalAmount(totalAmount);
        return this;
    }

    public BillBuilder withCurrency(Currency currency) {
        bill.setCurrency(currency);
        return this;
    }

    public BillBuilder withTrip(Trip trip) {
        bill.setTrip(trip);
        return this;
    }

    public BillBuilder withPaid(boolean paid) {
        bill.setPaid(paid);
        return this;
    }

    public BillBuilder withBillShares(List<BillShare> billShares) {
        bill.setBillShares(billShares);
        return this;
    }

    public Bill build() {
        return bill;
    }

    public static BillBuilder get() {
        return new BillBuilder();
    }
}
