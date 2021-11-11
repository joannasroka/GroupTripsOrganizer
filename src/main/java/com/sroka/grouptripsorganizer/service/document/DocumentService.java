package com.sroka.grouptripsorganizer.service.document;

import com.sroka.grouptripsorganizer.dto.document.DocumentCreateDto;
import com.sroka.grouptripsorganizer.dto.document.DocumentDto;
import com.sroka.grouptripsorganizer.dto.document.DocumentUpdateDto;
import com.sroka.grouptripsorganizer.entity.document.Document;
import com.sroka.grouptripsorganizer.entity.group.Group;
import com.sroka.grouptripsorganizer.entity.user.User;
import com.sroka.grouptripsorganizer.exception.DatabaseEntityNotFoundException;
import com.sroka.grouptripsorganizer.exception.NotAllowedFileTypeException;
import com.sroka.grouptripsorganizer.exception.ValidationException;
import com.sroka.grouptripsorganizer.mapper.DocumentMapper;
import com.sroka.grouptripsorganizer.repository.document.DocumentRepository;
import com.sroka.grouptripsorganizer.repository.group.GroupRepository;
import com.sroka.grouptripsorganizer.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.Set;

@Service
@Transactional
@RequiredArgsConstructor
public class DocumentService {
    private static final Set<String> ALLOWED_MIME_TYPES = Set.of("text/plain", "image/jpeg", "image/png", "application/pdf", "application/x-pdf");

    private final DocumentRepository documentRepository;
    private final GroupRepository groupRepository;
    private final UserRepository userRepository;

    private final DocumentMapper documentMapper;

    public DocumentDto create(DocumentCreateDto documentCreateDto, MultipartFile file, Long executorId) throws IOException {
        Group group = groupRepository.getById(documentCreateDto.getGroupId());
        User executor = userRepository.getById(executorId);

        String fileType = file.getContentType();

        validate(executor, group);
        validateFileType(fileType);

        Document newDocument = documentMapper.convertToEntity(documentCreateDto);
        newDocument.setFile(file.getBytes());
        newDocument.setGroup(group);
        newDocument.setType(fileType);
        newDocument = documentRepository.save(newDocument);

        return documentMapper.convertToDto(newDocument);
    }

    public DocumentDto update(DocumentUpdateDto documentUpdateDto, Long documentId, Long executorId) {
        Document documentToUpdate = documentRepository.getById(documentId);
        Group group = documentToUpdate.getGroup();
        User executor = userRepository.getById(executorId);

        validate(executor, group);

        documentToUpdate.setName(documentUpdateDto.getName());

        return documentMapper.convertToDto(documentToUpdate);
    }

    public void delete(Long documentId, Long executorId) {
        User executor = userRepository.getById(executorId);
        Document documentToDelete = documentRepository.getById(documentId);
        Group group = documentToDelete.getGroup();

        validate(executor, group);

        documentRepository.delete(documentToDelete);
    }

    public Page<DocumentDto> getByGroup(Long groupId, Long executorId, Pageable pageable) {
        User executor = userRepository.getById(executorId);
        Group group = groupRepository.getById(groupId);
        validate(executor, group);

        Page<Document> documents = documentRepository.findAllByGroup(group, pageable);
        return documents.map(documentMapper::convertToDto);
    }

    public DocumentDto getById(Long documentId, Long executorId) {
        User executor = userRepository.getById(executorId);
        Document document = documentRepository.getById(documentId);
        Group group = document.getGroup();

        validate(executor, group);

        return documentMapper.convertToDto(document);
    }


    private void validate(User executor, Group group) {
        if (!group.getParticipants().contains(executor)) {
            throw new DatabaseEntityNotFoundException();
        }
    }

    private void validateFileType(String type) {
        if (!ALLOWED_MIME_TYPES.contains(type)) {
            throw new NotAllowedFileTypeException();
        }
    }
}
