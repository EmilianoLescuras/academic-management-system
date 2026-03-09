package Sistema;

public class Administrador extends Persona { // Child class, inherits attributes and methods from Persona
    private int adminId;
    private String phone;
    private String password;

    // Super constructor and constructor for this class
    public Administrador(int adminId, String firstName, String phone, String password, String email) {
        super(firstName, "", email);
        this.adminId = adminId;
        this.phone = phone;
        this.password = password;
    }

    // Getters and setters
    public int getAdminId() {
        return adminId;
    }

    public void setAdminId(int adminId) {
        this.adminId = adminId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // Method overrides
    @Override
    public String toString() {
        return "Admin: " + super.getFirstName() + " (ID: " + adminId + ")";
    }
}
