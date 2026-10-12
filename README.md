# Fitness Microservice Platform

A backend fitness-tracking application built with **Java and Spring Boot microservices**. It supports user authentication, activity tracking, and asynchronous AI-generated recommendations.

**Author:** Prithvi Chandak  
**GitHub:** [@prithvichandak047](https://github.com/prithvichandak047)  
**LinkedIn:** [Prithvi Chandak](https://www.linkedin.com/in/prithvi-chandak-943869381/)

## Overview

The application separates responsibilities into independently running services. Requests enter through the API Gateway, services register with Eureka, and shared service configuration is provided by Spring Cloud Config. When an activity is recorded, Activity Service publishes an event to RabbitMQ; AI Service consumes the event and uses the Google Gemini API to generate a recommendation.

## Architecture

```text
                         Client
                           |
                           v
                  API Gateway (:8080)
                           |
          +----------------+----------------+
          |                |                |
          v                v                v
   User Service     Activity Service     AI Service
      :8081              :8082              :8083
        |                  |                  |
   PostgreSQL            MongoDB            MongoDB
                           |
                        RabbitMQ
                           |
                           v
                    Google Gemini API

       Eureka Server (:8761) - Service Discovery
       Config Server (:8888) - Central Configuration
```

## Services

| Service | Default port | Responsibility |
|---|---:|---|
| `gateway` | 8080 | Single entry point and request routing |
| `userservice` | 8081 | Registration, login, user management, JWT authentication |
| `activityservice` | 8082 | Create and retrieve activities; publish activity events |
| `aiservice` | 8083 | Consume activity events and generate AI recommendations |
| `eureka` | 8761 | Service registry and discovery |
| `configserver` | 8888 | Centralized configuration |

## Technology Stack

- Java 25 and Spring Boot
- Spring Cloud Gateway, Netflix Eureka, and Spring Cloud Config
- Spring Security and JWT-based authentication
- PostgreSQL with Spring Data JPA
- MongoDB with Spring Data MongoDB
- RabbitMQ for asynchronous messaging
- Google Gemini API for AI-generated recommendations
- Maven Wrapper for building and running services

## API Overview

Requests are intended to go through `http://localhost:8080`.

| Method | Endpoint | Authentication | Purpose |
|---|---|---|---|
| `POST` | `/api/users/register` | No | Register a user |
| `POST` | `/api/auth/login` | No | Log in and receive a JWT |
| `GET` | `/api/users/{userId}` | Yes | Retrieve a user profile |
| `POST` | `/api/activities` | Yes | Record an activity |
| `GET` | `/api/activities` | Yes | Retrieve activities |
| `GET` | `/api/activities/{activityId}` | Yes | Retrieve one activity |
| `GET` | `/api/recommendations/user/{userId}` | Yes | Retrieve a user's recommendations |
| `GET` | `/api/recommendations/activity/{activityId}` | Yes | Retrieve a recommendation for an activity |

Authenticated requests should include:

```http
Authorization: Bearer <your-jwt-token>
```

Activity types include `RUNNING`, `WALKING`, `CYCLING`, `WEIGHTS`, `YOGA`, and `OTHER`.

## Prerequisites

Install and configure the following before starting the application:

- JDK 25 (or the Java version required by the project POMs)
- PostgreSQL, with a database named `fitness_user_db`
- MongoDB
- RabbitMQ
- A Google Gemini API key from [Google AI Studio](https://aistudio.google.com/)

The project includes Maven Wrapper scripts, so a separate Maven installation is not normally required.

## Configuration and Secrets

Do **not** commit real passwords, API keys, or JWT secrets. The configuration files use environment-variable placeholders for sensitive values.

Set these environment variables in the terminal used to launch the services. Example for **Windows PowerShell**:

```powershell
$env:JWT_SECRET="<your-base64-encoded-secret>"
$env:DB_PASSWORD="<your-postgres-password>"
$env:GEMINI_API_KEY="<your-gemini-api-key>"
$env:GEMINI_API_URL="https://generativelanguage.googleapis.com/v1beta/models/gemini-flash-latest:generateContent"
```

Generate a strong Base64 JWT secret rather than using the placeholder. The same `JWT_SECRET` must be available to services that sign or validate tokens. Ensure the PostgreSQL username/password and local database settings match your environment. Never put actual secrets into this README or commit them to GitHub.

## Run Locally (Windows)

Start each service in a **separate PowerShell terminal**, from the project root. Start infrastructure dependencies (PostgreSQL, MongoDB, RabbitMQ) first.

1. Start Eureka:

   ```powershell
   cd eureka
   .\mvnw.cmd spring-boot:run
   ```

2. Start Config Server in another terminal:

   ```powershell
   cd configserver
   .\mvnw.cmd spring-boot:run
   ```

3. Start each application service in its own terminal:

   ```powershell
   cd userservice
   .\mvnw.cmd spring-boot:run
   ```

   ```powershell
   cd activityservice
   .\mvnw.cmd spring-boot:run
   ```

   ```powershell
   cd aiservice
   .\mvnw.cmd spring-boot:run
   ```

   ```powershell
   cd gateway
   .\mvnw.cmd spring-boot:run
   ```

Make sure required environment variables are set in each terminal that launches a service. When the services are running, visit [Eureka Dashboard](http://localhost:8761) to check service registration. This setup guide assumes the supporting databases and RabbitMQ are installed and running locally.

## Repository Structure

```text
fitness-microservice/
├── activityservice/   # Activity management and event publishing
├── aiservice/         # AI recommendation processing
├── configserver/      # Centralized service configuration
├── eureka/            # Service discovery
├── gateway/           # API gateway
├── userservice/       # Authentication and user management
├── .gitignore
└── README.md
```

## Concepts Demonstrated

- Decomposing a backend into domain-focused microservices
- Centralized routing and service discovery
- JWT-based stateless authentication
- Relational and document database integration
- Event-driven processing with RabbitMQ
- Integration with an external generative AI API

## Future Improvements

- Add Docker Compose for reproducible local setup
- Expand automated unit and integration test coverage
- Add API documentation (for example, OpenAPI/Swagger)
- Add centralized logging, metrics, and health monitoring

---

*Built by [Prithvi Chandak](https://github.com/prithvichandak047).* 
