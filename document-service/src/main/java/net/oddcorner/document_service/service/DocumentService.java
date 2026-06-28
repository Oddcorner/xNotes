package net.oddcorner.document_service.service;

import net.oddcorner.document_service.domain.TextDocument;

import java.util.List;

public interface DocumentService {
    TextDocument createDocument(TextDocument doc);
    TextDocument getDocumentById(String id);
    List<TextDocument> getDocumentByOwner(String ownerId);
}
