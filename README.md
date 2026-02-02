# Smart Clinic Management System (Spring Boot + MySQL + JWT)

This is a starter project for a Smart Clinic Management System with Doctor, Patient, and Admin roles.

## Tech
- Java 17
- Spring Boot 3
- Spring Data JPA
- MySQL
- JWT (jjwt)
- Maven
- Docker (multi-stage build)
- GitHub Actions CI

## Quick start (local)
1. Create a MySQL database named `smart_clinic`
2. Update `src/main/resources/application.properties` if needed
3. Run:
   ```bash
   mvn spring-boot:run
   ```

## Quick start (Docker)
```bash
docker build -t smart-clinic .
docker run -p 8080:8080 --env SPRING_DATASOURCE_URL=jdbc:mysql://host.docker.internal:3306/smart_clinic smart-clinic
```

## API (high level)
- `POST /api/auth/doctor/login`
- `POST /api/auth/patient/login`
- `GET  /api/doctors/{doctorId}/availability?date=YYYY-MM-DD` (JWT required)
- `POST /api/appointments` (JWT required)
- `GET  /api/appointments/doctor/{doctorId}?date=YYYY-MM-DD` (JWT required)
- `POST /api/prescriptions` (JWT required)
