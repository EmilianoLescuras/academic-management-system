
package InterfazVisual;

import SERVICIO.AdministradorService;
import EXCEPCIONES.ServiceException;
import Sistema.Administrador;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class AdministradorSistemaVisual extends JPanel {
    private JTextField txtAdminId, txtName, txtPhone, txtEmail;
    private JPasswordField txtPassword;
    private JTable table;
    private DefaultTableModel tableModel;
    private AdministradorService adminService;

    public AdministradorSistemaVisual() {
        setLayout(new BorderLayout(10, 10));
        UITheme.styleMainPanel(this);
        adminService = new AdministradorService();

        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        UITheme.stylePanel(formPanel);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtAdminId = UITheme.createTextField();
        txtName = UITheme.createTextField();
        txtPhone = UITheme.createTextField();
        txtEmail = UITheme.createTextField();
        txtPassword = new JPasswordField();
        txtPassword.setFont(UITheme.FONT_LABEL);

        int row = 0;

        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0;
        formPanel.add(UITheme.createLabel("Admin ID:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1;
        formPanel.add(txtAdminId, gbc);

        row++;
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0;
        formPanel.add(UITheme.createLabel("Name:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1;
        formPanel.add(txtName, gbc);

        row++;
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0;
        formPanel.add(UITheme.createLabel("Phone:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1;
        formPanel.add(txtPhone, gbc);

        row++;
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0;
        formPanel.add(UITheme.createLabel("Email:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1;
        formPanel.add(txtEmail, gbc);

        row++;
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0;
        formPanel.add(UITheme.createLabel("Password:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1;
        formPanel.add(txtPassword, gbc);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        buttonPanel.setOpaque(false);

        JButton btnSave = UITheme.createSecondaryButton("SAVE");
        JButton btnUpdate = UITheme.createPrimaryButton("UPDATE");
        JButton btnDelete = UITheme.createDangerButton("DELETE");
        JButton btnRefresh = UITheme.createPrimaryButton("REFRESH");

        buttonPanel.add(btnSave);
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnRefresh);

        JPanel topPanel = new JPanel(new BorderLayout());
        UITheme.stylePanel(topPanel);
        topPanel.add(formPanel, BorderLayout.CENTER);
        topPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Table
        tableModel = new DefaultTableModel(new Object[] { "Admin ID", "Name", "Phone", "Email", "Password" }, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        table = new JTable(tableModel);
        UITheme.styleTable(table);

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int r = table.getSelectedRow();
                if (r >= 0) {
                    txtAdminId.setText(tableModel.getValueAt(r, 0).toString());
                    txtName.setText(tableModel.getValueAt(r, 1).toString());
                    txtPhone.setText(tableModel.getValueAt(r, 2).toString());
                    txtEmail.setText(tableModel.getValueAt(r, 3).toString());
                    txtPassword.setText(tableModel.getValueAt(r, 4).toString());
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        // Events
        btnSave.addActionListener(this::saveAdmin);
        btnUpdate.addActionListener(this::updateAdmin);
        btnDelete.addActionListener(this::deleteAdmin);
        btnRefresh.addActionListener(e -> loadTable());

        loadTable();
    }

    private void saveAdmin(ActionEvent e) {
        try {
            int adminId = Integer.parseInt(txtAdminId.getText().trim());
            Administrador admin = new Administrador(adminId, txtName.getText(), txtPhone.getText(),
                    new String(txtPassword.getPassword()), txtEmail.getText());
            adminService.save(admin);
            loadTable();
            clearFields();
            JOptionPane.showMessageDialog(this, "Administrator saved successfully.", "Success",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (NumberFormatException ex) {
            showError("Admin ID must be a number.");
        } catch (ServiceException ex) {
            showError(ex.getMessage());
        }
    }

    private void updateAdmin(ActionEvent e) {
        try {
            int r = table.getSelectedRow();
            if (r < 0) {
                showError("Select an administrator from the table to modify.");
                return;
            }
            int adminId = (int) tableModel.getValueAt(r, 0);
            Administrador admin = new Administrador(adminId, txtName.getText(), txtPhone.getText(),
                    new String(txtPassword.getPassword()), txtEmail.getText());
            adminService.update(admin);
            loadTable();
            clearFields();
            JOptionPane.showMessageDialog(this, "Administrator modified successfully.", "Success",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (ServiceException ex) {
            showError(ex.getMessage());
        }
    }

    private void deleteAdmin(ActionEvent e) {
        try {
            int r = table.getSelectedRow();
            if (r < 0) {
                showError("Select an administrator from the table to delete.");
                return;
            }
            int adminId = (int) tableModel.getValueAt(r, 0);
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to delete administrator ID " + adminId + "?",
                    "Confirm Deletion", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                adminService.delete(adminId);
                loadTable();
                clearFields();
            }
        } catch (Exception ex) {
            showError(ex.getMessage());
        }
    }

    private void loadTable() {
        try {
            tableModel.setRowCount(0);
            List<Administrador> admins = adminService.findAll();
            for (Administrador a : admins) {
                tableModel.addRow(new Object[] { a.getAdminId(), a.getFirstName(), a.getPhone(), a.getEmail(),
                        a.getPassword() });
            }
            if (table.getColumnCount() > 0) {
                UITheme.styleTable(table);
            }
        } catch (ServiceException ex) {
            showError(ex.getMessage());
        }
    }

    private void clearFields() {
        txtAdminId.setText("");
        txtName.setText("");
        txtPhone.setText("");
        txtEmail.setText("");
        txtPassword.setText("");
        table.clearSelection();
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
