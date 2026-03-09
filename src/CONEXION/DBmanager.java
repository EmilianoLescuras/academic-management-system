
package CONEXION;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DBmanager {
    // Database credentials - can be overridden with environment variables
    private static final String URL = System.getenv("DB_URL") != null
            ? System.getenv("DB_URL")
            : "jdbc:mysql://localhost:3306/alumnosdb";
    private static final String USER = System.getenv("DB_USER") != null
            ? System.getenv("DB_USER")
            : "root";
    private static final String PASSWORD = System.getenv("DB_PASSWORD") != null
            ? System.getenv("DB_PASSWORD")
            : "root";

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            initializeTables();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    private static void initializeTables() {
        try (Connection conn = getConnection(); Statement stmt = conn.createStatement()) {
            // STUDENTS table
            stmt.execute("CREATE TABLE IF NOT EXISTS ALUMNOS (" +
                    "LEGAJO INT PRIMARY KEY AUTO_INCREMENT, " +
                    "FIRST_NAME VARCHAR(50), " +
                    "LAST_NAME VARCHAR(50), " +
                    "EMAIL VARCHAR(100))");

            // PROFESSORS table
            stmt.execute("CREATE TABLE IF NOT EXISTS PROFESORES (" +
                    "ID INT PRIMARY KEY AUTO_INCREMENT, " +
                    "FIRST_NAME VARCHAR(50), " +
                    "LAST_NAME VARCHAR(50), " +
                    "EMAIL VARCHAR(100))");

            // COURSES table
            stmt.execute("CREATE TABLE IF NOT EXISTS CURSOS (" +
                    "ID INT PRIMARY KEY AUTO_INCREMENT, " +
                    "NAME VARCHAR(100), " +
                    "PRICE DOUBLE, " +
                    "CAPACITY INT, " +
                    "PASSING_GRADE DOUBLE, " +
                    "NUM_PARTIAL_GRADES INT, " +
                    "PROMO_START_DATE DATE, " +
                    "PROMO_END_DATE DATE, " +
                    "PROMO_PRICE DOUBLE)");

            // COURSE_STUDENT table
            stmt.execute("CREATE TABLE IF NOT EXISTS CURSO_ALUMNO (" +
                    "ID_CURSO INT, " +
                    "ID_ALUMNO INT, " +
                    "PARTIAL_GRADE DOUBLE, " +
                    "FINAL_GRADE INT, " +
                    "PRIMARY KEY (ID_CURSO, ID_ALUMNO))");

            // ADMINISTRATORS table
            stmt.execute("CREATE TABLE IF NOT EXISTS ADMINISTRADORES (" +
                    "LEGAJO INT PRIMARY KEY AUTO_INCREMENT, " +
                    "NAME VARCHAR(50), " +
                    "PHONE VARCHAR(50), " +
                    "PASSWORD VARCHAR(100), " +
                    "EMAIL VARCHAR(100))");

            // PARTIAL GRADES table
            stmt.execute("CREATE TABLE IF NOT EXISTS NOTAS_PARCIALES (" +
                    "ID INT PRIMARY KEY AUTO_INCREMENT, " +
                    "ID_CURSO INT, " +
                    "ID_ALUMNO INT, " +
                    "GRADE DOUBLE)");

            // FINAL GRADES table
            stmt.execute("CREATE TABLE IF NOT EXISTS NOTAS_FINALES (" +
                    "ID INT PRIMARY KEY AUTO_INCREMENT, " +
                    "ID_CURSO INT, " +
                    "ID_ALUMNO INT, " +
                    "GRADE INT)");

            // RETAKES table
            stmt.execute("CREATE TABLE IF NOT EXISTS RECUPERATORIOS (" +
                    "ID INT PRIMARY KEY AUTO_INCREMENT, " +
                    "ID_CURSO INT, " +
                    "ID_ALUMNO INT, " +
                    "PARTIAL_NUMBER INT, " +
                    "GRADE DOUBLE)");

            // Add STATUS column to CURSO_ALUMNO if it does not exist
            try {
                stmt.execute("ALTER TABLE CURSO_ALUMNO ADD COLUMN STATUS VARCHAR(20) DEFAULT 'ACTIVE'");
            } catch (SQLException e) {
                // Column already exists, ignore
            }

            System.out.println("Tables verified/created successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
