# Goodreads Spring Boot Project

A Spring Boot application for managing books, authors, and publishers, inspired by Goodreads.com.

---

## Table of Contents
- [Features](#features)
- [Prerequisites](#prerequisites)
- [Build & Run](#build--run)
- [Accessing the H2 Database Console](#accessing-the-h2-database-console)
- [API Documentation (Swagger)](#api-documentation-swagger)
- [Running Tests](#running-tests)
- [Project Structure](#project-structure)

---

## Features
- Manage books, authors, and publishers
- RESTful API endpoints
- In-memory H2 database (default)
- Swagger/OpenAPI documentation
- Unit and integration tests

---

## Prerequisites
- Java 17 or higher
- Maven 3.6+

---

## Build & Run

1. **Clone the repository:**
   ```sh
   git clone <repo-url>
   cd SpringBoot-Good-reads.com-2-main
   ```

2. **Build the project:**
   ```sh
   mvn clean package
   ```

3. **Run the application:**
   ```sh
   mvn spring-boot:run
   # OR
   java -jar target/goodreads-*.jar
   ```

4. **Access the application:**
   - The app runs at: [http://localhost:8080](http://localhost:8080)

> **Note:**
> In `src/main/resources/application.properties`, the property `server.error.include-message` is set to `always` by default to help with debugging during your first run. 
> **After confirming the application works, change this value to `never` for better security in production:**
> ```properties
> server.error.include-message=never
> ```
>
> **Database Initialization:**
> For the first run, set `spring.sql.init.mode=always` in `src/main/resources/application.properties` to initialize the database with schema and data from `schema.sql` and `data.sql`.
> **After the first successful run, change it to `never` to prevent re-initialization and accidental data loss:**
> ```properties
> spring.sql.init.mode=never
> ```

---

## Accessing the H2 Database Console

1. **Open your browser and go to:**
   [http://localhost:8080/h2-console](http://localhost:8080/h2-console)

2. **JDBC URL:**
   - Default: `jdbc:h2:mem:testdb` (for in-memory)
   - Or check `src/main/resources/application.properties` for the exact URL.

3. **Username/Password:**
   - Default: `sa` / (leave password blank)

4. **Click Connect**

---

## API Documentation (Swagger)

1. **Swagger UI is available at:**
   [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
   - Or sometimes: `/swagger-ui/index.html`

2. **Explore and test all API endpoints directly from the browser.**

---

## Running Tests

1. **Run all tests with Maven:**
   ```sh
   mvn test
   ```

2. **Test reports:**
   - After running, reports are available in `target/surefire-reports/`

---

## Project Structure

```
SpringBoot-Good-reads.com-2-main/
├── src/main/java/com/example/goodreads/
│   ├── controller/    # REST controllers
│   ├── model/         # Entity classes
│   ├── repository/    # JPA repositories
│   └── service/       # Business logic
├── src/main/resources/
│   ├── application.properties
│   ├── schema.sql
│   └── data.sql
├── src/test/java/com/example/goodreads/
│   ├── controller/    # Controller tests
│   ├── service/       # Service tests
│   └── GoodreadsApplicationTests.java
├── pom.xml            # Maven build file
└── ...
```

---

## Notes
- For production, configure a persistent database in `application.properties`.
- Add security and environment variable management as needed.

---

**Feel free to contribute or open issues!** 
