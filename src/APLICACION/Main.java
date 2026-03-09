
package APLICACION;

import DAO.CursoDAO;
import SERVICIO.AlumnoService;
import SERVICIO.CursoAlumnoService;
import SERVICIO.CursoService;
import SERVICIO.ProfesorService;
import Sistema.*;

import java.time.LocalDate;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== ACADEMIC MANAGEMENT SYSTEM ===\n");

        // ======================== CRUD TESTS ========================

        // ---- STUDENTS CRUD ----
        AlumnoService alumnoService = new AlumnoService();
        CursoService cursoService = new CursoService();
        ProfesorService profesorService = new ProfesorService();
        CursoAlumnoService cursoAlumnoService = new CursoAlumnoService();

        try {
            System.out.println("--- Testing Student CRUD ---");
            Alumno a = new Alumno(0, "John", "Doe", "john@example.com");
            alumnoService.save(a);
            Alumno found = alumnoService.findById(a.getStudentId());
            System.out.println("Student found: " + found);

            found.setFirstName("John Updated");
            alumnoService.update(found);
            System.out.println("Student updated: " + alumnoService.findById(found.getStudentId()));

            List<Alumno> all = alumnoService.findAll();
            System.out.println("Total students: " + all.size());
        } catch (Exception e) {
            System.out.println("Error in student CRUD: " + e.getMessage());
        }

        // ---- PROFESSORS CRUD ----
        try {
            System.out.println("\n--- Testing Professor CRUD ---");
            Profesor p = new Profesor(0, "Carlos", "Martinez", "prof@example.com");
            profesorService.save(p);
            Profesor foundP = profesorService.findById(p.getId());
            System.out.println("Professor found: " + foundP);
        } catch (Exception e) {
            System.out.println("Error in professor CRUD: " + e.getMessage());
        }

        // ---- COURSES CRUD ----
        try {
            System.out.println("\n--- Testing Course CRUD ---");
            Curso c = new Curso(0, "Mathematics 1", 150.0, 30, 4.0, 2,
                    LocalDate.of(2025, 1, 1), LocalDate.of(2025, 3, 1), 100.0);
            cursoService.save(c);
            Curso foundC = cursoService.findById(c.getId());
            System.out.println("Course found: " + foundC);
        } catch (Exception e) {
            System.out.println("Error in course CRUD: " + e.getMessage());
        }

        // ======================== REPORTS ========================
        try {
            System.out.println("\n--- Revenue Report ---");
            List<String> revenue = cursoAlumnoService.revenueReport();
            revenue.forEach(System.out::println);
        } catch (Exception e) {
            System.out.println("Error in revenue report: " + e.getMessage());
        }

        try {
            System.out.println("\n--- Approved Report ---");
            List<String> approved = cursoAlumnoService.approvedReport();
            approved.forEach(System.out::println);
        } catch (Exception e) {
            System.out.println("Error in approved report: " + e.getMessage());
        }

        System.out.println("\n=== TESTS COMPLETED ===");
    }
}
