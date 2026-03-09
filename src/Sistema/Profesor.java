package Sistema;

public class Profesor extends Persona { // Child class, inherits attributes and methods from Persona
    private int id;

    public Profesor(int id, String firstName, String lastName, String email) {
        super(firstName, lastName, email);
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Professor: " + super.toString() + " (ID: " + id + ")";
    }
}
