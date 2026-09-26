# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Layout

The git root is an Eclipse workspace (`.metadata/` is Eclipse state). The actual Maven project lives in `CarSystem/`, so run all build commands from there. `CarSystem/bin/` and `CarSystem/target/` are build output, not source.

## Commands

Run these from `CarSystem/`, using the Maven wrapper (Maven 3.6.3):

```
mvnw.cmd spring-boot:run                 # run the app on :8080
mvnw.cmd clean package                   # build the jar (also runs the tests)
mvnw.cmd test                            # run all tests
mvnw.cmd test -Dtest=CarSystemApplicationTests#contextLoads   # run a single test
```

`JAVA_HOME` must point to the JDK root, not its `bin` folder.

Build with JDK 11 (`JAVA_HOME=C:\Program Files\Java\jdk-11.0.12`), matching `java.version` 11 in `pom.xml`. JDK 25 is also installed, but Spring Boot 2.2.6 does not work with it. The Lombok and compiler-plugin versions it brings in fail to compile on JDK 25. Even with those overridden, packaging fails with "Unsupported class file major version 69", because Spring 5.2's bytecode reader can't handle Java 25 classes. Moving to Java 25 needs a Spring Boot 3.x migration.

Running the app needs local PostgreSQL 13 (Windows service `postgresql-x64-13`, database `PostgresDB`) and local MongoDB (service `MongoDB`, `mongodb://localhost:27017/MongoDB`).

The only test is a `@SpringBootTest` context-load test. It needs both databases (see below) to be reachable.

Swagger UI: http://localhost:8080/swagger-ui.html

## Architecture

Spring Boot REST backend for a car-rental system. The frontend is an Angular app on `http://localhost:4200`, which is the only allowed CORS origin (`config/MyConfiguration`).

**Two datastores at once.** `Client` and `Car` are JPA entities in PostgreSQL. `ClientReceipt` is a MongoDB `@Document`. `CarSystemApplication` enables both `@EnableJpaRepositories` and `@EnableMongoRepositories`, and connection settings are in `src/main/resources/application.properties`. Hibernate uses `ddl-auto=update`, so it creates and alters the schema from the entities. `Client` has an EAGER `@OneToMany` list of `cars`. The client-car repository queries (`findClientCar*` in `CarRepository`) all join through `client.cars`.

**Custom token auth, not Spring Security.** `WebSecurityConfig` exists, but its `**/admin/**` style matchers don't actually protect the endpoints. The real auth flow is:
1. `POST /carSystem/login?userName&password&type=ADMIN|CLIENT` → `service/CarSystem.login()` returns a `Facade`. The admin login is hard-coded as `admin`/`1234`.
2. `LoginController` stores a `ClientSession` (the facade plus a last-accessed time) in the singleton `Map<String, ClientSession> tokensMap` bean (`config/WebConfiguration`), keyed by a random UUID token, and returns that token.
3. Every protected endpoint takes the token as a **path variable** (for example `/admin/viewClient/{token}/{id}`). It looks the token up in `tokensMap`, updates `lastAccessed`, and calls the service. A missing token returns 401 "Session Timeout". New endpoints should follow this same pattern.
4. `task/SessionTimeout` is a raw thread, started in `CarSystem`'s `@PostConstruct`. Once a minute it removes tokens idle for more than 30 minutes.

`Facade` is an empty marker interface implemented by all `*ServiceImpl` classes. Each `*Service` interface has one `*Impl`, and controllers autowire the `Impl` directly.

**Client identity lives in the session.** `ClientServiceImpl` is `@Scope("prototype")`. `CarSystem.login` creates a new instance per client login and sets its `clientId`, and `ClientController` gets that instance from the token's `ClientSession` facade. Never autowire `ClientServiceImpl` into a controller. That would create a separate instance with no client ID set.

**Billing job.** `task/ScheduledTasks` runs every 2 minutes. For each client with a positive balance, it deducts each rented car's `price`. For clients with a balance of 0 or less, it returns their cars (removes them from `client.cars` and restores the car's `amount`). Renting a car through `ClientServiceImpl` also writes a `ClientReceipt` to Mongo. Receipt IDs come from a static in-memory counter (`ClientReceipt.incrementId()`), which resets when the app restarts.

Code conventions: Lombok `@Data`/`@NoArgsConstructor`/`@AllArgsConstructor` on models. Services log with `System.out.println` banners and wrap failures in a generic `Exception`, which controllers turn into a 400 with a fixed message.
