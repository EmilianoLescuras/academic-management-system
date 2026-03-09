package Sistema;

public class Alumno extends Persona { // Child class, inherits attributes and methods from Persona
    private int studentId;

    public Alumno(int studentId, String firstName, String lastName, String email) {
        super(firstName, lastName, email);
        this.studentId = studentId;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    @Override
    public String toString() {
        return studentId + " - " + getFirstName() + " " + getLastName(); // Show only ID + Name + LastName for clarity
    }
}
