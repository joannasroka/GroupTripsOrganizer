package com.sroka.grouptripsorganizer.service.bill;

import com.sroka.grouptripsorganizer.builder.bill.BillBuilder;
import com.sroka.grouptripsorganizer.builder.bill.BillShareBuilder;
import com.sroka.grouptripsorganizer.builder.trip.TripBuilder;
import com.sroka.grouptripsorganizer.builder.user.UserBuilder;
import com.sroka.grouptripsorganizer.entity.bill.Bill;
import com.sroka.grouptripsorganizer.entity.bill.BillShare;
import com.sroka.grouptripsorganizer.entity.trip.Trip;
import com.sroka.grouptripsorganizer.entity.user.User;
import com.sroka.grouptripsorganizer.repository.bill.BillRepository;
import com.sroka.grouptripsorganizer.repository.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class BillServiceTest {

    @InjectMocks
    private BillService billService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BillRepository billRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Should mark Bill as paid")
    void shouldMarkBillAsPaid() {
        // given
        Long billId = 1L;
        Long executorId = 2L;

        User executor = UserBuilder.get()
                .build();

        Trip trip = TripBuilder.get()
                .withParticipants(Set.of(executor))
                .build();

        BillShare unpaidBillShare = BillShareBuilder.get()
                .withPaid(false)
                .build();
        List<BillShare> billShares = List.of(unpaidBillShare);

        Bill bill = BillBuilder.get()
                .withPayer(executor)
                .withTrip(trip)
                .withPaid(false)
                .withBillShares(billShares)
                .build();

        when(userRepository.getById(executorId)).thenReturn(executor);
        when(billRepository.getById(billId)).thenReturn(bill);

        // when
        billService.markBillAsPaid(billId, executorId);

        // then
        assertTrue(bill.isPaid());
        assertTrue(bill.getBillShares().stream().allMatch(BillShare::isPaid));

        verify(userRepository, times(1)).getById(executorId);
        verify(billRepository, times(1)).getById(billId);
    }
}
