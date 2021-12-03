package com.sroka.grouptripsorganizer.builder.bill;

import com.sroka.grouptripsorganizer.builder.user.UserBuilder;
import com.sroka.grouptripsorganizer.entity.bill.Bill;
import com.sroka.grouptripsorganizer.entity.bill.BillShare;
import com.sroka.grouptripsorganizer.entity.user.User;

import java.math.BigDecimal;

import static java.math.BigDecimal.valueOf;

public class BillShareBuilder {
    private BillShare billShare;

    private BillShareBuilder() {
        User payer = UserBuilder.get().build();
        User debtor = UserBuilder.get().build();
        BigDecimal amount = valueOf(10.00);
        Bill bill = BillBuilder.get().build();

        billShare = new BillShare(payer, debtor, amount, bill);
    }

    public BillShareBuilder withPayer(User payer) {
        billShare.setPayer(payer);
        return this;
    }

    public BillShareBuilder withDebtor(User debtor) {
        billShare.setDebtor(debtor);
        return this;
    }

    public BillShareBuilder withAmount(BigDecimal amount) {
        billShare.setAmount(amount);
        return this;
    }

    public BillShareBuilder withPaid(boolean paid) {
        billShare.setPaid(paid);
        return this;
    }

    public BillShareBuilder withBill(Bill bill) {
        billShare.setBill(bill);
        return this;
    }

    public BillShare build() {
        return billShare;
    }

    public static BillShareBuilder get() {
        return new BillShareBuilder();
    }
}
