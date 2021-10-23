package com.sroka.grouptripsorganizer.mapper;

import com.sroka.grouptripsorganizer.dto.bill.BillShareCreateDto;
import com.sroka.grouptripsorganizer.dto.bill.BillShareDto;
import com.sroka.grouptripsorganizer.entity.bill.BillShare;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class BillShareMapper {
    private final ModelMapper modelMapper;

    public BillShareDto convertToDto(BillShare billShare) {
        return modelMapper.map(billShare, BillShareDto.class);
    }

    public BillShare convertToEntity(BillShareCreateDto billShareCreateDto) {
        return modelMapper.map(billShareCreateDto, BillShare.class);
    }

    public List<BillShareDto> convertToDtos(List<BillShare> billShares) {
        return billShares
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
}
