package com.sroka.grouptripsorganizer.entity.bill;

import com.sroka.grouptripsorganizer.entity.BaseEntity;
import com.sroka.grouptripsorganizer.entity.trip.Trip;
import com.sroka.grouptripsorganizer.entity.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.EnumType.STRING;

@Entity
@Table(name = "bills")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
// eksport do PDF/ CSV?
public class Bill extends BaseEntity {
    @Column(name = "title", nullable = false, length = 100)
    private String title;

    @Column(name = "bill_category", nullable = false)
    @Enumerated(STRING)
    private BillCategory category;

    @ManyToOne
    @JoinColumn(name = "payer_id", nullable = false)
    private User payer;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "total_amount", nullable = false)
    private BigDecimal totalAmount;

    @Column(name = "currency", nullable = false)
    @Enumerated(STRING)
    private Currency currency;

    @Column(name = "split_category", nullable = false)
    @Enumerated(STRING)
    private SplitCategory splitCategory;

    @ManyToOne
    @JoinColumn(name = "trip_id", nullable = false)
    private Trip trip;

    @OneToMany(mappedBy = "bill", cascade = ALL)
    private List<BillShare> billShares;

    @Column(name = "paid", nullable = false)
    private boolean paid = false;
}
