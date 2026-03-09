package SERVICIO;

import DAO.AlumnoDAO;
import DAO.IAlumnoDAO;
import Sistema.Alumno;
import EXCEPCIONES.ServiceException;
import EXCEPCIONES.DAOException;

import java.util.List;

public class AlumnoService {
    private IAlumnoDAO alumnoDAO;

    public AlumnoService() {
        alumnoDAO = new AlumnoDAO();
    }

    public void save(Alumno student) throws ServiceException {
        try {
            alumnoDAO.save(student);
        } catch (DAOException e) {
            e.printStackTrace();
            throw new ServiceException("AlumnoService.save: " + e.getMessage());
        }
    }

    public void delete(int id) throws ServiceException {
        try {
            alumnoDAO.delete(id);
        } catch (DAOException e) {
            e.printStackTrace();
            throw new ServiceException("AlumnoService.delete: " + e.getMessage());
        }
    }

    public void update(Alumno student) throws ServiceException {
        try {
            alumnoDAO.update(student);
        } catch (DAOException e) {
            e.printStackTrace();
            throw new ServiceException("AlumnoService.update: " + e.getMessage());
        }
    }

    public Alumno findById(int id) throws ServiceException {
        try {
            return alumnoDAO.findById(id);
        } catch (DAOException e) {
            e.printStackTrace();
            throw new ServiceException("AlumnoService.findById: " + e.getMessage());
        }
    }

    public List<Alumno> findAll() throws ServiceException {
        try {
            return alumnoDAO.findAll();
        } catch (DAOException e) {
            e.printStackTrace();
            throw new ServiceException("AlumnoService.findAll: " + e.getMessage());
        }
    }
}
