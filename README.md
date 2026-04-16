# 🧭 Maplewood Course Planning App
![Java](https://img.shields.io/badge/Java-17-orange)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3-green)
![React](https://img.shields.io/badge/React-19-blue)
![Docker](https://img.shields.io/badge/Docker-ready-blue)

A full-stack **course enrollment and planning system** built as a **monorepo** containing a Spring Boot (backend) and React (frontend).

The system models real-world school scheduling constraints.

## ✨ Key Features

- 📚 Student course enrollment
- ⛔ Prerequisite enforcement
- 🕒 Schedule overlap detection
- 📈 Maximum course registration limits
- 🔄 REST API integration
- 🐳 One-command local setup via Docker Compose

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
├── maplewood_school.sqlite         # SQLite database
└── README.md
```

---

## 🧰 Tech Stack

| Layer | Technology |
|------|------------|
| Frontend | React, Vite, TypeScript |
| Backend | Spring Boot, Spring Web, Spring Data JPA |
| Database | SQLite |
| Build Tools | Maven, npm |
| DevOps | Docker, Docker Compose |

---

## 🏗️ Architecture

```text
Browser
   ↓
React Frontend (localhost:3000)
   ↓
Spring Boot REST API (localhost:8080)
   ↓
SQLite Database
```

---

## 🧠 Scheduling & Resource Allocation Strategy

The core challenge was not only student enrollment validation, but helping the school efficiently manage **limited operational resources** before registration begins.

These resources include:

- teacher availability
- classroom availability
- teacher subject expertise
- course workload hours
- timetable collisions
- student demand across courses

---

## 🎯 Core Design Philosophy

Instead of solving conflicts only at registration time, the system uses a **two-phase planning model**:

```text
Phase 1: Administrative Schedule Planning
Phase 2: Student Enrollment Validation
```

This mirrors how real schools operate.

---

## 🏗️ Phase 1: Preconfigured Academic Scheduling

Before students register, administrators define available course sections for the term.

Each section is created using four core constraints:

### 1. Teacher Availability

Teachers can only be assigned to times they are available.

Example:

```text
Mr. Smith available:
Mon–Fri, 08:00–13:00
```

Unavailable slots are excluded during scheduling.

---

### 2. Teacher Skill Set / Subject Qualification

Teachers are matched only to courses they are qualified to teach.

Example:

```text
Ms. Johnson:
✔ Chemistry
✔ Biology
✘ Mathematics
```

This prevents invalid resource assignment.

---

### 3. Room Availability

A classroom cannot host multiple sections at the same time.

Example:

```text
Room A101
Mon 10:00–11:00 already occupied
```

That slot becomes unavailable for other sections.

---

### 4. Course Workload Hours (Scheduling Driver)

Each course has a required number of teaching hours per week, and this was a major guide when planning timeslots.

Examples:

```text
Algebra I = 5 hours/week
Chemistry I = 3 hours/week
Programming Basics = 4 hours/week
```

The scheduling engine uses workload hours to determine:

- number of sessions required
- duration of each session
- weekly timetable placement
- efficient use of teacher time
- efficient use of classrooms

Example:

```text
Course requires 5 hours/week

Possible allocation:
Mon 09:00–10:00
Tue 09:00–10:00
Wed 09:00–10:00
Thu 09:00–10:00
Fri 09:00–10:00
```

This ensures the timetable reflects real academic workload requirements.

---

## 📚 Output of Phase 1

The result is a set of ready-to-enroll course sections such as:

```text
Algebra I - Section A
Teacher: Mr. Brown
Room: B201
Mon/Wed/Fri 09:00–10:00

Chemistry I - Section B
Teacher: Ms. Johnson
Room: Lab 1
Tue/Thu 11:00–12:30
```

Students register into these prebuilt sections rather than raw courses.

---

## 🧪 Phase 2: Student Enrollment Validation

Once sections exist, student registration becomes simpler and faster.

The system validates:

- prerequisite completion
- duplicate enrollment
- student schedule conflicts
- maximum course load
- seat availability

Because operational constraints were already solved earlier, student enrollment becomes efficient and predictable.

---

## ⚡ Why This Approach Is Effective

### Separation of Concerns

Administrative planning and student enrollment solve different problems.

By separating them:

- scheduling remains manageable
- enrollment becomes faster
- fewer runtime conflicts occur

---

### Better Resource Utilization

The school can maximize use of:

- teachers
- classrooms
- available timetable slots
- weekly teaching capacity

---

### Scalable Model

This design can grow into advanced optimization later:

- auto timetable generation
- waitlists
- teacher workload balancing
- peak demand forecasting
- room capacity optimization

---
## ⚖️ Trade-offs & Assumptions

- SQLite was selected for simplicity and zero external setup.
- The scheduling engine assumes administrator-managed section creation before enrollment opens.
- Enrollment validation prioritizes correctness over optimization.
- Docker Compose was chosen for local developer experience rather than production orchestration.
- Given challenge time constraints, emphasis was placed on core workflows over advanced UI polish.

---

## 🔮 Future Enhancements

Given more time, the planning engine could evolve into:

- timetable optimization algorithm
- constraint satisfaction solver
- teacher workload balancing
- preferred timeslot matching
- AI-assisted schedule generation
- demand-based section creation
- Comprehensive unit and integration test coverage
- Additional time would have been invested in automated testing, with priority given to core functionality during the challenge timeframe

---
## 🚀 Getting Started

### Prerequisites

Install:

- Docker
- Docker Compose

Verify:

```bash
docker --version
docker compose version
```

---

### Run Locally

#### 1. Clone Repository

```bash
git clone https://github.com/mgwman006/green_iguana.git
cd green_iguana
```

---

#### 2. Ensure Database Exists

```bash
touch maplewood_school.sqlite
chmod 666 maplewood_school.sqlite
```

---

#### 3. Start Application

```bash
docker compose up --build
```

---

#### 4. Open Application

| Service | URL |
|--------|-----|
| Frontend | http://localhost:3000 |
| Backend | http://localhost:8080 |

---

#### 🛑 Stop Application

```bash
docker compose down
```

---

## 🧪 Useful Commands

### Running Containers

```bash
docker ps
```

### View Logs

```bash
docker compose logs -f
```

### Backend Logs

```bash
docker logs maplewood-backend
```

### Frontend Logs

```bash
docker logs maplewood-frontend
```

### Rebuild

```bash
docker compose up --build
```

---

## 🧠 Design Decisions

### Monorepo Structure

Frontend and backend are maintained in one repository to simplify:

- onboarding
- local development
- version consistency
- deployment coordination

### SQLite Database

SQLite was selected because:

- zero external setup required
- lightweight
- ideal for demos and coding challenges

### Docker Compose

Used to provide a single-command startup experience and eliminate machine-specific setup issues.

---

## 📋 Testing Scenarios

### ✅ Scenario 1: Valid Enrollment

Student **Joseph Young** (`student_id: 101`) enrolls in **Chemistry I**

Checks:

- prerequisite satisfied
- no schedule conflict
- below 5-course limit

Result:

```text
Enrollment successful
```

---

### ❌ Scenario 2: Missing Prerequisite

Student attempts **English II: Literature**

Required:

```text
English I: Composition
```

Result:

```text
Enrollment blocked
```

---

### ❌ Scenario 3: Schedule Conflict

Student already enrolled in:

```text
Chemistry I (12:00–13:00)
```

Attempts:

```text
Algebra I (12:00–13:00)
```

Result:

```text
Enrollment blocked
```

---

### ❌ Scenario 4: Maximum Course Limit (5)

Ensure the student is already enrolled in the following five courses:

- World History
- Chemistry I
- Algebra I
- Intro to Programming
- Journalism

The student then attempts to enroll in **Biology I** as a sixth course.

Since maximum course load validation is executed first, enrollment is rejected.

Result:

```text
Enrollment blocked
```

---

### Valid Courses that Joseph Young can enroll at the same time (Student Number 101)

- World History
- Chemistry I
- Algebra I
- Intro to Programming
- Journalism

---

## ⚠️ Troubleshooting

### Backend Not Starting

```bash
docker logs maplewood-backend
```

### Frontend Not Loading

```bash
docker logs maplewood-frontend
```

### Database Issues

```bash
chmod 666 maplewood_school.sqlite
```

---



## 👨‍💻 Engineering Notes

This project emphasizes:

- clean separation of concerns
- maintainable code structure
- real-world validation logic
- production-style containerization
- developer experience

---

## 🎯 Quick Start

```bash
docker compose up --build
```

Application is ready 🚀

## ✅ Submission Notes

This solution focuses on demonstrating:

- practical system design
- business rule enforcement
- clean separation of responsibilities
- maintainable full-stack architecture
- realistic academic scheduling workflows