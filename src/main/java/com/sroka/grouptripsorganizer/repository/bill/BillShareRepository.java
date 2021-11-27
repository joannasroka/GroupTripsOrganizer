package com.sroka.grouptripsorganizer.repository.bill;

import com.sroka.grouptripsorganizer.entity.bill.BillShare;
import com.sroka.grouptripsorganizer.exception.DatabaseEntityNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BillShareRepository extends JpaRepository<BillShare, Long> {
    default BillShare getById(Long id) {
        return findById(id).orElseThrow(DatabaseEntityNotFoundException::new);
    }

    List<BillShare> getByBillId (Long billId);
}
