package InterfazVisual;

import SERVICIO.AlumnoService;
import DAO.CursoDAO;
import EXCEPCIONES.ServiceException;
import SERVICIO.CursoAlumnoService;
import Sistema.Alumno;
import Sistema.Curso;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class GestionNotasSistemaVisual extends JPanel {

    private JComboBox<Alumno> comboStudents;
    private JComboBox<Curso> comboCourses;
    private JComboBox<Integer> comboPartialNum;
    private JTextField txtPartialGrade;
    private JTextArea areaGrades;
    private JLabel lblSummary;
    private JLabel lblStatus;
    private JButton btnRetakeSubject;

    private CursoAlumnoService service;
    private AlumnoService studentService;
    private CursoDAO courseDAO;

    public GestionNotasSistemaVisual() {
        this.service = new CursoAlumnoService();
        this.studentService = new AlumnoService();
        this.courseDAO = new CursoDAO();

        setLayout(new BorderLayout(15, 15));
        UITheme.styleMainPanel(this);

        // Top panel with selection
        JPanel topSelectionPanel = new JPanel(new GridBagLayout());
        UITheme.stylePanel(topSelectionPanel);
        topSelectionPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(UITheme.PRIMARY, 2),
                "Grade Management",
                TitledBorder.LEFT, TitledBorder.TOP,
                UITheme.FONT_TITLE, UITheme.PRIMARY));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        comboStudents = new JComboBox<>();
        comboStudents.setFont(UITheme.FONT_LABEL);
        comboCourses = new JComboBox<>();
        comboCourses.setFont(UITheme.FONT_LABEL);
        comboPartialNum = new JComboBox<>();
        comboPartialNum.setFont(UITheme.FONT_LABEL);
        txtPartialGrade = UITheme.createTextField();
        lblSummary = new JLabel("Partial: 0/0");
        lblSummary.setFont(UITheme.FONT_LABEL);
        lblSummary.setForeground(UITheme.PRIMARY);
        lblStatus = new JLabel("Status: -");
        lblStatus.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblStatus.setForeground(UITheme.SECONDARY);

        int row = 0;
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0;
        topSelectionPanel.add(UITheme.createLabel("Student:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1;
        gbc.gridwidth = 3;
        topSelectionPanel.add(comboStudents, gbc);
        gbc.gridwidth = 1;

        row++;
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0;
        topSelectionPanel.add(UITheme.createLabel("Course:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1;
        gbc.gridwidth = 3;
        topSelectionPanel.add(comboCourses, gbc);
        gbc.gridwidth = 1;

        row++;
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0;
        topSelectionPanel.add(UITheme.createLabel("Partial N:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 0.3;
        topSelectionPanel.add(comboPartialNum, gbc);
        gbc.gridx = 2;
        gbc.weightx = 0;
        topSelectionPanel.add(UITheme.createLabel("Grade:"), gbc);
        gbc.gridx = 3;
        gbc.weightx = 0.7;
        topSelectionPanel.add(txtPartialGrade, gbc);

        row++;
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 2;
        topSelectionPanel.add(lblSummary, gbc);
        gbc.gridx = 2;
        gbc.gridwidth = 2;
        topSelectionPanel.add(lblStatus, gbc);
        gbc.gridwidth = 1;

        // Button panel
        JPanel buttonPanel = new JPanel(new GridLayout(2, 3, 10, 10));
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(new EmptyBorder(10, 0, 10, 0));

        JButton btnAddPartial = UITheme.createPrimaryButton("Add Partial");
        JButton btnRetake = UITheme.createButton("Retake", new Color(230, 126, 34),
                new Color(211, 84, 0));
        JButton btnAddFinal = UITheme.createSecondaryButton("Add Final");
        JButton btnViewGrades = UITheme.createButton("View Grades", new Color(155, 89, 182),
                new Color(142, 68, 173));
        btnRetakeSubject = UITheme.createDangerButton("Retake Subject");
        JButton btnRefresh = UITheme.createPrimaryButton("REFRESH");

        buttonPanel.add(btnAddPartial);
        buttonPanel.add(btnRetake);
        buttonPanel.add(btnAddFinal);
        buttonPanel.add(btnViewGrades);
        buttonPanel.add(btnRetakeSubject);
        buttonPanel.add(btnRefresh);

        JPanel topWrapper = new JPanel(new BorderLayout());
        topWrapper.setOpaque(false);
        topWrapper.add(topSelectionPanel, BorderLayout.CENTER);
        topWrapper.add(buttonPanel, BorderLayout.SOUTH);

        // Grade area
        areaGrades = new JTextArea(12, 50);
        areaGrades.setEditable(false);
        areaGrades.setFont(new Font("Consolas", Font.PLAIN, 14));
        areaGrades.setBackground(new Color(44, 62, 80));
        areaGrades.setForeground(new Color(236, 240, 241));
        areaGrades.setBorder(new EmptyBorder(15, 15, 15, 15));

        JScrollPane scrollPane = new JScrollPane(areaGrades);
        scrollPane.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(UITheme.TABLE_HEADER_BG, 2),
                "Grade Details",
                TitledBorder.LEFT, TitledBorder.TOP,
                UITheme.FONT_TITLE, UITheme.TABLE_HEADER_BG));

        add(topWrapper, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        loadCombos();

        // Events
        btnAddPartial.addActionListener(this::addPartialGrade);
        btnRetake.addActionListener(this::addRetake);
        btnAddFinal.addActionListener(this::addFinalGrade);
        btnViewGrades.addActionListener(this::viewGrades);
        btnRetakeSubject.addActionListener(this::retakeSubjectAction);
        btnRefresh.addActionListener(e -> loadCombos());

        // Update status when selection changes
        comboStudents.addActionListener(e -> updateStatus());
        comboCourses.addActionListener(e -> {
            updatePartialCombo();
            updateStatus();
        });
    }

    private void loadCombos() {
        try {
            comboStudents.removeAllItems();
            comboCourses.removeAllItems();

            List<Alumno> students = studentService.findAll();
            List<Curso> courses = courseDAO.findAll();

            for (Alumno a : students)
                comboStudents.addItem(a);
            for (Curso c : courses)
                comboCourses.addItem(c);

            updatePartialCombo();
        } catch (Exception e) {
            showError("Error loading data: " + e.getMessage());
        }
    }

    private void updatePartialCombo() {
        comboPartialNum.removeAllItems();
        Curso course = (Curso) comboCourses.getSelectedItem();
        if (course != null) {
            for (int i = 1; i <= course.getNumPartialGrades(); i++) {
                comboPartialNum.addItem(i);
            }
        }
    }

    private void updateStatus() {
        try {
            Alumno student = (Alumno) comboStudents.getSelectedItem();
            Curso course = (Curso) comboCourses.getSelectedItem();
            if (student == null || course == null)
                return;

            String status = service.getStatus(course.getId(), student.getStudentId());
            List<Double> partials = service.getPartialGrades(course.getId(), student.getStudentId());

            lblSummary.setText("Partials: " + partials.size() + "/" + course.getNumPartialGrades());

            if ("FAILED".equals(status)) {
                lblStatus.setText("Status: FAILED");
                lblStatus.setForeground(UITheme.DANGER);
                btnRetakeSubject.setEnabled(true);
            } else {
                lblStatus.setText("Status: ACTIVE");
                lblStatus.setForeground(UITheme.SECONDARY);
                btnRetakeSubject.setEnabled(false);
            }
        } catch (ServiceException ex) {
            lblStatus.setText("Status: Error");
        }
    }

    private void addPartialGrade(ActionEvent e) {
        try {
            Alumno student = (Alumno) comboStudents.getSelectedItem();
            Curso course = (Curso) comboCourses.getSelectedItem();
            Integer partialNumber = (Integer) comboPartialNum.getSelectedItem();

            if (student == null || course == null || partialNumber == null) {
                showError("Select a student, course, and partial number.");
                return;
            }

            // Check if all partials are loaded
            List<Double> partials = service.getPartialGrades(course.getId(), student.getStudentId());
            if (partials.size() >= course.getNumPartialGrades()) {
                showError(
                        "All partial grades have already been loaded (" + course.getNumPartialGrades() + ").\n" +
                                "If there was a failed partial, you must use Retake.");
                return;
            }

            double grade = validateDouble(txtPartialGrade.getText(), "Partial Grade");

            service.registerPartialGrade(course.getId(), student.getStudentId(), grade);
            txtPartialGrade.setText("");

            String gradeStatus = grade >= course.getPassingGrade() ? "APPROVED" : "FAILED (can retake)";
            showMessage("Partial " + partialNumber + " registered: " + grade + " - " + gradeStatus);
            updateStatus();
            viewGrades(null);
        } catch (Exception ex) {
            showError("Error registering partial: " + ex.getMessage());
        }
    }

    private void addRetake(ActionEvent e) {
        try {
            Alumno student = (Alumno) comboStudents.getSelectedItem();
            Curso course = (Curso) comboCourses.getSelectedItem();

            if (student == null || course == null) {
                showError("Select a student and a course.");
                return;
            }

            // Get failed partials
            List<Double> partials = service.getPartialGrades(course.getId(), student.getStudentId());
            if (partials.isEmpty()) {
                showError("The student does not have any partials loaded.");
                return;
            }

            // Find failed partials
            StringBuilder failedPartials = new StringBuilder();
            int count = 0;
            for (int i = 0; i < partials.size(); i++) {
                if (partials.get(i) < course.getPassingGrade()) {
                    count++;
                    failedPartials.append("  Partial ").append(i + 1).append(": ").append(partials.get(i))
                            .append("\n");
                }
            }

            if (count == 0) {
                showError("The student does not have failed partials. No retake needed.");
                return;
            }

            String input = JOptionPane.showInputDialog(this,
                    "Failed partials:\n" + failedPartials +
                            "\nWhich partial number do you want to retake? (1-" + partials.size() + "):",
                    "Select Partial", JOptionPane.QUESTION_MESSAGE);

            if (input == null || input.isEmpty())
                return;
            int partialNumber = Integer.parseInt(input);

            if (partialNumber < 1 || partialNumber > partials.size()) {
                showError("Invalid partial number.");
                return;
            }

            String gradeInput = JOptionPane.showInputDialog(this,
                    "Enter the retake grade for Partial " + partialNumber + ":",
                    "Retake Grade", JOptionPane.QUESTION_MESSAGE);

            if (gradeInput == null || gradeInput.isEmpty())
                return;
            double grade = Double.parseDouble(gradeInput);

            try {
                service.registerRetake(course.getId(), student.getStudentId(), partialNumber, grade,
                        course.getPassingGrade());
                showMessage("Retake registered with grade: " + grade);
            } catch (ServiceException ex) {
                showError(ex.getMessage());
            }
            updateStatus();
            viewGrades(null);
        } catch (Exception ex) {
            showError("Error registering retake: " + ex.getMessage());
        }
    }

    private void retakeSubjectAction(ActionEvent e) {
        try {
            Alumno student = (Alumno) comboStudents.getSelectedItem();
            Curso course = (Curso) comboCourses.getSelectedItem();

            if (student == null || course == null) {
                showError("Select a student and a course.");
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(this,
                    "ATTENTION: This action will delete ALL the student's grades in this course.\n\n" +
                            "Student: " + student.getFirstName() + " " + student.getLastName() + "\n" +
                            "Course: " + course.getName() + "\n\n" +
                            "Are you sure you want to retake the subject?",
                    "Retake Subject", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

            if (confirm == JOptionPane.YES_OPTION) {
                service.retakeSubject(course.getId(), student.getStudentId());
                showMessage("The subject has been reset. The student can take the course again.");
                updateStatus();
                viewGrades(null);
            }
        } catch (Exception ex) {
            showError("Error retaking the subject: " + ex.getMessage());
        }
    }

    private void addFinalGrade(ActionEvent e) {
        try {
            Alumno student = (Alumno) comboStudents.getSelectedItem();
            Curso course = (Curso) comboCourses.getSelectedItem();

            if (student == null || course == null) {
                showError("Select a student and a course.");
                return;
            }

            // Check if the student can take the final
            String error = service.canTakeFinal(course.getId(), student.getStudentId(),
                    course.getPassingGrade(), course.getNumPartialGrades());

            if (error != null) {
                showError(error);
                return;
            }

            // Show used attempts
            int attempts = service.getFinalAttempts(course.getId(), student.getStudentId());
            String input = JOptionPane.showInputDialog(this,
                    "Attempts used: " + attempts + "/3\n\nEnter the final exam grade (1-10):",
                    "Final Exam", JOptionPane.QUESTION_MESSAGE);
            if (input == null || input.isEmpty())
                return;

            int finalGrade = validateInt(input, "Final Exam");
            try {
                service.registerFinalGrade(course.getId(), student.getStudentId(), finalGrade,
                        course.getPassingGrade());
                showMessage("Final exam registered: " + finalGrade);
            } catch (ServiceException ex) {
                showError(ex.getMessage());
            }
            updateStatus();
            viewGrades(null);
        } catch (Exception ex) {
            showError("Error registering final: " + ex.getMessage());
        }
    }

    private void viewGrades(ActionEvent e) {
        try {
            Alumno student = (Alumno) comboStudents.getSelectedItem();
            Curso course = (Curso) comboCourses.getSelectedItem();

            if (student == null || course == null) {
                areaGrades.setText("Select a student and a course.");
                return;
            }

            List<Double> partials = service.getPartialGrades(course.getId(), student.getStudentId());
            List<Double> retakes = service.getRetakes(course.getId(), student.getStudentId());
            Integer finalGrade = service.getFinalGrade(course.getId(), student.getStudentId());
            String status = service.getStatus(course.getId(), student.getStudentId());

            areaGrades.setText("=".repeat(55) + "\n");
            areaGrades.append("  STUDENT: " + student.getFirstName() + " " + student.getLastName() + "\n");
            areaGrades.append("  COURSE: " + course.getName() + "\n");
            areaGrades.append("  STATUS: " + ("FAILED".equals(status) ? "FAILED" : "ACTIVE") + "\n");
            areaGrades.append("  PARTIAL EXAMS REQUIRED: " + course.getNumPartialGrades() + "\n");
            areaGrades.append("=".repeat(55) + "\n\n");

            areaGrades.append("  PARTIAL EXAMS:\n");
            if (partials.isEmpty()) {
                areaGrades.append("    No partial exams loaded.\n");
            } else {
                for (int i = 0; i < partials.size(); i++) {
                    String partialStatus = partials.get(i) >= course.getPassingGrade() ? "APPROVED"
                            : "FAILED";
                    areaGrades.append(
                            "     Partial " + (i + 1) + ": " + partials.get(i) + " - " + partialStatus + "\n");
                }
            }

            if (!retakes.isEmpty()) {
                areaGrades.append("\n  RETAKE EXAMS:\n");
                for (int i = 0; i < retakes.size(); i++) {
                    String retakeStatus = retakes.get(i) >= course.getPassingGrade() ? "APPROVED"
                            : "FAILED";
                    areaGrades.append(
                            "     Retake Exam: " + retakes.get(i) + " - " + retakeStatus + "\n");
                }
            }

            areaGrades.append("\n  FINAL EXAMS (max 3 attempts):\n");
            List<Integer> finalAttempts = service.getAllFinalGrades(course.getId(), student.getStudentId());
            if (finalAttempts.isEmpty()) {
                if ("FAILED".equals(status)) {
                    areaGrades.append("     CANNOT TAKE THE EXAM - COURSE FAILED\n");
                    areaGrades.append("\n   Use 'Retake Subject' to re-enroll.\n");
                } else {
                    areaGrades.append("     Pending (0/3 attempts used)\n");
                }
            } else {
                for (int i = 0; i < finalAttempts.size(); i++) {
                    String attemptStatus = finalAttempts.get(i) >= course.getPassingGrade() ? "APPROVED"
                            : "FAILED";
                    areaGrades.append(
                            "     Attempt " + (i + 1) + ": " + finalAttempts.get(i) + " - " + attemptStatus
                                    + "\n");
                }
                areaGrades.append("     (" + finalAttempts.size() + "/3 attempts used)\n");

                // If already approved
                boolean approved = finalAttempts.stream().anyMatch(n -> n >= course.getPassingGrade());
                if (approved) {
                    areaGrades.append("\nCOURSE APPROVED\n");
                } else if ("FAILED".equals(status)) {
                    areaGrades.append("\n Use 'Retake Subject' to re-enroll.\n");
                }
            }

        } catch (Exception ex) {
            showError("Error viewing grades: " + ex.getMessage());
        }
    }

    private int validateInt(String text, String field) throws Exception {
        if (text == null || text.isEmpty())
            throw new Exception("The field '" + field + "' cannot be empty.");
        return Integer.parseInt(text);
    }

    private double validateDouble(String text, String field) throws Exception {
        if (text == null || text.isEmpty())
            throw new Exception("The field '" + field + "' cannot be empty.");
        return Double.parseDouble(text);
    }

    private void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
