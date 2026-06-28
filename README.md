# xNotes: AI-Powered Career Journal and Tailored Resume Generator

xNotes is a personal career tracking dashboard and AI-driven resume generator built using a microservices architecture. It allows professionals to log daily tasks, career achievements, and professional "small wins." When applying for new positions, an integrated AI service parses this log history against a target job description to generate a highly tailored, optimized resume.

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
           │ Auth-User Service  │  │  Document Service  │  │  AI Resume Engine  │
           │  (Stateless JWT)   │  │ (CRUD & Task Logs) │  │    (Spring AI)     │
           └──────────┬─────────┘  └──────────┬─────────┘  └────────────────────┘
                      │                       │
                      └───────────┬───────────┘
                                  ▼
                     ┌────────────────────────┐
                     │ MongoDB (Local/Atlas)  │
                     └────────────────────────┘
```

### Components

1. **Angular Client:** A modular Single Page Application where users write journal entries, manage log logs, import target job descriptions, and view generated resumes.
2. **Spring Cloud Gateway:** Serves as the single entry point. Responsible for cross-cutting concerns, including global CORS configuration, path-based routing, and stateless authentication check filters.
3. **Netflix Eureka Discovery Server:** Facilitates service registration and dynamic discovery, enabling decoupled communication and horizontal scaling of downstream microservices.
4. **Auth-User Service:** Manages user identity, credentials, role assignments, secure BCrypt password hashing, and stateless JWT issuance.
5. **Document Service:** Manages text documents, career logs, and achievements. Supports real-time text updates via WebSockets (STOMP/SockJS) as a secondary collaboration feature.
6. **AI Resume Engine (Future / Spring AI):** The core intelligence service. Uses LLMs via Spring AI to parse historical task logs and achievements, synthesize them, and construct custom resumes tailored to specific job postings.

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

### Phase 3: Core Document & Achievement Service
* [x] **3.1 Optimize Database Aggregates:** Implement document schemas in MongoDB (`xNotes`) to store journal entries, wins, and daily tasks.
* [ ] **3.2 Expose Document CRUD Interfaces:** Expose RESTful endpoints for creating, retrieving, updating, and deleting career logs.
* [ ] **3.3 Implement WebSockets Connection Layer:** Spring WebSockets with STOMP/SockJS to sync dashboard updates in real time (collaborative sharing as a side goal).

### Phase 4: Frontend Application Architecture (Angular)
* [ ] **4.1 Construct Modular SPA Subsystems:** Setup lazy-loaded features (`AuthModule`, `DashboardModule`, `EditorModule`) and route authorization guards.
* [ ] **4.2 Build Reactive HTTP Services:** Enforce centralized data providers talking to the Gateway.
* [ ] **4.3 Integrate STOMP Client Wrappers:** Implement WebSocket client-side event loops to handle synchronized achievements and document states.

### Phase 5: AI Resume Tailoring Service & Verification
* [ ] **5.1 Integrate Spring AI:** Establish the standalone `ai-resume-service` connected to Eureka, utilizing LLMs to synthesize achievements against target resumes.
* [ ] **5.2 Author Test Suites:** Develop JUnit 5 unit and integration tests using `@DataMongoTest` with embedded in-memory MongoDB databases.
* [ ] **5.3 Environment Profile Realignment:** Setup dev/prod Spring profiles (`application-dev.yml`, `application-prod.yml`) and MongoDB Atlas cluster properties.
