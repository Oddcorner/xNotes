# Domain Context: xNotes

This document serves as the single source of truth for the ubiquitous language, domain concepts, and system architecture of the xNotes collaborative document editor.

---

## 1. System Vision
xNotes is a real-time collaborative document platform. Users can create rich-text documents, invite collaborators, and edit text simultaneously in their browsers with low-latency updates.

---

## 2. Ubiquitous Language & Domain Glossary

To ensure code classes and naming conventions are consistent, all modules must use this vocabulary:

*   **User:** An authenticated account within the system. Users have roles (e.g. `ROLE_USER`, `ROLE_ADMIN`) and own/collaborate on documents.
*   **TextDocument:** A distinct document workspace. It contains metadata (title, owner, modified timestamps) and content (raw text body).
*   **Collaborator:** A user granted read or write permissions to a document owned by another user.
*   **Session:** An active client websocket connection to a specific document.
*   **Operation (Op):** A discrete character insertion, deletion, or modification payload pushed over WebSockets to synchronize client frames.

---

## 3. Microservices Topology

*   **[discovery-service](file:///home/oddie/remotes/xNotes/discovery-service):** Eureka registry ensuring target services discover each other dynamically.
*   **[auth-service](file:///home/oddie/remotes/xNotes/auth-service):** Identity manager handling JWT generation and registration.
*   **[document-service](file:///home/oddie/remotes/xNotes/document-service):** Core engine handling document storage and websocket channels.
*   **gateway-service (planned):** Entry point routing traffic downstream and checking stateless JWT signatures.

---

## 4. Key Constraints

### MongoDB 16MB Document Boundary
*   **Constraint:** MongoDB limits single document sizes to 16MB.
*   **Strategy:** Never store a growing, unbounded edit log (operational history) nested directly inside the `TextDocument` entity. Operations and history must be separated into a referenced collection (e.g. `revisions` or `operations`) with periodic snapshots of the main document text.

### Stateless Edge Validation
*   **Constraint:** Centralized authentication check in Gateway.
*   **Strategy:** The Gateway intercepts JWT headers, decodes them, and passes username/role headers (e.g., `X-User-Id`, `X-User-Roles`) downstream to microservices. Individual services (like `document-service`) should trust these headers and remain completely stateless.
