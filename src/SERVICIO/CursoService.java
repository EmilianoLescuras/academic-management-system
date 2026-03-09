package SERVICIO;

import DAO.CursoDAO;
import DAO.ICursoDAO;
import EXCEPCIONES.DAOException;
import EXCEPCIONES.ServiceException;
import Sistema.Curso;

import java.util.List;

public class CursoService {
    private ICursoDAO cursoDAO;

    public CursoService() {
        this.cursoDAO = new CursoDAO();
    }

    public void save(Curso course) throws ServiceException {
        try {
            cursoDAO.save(course);
        } catch (DAOException e) {
            e.printStackTrace();
            throw new ServiceException("CursoService.save: " + e.getMessage());
        }
    }

    public void delete(int id) throws ServiceException {
        try {
            cursoDAO.delete(id);
        } catch (DAOException e) {
            e.printStackTrace();
            throw new ServiceException("CursoService.delete: " + e.getMessage());
        }
    }

    public void update(Curso course) throws ServiceException {
        try {
            cursoDAO.update(course);
        } catch (DAOException e) {
            e.printStackTrace();
            throw new ServiceException("CursoService.update: " + e.getMessage());
        }
    }

    public Curso findById(int id) throws ServiceException {
        try {
            return cursoDAO.findById(id);
        } catch (DAOException e) {
            e.printStackTrace();
            throw new ServiceException("CursoService.findById: " + e.getMessage());
        }
    }

    public List<Curso> findAll() throws ServiceException {
        try {
            return cursoDAO.findAll();
        } catch (DAOException e) {
            e.printStackTrace();
            throw new ServiceException("CursoService.findAll: " + e.getMessage());
        }
    }

    public void enrollStudent(int courseId, int studentId) throws ServiceException {
        try {
            cursoDAO.enrollStudent(courseId, studentId);
        } catch (DAOException e) {
            e.printStackTrace();
            throw new ServiceException("CursoService.enrollStudent: " + e.getMessage());
        }
    }

    public void savePartialGrade(int courseId, int studentId, double grade) throws ServiceException {
        try {
            cursoDAO.savePartialGrade(courseId, studentId, grade);
        } catch (DAOException e) {
            e.printStackTrace();
            throw new ServiceException("CursoService.savePartialGrade: " + e.getMessage());
        }
    }

    public void saveFinalGrade(int courseId, int studentId, int finalGrade) throws ServiceException {
        try {
            cursoDAO.saveFinalGrade(courseId, studentId, finalGrade);
        } catch (DAOException e) {
            e.printStackTrace();
            throw new ServiceException("CursoService.saveFinalGrade: " + e.getMessage());
        }
    }
}
