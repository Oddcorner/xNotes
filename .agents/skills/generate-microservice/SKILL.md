---
name: generate-microservice
description: Automates creation and registration of a new Spring Boot microservice in the xNotes cluster
---

# Skill: Generate Microservice

Use this skill when you need to create a new Spring Boot microservice in the xNotes workspace.

## Context

The xNotes project uses a microservices architecture with:
- **Java Version:** 25
- **Spring Boot Version:** 4.1.0
- **Spring Cloud Version:** 2025.1.2
- **Service Discovery:** Netflix Eureka Server (running on port 8761)
- **Database:** MongoDB

---

## Instructions

### Step 1: Create Maven Directory Structure
Create a new directory for the microservice under the project root. The structure must match standard Maven conventions:
```text
new-service/
├── pom.xml
├── Dockerfile
└── src/
    └── main/
        ├── java/net/oddcorner/new_service/
        │   └── NewServiceApplication.java
        └── resources/
            └── application.yaml
```

### Step 2: Configure `pom.xml`
Ensure the `pom.xml` inherits from `spring-boot-starter-parent` and includes standard dependencies. Use the following baseline:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
	<modelVersion>4.0.0</modelVersion>
	<parent>
		<groupId>org.springframework.boot</groupId>
		<artifactId>spring-boot-starter-parent</artifactId>
		<version>4.1.0</version>
		<relativePath/>
	</parent>
	<groupId>net.oddcorner</groupId>
	<artifactId>new-service</artifactId>
	<version>0.0.1-SNAPSHOT</version>
	<properties>
		<java.version>25</java.version>
		<spring-cloud.version>2025.1.2</spring-cloud.version>
	</properties>
	<dependencies>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-webmvc</artifactId>
		</dependency>
		<dependency>
			<groupId>org.springframework.cloud</groupId>
			<artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
		</dependency>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-data-mongodb</artifactId>
		</dependency>
		<dependency>
			<groupId>org.projectlombok</groupId>
			<artifactId>lombok</artifactId>
			<optional>true</optional>
		</dependency>
	</dependencies>
	<dependencyManagement>
		<dependencies>
			<dependency>
				<groupId>org.springframework.cloud</groupId>
				<artifactId>spring-cloud-dependencies</artifactId>
				<version>${spring-cloud.version}</version>
				<type>pom</type>
				<scope>import</scope>
			</dependency>
		</dependencies>
	</dependencyManagement>
	<build>
		<plugins>
			<plugin>
				<groupId>org.springframework.boot</groupId>
				<artifactId>spring-boot-maven-plugin</artifactId>
			</plugin>
		</plugins>
	</build>
</project>
```

### Step 3: Configure `application.yaml`
Set the server port and connect the service to Eureka. In `src/main/resources/application.yaml`:

```yaml
spring:
  application:
    name: new-service
  mongodb:
    uri: ${SPRING_MONGODB_URI:mongodb://admin:secretpassword@localhost:27017/xNotes_new?authSource=admin}

server:
  port: 808x # Choose an unused port (e.g. 8082, 8083)

eureka:
  client:
    service-url:
      defaultZone: ${EUREKA_CLIENT_SERVICE-URL_DEFAULTZONE:http://localhost:8761/eureka/}
```

### Step 4: Write Application Entry Point
Decorate the entry point with `@SpringBootApplication` and enable Eureka client annotations:

```java
package net.oddcorner.new_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class NewServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(NewServiceApplication.class, args);
    }
}
```

### Step 5: Containerize the Service
Create a standard `Dockerfile`:

```dockerfile
FROM openjdk:25-slim
WORKDIR /app
COPY target/*.jar app.jar
EXPOSE 808x
ENTRYPOINT ["java", "-jar", "app.jar"]
```

Append the new service block to the root `docker-compose.yml` registering the database parameters and linking it with `discovery-service`.
