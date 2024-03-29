package com.sroka.grouptripsorganizer.entity.bill;

import com.sroka.grouptripsorganizer.entity.BaseEntity;
import com.sroka.grouptripsorganizer.entity.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.minidev.json.annotate.JsonIgnore;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "bill_shares")
@Getter
@Setter
@NoArgsConstructor
public class BillShare extends BaseEntity {
    @ManyToOne
    @JoinColumn(name = "payer_id", nullable = false)
    private User payer;

    @ManyToOne
    @JoinColumn(name = "debtor_id", nullable = false)
    private User debtor;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @Column(name = "paid", nullable = false)
    private boolean paid = false;

    @ManyToOne
    @JoinColumn(name = "bill_id", nullable = false)
    private Bill bill;

    public BillShare(User payer, User debtor, BigDecimal amount, Bill bill) {
        this.payer = payer;
        this.debtor = debtor;
        this.amount = amount;
        this.bill = bill;
        paid = false;
    }
}
