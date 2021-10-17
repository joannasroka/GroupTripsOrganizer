package com.sroka.grouptripsorganizer.mapper;

import com.sroka.grouptripsorganizer.dto.bill.BillCreateDto;
import com.sroka.grouptripsorganizer.dto.bill.BillDto;
import com.sroka.grouptripsorganizer.entity.bill.Bill;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BillMapper {
    private final ModelMapper modelMapper;

    public BillDto convertToDto(Bill bill) {
        return modelMapper.map(bill, BillDto.class);
    }

    public Bill convertToEntity(BillCreateDto billCreateDto) {
        return modelMapper.map(billCreateDto, Bill.class);
    }
}
