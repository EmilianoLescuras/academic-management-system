package DAO;

import Sistema.Administrador;
import EXCEPCIONES.DAOException;
import java.util.List;

public interface IAdministradorDAO {
    void save(Administrador admin) throws DAOException;

    void delete(int adminId) throws DAOException;

    void update(Administrador admin) throws DAOException;

    Administrador findById(int adminId) throws DAOException;

    List<Administrador> findAll() throws DAOException;
}
