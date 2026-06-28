package net.oddcorner.document_service.domain;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "documents")
public class TextDocument {

    @Id
    private String id;
    private String title;
    private String content;
    private String ownerId;
    private LocalDateTime lastModified;

    public TextDocument() {};

    public TextDocument(String title, String content, String ownerId) {
        this.title = title;
        this.content= content;
        this.ownerId = ownerId ;
        this.lastModified = LocalDateTime.now();
    };

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public String getOwnerId() { return ownerId; }
    public void setOwnerId(String ownerId) { this.ownerId = ownerId; }
    public LocalDateTime getLastModified() { return lastModified; }
    public void setLastModified(LocalDateTime lastModified) { this.lastModified = lastModified; }
}
