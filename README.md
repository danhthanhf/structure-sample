# Structure Sample With Spring Boot project
A sample Spring Boot project demonstrating the structure of a typical Spring Boot application.

## Technologies 
- Java 17
- Spring Boot 3.4.4
- MySQL Community Server 8.x
## Project Structure
```
src/
├── main/
│   ├── java/
│   │   └── com.r2s.structure_sample/
│   │       ├── common/                 # Shared utilities and definitions
│   │       │   ├── annotation/         # Custom annotations
│   │       │   ├── constant/           # Constant definitions (e.g. roles, status)
│   │       │   ├── enums/              # Enum definitions
│   │       │   ├── event/              # Application events (publish/subscribe)
│   │       │   ├── model/              # Common models (e.g. paging)
│   │       │   ├── response/           # Standard response structure (ApiResponse, ErrorResponse)
│   │       │   └── util/               # Utility classes (e.g. DateUtils, JwtUtils)
│   │       ├── config/                 # Application configuration (CORS, Swagger, Security, Beans)
│   │       ├── controller/             # REST API controllers
│   │       ├── dto/                    # Data Transfer Objects for input/output
│   │       ├── entity/                 # JPA entities mapped to DB tables
│   │       ├── exception/              # Custom exceptions & global error handling
│   │       ├── mapper/                 # MapStruct mappers for DTO ↔ Entity
│   │       ├── repository/             # Spring Data JPA repositories
│   │       ├── security/               # Security config (JWT, OAuth2, filter)
│   │       └── service/                # Service interfaces and business logic
│   │           └── impl/               # Implementation of services
│   └── resources/
│       ├── application.yml             # Main configuration file
│       ├── static/                     # Static web resources (if any)
│       └── templates/                  # Thymeleaf or HTML templates (if used)
└── test/
    └── java/com.r2s.structure_sample/  # Unit and integration tests

target/                                  # Compiled classes and build output (generated)
```
## Getting started
- Add your environment variables (or set them in IntelliJ):
```
DB_USERNAME=root
DB_PASSWORD=your_password
```
- And update add database name in file `application.properties`:
```yaml
spring.datasource.url=jdbc:mysql://localhost:3306/<your_database_name>?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
```
- Run the app
``
mvn spring-boot:run
``
## Test the API

You can test the API using:
- Postman
- Swagger UI with href: `http://localhost:8080/swagger-ui/index.html`
