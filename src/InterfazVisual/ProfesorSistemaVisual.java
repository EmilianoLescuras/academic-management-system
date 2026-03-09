
package InterfazVisual;

import SERVICIO.ProfesorService;
import EXCEPCIONES.ServiceException;
import Sistema.Profesor;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class ProfesorSistemaVisual extends JPanel {
    private JTextField txtFirstName, txtLastName, txtEmail;
    private JTable table;
    private DefaultTableModel tableModel;
    private ProfesorService profesorService;

    public ProfesorSistemaVisual() {
        setLayout(new BorderLayout(10, 10));
        UITheme.styleMainPanel(this);
        profesorService = new ProfesorService();

        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        UITheme.stylePanel(formPanel);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtFirstName = UITheme.createTextField();
        txtLastName = UITheme.createTextField();
        txtEmail = UITheme.createTextField();

        // Row 1: Name
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0;
        formPanel.add(UITheme.createLabel("NAME:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1;
        formPanel.add(txtFirstName, gbc);

        // Row 2: Last Name
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0;
        formPanel.add(UITheme.createLabel("LAST NAME:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1;
        formPanel.add(txtLastName, gbc);

        // Row 3: Email
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0;
        formPanel.add(UITheme.createLabel("Email:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1;
        formPanel.add(txtEmail, gbc);

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
        tableModel = new DefaultTableModel(new Object[] { "ID", "NAME", "LAST NAME", "EMAIL" }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);
        UITheme.styleTable(table);

        // Row selection to load data into form
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                if (row >= 0) {
                    txtFirstName.setText(tableModel.getValueAt(row, 1).toString());
                    txtLastName.setText(tableModel.getValueAt(row, 2).toString());
                    txtEmail.setText(tableModel.getValueAt(row, 3).toString());
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        // Events
        btnSave.addActionListener(this::saveProfessor);
        btnUpdate.addActionListener(this::updateProfessor);
        btnDelete.addActionListener(this::deleteProfessor);
        btnRefresh.addActionListener(e -> loadTable());

        loadTable();
    }

    private void saveProfessor(ActionEvent e) {
        try {
            if (txtFirstName.getText().isEmpty() || txtLastName.getText().isEmpty() || txtEmail.getText().isEmpty()) {
                showError("All fields are required.");
                return;
            }
            Profesor p = new Profesor(0, txtFirstName.getText(), txtLastName.getText(), txtEmail.getText());
            profesorService.save(p);
            loadTable();
            clearFields();
            JOptionPane.showMessageDialog(this, "Teacher saved successfully with ID: " + p.getId(), "Success",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (ServiceException ex) {
            showError(ex.getMessage());
        }
    }

    private void updateProfessor(ActionEvent e) {
        try {
            int row = table.getSelectedRow();
            if (row < 0) {
                showError("Select a teacher from the table to modify.");
                return;
            }
            int id = (int) tableModel.getValueAt(row, 0);
            Profesor p = new Profesor(id, txtFirstName.getText(), txtLastName.getText(), txtEmail.getText());
            profesorService.update(p);
            loadTable();
            clearFields();
            JOptionPane.showMessageDialog(this, "Teacher modified successfully.", "Success",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (ServiceException ex) {
            showError(ex.getMessage());
        }
    }

    private void deleteProfessor(ActionEvent e) {
        try {
            int row = table.getSelectedRow();
            if (row < 0) {
                showError("Select a teacher from the table to delete.");
                return;
            }
            int id = (int) tableModel.getValueAt(row, 0);
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to delete the teacher with ID " + id + "?",
                    "Confirm deletion", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                profesorService.delete(id);
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
            List<Profesor> professors = profesorService.findAll();
            for (Profesor p : professors) {
                tableModel.addRow(new Object[] { p.getId(), p.getFirstName(), p.getLastName(), p.getEmail() });
            }
            if (table.getColumnCount() > 0) {
                UITheme.styleTable(table);
            }
        } catch (ServiceException ex) {
            showError(ex.getMessage());
        }
    }

    private void clearFields() {
        txtFirstName.setText("");
        txtLastName.setText("");
        txtEmail.setText("");
        table.clearSelection();
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
