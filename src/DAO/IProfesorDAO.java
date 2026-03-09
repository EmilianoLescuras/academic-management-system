package DAO;

import Sistema.Profesor;
import EXCEPCIONES.DAOException;

import java.util.List;

public interface IProfesorDAO {
    int save(Profesor professor) throws DAOException;

    void delete(int id) throws DAOException;

    void update(Profesor professor) throws DAOException;

    Profesor findById(int id) throws DAOException;

    List<Profesor> findAll() throws DAOException;
}