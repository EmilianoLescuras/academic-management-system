package DAO;

import EXCEPCIONES.DAOException;
import Sistema.CursoAlumno;

import java.util.List;

public interface ICursoAlumnoDAO {
    void enrollStudent(CursoAlumno enrollment) throws DAOException;

    void savePartialGrade(int courseId, int studentId, double grade) throws DAOException;

    void saveFinalGrade(int courseId, int studentId, int grade) throws DAOException;

    List<CursoAlumno> findAll() throws DAOException;

    List<String> revenueReport() throws DAOException;

    List<String> approvedReport() throws DAOException;
}
