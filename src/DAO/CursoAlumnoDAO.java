
package DAO;

import CONEXION.DBmanager;
import EXCEPCIONES.DAOException;
import Sistema.CursoAlumno;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CursoAlumnoDAO {

    public List<CursoAlumno> getEnrollmentsByStudent(int studentId) throws DAOException {
        List<CursoAlumno> list = new ArrayList<>();
        String sql = "SELECT * FROM CURSO_ALUMNO WHERE ID_ALUMNO = ?";
        try (Connection conn = DBmanager.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, studentId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    CursoAlumno ca = new CursoAlumno(rs.getInt("ID_CURSO"), rs.getInt("ID_ALUMNO"));
                    ca.setPartialGrade(rs.getDouble("PARTIAL_GRADE"));
                    ca.setFinalGrade(rs.getObject("FINAL_GRADE") != null ? rs.getInt("FINAL_GRADE") : null);
                    list.add(ca);
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Error getting student enrollments: " + e.getMessage(), e);
        }
        return list;
    }

    public void enrollStudent(CursoAlumno enrollment) throws DAOException {
        String checkSql = "SELECT COUNT(*) AS total FROM CURSO_ALUMNO WHERE ID_CURSO=? AND ID_ALUMNO=?";
        String insertSql = "INSERT INTO CURSO_ALUMNO (ID_CURSO, ID_ALUMNO) VALUES (?, ?)";

        try (Connection conn = DBmanager.getConnection();
                PreparedStatement checkPs = conn.prepareStatement(checkSql)) {

            // Validate if enrollment already exists
            checkPs.setInt(1, enrollment.getCourseId());
            checkPs.setInt(2, enrollment.getStudentId());
            try (ResultSet rs = checkPs.executeQuery()) {
                if (rs.next() && rs.getInt("total") > 0) {
                    throw new DAOException("Student is already enrolled in this course.");
                }
            }

            // Insert enrollment
            try (PreparedStatement ps = conn.prepareStatement(insertSql)) {
                ps.setInt(1, enrollment.getCourseId());
                ps.setInt(2, enrollment.getStudentId());
                ps.executeUpdate();
            }

        } catch (SQLException e) {
            throw new DAOException("Error enrolling student: " + e.getMessage(), e);
        }
    }

    public void unenrollStudent(int courseId, int studentId) throws DAOException {
        try (Connection conn = DBmanager.getConnection()) {
            // Delete partial grades
            try (PreparedStatement ps = conn.prepareStatement(
                    "DELETE FROM NOTAS_PARCIALES WHERE ID_CURSO = ? AND ID_ALUMNO = ?")) {
                ps.setInt(1, courseId);
                ps.setInt(2, studentId);
                ps.executeUpdate();
            }

            // Delete retakes
            try (PreparedStatement ps = conn.prepareStatement(
                    "DELETE FROM RECUPERATORIOS WHERE ID_CURSO = ? AND ID_ALUMNO = ?")) {
                ps.setInt(1, courseId);
                ps.setInt(2, studentId);
                ps.executeUpdate();
            }

            // Delete final grade
            try (PreparedStatement ps = conn.prepareStatement(
                    "DELETE FROM NOTAS_FINALES WHERE ID_CURSO = ? AND ID_ALUMNO = ?")) {
                ps.setInt(1, courseId);
                ps.setInt(2, studentId);
                ps.executeUpdate();
            }

            // Delete enrollment
            try (PreparedStatement ps = conn.prepareStatement(
                    "DELETE FROM CURSO_ALUMNO WHERE ID_CURSO = ? AND ID_ALUMNO = ?")) {
                ps.setInt(1, courseId);
                ps.setInt(2, studentId);
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DAOException("Error unenrolling student: " + e.getMessage(), e);
        }
    }

    public void savePartialGrade(int courseId, int studentId, double grade) throws DAOException {
        String sql = "INSERT INTO NOTAS_PARCIALES (ID_CURSO, ID_ALUMNO, GRADE) VALUES (?, ?, ?)";
        try (Connection conn = DBmanager.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, courseId);
            ps.setInt(2, studentId);
            ps.setDouble(3, grade);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Error saving partial grade: " + e.getMessage(), e);
        }
    }

    public void saveFinalGrade(int courseId, int studentId, int finalGrade) throws DAOException {
        String sql = "INSERT INTO NOTAS_FINALES (ID_CURSO, ID_ALUMNO, GRADE) VALUES (?, ?, ?)";
        try (Connection conn = DBmanager.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, courseId);
            ps.setInt(2, studentId);
            ps.setInt(3, finalGrade);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Error saving final grade: " + e.getMessage(), e);
        }
    }

    public List<CursoAlumno> findAll() throws DAOException {
        List<CursoAlumno> list = new ArrayList<>();
        String sql = "SELECT ID_CURSO, ID_ALUMNO, FINAL_GRADE FROM CURSO_ALUMNO";

        try (Connection conn = DBmanager.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                CursoAlumno ca = new CursoAlumno(rs.getInt("ID_CURSO"), rs.getInt("ID_ALUMNO"));
                ca.setFinalGrade(rs.getObject("FINAL_GRADE") != null ? rs.getInt("FINAL_GRADE") : null);
                list.add(ca);
            }
        } catch (SQLException e) {
            throw new DAOException("Error getting enrollments: " + e.getMessage(), e);
        }
        return list;
    }

    // Get all partial grades of a student in a course
    public List<Double> getPartialGrades(int courseId, int studentId) throws DAOException {
        List<Double> grades = new ArrayList<>();
        String sql = "SELECT GRADE FROM NOTAS_PARCIALES WHERE ID_CURSO = ? AND ID_ALUMNO = ?";
        try (Connection conn = DBmanager.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, courseId);
            ps.setInt(2, studentId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    grades.add(rs.getDouble("GRADE"));
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Error getting partial grades: " + e.getMessage(), e);
        }
        return grades;
    }

    // Get final grade
    public Integer getFinalGrade(int courseId, int studentId) throws DAOException {
        String sql = "SELECT GRADE FROM NOTAS_FINALES WHERE ID_CURSO = ? AND ID_ALUMNO = ? ORDER BY ID DESC LIMIT 1";
        try (Connection conn = DBmanager.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, courseId);
            ps.setInt(2, studentId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("GRADE");
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Error getting final grade: " + e.getMessage(), e);
        }
        return null;
    }

    // Count final attempts
    public int countFinalAttempts(int courseId, int studentId) throws DAOException {
        String sql = "SELECT COUNT(*) AS total FROM NOTAS_FINALES WHERE ID_CURSO = ? AND ID_ALUMNO = ?";
        try (Connection conn = DBmanager.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, courseId);
            ps.setInt(2, studentId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("total");
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Error counting final attempts: " + e.getMessage(), e);
        }
        return 0;
    }

    // Get all final grades (to see attempt history)
    public List<Integer> getAllFinalGrades(int courseId, int studentId) throws DAOException {
        List<Integer> grades = new ArrayList<>();
        String sql = "SELECT GRADE FROM NOTAS_FINALES WHERE ID_CURSO = ? AND ID_ALUMNO = ? ORDER BY ID";
        try (Connection conn = DBmanager.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, courseId);
            ps.setInt(2, studentId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    grades.add(rs.getInt("GRADE"));
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Error getting final grades: " + e.getMessage(), e);
        }
        return grades;
    }

    // Revenue report by course
    public List<String> revenueReport() throws DAOException {
        List<String> report = new ArrayList<>();
        String sql = """
                    SELECT c.NAME, COUNT(ca.ID_ALUMNO) AS ENROLLED,
                           c.PRICE, (COUNT(ca.ID_ALUMNO) * c.PRICE) AS REVENUE
                    FROM CURSOS c
                    LEFT JOIN CURSO_ALUMNO ca ON c.ID = ca.ID_CURSO
                    GROUP BY c.ID, c.NAME, c.PRICE
                    ORDER BY REVENUE DESC
                """;

        try (Connection conn = DBmanager.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                String name = rs.getString("NAME");
                int enrolled = rs.getInt("ENROLLED");
                double price = rs.getDouble("PRICE");
                double revenue = rs.getDouble("REVENUE");

                report.add("Course: " + name +
                        " | Enrolled: " + enrolled +
                        " | Price: $" + price +
                        " | Revenue: $" + revenue);
            }

        } catch (SQLException e) {
            throw new DAOException("Error generating revenue report", e);
        }
        return report;
    }

    // Approved report
    public List<String> approvedReport() throws DAOException {
        List<String> report = new ArrayList<>();
        String sql = """
                    SELECT c.NAME,
                           COUNT(ca.ID_ALUMNO) AS ENROLLED,
                           SUM(CASE WHEN nf.GRADE >= c.PASSING_GRADE THEN 1 ELSE 0 END) AS APPROVED
                    FROM CURSOS c
                    LEFT JOIN CURSO_ALUMNO ca ON c.ID = ca.ID_CURSO
                    LEFT JOIN NOTAS_FINALES nf ON ca.ID_CURSO = nf.ID_CURSO AND ca.ID_ALUMNO = nf.ID_ALUMNO
                    GROUP BY c.ID, c.NAME
                    ORDER BY APPROVED DESC;
                """;

        try (Connection conn = DBmanager.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                String course = rs.getString("NAME");
                int enrolled = rs.getInt("ENROLLED");
                int approved = rs.getInt("APPROVED");

                report.add("Course: " + course +
                        " | Enrolled: " + enrolled +
                        " | Approved: " + approved);
            }
        } catch (SQLException e) {
            throw new DAOException("Error in approved report", e);
        }
        return report;
    }

    // ==================== RETAKES ====================

    // Save retake grade
    public void saveRetake(int courseId, int studentId, int partialNumber, double grade)
            throws DAOException {
        String sql = "INSERT INTO RECUPERATORIOS (ID_CURSO, ID_ALUMNO, PARTIAL_NUMBER, GRADE) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBmanager.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, courseId);
            ps.setInt(2, studentId);
            ps.setInt(3, partialNumber);
            ps.setDouble(4, grade);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Error saving retake: " + e.getMessage(), e);
        }
    }

    // Get retakes for a student in a course
    public List<Double> getRetakes(int courseId, int studentId) throws DAOException {
        List<Double> grades = new ArrayList<>();
        String sql = "SELECT GRADE FROM RECUPERATORIOS WHERE ID_CURSO = ? AND ID_ALUMNO = ? ORDER BY PARTIAL_NUMBER";
        try (Connection conn = DBmanager.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, courseId);
            ps.setInt(2, studentId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    grades.add(rs.getDouble("GRADE"));
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Error getting retakes: " + e.getMessage(), e);
        }
        return grades;
    }

    // Check if student already has a retake for a specific partial
    public boolean hasRetake(int courseId, int studentId, int partialNumber) throws DAOException {
        String sql = "SELECT COUNT(*) AS total FROM RECUPERATORIOS WHERE ID_CURSO = ? AND ID_ALUMNO = ? AND PARTIAL_NUMBER = ?";
        try (Connection conn = DBmanager.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, courseId);
            ps.setInt(2, studentId);
            ps.setInt(3, partialNumber);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("total") > 0;
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Error checking retake: " + e.getMessage(), e);
        }
        return false;
    }

    // Mark course as failed
    public void markAsFailed(int courseId, int studentId) throws DAOException {
        String sql = "UPDATE CURSO_ALUMNO SET STATUS = 'FAILED' WHERE ID_CURSO = ? AND ID_ALUMNO = ?";
        try (Connection conn = DBmanager.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, courseId);
            ps.setInt(2, studentId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Error marking as failed: " + e.getMessage(), e);
        }
    }

    // Get student status in a course
    public String getStatus(int courseId, int studentId) throws DAOException {
        String sql = "SELECT STATUS FROM CURSO_ALUMNO WHERE ID_CURSO = ? AND ID_ALUMNO = ?";
        try (Connection conn = DBmanager.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, courseId);
            ps.setInt(2, studentId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String status = rs.getString("STATUS");
                    return status != null ? status : "ACTIVE";
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Error getting status: " + e.getMessage(), e);
        }
        return "ACTIVE";
    }

    // Retake subject - deletes all grades and resets status
    public void retakeSubject(int courseId, int studentId) throws DAOException {
        try (Connection conn = DBmanager.getConnection()) {
            // Delete partial grades
            try (PreparedStatement ps = conn.prepareStatement(
                    "DELETE FROM NOTAS_PARCIALES WHERE ID_CURSO = ? AND ID_ALUMNO = ?")) {
                ps.setInt(1, courseId);
                ps.setInt(2, studentId);
                ps.executeUpdate();
            }

            // Delete retakes
            try (PreparedStatement ps = conn.prepareStatement(
                    "DELETE FROM RECUPERATORIOS WHERE ID_CURSO = ? AND ID_ALUMNO = ?")) {
                ps.setInt(1, courseId);
                ps.setInt(2, studentId);
                ps.executeUpdate();
            }

            // Delete final grade
            try (PreparedStatement ps = conn.prepareStatement(
                    "DELETE FROM NOTAS_FINALES WHERE ID_CURSO = ? AND ID_ALUMNO = ?")) {
                ps.setInt(1, courseId);
                ps.setInt(2, studentId);
                ps.executeUpdate();
            }

            // Reset status to ACTIVE
            try (PreparedStatement ps = conn.prepareStatement(
                    "UPDATE CURSO_ALUMNO SET STATUS = 'ACTIVE', FINAL_GRADE = NULL WHERE ID_CURSO = ? AND ID_ALUMNO = ?")) {
                ps.setInt(1, courseId);
                ps.setInt(2, studentId);
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DAOException("Error retaking subject: " + e.getMessage(), e);
        }
    }
}
