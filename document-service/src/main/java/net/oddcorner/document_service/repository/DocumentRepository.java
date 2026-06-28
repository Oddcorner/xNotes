package net.oddcorner.document_service.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import net.oddcorner.document_service.domain.TextDocument;

public interface DocumentRepository extends MongoRepository<TextDocument, String> {
    List<TextDocument> findByOwnerId(String ownerId);
}
