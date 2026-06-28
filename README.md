# xNotes: Distributed Online Document Editor

xNotes is a distributed, real-time collaborative document editing platform built using a microservices architecture. The project demonstrates production-grade Java development practices, utilizing the Spring ecosystem, Spring Cloud, WebSockets, and Angular. It is designed to be highly scalable, decoupled, and maintainable.

---

## Technical Stack

* **Backend Framework:** Java 25 / Spring Boot 4.1.x & Spring Cloud 2025.x
* **Frontend Framework:** Angular (Modular SPA)
* **Primary Database:** MongoDB (Document-Oriented)
* **Containerization:** Docker & Docker Compose
* **Design Patterns:** SOLID Principles, Domain-Driven Design (DDD), Layered Clean Architecture (Controller -> Service -> Repository), Data Transfer Object (DTO) pattern.

---

## System Architecture

The application is decomposed into isolated, single-responsibility microservices that coordinate via service discovery and route through a central API gateway.

```
                                  ┌────────────────────────┐
                                  │      Angular SPA       │
                                  └───────────┬────────────┘
                                              │ 
                                              ▼ (HTTP / WebSockets)
                                  ┌────────────────────────┐
                                  │  Spring Cloud Gateway  │ (Global Auth, CORS, & Routing)
                                  └───────────┬────────────┘
                                              │
                      ┌───────────────────────┼───────────────────────┐
                      │ (Service Discovery via Netflix Eureka)        │
                      ▼                       ▼                       ▼
           ┌────────────────────┐  ┌────────────────────┐  ┌────────────────────┐
           │ Auth-User Service  │  │  Document Service  │  │  AI Engine (Future)│
           │  (Stateless JWT)   │  │ (WebSockets & CRUD)│  │    (Spring AI)     │
           └──────────┬─────────┘  └──────────┬─────────┘  └────────────────────┘
                      │                       │
                      └───────────┬───────────┘
                                  ▼
                     ┌────────────────────────┐
                     │ MongoDB (Local/Atlas)  │
                     └────────────────────────┘
```

### Components

1. **Angular Client:** A modern Single Page Application (SPA) utilizing reactive state management and WebSocket clients to support collaborative typing.
2. **Spring Cloud Gateway:** Serves as the single entry point. Responsible for cross-cutting concerns, including global CORS configuration, path-based routing, and stateless authentication check filters.
3. **Netflix Eureka Discovery Server:** Facilitates service registration and dynamic discovery, enabling decoupled communication and horizontal scaling of downstream microservices.
4. **Auth-User Service:** Manages user identity, credentials, role assignments, secure BCrypt password hashing, and stateless JWT issuance.
5. **Document Service:** Manages text document metadata and contents. Uses WebSockets (STOMP/SockJS) to broadcast live document changes to active editors.

---

## Design Patterns and Code Standards

### 1. Data Isolation & DTO Pattern
* **Standard:** Direct MongoDB `@Document` entities must never escape the Service layer to the Controller or Client.
* **Reasoning:** Database entities are mapped to transient Data Transfer Objects (DTOs) before being serialized over REST or WebSockets. This prevents schema leaks and decouples client response structures from persistence schemas.

### 2. Inversion of Control (IoC) & Unit Testing
* **Standard:** Design to interfaces and inject dependencies.
* **Reasoning:** Dependencies are injected using Spring constructor injection. This enforces loose coupling, isolates business components, and simplifies unit testing with Mockito.

### 3. Strict Layered Separation
* **Controllers:** Handle transport layer protocols, HTTP/WebSocket mapping, request validation, and HTTP response mapping.
* **Services:** Enforce business rules and manage core domain logic.
* **Repositories:** Enforce data access abstractions.

---

## Implementation Roadmap

### Phase 1: Infrastructure and Service Discovery
* [x] **1.1 Setup Discovery Engine (Eureka Server):** Standalone Spring Cloud Netflix Eureka service registry.
* [ ] **1.2 Setup API Routing Gateway (Spring Cloud Gateway):** Dynamic routing configuration, global CORS policy, and downstream route filters.
* [x] **1.3 Environment Externalization (Dockerization):** Containerization of services and MongoDB orchestration with Docker Compose.

### Phase 2: Authentication and Edge Security Infrastructure
* [x] **2.1 Connect Auth DB Context:** Bind Authentication service to MongoDB database `xNotes_auth`.
* [ ] **2.2 Design Identity Core Domain:** Complete JWT generation, validation utility, and login endpoints.
* [ ] **2.3 Integrate Gateway Identity Guard:** Global filter in the API Gateway to validate Bearer JWTs and forward user headers downstream.

### Phase 3: Core Document Service (CRUD & Low-Latency Sync)
* [x] **3.1 Optimize Database Aggregates:** Implement document schemas in MongoDB (`xNotes`). Limit nested edit history to remain below MongoDB's 16MB document boundary.
* [ ] **3.2 Expose Document CRUD Interfaces:** Expose RESTful endpoints for document creation, retrieval, updates, and deletion.
* [ ] **3.3 Implement WebSockets Connection Layer:** Spring WebSockets with STOMP/SockJS to synchronize edits in real time.

### Phase 4: Frontend Application Architecture (Angular)
* [ ] **4.1 Construct Modular SPA Subsystems:** Setup lazy-loaded features (`AuthModule`, `DashboardModule`, `EditorModule`) and route authorization guards.
* [ ] **4.2 Build Reactive HTTP Services:** Enforce centralized data providers talking to the Gateway.
* [ ] **4.3 Integrate STOMP Client Wrappers:** Implement WebSocket client-side event loops to handle synchronized cursor positions and document updates.

### Phase 5: Verification & Production Target Adaptation
* [ ] **5.1 Author Test Suites:** Develop JUnit 5 unit and integration tests using `@DataMongoTest` with embedded in-memory MongoDB databases.
* [ ] **5.2 Environment Profile Realignment:** Setup dev/prod Spring profiles (`application-dev.yml`, `application-prod.yml`) and MongoDB Atlas cluster properties.

---

## Future System Extensibility (AI Module)

The architecture is designed to scale horizontally by integrating additional services without altering the pre-existing codebase:

1. **Standalone Deployment:** A separate `ai-intelligence-service` built with Spring AI can be introduced and registered dynamically with Eureka.
2. **Gateway Registration:** Expose the AI features to the client by declaring routes in the Gateway (e.g., routing `/api/v1/ai/**` to the AI service).
3. **Asynchronous Processing:** For long-running operations (like summarizations or style adjustments), the Document Service can publish messages to an AMQP broker (RabbitMQ/Kafka). The AI service can process requests asynchronously and write results back to the shared MongoDB cluster.
