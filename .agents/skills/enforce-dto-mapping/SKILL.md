---
name: enforce-dto-mapping
description: Enforces mapping MongoDB @Document entities to DTO records before exposing them in Controller layers
---

# Skill: Enforce DTO Mapping

Use this skill when defining database entities, controllers, or service methods that expose data to the REST or WebSocket layers.

## Context

To prevent internal database schema leaks and maintain clean separation of concerns, this project enforces the **Data Transfer Object (DTO) Pattern**:
- **Rule:** Direct MongoDB `@Document` entities must **never** escape the Service layer.
- **Rule:** Controllers must accept request DTOs and return response DTOs.
- **Rule:** DTOs must be implemented as Java `record` classes to guarantee immutability and concise syntax.

---

## Instructions

### Step 1: Design the `@Document` Entity
Design database documents with fields, getters, setters, and auditing parameters. Keep it isolated to the repository and service package scope.

```java
package net.oddcorner.new_service.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Getter;
import lombok.Setter;

@Document(collection = "items")
@Getter
@Setter
public class Item {
    @Id
    private String id;
    private String name;
    private String description;
}
```

### Step 2: Define DTO Records
Create DTO records under the `dto` subpackage. Do not include database annotations.

```java
package net.oddcorner.new_service.dto;

public record ItemResponse(
    String id,
    String name,
    String description
) {}
```

### Step 3: Implement Mapping in the Service layer
Perform mapping inside the `Service` implementation class. Do not return the entity directly.

```java
package net.oddcorner.new_service.service;

import net.oddcorner.new_service.domain.Item;
import net.oddcorner.new_service.dto.ItemResponse;
import net.oddcorner.new_service.repository.ItemRepository;
import org.springframework.stereotype.Service;

@Service
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;

    public ItemServiceImpl(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @Override
    public ItemResponse getItem(String id) {
        Item item = itemRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Item not found"));
            
        // Map entity to DTO record
        return new ItemResponse(
            item.getId(),
            item.getName(),
            item.getDescription()
        );
    }
}
```

### Step 4: Write Clean Controllers
Controllers must expose only DTO records.

```java
package net.oddcorner.new_service.controller;

import net.oddcorner.new_service.dto.ItemResponse;
import net.oddcorner.new_service.service.ItemService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/items")
public class ItemController {
    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ItemResponse> getItemById(@PathVariable String id) {
        return ResponseEntity.ok(itemService.getItem(id));
    }
}
```
