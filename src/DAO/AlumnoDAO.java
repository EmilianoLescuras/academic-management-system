package DAO;

import Sistema.Alumno;
import EXCEPCIONES.DAOException;
import CONEXION.DBmanager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AlumnoDAO implements IAlumnoDAO {

    @Override
    public int save(Alumno student) throws DAOException {
        String sql = "INSERT INTO ALUMNOS (EMAIL, FIRST_NAME, LAST_NAME) VALUES (?, ?, ?)";
        try (Connection conn = DBmanager.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, student.getEmail());
            ps.setString(2, student.getFirstName());
            ps.setString(3, student.getLastName());

            ps.executeUpdate();

            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int studentId = generatedKeys.getInt(1);
                    student.setStudentId(studentId);
                    System.out.println("Student saved with ID: " + studentId);
                    return studentId;
                }
            }
            return -1;
        } catch (SQLException e) {
            throw new DAOException("AlumnoDAO.save: " + e.getMessage(), e);
        }
    }

    @Override
    public void delete(int studentId) throws DAOException {
        String sql = "DELETE FROM ALUMNOS WHERE LEGAJO = ?";
        try (Connection conn = DBmanager.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, studentId);
            ps.executeUpdate();

            System.out.println("Student deleted successfully.");
        } catch (SQLException e) {
            throw new DAOException("AlumnoDAO.delete: " + e.getMessage(), e);
        }
    }

    @Override
    public void update(Alumno student) throws DAOException {
        String sql = "UPDATE ALUMNOS SET EMAIL = ?, FIRST_NAME = ?, LAST_NAME = ? WHERE LEGAJO = ?";
        try (Connection conn = DBmanager.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, student.getEmail());
            ps.setString(2, student.getFirstName());
            ps.setString(3, student.getLastName());
            ps.setInt(4, student.getStudentId());

            ps.executeUpdate();

            System.out.println("Student modified successfully.");
        } catch (SQLException e) {
            throw new DAOException("AlumnoDAO.update: " + e.getMessage(), e);
        }
    }

    @Override
    public Alumno findById(int studentId) throws DAOException {
        String sql = "SELECT * FROM ALUMNOS WHERE LEGAJO = ?";
        try (Connection conn = DBmanager.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, studentId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Alumno(
                            rs.getInt("LEGAJO"),
                            rs.getString("FIRST_NAME"),
                            rs.getString("LAST_NAME"),
                            rs.getString("EMAIL"));
                } else {
                    return null;
                }
            }
        } catch (SQLException e) {
            throw new DAOException("AlumnoDAO.findById: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Alumno> findAll() throws DAOException {
        List<Alumno> students = new ArrayList<>();
        String sql = "SELECT * FROM ALUMNOS";
        try (Connection conn = DBmanager.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Alumno student = new Alumno(
                        rs.getInt("LEGAJO"),
                        rs.getString("FIRST_NAME"),
                        rs.getString("LAST_NAME"),
                        rs.getString("EMAIL"));
                students.add(student);
            }
        } catch (SQLException e) {
            throw new DAOException("AlumnoDAO.findAll: " + e.getMessage(), e);
        }
        return students;
    }

}
