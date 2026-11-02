# Critter Chronologer

A clean Spring Boot REST API implementation for the Udacity Critter Chronologer assignment.

## Stack
- Java 21 (runs on newer JDKs such as 22)
- Spring Boot 3.3.5
- Spring Web
- Spring Data JPA / Hibernate
- PostgreSQL
- Maven

## 1. Create the PostgreSQL database

```sql
CREATE DATABASE critter;
```

Default local connection:
- database: `critter`
- username: `postgres`
- password: `postgres`

Change credentials in `src/main/resources/application.properties`, or set environment variables:
- `DB_URL`
- `DB_USERNAME`
- `DB_PASSWORD`

## 2. Run

```bash
mvn spring-boot:run
```

Or import `pom.xml` into IntelliJ IDEA / VS Code and run `CritterApplication`.

Health check:
`GET http://localhost:8082/test`

## 3. Main endpoints

| Method | Endpoint | Purpose |
|---|---|---|
| POST | `/user/customer` | Create customer |
| GET | `/user/customer` | List customers |
| GET | `/user/customer/pet/{petId}` | Find pet owner |
| POST | `/user/employee` | Create employee |
| GET | `/user/employee/{employeeId}` | Get employee |
| PUT | `/user/employee/{employeeId}` | Set employee availability |
| GET | `/user/employee/availability` | Find employees by date and skills (JSON body) |
| POST | `/pet` | Create pet |
| GET | `/pet/{petId}` | Get pet |
| GET | `/pet/owner/{ownerId}` | List owner's pets |
| POST | `/schedule` | Create schedule |
| GET | `/schedule/employee/{employeeId}` | Schedules by employee |
| GET | `/schedule/pet/{petId}` | Schedules by pet |
| GET | `/schedule/customer/{customerId}` | Schedules by customer |

## 4. Request examples

Create customer:
```json
{
  "name": "Nandini",
  "phoneNumber": "9876543210",
  "notes": "Morning appointments",
  "petIds": []
}
```

Create employee:
```json
{
  "name": "Rahul",
  "skills": ["FEEDING", "WALKING"],
  "daysAvailable": ["MONDAY", "WEDNESDAY", "FRIDAY"]
}
```

Create pet (ownerId must be a number, not an object):
```json
{
  "name": "Bruno",
  "type": "DOG",
  "birthDate": "2023-05-10",
  "notes": "Friendly dog",
  "ownerId": 1
}
```

Create schedule:
```json
{
  "employeeIds": [1],
  "petIds": [1],
  "date": "2026-11-04",
  "activities": ["FEEDING", "WALKING"]
}
```

## Notes
- DTO mapping is explicit; this avoids reflection-based `BeanUtils.copyProperties()` conversion errors.
- `ownerId` is a numeric `Long`.
- Dates use ISO format `yyyy-MM-dd`.
- The availability endpoint follows the original assignment's GET-with-request-body convention. Some HTTP clients/proxies do not handle GET bodies consistently; if needed, change it to POST in both controller and client.
- This is a clean implementation scaffold. Run the supplied functional tests from your course starter against it and compare any course-specific DTO fields or expected endpoint details before submitting.
