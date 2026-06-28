# Workspace Rules: xNotes Microservices

This file defines the styling rules, standards, and architecture boundaries for any AI agent working on the xNotes project.

---

## 1. Technical Stack Constraints
* **Java Version:** Java 25. Enforce modern language features (such as `records`, pattern matching, and standard collections).
* **Framework:** Spring Boot 4.1.x & Spring Cloud 2025.x.
* **Database:** MongoDB (using Spring Data MongoDB).

---

## 2. Coding Standards & Design Patterns

### Data Transfer Objects (DTOs)
* **Rule:** Database model entities annotated with `@Document` (e.g., net.oddcorner.auth_service.domain.User) **must never** escape the Service layer.
* **Action:** Always map entities to immutable Java `record` classes in the service implementation before returning them to controllers or WebSockets.

### Inversion of Control (IoC)
* **Rule:** Program to interfaces. Dependencies must be injected using constructor injection rather than `@Autowired` fields to facilitate unit testing.

### Library Consistency
* **Rule:** Follow the existing project library patterns:
  * Lombok is allowed for clean boilerplate control (e.g. `@Getter`, `@Setter`). Avoid `@Data` or `@ToString` on circular relational mappings.
  * Use the modern Spring Security DSL (lambda configurations) in filter chains.

---

## 3. Testing Requirements
* **Unit Testing:** Use JUnit 5 and Mockito.
* **Integration Testing:** 
  * Do **not** use embedded in-memory fakes for database integration tests.
  * Always use **Testcontainers** for spinning up real MongoDB containers to assert query and transactional behaviors accurately.

---

## 4. Port & Service Topology
Keep the service ports and registries configured as follows:
* **Eureka Registry Server:** Port `8761`
* **Document Service:** Port `8080`
* **Auth Service:** Port `8081`
* **Gateway Service:** Port `8080` (or proxy mappings)
* **MongoDB Host:** `localhost:27017`
