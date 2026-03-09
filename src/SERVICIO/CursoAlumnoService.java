
package SERVICIO;

import DAO.CursoAlumnoDAO;
import EXCEPCIONES.DAOException;
import EXCEPCIONES.ServiceException;
import Sistema.Alumno;
import Sistema.CursoAlumno;

import java.util.List;

public class CursoAlumnoService {

    private CursoAlumnoDAO dao;

    public CursoAlumnoService() {
        this.dao = new CursoAlumnoDAO();
    }

    // Enroll a student in a course
    public void enrollStudent(CursoAlumno enrollment, double coursePassingGrade) throws ServiceException {
        try {
            List<CursoAlumno> enrollments = dao.getEnrollmentsByStudent(enrollment.getStudentId());

            // Count active courses (not yet approved)
            int activeCourses = 0;
            boolean hasApprovedCourse = false;

            for (CursoAlumno e : enrollments) {
                if (e.getFinalGrade() == null || e.getFinalGrade() < coursePassingGrade) {
                    activeCourses++;
                } else {
                    hasApprovedCourse = true;
                }
            }

            if (activeCourses >= 3 && !hasApprovedCourse) {
                throw new ServiceException("The student already has 3 active courses and has not approved any.");
            }

            dao.enrollStudent(enrollment);
        } catch (DAOException e) {
            throw new ServiceException("Error enrolling student", e);
        }
    }

    public void unenrollStudent(int courseId, int studentId) throws ServiceException {
        try {
            dao.unenrollStudent(courseId, studentId);
        } catch (DAOException e) {
            throw new ServiceException("Error unenrolling student", e);
        }
    }

    // Register a partial grade
    public void registerPartialGrade(int courseId, int studentId, double grade) throws ServiceException {
        try {
            dao.savePartialGrade(courseId, studentId, grade);
        } catch (DAOException e) {
            throw new ServiceException("Error saving partial grade", e);
        }
    }

    // Register a final grade (max 3 attempts)
    public void registerFinalGrade(int courseId, int studentId, int finalGrade, double passingGrade)
            throws ServiceException {
        try {
            // Check number of attempts
            int attempts = dao.countFinalAttempts(courseId, studentId);
            if (attempts >= 3) {
                throw new ServiceException("The student has already used 3 final attempts. Must retake the subject.");
            }

            // Save the grade
            dao.saveFinalGrade(courseId, studentId, finalGrade);

            // If failed and it was the third attempt, mark as FAILED
            if (finalGrade < passingGrade && attempts + 1 >= 3) {
                dao.markAsFailed(courseId, studentId);
                throw new ServiceException("The student failed the third final attempt. " +
                        "The course is now FAILED. Must retake the subject.");
            }
        } catch (DAOException e) {
            throw new ServiceException("Error saving the final grade", e);
        }
    }

    // Get number of final attempts
    public int getFinalAttempts(int courseId, int studentId) throws ServiceException {
        try {
            return dao.countFinalAttempts(courseId, studentId);
        } catch (DAOException e) {
            throw new ServiceException("Error counting final attempts", e);
        }
    }

    // Get final grade history
    public List<Integer> getAllFinalGrades(int courseId, int studentId) throws ServiceException {
        try {
            return dao.getAllFinalGrades(courseId, studentId);
        } catch (DAOException e) {
            throw new ServiceException("Error getting final grade history", e);
        }
    }

    // Get all enrollments
    public List<CursoAlumno> getEnrollments() throws ServiceException {
        try {
            return dao.findAll();
        } catch (DAOException e) {
            throw new ServiceException("Error getting enrollments", e);
        }
    }

    // Get a revenue report
    public List<String> revenueReport() throws ServiceException {
        try {
            return dao.revenueReport();
        } catch (DAOException e) {
            throw new ServiceException("Error in revenue report", e);
        }
    }

    // Approved count
    public List<String> approvedReport() throws ServiceException {
        try {
            return dao.approvedReport();
        } catch (DAOException e) {
            throw new ServiceException("Error in approved report", e);
        }
    }

    // Get partial grades
    public List<Double> getPartialGrades(int courseId, int studentId) throws ServiceException {
        try {
            return dao.getPartialGrades(courseId, studentId);
        } catch (DAOException e) {
            throw new ServiceException("Error getting partial grades", e);
        }
    }

    // Get final grade
    public Integer getFinalGrade(int courseId, int studentId) throws ServiceException {
        try {
            return dao.getFinalGrade(courseId, studentId);
        } catch (DAOException e) {
            throw new ServiceException("Error getting final grade", e);
        }
    }

    // ==================== RETAKES ====================

    /**
     * Register a retake grade.
     * If the student passes, it conceptually replaces the failed grade.
     * If the student fails, the course is marked as FAILED and they cannot take the
     * final.
     */
    public void registerRetake(int courseId, int studentId, int partialNumber, double grade,
            double passingGrade) throws ServiceException {
        try {
            // Check if the student already took the retake for this partial
            if (dao.hasRetake(courseId, studentId, partialNumber)) {
                throw new ServiceException("The student has already taken the retake for this partial.");
            }

            // Save the retake grade
            dao.saveRetake(courseId, studentId, partialNumber, grade);

            // If the student fails the retake, mark course as FAILED
            if (grade < passingGrade) {
                dao.markAsFailed(courseId, studentId);
                throw new ServiceException("The student failed the retake. The course is now FAILED. " +
                        "The only way to pass is to retake the subject.");
            }
        } catch (DAOException e) {
            throw new ServiceException("Error registering retake", e);
        }
    }

    /**
     * Get retake grades for a student in a course.
     */
    public List<Double> getRetakes(int courseId, int studentId) throws ServiceException {
        try {
            return dao.getRetakes(courseId, studentId);
        } catch (DAOException e) {
            throw new ServiceException("Error getting retakes", e);
        }
    }

    /**
     * Check if a student can take the final exam.
     * Returns null if they can, or an error message if they cannot.
     */
    public String canTakeFinal(int courseId, int studentId, double passingGrade, int numPartials)
            throws ServiceException {
        try {
            // Check if the student is failed
            String status = dao.getStatus(courseId, studentId);
            if ("FAILED".equals(status)) {
                return "The student has FAILED the course. Must retake the subject.";
            }

            // Get partial grades and retakes
            List<Double> partials = dao.getPartialGrades(courseId, studentId);

            if (partials.size() < numPartials) {
                return "The student does not have all partials loaded. Required: " + numPartials + ", Loaded: "
                        + partials.size();
            }

            // Check each partial
            for (int i = 0; i < partials.size(); i++) {
                double grade = partials.get(i);
                if (grade < passingGrade) {
                    // Check if the student has an approved retake for this partial
                    boolean hasRetake = dao.hasRetake(courseId, studentId, i + 1);
                    if (!hasRetake) {
                        return "The student failed partial " + (i + 1) + " and has not yet taken the retake.";
                    }
                }
            }

            return null; // Can take the final
        } catch (DAOException e) {
            throw new ServiceException("Error checking if the student can take the final", e);
        }
    }

    /**
     * Get the student's status in a course.
     */
    public String getStatus(int courseId, int studentId) throws ServiceException {
        try {
            return dao.getStatus(courseId, studentId);
        } catch (DAOException e) {
            throw new ServiceException("Error getting status", e);
        }
    }

    /**
     * Retake subject - deletes all grades and resets enrollment.
     */
    public void retakeSubject(int courseId, int studentId) throws ServiceException {
        try {
            dao.retakeSubject(courseId, studentId);
        } catch (DAOException e) {
            throw new ServiceException("Error when retaking the subject", e);
        }
    }
}
