---
name: generate-testcontainer-test
description: Automates integration testing setups using JUnit 5, MockMvc, and Testcontainers (MongoDB)
---

# Skill: Generate Testcontainer Test

Use this skill when writing integration tests for controllers or repository queries that depend on an active MongoDB instance.

## Context

Instead of using in-memory databases (which are deprecated and lack Mongo-specific indexing/behavior replication), this project uses **Testcontainers** to spin up temporary Docker containers running real MongoDB instances.

---

## Instructions

### Step 1: Ensure Pom Test Dependencies
Ensure the service `pom.xml` contains the Testcontainers dependencies:

```xml
<dependency>
    <groupId>org.testcontainers</groupId>
    <artifactId>mongodb</artifactId>
    <scope>test</scope>
</dependency>
<dependency>
    <groupId>org.testcontainers</groupId>
    <artifactId>junit-jupiter</artifactId>
    <scope>test</scope>
</dependency>
```

### Step 2: Create a Base Integration Test Class
Create an abstract base class under `src/test/java/net/oddcorner/new_service/BaseIntegrationTest.java` that handles starting the Mongo container and overriding spring properties:

```java
package net.oddcorner.new_service;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
public abstract class BaseIntegrationTest {

    @Container
    static final MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:latest");

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }
}
```

### Step 3: Write the Controller Integration Test
Inherit from `BaseIntegrationTest` to write MockMvc verification suites:

```java
package net.oddcorner.new_service.controller;

import net.oddcorner.new_service.BaseIntegrationTest;
import net.oddcorner.new_service.domain.Item;
import net.oddcorner.new_service.repository.ItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ItemControllerIT extends BaseIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ItemRepository itemRepository;

    @BeforeEach
    void setUp() {
        itemRepository.deleteAll();
    }

    @Test
    void shouldReturnItemById() throws Exception {
        Item item = new Item();
        item.setName("Test Notebook");
        item.setDescription("Integration testing with Testcontainers");
        Item saved = itemRepository.save(item);

        mockMvc.perform(get("/api/v1/items/" + saved.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Notebook"))
                .andExpect(jsonPath("$.description").value("Integration testing with Testcontainers"));
    }
}
```
