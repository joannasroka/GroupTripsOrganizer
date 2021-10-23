package com.sroka.grouptripsorganizer.repository.bill;

import com.sroka.grouptripsorganizer.entity.bill.Bill;
import com.sroka.grouptripsorganizer.exception.DatabaseEntityNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BillRepository extends JpaRepository<Bill, Long> {
    default Bill getById(Long id) {
        return findById(id).orElseThrow(DatabaseEntityNotFoundException::new);
    }
}
