package DAO;

import EXCEPCIONES.DAOException;
import Sistema.Curso;
import CONEXION.DBmanager;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CursoDAO implements ICursoDAO {

    @Override
    public int save(Curso course) throws DAOException {
        String sql = "INSERT INTO CURSOS (NAME, PRICE, CAPACITY, PASSING_GRADE, NUM_PARTIAL_GRADES, PROMO_START_DATE, PROMO_END_DATE, PROMO_PRICE) "
                +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBmanager.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, course.getName());
            ps.setDouble(2, course.getPrice());
            ps.setInt(3, course.getCapacity());
            ps.setDouble(4, course.getPassingGrade());
            ps.setInt(5, course.getNumPartialGrades());
            ps.setDate(6, course.getPromoStartDate() != null ? Date.valueOf(course.getPromoStartDate()) : null);
            ps.setDate(7, course.getPromoEndDate() != null ? Date.valueOf(course.getPromoEndDate()) : null);
            ps.setDouble(8, course.getPromoPrice());

            ps.executeUpdate();

            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int id = generatedKeys.getInt(1);
                    course.setId(id);
                    System.out.println("Course saved with ID: " + id);
                    return id;
                }
            }
            return -1;
        } catch (SQLException e) {
            throw new DAOException("CursoDAO.save: " + e.getMessage(), e);
        }
    }

    @Override
    public void delete(int id) throws DAOException {
        String sql = "DELETE FROM CURSOS WHERE ID = ?";
        try (Connection conn = DBmanager.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("CursoDAO.delete: " + e.getMessage(), e);
        }
    }

    @Override
    public void update(Curso course) throws DAOException {
        String sql = "UPDATE CURSOS SET NAME = ?, PRICE = ?, CAPACITY = ?, PASSING_GRADE = ?, NUM_PARTIAL_GRADES = ?, PROMO_START_DATE = ?, PROMO_END_DATE = ?, PROMO_PRICE = ? WHERE ID = ?";
        try (Connection conn = DBmanager.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, course.getName());
            ps.setDouble(2, course.getPrice());
            ps.setInt(3, course.getCapacity());
            ps.setDouble(4, course.getPassingGrade());
            ps.setInt(5, course.getNumPartialGrades());
            ps.setDate(6, course.getPromoStartDate() != null ? Date.valueOf(course.getPromoStartDate()) : null);
            ps.setDate(7, course.getPromoEndDate() != null ? Date.valueOf(course.getPromoEndDate()) : null);
            ps.setDouble(8, course.getPromoPrice());
            ps.setInt(9, course.getId());

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("CursoDAO.update: " + e.getMessage(), e);
        }
    }

    @Override
    public Curso findById(int id) throws DAOException {
        String sql = "SELECT * FROM CURSOS WHERE ID = ?";
        try (Connection conn = DBmanager.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Curso(
                        rs.getInt("ID"),
                        rs.getString("NAME"),
                        rs.getDouble("PRICE"),
                        rs.getInt("CAPACITY"),
                        rs.getDouble("PASSING_GRADE"),
                        rs.getInt("NUM_PARTIAL_GRADES"),
                        rs.getDate("PROMO_START_DATE") != null ? rs.getDate("PROMO_START_DATE").toLocalDate()
                                : null,
                        rs.getDate("PROMO_END_DATE") != null ? rs.getDate("PROMO_END_DATE").toLocalDate() : null,
                        rs.getDouble("PROMO_PRICE"));
            }
            return null;
        } catch (SQLException e) {
            throw new DAOException("CursoDAO.findById: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Curso> findAll() throws DAOException {
        List<Curso> list = new ArrayList<>();
        String sql = "SELECT * FROM CURSOS";
        try (Connection conn = DBmanager.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(new Curso(
                        rs.getInt("ID"),
                        rs.getString("NAME"),
                        rs.getDouble("PRICE"),
                        rs.getInt("CAPACITY"),
                        rs.getDouble("PASSING_GRADE"),
                        rs.getInt("NUM_PARTIAL_GRADES"),
                        rs.getDate("PROMO_START_DATE") != null ? rs.getDate("PROMO_START_DATE").toLocalDate()
                                : null,
                        rs.getDate("PROMO_END_DATE") != null ? rs.getDate("PROMO_END_DATE").toLocalDate() : null,
                        rs.getDouble("PROMO_PRICE")));
            }
        } catch (SQLException e) {
            throw new DAOException("CursoDAO.findAll: " + e.getMessage(), e);
        }
        return list;
    }

    @Override
    public void enrollStudent(int courseId, int studentId) throws DAOException {

    }

    @Override
    public void savePartialGrade(int courseId, int studentId, double grade) throws DAOException {

    }

    @Override
    public void saveFinalGrade(int courseId, int studentId, int grade) throws DAOException {

    }

    @Override
    public List<Integer> getEnrolledStudents(int courseId) throws DAOException {
        return List.of();
    }

    @Override
    public List<Double> getPartialGrades(int courseId, int studentId) throws DAOException {
        return List.of();
    }

    @Override
    public Integer getFinalGrade(int courseId, int studentId) throws DAOException {
        return 0;
    }
}
