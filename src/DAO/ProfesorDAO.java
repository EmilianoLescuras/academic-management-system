package DAO;

import EXCEPCIONES.DAOException;
import Sistema.Profesor;
import CONEXION.DBmanager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProfesorDAO implements IProfesorDAO {

    @Override
    public int save(Profesor professor) throws DAOException {
        String sql = "INSERT INTO PROFESORES (FIRST_NAME, LAST_NAME, EMAIL) VALUES (?, ?, ?)";
        try (Connection conn = DBmanager.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, professor.getFirstName());
            ps.setString(2, professor.getLastName());
            ps.setString(3, professor.getEmail());
            ps.executeUpdate();

            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int id = generatedKeys.getInt(1);
                    professor.setId(id);
                    System.out.println("Professor saved with ID: " + id);
                    return id;
                }
            }
            return -1;
        } catch (SQLException e) {
            throw new DAOException("ProfesorDAO.save: " + e.getMessage(), e);
        }
    }

    @Override
    public void delete(int id) throws DAOException {
        String sql = "DELETE FROM PROFESORES WHERE ID = ?";
        try (Connection conn = DBmanager.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("ProfesorDAO.delete: " + e.getMessage(), e);
        }
    }

    @Override
    public void update(Profesor professor) throws DAOException {
        String sql = "UPDATE PROFESORES SET FIRST_NAME = ?, LAST_NAME = ?, EMAIL = ? WHERE ID = ?";
        try (Connection conn = DBmanager.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, professor.getFirstName());
            ps.setString(2, professor.getLastName());
            ps.setString(3, professor.getEmail());
            ps.setInt(4, professor.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("ProfesorDAO.update: " + e.getMessage(), e);
        }
    }

    @Override
    public Profesor findById(int id) throws DAOException {
        String sql = "SELECT * FROM PROFESORES WHERE ID = ?";
        try (Connection conn = DBmanager.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Profesor(rs.getInt("ID"),
                        rs.getString("FIRST_NAME"),
                        rs.getString("LAST_NAME"),
                        rs.getString("EMAIL"));
            }
            return null;
        } catch (SQLException e) {
            throw new DAOException("ProfesorDAO.findById: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Profesor> findAll() throws DAOException {
        List<Profesor> list = new ArrayList<>();
        String sql = "SELECT * FROM PROFESORES";
        try (Connection conn = DBmanager.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(new Profesor(rs.getInt("ID"),
                        rs.getString("FIRST_NAME"),
                        rs.getString("LAST_NAME"),
                        rs.getString("EMAIL")));
            }
        } catch (SQLException e) {
            throw new DAOException("ProfesorDAO.findAll: " + e.getMessage(), e);
        }
        return list;
    }
}
