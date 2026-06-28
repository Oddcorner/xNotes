package net.oddcorner.document_service;

import net.oddcorner.document_service.domain.TextDocument;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import net.oddcorner.document_service.repository.DocumentRepository;

@Component
public class DbCheckInitializer implements CommandLineRunner {

    private final DocumentRepository documentRepository;

    public DbCheckInitializer(DocumentRepository documentRepository) {
        this.documentRepository = documentRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("====== DB CHECK: Tying a write operation to MongoDB... ======");
        
        // 1. Wipe out any existing data from previous tests
        documentRepository.deleteAll();

        // 2. Insert a dummy test document
        TextDocument testDoc = new TextDocument("Connection Test", "Hello MongoDB!", "system-admin");
        TextDocument savedDoc = documentRepository.save(testDoc);
        
        System.out.println("====== DB CHECK SUCCESS! Document saved with ID: " + savedDoc.getId() + " ======");
    }
}
