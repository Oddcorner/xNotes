# FE
- AngularJS (For learning)

# BE
- Java spring-boot (For learning)
- Micro-services

# DB
- MongoDB

# Apps are split into layers
- Presentation layer (Controllers)

- Service layer (business logic)

- Persistence layer (Database)


# Spring vs Springboot
- Spring-boot is layer on top of Spring Framework

- Spring Framework is a dependency injection framework

- Spring-boot speeds up deployment by using sensible configuration, and automatically determining configurations required. Thus, eliminating the need for constant adjustment of configs.

Spring-boot also has a nice initialization process that handles project dependencies and structure, allowing quick-start via zip file download.

# Dependency injection:
- Dependencies can be labeled as "beans" using the `@Bean` flag
- Beans should live in a configuration class annotated with the flag `@Configuration`

# Component annotations
- Instead of using `@Bean` in a class annotated with `@Configuration` that exposes the class into the Spring context for injection, we can directly annotate a class used for injection with `@Component`
- Using `@Component` makes it so that any args added into its constructor is also assumed to be beans and thus, requires those args classes to also be annotated.- `@Service` is the same annotation but is more descriptive 






