package net.oddcorner.document_service.service;

import java.util.List;

import org.springframework.stereotype.Service;

import net.oddcorner.document_service.domain.TextDocument;
import net.oddcorner.document_service.repository.DocumentRepository;

@Service
public class DocumentServiceImpl implements DocumentService {

    private final DocumentRepository documentRepository;

    public DocumentServiceImpl(DocumentRepository documentRepository) {
        this.documentRepository = documentRepository;
    }

	@Override
	public TextDocument createDocument(TextDocument doc) {
        return documentRepository.save(doc);
	}

	@Override
	public TextDocument getDocumentById(String id) {
        return documentRepository.findById(id).orElseThrow(() -> new RuntimeException("Document not found with id: " + id));
	}


	@Override
	public List<TextDocument> getDocumentByOwner(String ownerId) {
        return documentRepository.findByOwnerId(ownerId);

	}

}
