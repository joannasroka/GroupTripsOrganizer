package com.sroka.grouptripsorganizer.controller.bill;

import com.sroka.grouptripsorganizer.controller.BaseController;
import com.sroka.grouptripsorganizer.dto.bill.BillCreateDto;
import com.sroka.grouptripsorganizer.dto.bill.BillDto;
import com.sroka.grouptripsorganizer.entity.user.User;
import com.sroka.grouptripsorganizer.security.AuthenticationContextService;
import com.sroka.grouptripsorganizer.service.bill.BillService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/api/bills")
@RequiredArgsConstructor
public class BillController extends BaseController {
    private final BillService billService;

    private final AuthenticationContextService authenticationContextService;

    @PreAuthorize("permitAll()")
    @PostMapping
    @ResponseStatus(CREATED)
    public BillDto createBill(@Valid @RequestBody BillCreateDto billCreateDto) {
        Long currentUserId = authenticationContextService.getCurrentUserId();

        return billService.create(billCreateDto, currentUserId);
    }
}
