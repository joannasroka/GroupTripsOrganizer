package com.sroka.grouptripsorganizer.controller.bill;

import com.sroka.grouptripsorganizer.controller.BaseController;
import com.sroka.grouptripsorganizer.controller.sorting_pagination.CustomPageable;
import com.sroka.grouptripsorganizer.dto.bill.BillCreateDto;
import com.sroka.grouptripsorganizer.dto.bill.BillDto;
import com.sroka.grouptripsorganizer.dto.bill.BillUpdateDto;
import com.sroka.grouptripsorganizer.security.AuthenticationContextService;
import com.sroka.grouptripsorganizer.service.bill.BillService;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Set;

import static com.sroka.grouptripsorganizer.entity.bill.Bill.DATE_FIELD_NAME;
import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/api/bills")
@RequiredArgsConstructor
public class BillController extends BaseController {
    private final BillService billService;

    private final AuthenticationContextService authenticationContextService;

    private static final int MAX_ALLOWED_BILLS_PER_PAGE = 25;
    private static final Set<String> ALLOWED_BILL_SORTING_PARAMS = Set.of(DATE_FIELD_NAME);

    @PreAuthorize("permitAll()")
    @PostMapping
    @ResponseStatus(CREATED)
    public BillDto createBill(@Valid @RequestBody BillCreateDto billCreateDto) {
        Long currentUserId = authenticationContextService.getCurrentUserId();

        return billService.create(billCreateDto, currentUserId);
    }

    @PreAuthorize("permitAll()")
    @PutMapping("/{billId}")
    public BillDto updateBill(@PathVariable Long billId,
                              @RequestBody @Valid BillUpdateDto billUpdateDto) {
        Long currentUserId = authenticationContextService.getCurrentUserId();

        return billService.update(billUpdateDto, billId, currentUserId);
    }


    @PreAuthorize("permitAll()")
    @DeleteMapping("/{billId}")
    public void deleteBill(@PathVariable Long billId) {
        Long currentUserId = authenticationContextService.getCurrentUserId();
        billService.delete(billId, currentUserId);
    }

    @PreAuthorize("permitAll()")
    @GetMapping("/trips/{tripId}")
    @CustomPageable
    public Page<BillDto> getBillsByTrip(@PathVariable Long tripId,
                                        @PageableDefault(sort = DATE_FIELD_NAME) @Parameter(hidden = true) Pageable pageable) {
        pageSizeValidation(pageable, MAX_ALLOWED_BILLS_PER_PAGE);
        sortParametersValidation(pageable, ALLOWED_BILL_SORTING_PARAMS);

        Long currentUserId = authenticationContextService.getCurrentUserId();

        return billService.getByTrip(tripId, currentUserId, pageable);
    }

    @PreAuthorize("permitAll()")
    @GetMapping("/{billId}")
    public BillDto getBillById(@PathVariable Long billId) {
        Long currentUserId = authenticationContextService.getCurrentUserId();

        return billService.getById(billId, currentUserId);
    }
}
