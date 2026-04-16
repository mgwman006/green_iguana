# 🧭 Maplewood Course Planning App

This project is a **monorepo** containing both Backend and Frontend applications.

---

## 📁 Project Structure

```text
.
├── course-planning-api              # Spring Boot backend
│   ├── Dockerfile
│   ├── mvnw
│   ├── pom.xml
│   └── src
│       ├── main
│       └── test
│
├── course-planning-ui               # React + Vite frontend
│   ├── app
│   │   ├── api
│   │   ├── components
│   │   ├── service
│   │   ├── store
│   │   ├── types
│   │   └── utilities
│   ├── index.html
│   ├── package.json
│   ├── vite.config.ts
│   └── tsconfig.json
│
├── docker-compose.yml              # Multi-container setup
├── maplewood_school.sqlite         # SQLite database file
└── README.md
```

---

## 🧰 Tech Stack

| Layer        | Technology |
|-------------|------------|
| Frontend    | React (Vite), TypeScript |
| Backend     | Spring Boot, Spring Web, JPA |
| Database    | SQLite (file-based) |
| Build Tools | Maven, Vite |
| DevOps      | Docker, Docker Compose |

---

## 📦 Prerequisites

Make sure you have installed:

- Docker
- Docker Compose

### 🔍 Verify installation

```bash
docker --version
docker compose version
```

---

## ▶️ Running the Application

### 1. Navigate to the project root

```bash
cd green_iguana
```

---

### 2. Ensure SQLite database exists

The database file must be in the project root:

```text
maplewood_school.sqlite
```

If it does not exist:

```bash
touch maplewood_school.sqlite
```

(Optional) Fix permissions:

```bash
chmod 666 maplewood_school.sqlite
```

---

### 3. Start the application

```bash
docker compose up --build
```

This will:

- Build backend (Spring Boot)
- Build frontend (React + Vite)
- Start both containers

---

### 4. Access the application

| Service   | URL                     |
|----------|--------------------------|
| Frontend | http://localhost:3000   |
| Backend  | http://localhost:8080   |

---

## 🔄 Stopping the Application

Stop running containers:

```text
Ctrl + C
```

Or:

```bash
docker compose down
```

---

## 🧪 Useful Commands

### View running containers
```bash
docker ps
```

### View logs (all services)
```bash
docker compose logs -f
```

### Frontend logs only
```bash
docker logs maplewood-frontend
```

### Backend logs only
```bash
docker logs maplewood-backend
```

### Rebuild after changes
```bash
docker compose up --build
```

---

## 🧠 Notes

- Frontend communicates with backend via:
  ```text
  http://localhost:8080
  ```

- Backend uses SQLite database mounted from host:
  ```text
  ./maplewood_school.sqlite → /data/maplewood_school.sqlite
  ```

- Spring profile `docker` is active when running via Docker Compose

---

## ⚠️ Troubleshooting

### Backend not starting
```bash
docker logs maplewood-backend
```

### Frontend not loading
```bash
docker logs maplewood-frontend
```

### Database issues
Ensure file exists and has correct permissions:

```bash
chmod 666 maplewood_school.sqlite
```

---

## 🏗️ Architecture Overview

```text
Browser (localhost:3000)
        ↓
React (Vite frontend)
        ↓
Spring Boot API (localhost:8080)
        ↓
SQLite database (file)
```

---

## 🎯 Quick Start

Run everything with:

```bash
docker compose up --build
```

🚀 Done — application is ready.


## 📊 Testing Scenarios For Existing Data

### Scenario 1: Valid Enrollment
- Student "Joseph Young" (Grade 10, student_id: 101) wants to enroll in "Chemistry I"
- System checks: Has she passed "Biology I"? (Yes)
- System checks: Does it conflict with her current schedule? (No)
- System checks: Is she at/below 5 courses? (Currently has 0)
- Result: ✅ Enrollment succeeds

### Scenario 2: Prerequisite Violation
- Student "Joseph Young" (Grade 10, student_id: 101) wants to enroll in "English II: Literature"
- System checks: Has he passed "English I: Composition"? (No record)
- Result: ❌ Enrollment blocked - missing prerequisite

### Scenario 3: Time Conflict
- Student "Joseph Young" (Grade 10, student_id: 101) attempts to add "Algebra I" (Mon/Tue/Wed/Thus/Fri 12:00-13:00)
- Current schedule includes "Chemistry I" (Mon/Tue/Wed/Thus/Fri 12:00-13:00)
- Result: ❌ Enrollment blocked - schedule conflict
  
### Scenario 4: Course Limit 
- Due to resource this scenario can only be tested if you reduce class capacity to 4 or lower
- Student already enrolled in 5 courses attempts to add a 6th
- Result: ❌ Enrollment blocked - maximum courses exceeded

### 5 Courses Tha can registered by Joseph Young
- World History
- Chemistry I
- Algebra I
- Intro to Programming
- Journalism