package DAO;

import EXCEPCIONES.DAOException;
import Sistema.Curso;

import java.util.List;

public interface ICursoDAO {
    int save(Curso course) throws DAOException;

    void delete(int id) throws DAOException;

    void update(Curso course) throws DAOException;

    Curso findById(int id) throws DAOException;

    List<Curso> findAll() throws DAOException;

    void enrollStudent(int courseId, int studentId) throws DAOException;

    void savePartialGrade(int courseId, int studentId, double grade) throws DAOException;

    void saveFinalGrade(int courseId, int studentId, int grade) throws DAOException;

    List<Integer> getEnrolledStudents(int courseId) throws DAOException;

    List<Double> getPartialGrades(int courseId, int studentId) throws DAOException;

    Integer getFinalGrade(int courseId, int studentId) throws DAOException;
}
