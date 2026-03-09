
package APLICACION;

import SERVICIO.*;
import EXCEPCIONES.*;
import Sistema.*;

import java.time.LocalDate;
import java.util.List;

/**
 * Utility class to seed the database with initial data.
 * Run this once before using the application for the first time.
 */
public class DataSeeder {

    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("  DATABASE SEEDER");
        System.out.println("========================================\n");

        seedProfessors();
        seedCourses();
        seedStudents();
        seedAdministrators();

        System.out.println("\n========================================");
        System.out.println("  SEEDING COMPLETED SUCCESSFULLY");
        System.out.println("========================================");
    }

    private static void seedProfessors() {
        System.out.println("--- Seeding Professors ---");
        ProfesorService profesorService = new ProfesorService();
        try {
            profesorService.save(new Profesor(0, "Carlos", "Ramirez", "cramirez@uni.edu"));
            profesorService.save(new Profesor(0, "Ana", "Lopez", "alopez@uni.edu"));
            profesorService.save(new Profesor(0, "Jorge", "Martinez", "jmartinez@uni.edu"));
            System.out.println("Professors seeded successfully.");
        } catch (ServiceException e) {
            System.out.println("Error seeding professors: " + e.getMessage());
        }
    }

    private static void seedCourses() {
        System.out.println("\n--- Seeding Courses ---");
        CursoService cursoService = new CursoService();
        try {
            cursoService.save(new Curso(0, "Mathematics 1", 2500.0, 30, 4.0, 2,
                    LocalDate.of(2025, 1, 1), LocalDate.of(2025, 3, 1), 2000.0));
            cursoService.save(new Curso(0, "Programming 1", 3000.0, 25, 4.0, 3,
                    LocalDate.of(2025, 2, 1), LocalDate.of(2025, 4, 1), 2500.0));
            cursoService.save(new Curso(0, "Databases", 2800.0, 20, 4.0, 2,
                    null, null, 0));
            cursoService.save(new Curso(0, "English", 1500.0, 40, 6.0, 2,
                    LocalDate.of(2025, 1, 15), LocalDate.of(2025, 2, 15), 1200.0));
            System.out.println("Courses seeded successfully.");
        } catch (ServiceException e) {
            System.out.println("Error seeding courses: " + e.getMessage());
        }
    }

    private static void seedStudents() {
        System.out.println("\n--- Seeding Students ---");
        AlumnoService alumnoService = new AlumnoService();
        try {
            alumnoService.save(new Alumno(0, "Lucia", "Fernandez", "lucia@mail.com"));
            alumnoService.save(new Alumno(0, "Martin", "Gonzalez", "martin@mail.com"));
            alumnoService.save(new Alumno(0, "Sofia", "Rodriguez", "sofia@mail.com"));
            alumnoService.save(new Alumno(0, "Diego", "Torres", "diego@mail.com"));
            alumnoService.save(new Alumno(0, "Valentina", "Perez", "valentina@mail.com"));
            System.out.println("Students seeded successfully.");
        } catch (ServiceException e) {
            System.out.println("Error seeding students: " + e.getMessage());
        }
    }

    private static void seedAdministrators() {
        System.out.println("\n--- Seeding Administrators ---");
        AdministradorService adminService = new AdministradorService();
        try {
            adminService.save(new Administrador(1001, "Admin", "1234567890", "admin123", "admin@uni.edu"));
            adminService.save(new Administrador(1002, "Supervisor", "0987654321", "super456", "super@uni.edu"));
            System.out.println("Administrators seeded successfully.");
            System.out.println("  Admin credentials: ID=1001 | Password=admin123");
            System.out.println("  Supervisor credentials: ID=1002 | Password=super456");
        } catch (ServiceException e) {
            System.out.println("Error seeding administrators: " + e.getMessage());
        }
    }
}
