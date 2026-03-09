package SERVICIO;

import DAO.AdministradorDAO;
import DAO.IAdministradorDAO;
import Sistema.Administrador;
import EXCEPCIONES.ServiceException;
import EXCEPCIONES.DAOException;
import java.util.List;

public class AdministradorService {
    private IAdministradorDAO adminDAO;

    public AdministradorService() {
        adminDAO = new AdministradorDAO();
    }

    public void save(Administrador admin) throws ServiceException {
        try {
            adminDAO.save(admin);
        } catch (DAOException e) {
            throw new ServiceException("AdministradorService.save: " + e.getMessage());
        }
    }

    public void delete(int adminId) throws ServiceException {
        try {
            adminDAO.delete(adminId);
        } catch (DAOException e) {
            throw new ServiceException("AdministradorService.delete: " + e.getMessage());
        }
    }

    public void update(Administrador admin) throws ServiceException {
        try {
            adminDAO.update(admin);
        } catch (DAOException e) {
            throw new ServiceException("AdministradorService.update: " + e.getMessage());
        }
    }

    public Administrador findById(int adminId) throws ServiceException {
        try {
            return adminDAO.findById(adminId);
        } catch (DAOException e) {
            throw new ServiceException("AdministradorService.findById: " + e.getMessage());
        }
    }

    public List<Administrador> findAll() throws ServiceException {
        try {
            return adminDAO.findAll();
        } catch (DAOException e) {
            throw new ServiceException("AdministradorService.findAll: " + e.getMessage());
        }
    }
}
