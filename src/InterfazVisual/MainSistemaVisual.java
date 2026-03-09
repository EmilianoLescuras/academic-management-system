package InterfazVisual;

import DAO.AdministradorDAO;
import Sistema.Administrador;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class MainSistemaVisual extends JFrame {

    private String adminName = "User";

    public MainSistemaVisual() {
        setTitle("ACADEMIC MANAGEMENT SYSTEM");
        setSize(1100, 750);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Set system Look and Feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
        }

        // Window background
        getContentPane().setBackground(UITheme.BACKGROUND);

        // Role selection with modern design
        String[] roles = { "ADMINISTRATOR", "PROFESSOR" };

        JPanel rolePanel = new JPanel(new BorderLayout(20, 20));
        rolePanel.setBackground(UITheme.BACKGROUND);
        rolePanel.setBorder(new EmptyBorder(30, 50, 30, 50));

        JLabel lblTitle = new JLabel("ACADEMIC MANAGEMENT SYSTEM", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setForeground(UITheme.PRIMARY);

        JLabel lblSubtitle = new JLabel("Select your role to continue:", SwingConstants.CENTER);
        lblSubtitle.setFont(UITheme.FONT_LABEL);
        lblSubtitle.setForeground(UITheme.TEXT_SECONDARY);

        JPanel headerPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        headerPanel.setOpaque(false);
        headerPanel.add(lblTitle);
        headerPanel.add(lblSubtitle);
        rolePanel.add(headerPanel, BorderLayout.NORTH);

        String role = (String) JOptionPane.showInputDialog(
                null,
                rolePanel,
                "Login",
                JOptionPane.PLAIN_MESSAGE,
                null,
                roles,
                roles[0]);

        if (role == null) {
            System.exit(0);
        }

        // If Administrator, request credentials
        if ("ADMINISTRATOR".equals(role)) {
            if (!loginAdministrator()) {
                JOptionPane.showMessageDialog(null,
                        "Incorrect credentials or access denied.",
                        "Login Error", JOptionPane.ERROR_MESSAGE);
                System.exit(0);
            }
        }

        // Create JTabbedPane with modern style
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(UITheme.FONT_LABEL);
        tabbedPane.setBackground(UITheme.PANEL_BG);

        // Set tab colors
        UIManager.put("TabbedPane.selected", UITheme.PRIMARY);
        UIManager.put("TabbedPane.contentAreaColor", UITheme.PANEL_BG);
        UIManager.put("TabbedPane.focus", UITheme.PRIMARY);

        if ("ADMINISTRATOR".equals(role)) {
            tabbedPane.addTab(" STUDENTS", new AlumnoSistemaVisuall());
            tabbedPane.addTab(" TEACHERS", new ProfesorSistemaVisual());
            tabbedPane.addTab(" COURSES", new CursoSistemaVisual());
            tabbedPane.addTab(" ADMINISTRATORS", new AdministradorSistemaVisual());
            tabbedPane.addTab(" ENROLLMENTS", new CursoAlumnoSistemaVisual());
            tabbedPane.addTab(" GRADE MANAGEMENT", new GestionNotasSistemaVisual());
            tabbedPane.addTab(" CHARTS", new GraficoRecaudacionPanel());
        } else if ("PROFESSOR".equals(role)) {
            tabbedPane.addTab(" GRADE MANAGEMENT", new GestionNotasSistemaVisual());
        }

        // Main panel with border
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(UITheme.BACKGROUND);
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Header
        JPanel headerBar = new JPanel(new BorderLayout());
        headerBar.setBackground(UITheme.TABLE_HEADER_BG);
        headerBar.setBorder(new EmptyBorder(15, 20, 15, 20));

        JLabel lblApp = new JLabel("ACADEMIC MANAGEMENT SYSTEM");
        lblApp.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblApp.setForeground(Color.WHITE);

        JLabel lblRole = new JLabel("Role: " + role + " | " + adminName);
        lblRole.setFont(UITheme.FONT_LABEL);
        lblRole.setForeground(new Color(189, 195, 199));

        headerBar.add(lblApp, BorderLayout.WEST);
        headerBar.add(lblRole, BorderLayout.EAST);

        mainPanel.add(headerBar, BorderLayout.NORTH);
        mainPanel.add(tabbedPane, BorderLayout.CENTER);

        add(mainPanel);
        setVisible(true);
    }

    private boolean loginAdministrator() {
        JPanel loginPanel = new JPanel(new GridBagLayout());
        loginPanel.setBackground(UITheme.BACKGROUND);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblLogin = new JLabel("Administrator Login", SwingConstants.CENTER);
        lblLogin.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblLogin.setForeground(UITheme.PRIMARY);

        JTextField txtId = new JTextField(15);
        txtId.setFont(UITheme.FONT_LABEL);
        JPasswordField txtPassword = new JPasswordField(15);
        txtPassword.setFont(UITheme.FONT_LABEL);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        loginPanel.add(lblLogin, gbc);

        gbc.gridy = 1;
        gbc.gridwidth = 1;
        loginPanel.add(new JLabel("ID:"), gbc);
        gbc.gridx = 1;
        loginPanel.add(txtId, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        loginPanel.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1;
        loginPanel.add(txtPassword, gbc);

        int result = JOptionPane.showConfirmDialog(null, loginPanel,
                "Administrator Login", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result != JOptionPane.OK_OPTION) {
            return false;
        }

        try {
            int adminId = Integer.parseInt(txtId.getText().trim());
            String password = new String(txtPassword.getPassword());

            AdministradorDAO dao = new AdministradorDAO();
            Administrador admin = dao.authenticate(adminId, password);

            if (admin != null) {
                adminName = admin.getFirstName();
                return true;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "The ID must be numeric.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Connection error: " + e.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
        }

        return false;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainSistemaVisual::new);
    }
}
