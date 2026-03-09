
package InterfazVisual;

import SERVICIO.CursoService;
import EXCEPCIONES.ServiceException;
import Sistema.Curso;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.util.List;

public class CursoSistemaVisual extends JPanel {
    private JTextField txtName, txtPrice, txtCapacity, txtPassingGrade, txtNumPartials;
    private JTextField txtPromoStart, txtPromoEnd, txtPromoPrice;
    private JTable table;
    private DefaultTableModel tableModel;
    private CursoService cursoService;

    public CursoSistemaVisual() {
        setLayout(new BorderLayout(10, 10));
        UITheme.styleMainPanel(this);
        cursoService = new CursoService();

        // Main form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        UITheme.stylePanel(formPanel);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtName = UITheme.createTextField();
        txtPrice = UITheme.createTextField();
        txtCapacity = UITheme.createTextField();
        txtPassingGrade = UITheme.createTextField();
        txtNumPartials = UITheme.createTextField();
        txtPromoStart = UITheme.createTextField();
        txtPromoEnd = UITheme.createTextField();
        txtPromoPrice = UITheme.createTextField();

        int row = 0;

        // Row: Name
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0;
        formPanel.add(UITheme.createLabel("Name:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1;
        formPanel.add(txtName, gbc);

        // Row: Price
        gbc.gridx = 2;
        gbc.weightx = 0;
        formPanel.add(UITheme.createLabel("Price:"), gbc);
        gbc.gridx = 3;
        gbc.weightx = 1;
        formPanel.add(txtPrice, gbc);

        row++;
        // Row: Capacity
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0;
        formPanel.add(UITheme.createLabel("Capacity:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1;
        formPanel.add(txtCapacity, gbc);

        // Row: Passing Grade
        gbc.gridx = 2;
        gbc.weightx = 0;
        formPanel.add(UITheme.createLabel("Passing Grade:"), gbc);
        gbc.gridx = 3;
        gbc.weightx = 1;
        formPanel.add(txtPassingGrade, gbc);

        row++;
        // Row: Num Partials
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0;
        formPanel.add(UITheme.createLabel("Partial Exams:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1;
        formPanel.add(txtNumPartials, gbc);

        // Row: Promo Price
        gbc.gridx = 2;
        gbc.weightx = 0;
        formPanel.add(UITheme.createLabel("Promo Price:"), gbc);
        gbc.gridx = 3;
        gbc.weightx = 1;
        formPanel.add(txtPromoPrice, gbc);

        row++;
        // Row: Promo Dates
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0;
        formPanel.add(UITheme.createLabel("Promo Start (YYYY-MM-DD):"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1;
        formPanel.add(txtPromoStart, gbc);

        gbc.gridx = 2;
        gbc.weightx = 0;
        formPanel.add(UITheme.createLabel("Promo End (YYYY-MM-DD):"), gbc);
        gbc.gridx = 3;
        gbc.weightx = 1;
        formPanel.add(txtPromoEnd, gbc);

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
        tableModel = new DefaultTableModel(
                new Object[] { "ID", "Name", "Price", "Capacity", "Passing Grade", "Partials",
                        "Promo Start", "Promo End", "Promo Price" },
                0) {
            @Override
            public boolean isCellEditable(int row2, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);
        UITheme.styleTable(table);

        // Row selection to load data into form
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int r = table.getSelectedRow();
                if (r >= 0) {
                    txtName.setText(tableModel.getValueAt(r, 1).toString());
                    txtPrice.setText(tableModel.getValueAt(r, 2).toString());
                    txtCapacity.setText(tableModel.getValueAt(r, 3).toString());
                    txtPassingGrade.setText(tableModel.getValueAt(r, 4).toString());
                    txtNumPartials.setText(tableModel.getValueAt(r, 5).toString());
                    txtPromoStart
                            .setText(tableModel.getValueAt(r, 6) != null ? tableModel.getValueAt(r, 6).toString() : "");
                    txtPromoEnd
                            .setText(tableModel.getValueAt(r, 7) != null ? tableModel.getValueAt(r, 7).toString() : "");
                    txtPromoPrice.setText(tableModel.getValueAt(r, 8).toString());
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        // Events
        btnSave.addActionListener(this::saveCourse);
        btnUpdate.addActionListener(this::updateCourse);
        btnDelete.addActionListener(this::deleteCourse);
        btnRefresh.addActionListener(e -> loadTable());

        loadTable();
    }

    private void saveCourse(ActionEvent e) {
        try {
            Curso c = buildCourseFromForm();
            cursoService.save(c);
            loadTable();
            clearFields();
            JOptionPane.showMessageDialog(this, "Course saved with ID: " + c.getId(), "Success",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            showError(ex.getMessage());
        }
    }

    private void updateCourse(ActionEvent e) {
        try {
            int r = table.getSelectedRow();
            if (r < 0) {
                showError("Select a course from the table to modify.");
                return;
            }
            int id = (int) tableModel.getValueAt(r, 0);
            Curso c = buildCourseFromForm();
            c.setId(id);
            cursoService.update(c);
            loadTable();
            clearFields();
            JOptionPane.showMessageDialog(this, "Course modified successfully.", "Success",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            showError(ex.getMessage());
        }
    }

    private void deleteCourse(ActionEvent e) {
        try {
            int r = table.getSelectedRow();
            if (r < 0) {
                showError("Select a course from the table to delete.");
                return;
            }
            int id = (int) tableModel.getValueAt(r, 0);
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to delete course ID " + id + "?",
                    "Confirm deletion", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                cursoService.delete(id);
                loadTable();
                clearFields();
            }
        } catch (Exception ex) {
            showError(ex.getMessage());
        }
    }

    private Curso buildCourseFromForm() throws Exception {
        if (txtName.getText().isEmpty() || txtPrice.getText().isEmpty()) {
            throw new Exception("Name and Price are required.");
        }

        String name = txtName.getText();
        double price = Double.parseDouble(txtPrice.getText());
        int capacity = txtCapacity.getText().isEmpty() ? 0 : Integer.parseInt(txtCapacity.getText());
        double passingGrade = txtPassingGrade.getText().isEmpty() ? 4.0 : Double.parseDouble(txtPassingGrade.getText());
        int numPartials = txtNumPartials.getText().isEmpty() ? 2 : Integer.parseInt(txtNumPartials.getText());
        double promoPrice = txtPromoPrice.getText().isEmpty() ? 0 : Double.parseDouble(txtPromoPrice.getText());

        LocalDate promoStart = null;
        LocalDate promoEnd = null;
        if (!txtPromoStart.getText().isEmpty()) {
            promoStart = LocalDate.parse(txtPromoStart.getText());
        }
        if (!txtPromoEnd.getText().isEmpty()) {
            promoEnd = LocalDate.parse(txtPromoEnd.getText());
        }

        return new Curso(0, name, price, capacity, passingGrade, numPartials, promoStart, promoEnd, promoPrice);
    }

    private void loadTable() {
        try {
            tableModel.setRowCount(0);
            List<Curso> courses = cursoService.findAll();
            for (Curso c : courses) {
                tableModel.addRow(new Object[] {
                        c.getId(), c.getName(), c.getPrice(), c.getCapacity(),
                        c.getPassingGrade(), c.getNumPartialGrades(),
                        c.getPromoStartDate(), c.getPromoEndDate(), c.getPromoPrice()
                });
            }
            if (table.getColumnCount() > 0) {
                UITheme.styleTable(table);
            }
        } catch (ServiceException ex) {
            showError(ex.getMessage());
        }
    }

    private void clearFields() {
        txtName.setText("");
        txtPrice.setText("");
        txtCapacity.setText("");
        txtPassingGrade.setText("");
        txtNumPartials.setText("");
        txtPromoStart.setText("");
        txtPromoEnd.setText("");
        txtPromoPrice.setText("");
        table.clearSelection();
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
