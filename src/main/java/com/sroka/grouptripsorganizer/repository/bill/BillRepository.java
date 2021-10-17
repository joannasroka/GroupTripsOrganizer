package com.sroka.grouptripsorganizer.repository.bill;

import com.sroka.grouptripsorganizer.entity.bill.Bill;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BillRepository extends JpaRepository<Bill, Long> {
}
