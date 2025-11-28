# Surest - Member Management Service
Demo project for Member management with JWT role based authentication.

### Tech-Stack:

* Java 17
* Hibernate
* Spring Boot 3+
* Spring Security + JWT
* Spring Data JPA
* Flyway (Database migration)
* JUnit
* Mockito

### Build:

* Gradle

### Features:
* CRUD operations for MemberService
* JWT authentication
* Caching using in-memory cache
* Unit test cases with JaCoCo report generation
* Integration testing
* Logging and handling of necessary exceptions

### API:
* POST /api/auth/login
*  GET /api/v1/member
*  GET /api/v1/member/{uuid}
*  POST /api/v1/member
*  PUT /api/v1/member/{uuid}
*  DELETE /api/v1/member/{uuid}