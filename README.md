# Academic Management System

A full-stack desktop application built with **Java Swing** and **MySQL** for managing students, professors, courses, enrollments, and grades in an academic institution.

## Features

- **Student Management** -- CRUD operations for student records
- **Professor Management** -- CRUD operations for professor records
- **Course Management** -- Create courses with pricing, capacity, passing grades, and promotional periods
- **Enrollment System** -- Enroll/unenroll students in courses with business rule validation (max 3 active courses)
- **Grade Management** -- Partial exams, retakes, and final exams (max 3 attempts)
- **Revenue Reports** -- View revenue per course with interactive bar charts
- **Approved Reports** -- Track approved students per course
- **PDF Receipts** -- Generate enrollment receipts (PDF with iText or plain text fallback)
- **Admin Login** -- Secure administrator access with ID and password
- **Modern UI** -- Clean, themed interface with custom colors, hover effects, and styled tables

## Tech Stack

| Component | Technology |
|-----------|------------|
| Language | Java 17+ |
| UI Framework | Java Swing |
| Database | MySQL 8.0+ |
| Charts | JFreeChart 1.5.4 |
| PDF Generation | iText 7.2.5 |
| Build | Maven / javac |

## Prerequisites

- **Java JDK 17** or higher
- **MySQL 8.0** or higher (running on `localhost:3306`)

## Setup & Installation

### 1. Clone the repository

```bash
git clone https://github.com/YOUR_USERNAME/academic-management-system.git
cd academic-management-system
```

### 2. Create the MySQL database

```sql
mysql -u root -p
```

```sql
CREATE DATABASE alumnosdb;
EXIT;
```

### 3. Configure database credentials

The application reads credentials from environment variables. Set them before running:

```bash
export DB_URL="jdbc:mysql://localhost:3306/alumnosdb"
export DB_USER="root"
export DB_PASSWORD="your_mysql_password"
```

> If no environment variables are set, the app uses default values: `root` / `root` on `localhost:3306/alumnosdb`.

### 4. Compile the project

```bash
javac -cp "lib/*" -d out src/CONEXION/*.java src/Sistema/*.java src/EXCEPCIONES/*.java src/DAO/*.java src/SERVICIO/*.java src/InterfazVisual/*.java src/APLICACION/*.java
```

### 5. Seed the database with initial data

```bash
java -cp "out:lib/*" APLICACION.DataSeeder
```

This creates sample professors, courses, students, and two administrator accounts:

| Role | ID | Password |
|------|-----|----------|
| Admin | 1001 | admin123 |
| Supervisor | 1002 | super456 |

### 6. Run the application

```bash
java -cp "out:lib/*" InterfazVisual.MainSistemaVisual
```

## Project Structure

```
src/
├── APLICACION/          # Entry points (Main, DataSeeder)
├── CONEXION/            # Database connection (DBmanager)
├── DAO/                 # Data Access Objects (interfaces + implementations)
├── EXCEPCIONES/         # Custom exceptions (DAOException, ServiceException)
├── InterfazVisual/      # Swing UI panels and theme
├── SERVICIO/            # Business logic / Service layer
└── Sistema/             # Model classes (Alumno, Profesor, Curso, etc.)
lib/                     # Dependencies (MySQL connector, JFreeChart, iText)
```

## Architecture

The project follows a **layered architecture**:

```
UI (Swing Panels) --> Services --> DAOs --> MySQL Database
```

- **Model Layer** (`Sistema/`) -- POJOs representing domain entities
- **DAO Layer** (`DAO/`) -- Database operations with prepared statements
- **Service Layer** (`SERVICIO/`) -- Business rules and validations
- **UI Layer** (`InterfazVisual/`) -- Swing panels with themed components

## Screenshots

After running the application, log in as Administrator (ID: 1001, Password: admin123) to access all management tabs including Students, Teachers, Courses, Administrators, Enrollments, Grade Management, and Charts.

## License

This project was developed as an academic assignment.
