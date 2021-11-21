package com.sroka.grouptripsorganizer.controller.bill;

import com.sroka.grouptripsorganizer.controller.BaseController;
import com.sroka.grouptripsorganizer.dto.bill.BillShareCreateDto;
import com.sroka.grouptripsorganizer.dto.bill.BillShareDto;
import com.sroka.grouptripsorganizer.security.AuthenticationContextService;
import com.sroka.grouptripsorganizer.service.bill.BillShareService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/api/bill-shares")
@RequiredArgsConstructor
public class BillShareController extends BaseController {
    private final BillShareService billShareService;
    private final AuthenticationContextService authenticationContextService;

    @PreAuthorize("permitAll()")
    @PostMapping
    @ResponseStatus(CREATED)
    public List<BillShareDto> splitBill(@Valid @RequestBody BillShareCreateDto billShareCreateDto) {
        Long currentUserId = authenticationContextService.getCurrentUserId();

        return billShareService.splitBill(billShareCreateDto, currentUserId);
    }

    @PreAuthorize("permitAll()")
    @PutMapping("/{billShareId}")
    public List<BillShareDto> markBillShareAsPaid(@PathVariable("billShareId") Long billShareId) {
        Long currentUserId = authenticationContextService.getCurrentUserId();

        return billShareService.markBillShareAsPaid(billShareId, currentUserId);
    }

    @PreAuthorize("permitAll()")
    @GetMapping("/bills/{billId}")
    public List<BillShareDto> getBillSharesByBillId(@PathVariable Long billId) {
        Long currentUserId = authenticationContextService.getCurrentUserId();

        return billShareService.getByBillId(billId, currentUserId);
    }
}
