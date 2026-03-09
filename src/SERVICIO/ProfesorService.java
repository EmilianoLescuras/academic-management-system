package SERVICIO;

import DAO.IProfesorDAO;
import DAO.ProfesorDAO;
import Sistema.Profesor;
import EXCEPCIONES.DAOException;
import EXCEPCIONES.ServiceException;

import java.util.List;

public class ProfesorService {
    private IProfesorDAO profesorDAO;

    public ProfesorService() {
        profesorDAO = new ProfesorDAO();
    }

    public void save(Profesor professor) throws ServiceException {
        try {
            profesorDAO.save(professor);
        } catch (DAOException e) {
            e.printStackTrace();
            throw new ServiceException("ProfesorService.save: " + e.getMessage());
        }
    }

    public void delete(int id) throws ServiceException {
        try {
            profesorDAO.delete(id);
        } catch (DAOException e) {
            e.printStackTrace();
            throw new ServiceException("ProfesorService.delete: " + e.getMessage());
        }
    }

    public void update(Profesor professor) throws ServiceException {
        try {
            profesorDAO.update(professor);
        } catch (DAOException e) {
            e.printStackTrace();
            throw new ServiceException("ProfesorService.update: " + e.getMessage());
        }
    }

    public Profesor findById(int id) throws ServiceException {
        try {
            return profesorDAO.findById(id);
        } catch (DAOException e) {
            e.printStackTrace();
            throw new ServiceException("ProfesorService.findById: " + e.getMessage());
        }
    }

    public List<Profesor> findAll() throws ServiceException {
        try {
            return profesorDAO.findAll();
        } catch (DAOException e) {
            e.printStackTrace();
            throw new ServiceException("ProfesorService.findAll: " + e.getMessage());
        }
    }
}
