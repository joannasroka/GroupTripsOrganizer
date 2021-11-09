package com.sroka.grouptripsorganizer.controller.document;

import com.sroka.grouptripsorganizer.controller.BaseController;
import com.sroka.grouptripsorganizer.controller.sorting_pagination.CustomPageable;
import com.sroka.grouptripsorganizer.dto.document.DocumentCreateDto;
import com.sroka.grouptripsorganizer.dto.document.DocumentDto;
import com.sroka.grouptripsorganizer.dto.document.DocumentUpdateDto;
import com.sroka.grouptripsorganizer.security.AuthenticationContextService;
import com.sroka.grouptripsorganizer.service.document.DocumentService;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.Set;

import static com.sroka.grouptripsorganizer.entity.document.Document.NAME_FIELD_NAME;
import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/api/documents")
@RequiredArgsConstructor
public class DocumentController extends BaseController {
    private final DocumentService documentService;
    private final AuthenticationContextService authenticationContextService;

    private static final int MAX_ALLOWED_DOCUMENTS_PER_PAGE = 10;
    private static final Set<String> ALLOWED_DOCUMENT_SORTING_PARAMS = Set.of(NAME_FIELD_NAME);

    @PreAuthorize("permitAll()")
    @PostMapping
    @ResponseStatus(CREATED)
    public DocumentDto createDocument(@Valid @RequestBody DocumentCreateDto documentCreateDto,
                                      @RequestParam("file") MultipartFile file) throws IOException {
        Long currentUserId = authenticationContextService.getCurrentUserId();

        return documentService.create(documentCreateDto, file, currentUserId);
    }

    @PreAuthorize("permitAll()")
    @PutMapping("/{documentId}")
    public DocumentDto updateDocument(@PathVariable Long documentId,
                                      @RequestBody @Valid DocumentUpdateDto documentUpdateDto) {
        Long currentUserId = authenticationContextService.getCurrentUserId();

        return documentService.update(documentUpdateDto, documentId, currentUserId);
    }

    @PreAuthorize("permitAll()")
    @DeleteMapping("/{documentId}")
    public void deleteDocument(@PathVariable Long documentId) {
        Long currentUserId = authenticationContextService.getCurrentUserId();
        documentService.delete(documentId, currentUserId);
    }

    @PreAuthorize("permitAll()")
    @GetMapping("/groups/{groupId}")
    @CustomPageable
    public Page<DocumentDto> getDocumentsByGroup(@PathVariable Long groupId,
                                                 @PageableDefault(sort = NAME_FIELD_NAME) @Parameter(hidden = true) Pageable pageable) {
        pageSizeValidation(pageable, MAX_ALLOWED_DOCUMENTS_PER_PAGE);
        sortParametersValidation(pageable, ALLOWED_DOCUMENT_SORTING_PARAMS);

        Long currentUserId = authenticationContextService.getCurrentUserId();

        return documentService.getByGroup(groupId, currentUserId, pageable);
    }

    @PreAuthorize("permitAll()")
    @GetMapping("/{documentId}")
    public DocumentDto getDocumentById(@PathVariable Long documentId) {
        Long currentUserId = authenticationContextService.getCurrentUserId();

        return documentService.getById(documentId, currentUserId);
    }
}
