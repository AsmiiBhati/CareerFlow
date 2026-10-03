# CareerFlow 🏢

A backend application for tracking job applications, monitoring application status, and maintaining status history.

## Overview

CareerFlow is a Java Spring Boot REST API designed to organize the job application process in one place.

It allows users to:
* Register and log in securely
* Create and manage job applications
* Track application statuses
* Enforce valid status transitions
* Search and filter applications
* Paginate application results
* Maintain a history of status changes

## Tech Stack

* **Java 21**
* **Spring Boot 3.5.16**
* **Spring Web**
* **Spring Data JPA**
* **Hibernate**
* **PostgreSQL**
* **Maven**
* **Lombok**
* **Bean Validation**
* **BCrypt**
* **IntelliJ IDEA**
* **Postman** *(planned for manual API testing)*

## Architecture

The project follows a layered backend architecture:

```text
Controller
    ↓
  Service
    ↓
Repository
    ↓
PostgreSQL
```

### Main layers

**Controller**

Handles HTTP requests and maps them to the appropriate service operations.

**Service**

Contains business logic such as application creation, status-transition validation, authentication, and status-history creation.

**Repository**

Uses Spring Data JPA to interact with the PostgreSQL database.

**Entity**

Maps Java objects to database tables using JPA/Hibernate.

**DTO**

Separates API request/response models from database entities.

**Exception Handling**

A global exception handler provides consistent API error responses for validation errors, missing resources, conflicts, invalid status transitions, and authentication failures.

## Database Design

### Tables

#### `users`

Stores registered user information.

* `id`
* `name`
* `email`
* `password_hash`
* `created_at`
* `updated_at`

#### `job_applications`

Stores job application details.

* `id`
* `user_id`
* `company_name`
* `role`
* `location`
* `status`
* `applied_at`
* `created_at`
* `updated_at`

#### `interviews`

Stores interview information associated with applications.

* `id`
* `application_id`
* `scheduled_at`
* `type`
* `status`
* `notes`

#### `status_history`

Stores every application status transition.

* `id`
* `application_id`
* `from_status`
* `to_status`
* `changed_at`

## Application Status Flow

CareerFlow enforces valid transitions between application states.

```text
 APPLIED
   ↓
SCREENING
   ↓
INTERVIEW
   ↓
 OFFER
   ↓
ACCEPTED
```

Applications can also transition to:

```text
REJECTED
WITHDRAWN
```

Terminal states (`ACCEPTED`, `REJECTED`, and `WITHDRAWN`) cannot transition to another status.

The transition rules are implemented in the `ApplicationStatus` enum and enforced by the service layer.

## REST API

### Authentication

| Method | Endpoint             | Description         |
| ------ | -------------------- | ------------------- |
| POST   | `/api/auth/register` | Register a new user |
| POST   | `/api/auth/login`    | Authenticate a user |

### Applications

| Method | Endpoint                         | Description                               |
| ------ | -------------------------------- | ----------------------------------------- |
| POST   | `/api/applications`              | Create an application                     |
| GET    | `/api/applications`              | Search, filter, and paginate applications |
| GET    | `/api/applications/{id}`         | Get an application                        |
| PUT    | `/api/applications/{id}`         | Update an application                     |
| DELETE | `/api/applications/{id}`         | Delete an application                     |
| PATCH  | `/api/applications/{id}/status`  | Change application status                 |
| GET    | `/api/applications/{id}/history` | View status history                       |

**Total: 9 REST endpoints**

## Filtering and Pagination

Application search supports filtering by:

* User
* Application status
* Company
* Location

Pagination and sorting are handled using Spring Data's `Pageable`.

Example:

```text
GET /api/applications?status=INTERVIEW&company=google&page=0&size=10
```

## Security

Passwords are never stored directly.

During registration:

```text
 Plain password
      ↓
    BCrypt
      ↓
 password_hash
      ↓
 PostgreSQL
```

During login, BCrypt verifies the supplied password against the stored hash.

The current version does not implement JWT-based authentication.

## Error Handling

A centralized `GlobalExceptionHandler` provides consistent error responses for:

* Resource not found
* Duplicate/conflicting data
* Invalid status transitions
* Authentication failures
* Validation errors
* Invalid request parameters
* Malformed JSON
* Database constraint violations

## Running Locally

### Prerequisites

* Java 21+
* Maven
* PostgreSQL
* IntelliJ IDEA or another Java IDE

### Database

Create a PostgreSQL database named:

```text
CareerFlow
```

Run the SQL script located at:

```text
db/schema.sql
```

### Environment Variable

The PostgreSQL password is read from the environment variable:

```text
DB_PASSWORD
```

The application configuration uses:

```properties
spring.datasource.password=${DB_PASSWORD}
```

### Run

Using Maven:

```bash
mvn spring-boot:run
```

The API runs on:

```text
http://localhost:8080
```

## Future Improvements

Possible future iterations include:

* JWT-based authentication and authorization
* Interview management endpoints
* User-specific authorization for application resources
* Automated integration/API tests
* API documentation with OpenAPI/Swagger
* Dashboard and analytics
* Frontend client

**Made By -  Asmii Bhati**

