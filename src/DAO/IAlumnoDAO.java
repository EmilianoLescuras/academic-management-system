package DAO;

import Sistema.Alumno;
import EXCEPCIONES.DAOException;

import java.util.List;

public interface IAlumnoDAO {
    int save(Alumno student) throws DAOException;

    void delete(int studentId) throws DAOException;

    void update(Alumno student) throws DAOException;

    Alumno findById(int studentId) throws DAOException;

    List<Alumno> findAll() throws DAOException;
}
