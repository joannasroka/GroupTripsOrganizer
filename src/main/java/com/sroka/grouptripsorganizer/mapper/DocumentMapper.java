package com.sroka.grouptripsorganizer.mapper;

import com.sroka.grouptripsorganizer.dto.document.DocumentCreateDto;
import com.sroka.grouptripsorganizer.dto.document.DocumentDto;
import com.sroka.grouptripsorganizer.entity.document.Document;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DocumentMapper {
    private final ModelMapper modelMapper;

    public DocumentDto convertToDto(Document document) {
        return modelMapper.map(document, DocumentDto.class);
    }

    public Document convertToEntity(DocumentCreateDto documentCreateDto) {
        return modelMapper.map(documentCreateDto, Document.class);
    }
}
