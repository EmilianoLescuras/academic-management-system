package DAO;

import EXCEPCIONES.DAOException;
import Sistema.Administrador;
import CONEXION.DBmanager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AdministradorDAO implements IAdministradorDAO {

    @Override
    public void save(Administrador admin) throws DAOException {
        String sql = "INSERT INTO ADMINISTRADORES (LEGAJO, NAME, PHONE, PASSWORD, EMAIL) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBmanager.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, admin.getAdminId());
            ps.setString(2, admin.getFirstName());
            ps.setString(3, admin.getPhone());
            ps.setString(4, admin.getPassword());
            ps.setString(5, admin.getEmail());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("AdministradorDAO.save: " + e.getMessage(), e);
        }
    }

    @Override
    public void delete(int adminId) throws DAOException {
        String sql = "DELETE FROM ADMINISTRADORES WHERE LEGAJO = ?";
        try (Connection conn = DBmanager.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, adminId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("AdministradorDAO.delete: " + e.getMessage(), e);
        }
    }

    @Override
    public void update(Administrador admin) throws DAOException {
        String sql = "UPDATE ADMINISTRADORES SET NAME = ?, PHONE = ?, PASSWORD = ?, EMAIL = ? WHERE LEGAJO = ?";
        try (Connection conn = DBmanager.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, admin.getFirstName());
            ps.setString(2, admin.getPhone());
            ps.setString(3, admin.getPassword());
            ps.setString(4, admin.getEmail());
            ps.setInt(5, admin.getAdminId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("AdministradorDAO.update: " + e.getMessage(), e);
        }
    }

    @Override
    public Administrador findById(int adminId) throws DAOException {
        String sql = "SELECT * FROM ADMINISTRADORES WHERE LEGAJO = ?";
        try (Connection conn = DBmanager.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, adminId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Administrador(rs.getInt("LEGAJO"),
                        rs.getString("NAME"),
                        rs.getString("PHONE"),
                        rs.getString("PASSWORD"),
                        rs.getString("EMAIL"));
            }
            return null;
        } catch (SQLException e) {
            throw new DAOException("AdministradorDAO.findById: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Administrador> findAll() throws DAOException {
        List<Administrador> list = new ArrayList<>();
        String sql = "SELECT * FROM ADMINISTRADORES";
        try (Connection conn = DBmanager.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new Administrador(rs.getInt("LEGAJO"),
                        rs.getString("NAME"),
                        rs.getString("PHONE"),
                        rs.getString("PASSWORD"),
                        rs.getString("EMAIL")));
            }
        } catch (SQLException e) {
            throw new DAOException("AdministradorDAO.findAll: " + e.getMessage(), e);
        }
        return list;
    }

    // Authenticate administrator by ID and password
    public Administrador authenticate(int adminId, String password) throws DAOException {
        String sql = "SELECT * FROM ADMINISTRADORES WHERE LEGAJO = ? AND PASSWORD = ?";
        try (Connection conn = DBmanager.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, adminId);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Administrador(rs.getInt("LEGAJO"),
                        rs.getString("NAME"),
                        rs.getString("PHONE"),
                        rs.getString("PASSWORD"),
                        rs.getString("EMAIL"));
            }
            return null;
        } catch (SQLException e) {
            throw new DAOException("AdministradorDAO.authenticate: " + e.getMessage(), e);
        }
    }
}
