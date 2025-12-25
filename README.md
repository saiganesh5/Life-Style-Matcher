# LifeStyleMatcherProject

LifeStyleMatcherProject is a Spring Boot application that helps students and renters find the best neighborhoods and real‑estate listings based on their lifestyle preferences. It combines neighborhood attributes (safety, walkability, markets, transport, internet, food, noise) with user requirements (budget, pet‑friendly, hospital/gym access) to return top matches. The project also includes user signup, login, and password recovery flows with email‑based OTP verification.

---

## Key Features
- Lifestyle‑based matching endpoint that scores neighborhoods and returns top listings.
- User management:
  - Signup with email validation and OTP verification.
  - Login with hashed passwords (Spring Security `PasswordEncoder`).
  - Forgot password flow with OTP and reset.
- Email notifications via SMTP (Gmail).
- CORS enabled for browser‑based clients.
- Observability via Spring Boot Actuator (dependency present).

---

## Tech Stack
- Java 21
- Spring Boot 3.5.x
  - Web, Security, Data JPA, Data MongoDB, Mail, Actuator, DevTools (runtime)
- Datastores:
  - MySQL (primary relational store; configured in `application.properties`)
  - MongoDB (for neighborhood/listing datasets)
- Maven (build tool)

See `pom.xml` for full dependencies.

---

## Project Structure (high‑level)
- `src/main/java/com/ganesh/LifeStyleMatcherProject/`
  - Controllers: `MatchController`, `SignUpController`, `LoginController`, `PasswordRecoveryController`, `ProfileController`, `StudentController`, `WebpageController`
  - Data + Repos: `User`, `UserRepository`, `Neighborhood`, `NeighborhoodRepository`, `Listing`, `ListingRepository`
  - Services/Utils: `ListingImportService`, `EmailService`, `OtpService`, `AESUtil`, `OTPUtil`
  - Security/CORS: `SecurityConfig`, `DevSecurityConfig`, `ProdSecurityConfig`, `CorsConfig`
  - App entry: `LifeStyleMatcherProjectApplication`
- `src/main/resources/application.properties` (profiles, DB, mail, logging)
- `API_keys.env` (example env file for secrets)

---

## Requirements
- Java 21 (JDK 21)
- Maven 3.9+
- MySQL 8+ running with a database and user accessible to the app
- MongoDB 6+ running locally or accessible remotely
- An SMTP account (Gmail or other) for sending OTP emails

---

## Configuration
All credentials should be provided via environment variables or externalized config. Do not commit secrets to VCS.

The project includes a sample `API_keys.env` (not for production). Recommended environment variables:

- MySQL
  - `SPRING_DATASOURCE_URL` (e.g., `jdbc:mysql://localhost:3306/lifestyle_db`)
  - `SPRING_DATASOURCE_USERNAME`
  - `SPRING_DATASOURCE_PASSWORD`
- JPA
  - `SPRING_JPA_HIBERNATE_DDL_AUTO` (e.g., `validate`/`update` for local dev)
- MongoDB
  - `SPRING_DATA_MONGODB_HOST` (default: `localhost`)
  - `SPRING_DATA_MONGODB_PORT` (default: `27017`)
  - `SPRING_DATA_MONGODB_DATABASE` (e.g., `University`)
- Mail (Gmail example)
  - `SPRING_MAIL_HOST` (e.g., `smtp.gmail.com`)
  - `SPRING_MAIL_PORT` (e.g., `587`)
  - `SPRING_MAIL_USERNAME`
  - `SPRING_MAIL_PASSWORD` (use an App Password)
- Spring profile
  - `SPRING_PROFILES_ACTIVE` = `dev` (default) or `prod`

Note: The provided `src/main/resources/application.properties` contains example values. For security, override them with env vars in your environment.

---

## Build & Run

- Using Maven Wrapper (recommended):
  - Windows: `mvnw.cmd spring-boot:run`
  - Linux/macOS: `./mvnw spring-boot:run`
- Or build a JAR: `mvnw.cmd clean package` and run it: `java -jar target/LifeStyleMatcherProject-0.0.1-SNAPSHOT.jar`

Default server port: `8080` (override via `SERVER_PORT` or `server.port`).

---

## API Overview
Base URLs (default):
- Public APIs served on `http://localhost:8080`

### Authentication & Users (`/auth`)
- POST `/auth/signup`
  - Body (JSON):
    ```json
    { "name": "Alice", "email": "alice@example.com", "password": "Secret123!" }
    ```
  - Response: `{ "message": "Signup successful!" }` or error message.

- POST `/auth/send-otp`
  - Body (JSON): `{ "email": "alice@example.com" }`
  - Validates email deliverability via external API, sends OTP if OK.
  - Response: `{ "message": "OTP sent to your email." }` or error.

- POST `/auth/verify-signup-otp`
  - Body (JSON): `{ "email": "alice@example.com", "otp": "123456" }`
  - Response: `{ "message": "OTP verified successfully." }` or `Invalid OTP`.

- POST `/auth/login`
  - Body (JSON): `{ "email": "alice@example.com", "password": "Secret123!" }`
  - Response: `{ "message": "Welcome back, <name>!" }` or `Invalid credentials`.

- POST `/auth/forgot-password`
  - Body (JSON): `{ "email": "alice@example.com" }`
  - Sends OTP to email if registered.

- POST `/auth/verify-otp`
  - Body (JSON): `{ "email": "alice@example.com", "otp": "123456" }`

- PUT `/auth/reset-password`
  - Body (JSON): `{ "email": "alice@example.com", "password": "NewSecret!" }`

Notes:
- Passwords are stored hashed using Spring Security `PasswordEncoder`.
- CORS is enabled for all origins in controllers via `@CrossOrigin(origins = "*")` and `CorsConfig`.

### Matching (`/api`)
- POST `/api/match`
  - Body (example):
    ```json
    {
      "budget": 20000,
      "gym": true,
      "hospital": false,
      "pet": true,
      "priorities": {
        "safety": 8,
        "walkability": 7,
        "food": 6,
        "internet": 8,
        "transport": 7,
        "market": 6,
        "noise": 3
      }
    }
    ```
  - Returns an array of top results (limited to 10), each like:
    ```json
    {
      "name": "Neighborhood Name",
      "city": "Ahmedabad",
      "score": 87,
      "avgRent": 12000,
      "type": "APARTMENT",
      "bhkOrRoomType": "2BHK",
      "location": "Specific Location",
      "furnishing": "SEMI_FURNISHED",
      "gender": "ANY",
      "source": "Listing Source"
    }
    ```

Implementation details:
- Neighborhoods are fetched by city (currently hardcoded `Ahmedabad`).
- Boolean filters (gym/hospital/pet) are mandatory filters when set.
- Listings are fetched with rent ≤ budget and sorted by lowest rent.
- Results are sorted by a computed score and top 10 are returned.

---

## Data & Persistence
- MySQL: User accounts (and other relational entities like `Student`).
- MongoDB: Neighborhood and Listing datasets via `NeighborhoodRepository` and `ListingRepository`.

Ensure the databases are running and accessible per your configuration before starting the app.

---

## Security
- Spring Security configured with separate `DevSecurityConfig` and `ProdSecurityConfig` (profiles).
- Passwords hashed with `PasswordEncoder`.
- CORS allowed for cross‑origin frontends (adjust in `CorsConfig` for production).

---

## Environment Profiles
- `dev` (default): development‑oriented settings
- `prod`: production settings

Set with `SPRING_PROFILES_ACTIVE` or `spring.profiles.active`.

---

## Running Tests
- Unit tests (if any) can be run with:
  - Windows: `mvnw.cmd test`
  - Linux/macOS: `./mvnw test`

---

## Troubleshooting
- "Cannot connect to MySQL": verify `SPRING_DATASOURCE_*` and that MySQL is running and user has privileges.
- "Cannot connect to MongoDB": verify host/port and database.
- "Mail authentication failed": use an App Password for Gmail and enable TLS (`587`).
- CORS issues: confirm `CorsConfig` and controller‑level `@CrossOrigin` settings; adjust allowed origins in production.
- Invalid OTP: OTPs are time/attempt limited; request a new one and check email deliverability.

---

## Security & Secrets
- Do NOT commit real credentials to the repository.
- Prefer environment variables or a secure secrets manager.
- Rotate any credentials that have ever been committed.

---

## License
Add your license text here (e.g., MIT, Apache 2.0). If this is private/proprietary, specify usage restrictions accordingly.

---

## Acknowledgements
- Spring Boot team and ecosystem.
- Abstract API (email validation) — used in signup OTP validation flow.
