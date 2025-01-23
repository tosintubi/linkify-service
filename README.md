# ⛓️‍💥 Linkify URL Shortener Service

Introducing  "Linkify", a simple, yet awesome production-ready URL shortener service built with **Kotlin**, **Spring Boot**, **PostgreSQL**, and **Docker Compose**. It generates secure, URL-friendly short links for long URLs and provides APIs to shorten and resolve links.

## Features

- **Shortens** long URLs into unique, cryptographically secure identifiers.
- **Resolves** shortened URLs back to their original long links.
- Supports the most common URL protocols: **http**, **https**, and **ftp**.

## Endpoints

### 1. **POST /shorten**

Shortens a given long URL.

#### Request Body:
```json
{
  "url": "https://www.example-of-a-random-long-very-long-url.com/adjustment.aspx?basketball=airport#boat"
}
```

#### Response:
```json
{
  "shortenLinkIdentifier": "D6Ym8aA7QW2Z"
}
```

#### Notes:
- Generates a 12-character, URL-safe, cryptographically secure identifier using **NanoId**.
- Supports only **http**, **https**, and **ftp** protocols. Other protocols are rejected during validation.

---

### 2. **GET /resolve/{shortenLinkIdentifier}**

Resolves a shortened identifier back to the original long URL.

#### Request Parameter:
- `shortenLinkIdentifier`: The unique shortened identifier (e.g., `D6Ym8aA7QW2Z`).

#### Response:
```json
{
  "longUrl": "https://another-example-of-a-somewhat-long-url.com/very/long/url"
}
```

**Note**: This endpoint returns the long URL as a response. Redirection was not implemented.

---

## Key Features

- **Shortlink Generation**:  
  Uses **NanoId** for cryptographically secure, random, and URL-friendly short link identifiers. Each identifier is a unique primary key in the database.

- **Protocol Support**:  
  The service currently supports:
    - `http`
    - `https`
    - `ftp`

  Links with other protocols are rejected during the validation process.

---

## Technologies Used

- **Kotlin**: Modern JVM language known for conciseness and null-safety.
- **Spring Boot**: Framework for building production-ready Spring-based applications.
- **PostgreSQL**: Relational database for storing original and shortened URLs.
- **NanoId**: Secure random string generator for creating unique identifiers.
- **Docker Compose**: Tool for defining and running multi-container Docker applications.

---

## Future Enhancements

- **Redirection Support**: Add HTTP redirection functionality to `GET /resolve/{shortenLinkIdentifier}`.
- **Distributed Cache**: Integrate a distributed cache like **Redis** to improve performance and reduce database load.
- **Custom URL Aliases**: Allow users to specify custom shortened identifiers.
- **Extended Protocol Support**: Support additional URL protocols beyond `http`, `https`, and `ftp`.
- **Analytics**: Track usage statistics for each shortened link for reporting and analytics.

---

## Running the Service

### Prerequisites

- Docker & Docker Compose
- Java 17 or higher

### Setup Instructions

1. **Clone the repository**:
   ```bash
   git clone https://github.com/tosintubi/linkify-service.git
   cd linkify-service
   ```

2. **Build and run with Docker Compose**:
   ```bash
   docker-compose up --build
   ```

3. The service will be accessible at `http://localhost:8080/api/v1/url`.

---

## OpenAPI Documentation

You can access the Swagger UI documentation [here](http://localhost:8080/api-doc/swagger-ui/index.html).
