# Academic Management System

A full-stack desktop application built with **Java Swing** and **MySQL** for managing students, professors, courses, enrollments, and grades in an academic institution.

Developed as a final project for the Programming Lab course at **Universidad de Palermo**.

---

## Assignment Requirements

### Core Functionality

| # | Requirement | Status |
|---|-------------|--------|
| 1 | **User roles**: Administrators can create courses and register students. Professors can enter grades. | Done |
| 2 | Each course has a **price** and a **capacity** (max students). | Done |
| 3 | Each student has a **limit of courses** they can enroll in simultaneously. If they reach the limit (3), they must pass one before enrolling in another. | Done |
| 4 | Each course has a configurable **passing grade**. Only final grades are managed for approval. | Done |
| 5 | Generate a **report of courses** with enrolled students and **revenue**. | Done |
| 6 | Display a **revenue chart** per course. | Done |

### Additional Features (Required)

| # | Requirement | Status |
|---|-------------|--------|
| 1 | Manage **partial grades**. A student cannot take the final exam without passing all N partials. | Done |
| 2 | Each course has a configurable **number of partial exams**. | Done |
| 3 | **Date-based promotions**: courses can have a reduced price within a date range. | Done |
| 4 | **Enrolled vs. Approved report** per course. | Done |

### Bonus Points

| # | Requirement | Status |
|---|-------------|--------|
| 1 | Manage student **subscriptions** (enrollment costs zero, prorated in revenue report). | Not implemented |

---

## Extras Added Beyond the Assignment

- **Retake exams (Recuperatorios)** -- Students who fail a partial can take a retake. If they fail the retake, the course is marked as FAILED and they must re-enroll.
- **Final exam attempts** -- Students get up to 3 attempts at the final exam. After 3 failures, the course is marked as FAILED.
- **Subject retake** -- When a course is FAILED, the admin can reset all grades so the student can start fresh.
- **PDF enrollment receipts** -- Generate downloadable PDF receipts using iText 7 (with plain text fallback).
- **Modern themed UI** -- Custom color palette, hover effects, styled tables, and a consistent visual theme across all panels.
- **Interactive revenue chart** -- Bar chart using JFreeChart (with a custom Swing-painted fallback if JFreeChart is unavailable).
- **Admin authentication** -- Login screen with ID and password validation against the database.
- **Role-based access** -- Administrators see all tabs; Professors only see Grade Management.
- **Database seeder** -- Pre-built `DataSeeder` class to populate the database with sample data for quick testing.
- **Portable configuration** -- Database credentials via environment variables so anyone can run the project without modifying source code.

---

## Tech Stack

| Component | Technology |
|-----------|------------|
| Language | Java 17+ |
| UI Framework | Java Swing |
| Database | MySQL 8.0+ |
| Charts | JFreeChart 1.5.4 |
| PDF Generation | iText 7.2.5 |

## Project Structure

```
src/
├── APLICACION/          # Entry points (Main, DataSeeder)
├── CONEXION/            # Database connection manager
├── DAO/                 # Data Access Objects (interfaces + implementations)
├── EXCEPCIONES/         # Custom exceptions
├── InterfazVisual/      # Swing UI panels and theme
├── SERVICIO/            # Business logic / Service layer
└── Sistema/             # Model classes (Alumno, Profesor, Curso, etc.)
lib/                     # Dependencies (MySQL connector, JFreeChart, iText)
```

## Architecture

```
UI (Swing Panels) --> Services --> DAOs --> MySQL Database
```

---

## Setup & Installation

### 1. Clone the repository

```bash
git clone https://github.com/EmilianoLescuras/academic-management-system.git
cd academic-management-system
```

### 2. Create the MySQL database

```bash
mysql -u root -p
```

```sql
CREATE DATABASE alumnosdb;
EXIT;
```

### 3. Configure database credentials

```bash
export DB_USER="root"
export DB_PASSWORD="your_mysql_password"
```

> If no environment variables are set, defaults are `root` / `root` on `localhost:3306/alumnosdb`.

### 4. Compile

```bash
javac -cp "lib/*" -d out src/CONEXION/*.java src/Sistema/*.java src/EXCEPCIONES/*.java src/DAO/*.java src/SERVICIO/*.java src/InterfazVisual/*.java src/APLICACION/*.java
```

### 5. Seed the database

```bash
java -cp "out:lib/*" APLICACION.DataSeeder
```

Default admin credentials:

| Role | ID | Password |
|------|-----|----------|
| Admin | 1001 | admin123 |
| Supervisor | 1002 | super456 |

### 6. Run the application

```bash
java -cp "out:lib/*" InterfazVisual.MainSistemaVisual
```

---

## License

This project was developed as an academic assignment at Universidad de Palermo.
