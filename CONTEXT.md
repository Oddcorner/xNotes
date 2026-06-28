# Domain Context: xNotes

This document serves as the single source of truth for the ubiquitous language, domain concepts, and system architecture of the xNotes AI-powered career tracking and resume tailoring system.

---

## 1. System Vision
xNotes is an achievement journal and AI resume compiler. Users document their daily tasks, professional wins, and project metrics. When applying for a new job, the system leverages AI to cross-reference this log history with the targeted job description, compiling an optimized, highly tailored resume.

---

## 2. Ubiquitous Language & Domain Glossary

To ensure code classes and naming conventions are consistent, all modules must use this vocabulary:

*   **User:** An authenticated account representing a professional using the system.
*   **JournalEntry / WorkLog:** A logged record of daily tasks, professional wins, project metrics, or performance feedback.
*   **JobDescription:** The raw text block or URL representing the target job posting the user is applying to.
*   **TailoredResume:** The generated, optimized resume draft constructed by the AI engine based on matching the User's logs to the target JobDescription requirements.
*   **Session:** An active client websocket connection to a specific document dashboard.
*   **Operation (Op):** A discrete cursor or text modification payload pushed over WebSockets to sync changes (collaboration is a side feature).

---

## 3. Microservices Topology

*   **[discovery-service](file:///home/oddie/remotes/xNotes/discovery-service):** Eureka registry ensuring target services discover each other dynamically.
*   **[auth-service](file:///home/oddie/remotes/xNotes/auth-service):** Identity manager handling JWT generation and user registrations.
*   **[document-service](file:///home/oddie/remotes/xNotes/document-service):** Core engine storing daily journal entries and work logs.
*   **ai-resume-service (planned):** Spring AI engine that parses WorkLogs against a JobDescription to generate tailored resumes.
*   **gateway-service (planned):** Entry point routing traffic downstream and checking stateless JWT signatures.

---

## 4. Key Constraints

### MongoDB 16MB Document Boundary
*   **Constraint:** MongoDB limits single document sizes to 16MB.
*   **Strategy:** WorkLogs and JournalEntries are stored in collections linked by user ID rather than nesting all history inside a single User document.

### Stateless Edge Validation
*   **Constraint:** Centralized authentication check in Gateway.
*   **Strategy:** The Gateway intercepts JWT headers, decodes them, and passes user identifiers (e.g., `X-User-Id`, `X-User-Roles`) downstream to microservices. Individual services should trust these headers and remain completely stateless.
