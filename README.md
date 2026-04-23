# 🔧 FleetOps Maintenance Service

The Maintenance Service manages the **pending task queue** for drivers within the FleetOps Vehicle Maintenance Platform. It acts as a staging area where drivers can draft service issues before formalizing them into an official Service Request.

## 🛠️ Tech Stack
*   **Framework:** Spring Boot 3.4
*   **Database:** PostgreSQL (uses `maintenance_db`)
*   **Authentication:** Stateless JWT (Validated via `JwtAuthenticationFilter`)

## 🎯 Responsibilities
*   **Task Staging:** Maintains a persistent queue of pending maintenance tasks per authenticated user.
*   **Task Management:** Supports adding and removing items from the queue.
*   **Queue Clearing:** The queue is automatically cleared when a driver successfully submits a formal Service Request.

## 📡 API Endpoints

| Method | Endpoint | Auth Required | Description |
| :--- | :--- | :--- | :--- |
| `GET` | `/tasks` | Yes (JWT) | Get current user's queued tasks |
| `POST` | `/tasks/add` | Yes (JWT) | Add a task to the queue `{vehicleId, description}` |
| `DELETE` | `/tasks/clear` | Yes (JWT) | Clear all items from the current user's queue |

## 🚀 Running Locally

### Prerequisites
*   Java 21
*   Maven
*   PostgreSQL running locally (with `maintenance_db` created)

### Environment Variables
```bash
export JWT_SECRET=your-super-secret-key-minimum-32-chars
./mvnw spring-boot:run
```

## 🐳 Docker

```bash
docker build -t fleetops-maintenance-service:v1.0.0 .
```
