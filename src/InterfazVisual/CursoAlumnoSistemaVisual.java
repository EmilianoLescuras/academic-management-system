package InterfazVisual;

import SERVICIO.AlumnoService;
import DAO.CursoDAO;
import EXCEPCIONES.ServiceException;
import SERVICIO.CursoAlumnoService;
import Sistema.Alumno;
import Sistema.Curso;
import Sistema.CursoAlumno;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class CursoAlumnoSistemaVisual extends JPanel {

    private JComboBox<Alumno> comboStudents;
    private JComboBox<Curso> comboCourses;
    private JTextArea areaReports;
    private Alumno lastEnrolledStudent;
    private Curso lastEnrolledCourse;

    private CursoAlumnoService service;
    private AlumnoService studentService;
    private CursoDAO courseDAO;

    public CursoAlumnoSistemaVisual() {
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
                "Course Enrollments",
                TitledBorder.LEFT, TitledBorder.TOP,
                UITheme.FONT_TITLE, UITheme.PRIMARY));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        comboStudents = new JComboBox<>();
        comboStudents.setFont(UITheme.FONT_LABEL);
        comboCourses = new JComboBox<>();
        comboCourses.setFont(UITheme.FONT_LABEL);

        int row = 0;
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0;
        topSelectionPanel.add(UITheme.createLabel("Student:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1;
        topSelectionPanel.add(comboStudents, gbc);

        row++;
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0;
        topSelectionPanel.add(UITheme.createLabel("Course:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1;
        topSelectionPanel.add(comboCourses, gbc);

        // Button panel
        JPanel buttonPanel = new JPanel(new GridLayout(2, 3, 10, 10));
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(new EmptyBorder(10, 0, 10, 0));

        JButton btnEnroll = UITheme.createSecondaryButton("Enroll");
        JButton btnUnenroll = UITheme.createDangerButton("Unenroll");
        JButton btnRefresh = UITheme.createPrimaryButton("Refresh");
        JButton btnRevenueReport = UITheme.createButton("Revenue Report", new Color(155, 89, 182),
                new Color(142, 68, 173));
        JButton btnApprovedReport = UITheme.createButton("Approved Report", new Color(52, 152, 219),
                new Color(41, 128, 185));
        JButton btnDownloadPDF = UITheme.createButton("Download Receipt", new Color(39, 174, 96),
                new Color(30, 145, 80));

        buttonPanel.add(btnEnroll);
        buttonPanel.add(btnUnenroll);
        buttonPanel.add(btnRefresh);
        buttonPanel.add(btnRevenueReport);
        buttonPanel.add(btnApprovedReport);
        buttonPanel.add(btnDownloadPDF);

        JPanel topWrapper = new JPanel(new BorderLayout());
        topWrapper.setOpaque(false);
        topWrapper.add(topSelectionPanel, BorderLayout.CENTER);
        topWrapper.add(buttonPanel, BorderLayout.SOUTH);

        // Report area
        areaReports = new JTextArea(14, 50);
        areaReports.setEditable(false);
        areaReports.setFont(new Font("Consolas", Font.PLAIN, 13));
        areaReports.setBackground(new Color(44, 62, 80));
        areaReports.setForeground(new Color(236, 240, 241));
        areaReports.setBorder(new EmptyBorder(15, 15, 15, 15));

        JScrollPane scrollPane = new JScrollPane(areaReports);
        scrollPane.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(UITheme.TABLE_HEADER_BG, 2),
                "Reports",
                TitledBorder.LEFT, TitledBorder.TOP,
                UITheme.FONT_TITLE, UITheme.TABLE_HEADER_BG));

        add(topWrapper, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        loadCombos();

        // Events
        btnEnroll.addActionListener(this::enrollStudent);
        btnUnenroll.addActionListener(this::unenrollStudent);
        btnRefresh.addActionListener(e -> loadCombos());
        btnRevenueReport
                .addActionListener(e -> showReport(service::revenueReport, "Revenue Report"));
        btnApprovedReport
                .addActionListener(e -> showReport(service::approvedReport, "Approved Report"));
        btnDownloadPDF.addActionListener(this::downloadReceipt);
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

            areaReports.setText("Select a student and a course to enroll or unenroll.\n\n" +
                    "Grades are assigned in the 'Grade Management' tab.");
        } catch (Exception e) {
            showError("Error loading data: " + e.getMessage());
        }
    }

    private void enrollStudent(ActionEvent e) {
        try {
            Alumno student = (Alumno) comboStudents.getSelectedItem();
            Curso course = (Curso) comboCourses.getSelectedItem();

            if (student == null || course == null) {
                showError("You must select a student and a course.");
                return;
            }

            CursoAlumno enrollment = new CursoAlumno(course.getId(), student.getStudentId());
            service.enrollStudent(enrollment, course.getPassingGrade());

            // Save for receipt
            lastEnrolledStudent = student;
            lastEnrolledCourse = course;

            showMessage("Student enrolled successfully.\n\n" +
                    "Student: " + student.getFirstName() + " " + student.getLastName() + "\n" +
                    "Course: " + course.getName() + "\n\n" +
                    "You can download the receipt with the 'Download Receipt' button");

            areaReports.setText("ENROLLMENT SUCCESSFUL\n\n" +
                    "Student: " + student.getFirstName() + " " + student.getLastName() + "\n" +
                    "Student ID: " + student.getStudentId() + "\n" +
                    "Email: " + student.getEmail() + "\n\n" +
                    "Course: " + course.getName() + "\n" +
                    "Price: $" + course.getPrice() + "\n" +
                    "Capacity: " + course.getCapacity() + "\n" +
                    "Partials: " + course.getNumPartialGrades() + "\n\n" +
                    "Use 'Download Receipt' to get the PDF.");
        } catch (Exception ex) {
            showError("Error enrolling: " + ex.getMessage());
        }
    }

    private void unenrollStudent(ActionEvent e) {
        try {
            Alumno student = (Alumno) comboStudents.getSelectedItem();
            Curso course = (Curso) comboCourses.getSelectedItem();

            if (student == null || course == null) {
                showError("You must select a student and a course.");
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(this,
                    "WARNING: This action will delete the enrollment and ALL grades.\n\n" +
                            "Student: " + student.getFirstName() + " " + student.getLastName() + "\n" +
                            "Course: " + course.getName() + "\n\n" +
                            "Are you sure?",
                    "Confirm Unenrollment", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

            if (confirm == JOptionPane.YES_OPTION) {
                service.unenrollStudent(course.getId(), student.getStudentId());
                showMessage("Student unenrolled successfully.");

                areaReports.setText("UNENROLLMENT COMPLETED\n\n" +
                        "The student " + student.getFirstName() + " " + student.getLastName() + "\n" +
                        "has been unenrolled from the course " + course.getName() + ".");
            }
        } catch (Exception ex) {
            showError("Error unenrolling: " + ex.getMessage());
        }
    }

    private void downloadReceipt(ActionEvent e) {
        Alumno student = lastEnrolledStudent != null ? lastEnrolledStudent
                : (Alumno) comboStudents.getSelectedItem();
        Curso course = lastEnrolledCourse != null ? lastEnrolledCourse : (Curso) comboCourses.getSelectedItem();

        if (student == null || course == null) {
            showError("You must enroll a student first or select a student and course.");
            return;
        }

        // Check if iText is available
        if (isITextAvailable()) {
            generatePDFiText(student, course);
        } else {
            generateTextReceipt(student, course);
        }
    }

    private boolean isITextAvailable() {
        try {
            Class.forName("com.itextpdf.kernel.pdf.PdfDocument");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    private void generatePDFiText(Alumno student, Curso course) {
        try {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setSelectedFile(
                    new File("Receipt_" + student.getStudentId() + "_" + course.getId() + ".pdf"));

            if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();

                // Use iText 7
                com.itextpdf.kernel.pdf.PdfWriter writer = new com.itextpdf.kernel.pdf.PdfWriter(file);
                com.itextpdf.kernel.pdf.PdfDocument pdf = new com.itextpdf.kernel.pdf.PdfDocument(writer);
                com.itextpdf.layout.Document document = new com.itextpdf.layout.Document(pdf);

                // Title
                document.add(new com.itextpdf.layout.element.Paragraph("ENROLLMENT RECEIPT")
                        .setFontSize(20)
                        .setBold()
                        .setTextAlignment(com.itextpdf.layout.properties.TextAlignment.CENTER));

                document.add(new com.itextpdf.layout.element.Paragraph("\n"));

                // Student data
                document.add(
                        new com.itextpdf.layout.element.Paragraph("STUDENT DATA").setBold().setFontSize(14));
                document.add(new com.itextpdf.layout.element.Paragraph("ID: " + student.getStudentId()));
                document.add(new com.itextpdf.layout.element.Paragraph(
                        "Name: " + student.getFirstName() + " " + student.getLastName()));
                document.add(new com.itextpdf.layout.element.Paragraph("Email: " + student.getEmail()));

                document.add(new com.itextpdf.layout.element.Paragraph("\n"));

                // Course data
                document.add(
                        new com.itextpdf.layout.element.Paragraph("COURSE DATA").setBold().setFontSize(14));
                document.add(new com.itextpdf.layout.element.Paragraph("ID: " + course.getId()));
                document.add(new com.itextpdf.layout.element.Paragraph("Name: " + course.getName()));
                document.add(new com.itextpdf.layout.element.Paragraph("Price: $" + course.getPrice()));
                document.add(
                        new com.itextpdf.layout.element.Paragraph(
                                "Partial Exams: " + course.getNumPartialGrades()));
                document.add(
                        new com.itextpdf.layout.element.Paragraph(
                                "Passing Grade: " + course.getPassingGrade()));

                document.add(new com.itextpdf.layout.element.Paragraph("\n"));

                // Date
                String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss"));
                document.add(new com.itextpdf.layout.element.Paragraph("Enrollment Date: " + date));

                document.close();
                showMessage("PDF generated successfully:\n" + file.getAbsolutePath());
            }
        } catch (Exception ex) {
            showError("Error generating PDF: " + ex.getMessage());
        }
    }

    // Alternative if iText is not available - generates text file
    private void generateTextReceipt(Alumno student, Curso course) {
        try {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setSelectedFile(
                    new File("Receipt_" + student.getStudentId() + "_" + course.getId() + ".txt"));

            if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();

                String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss"));

                StringBuilder sb = new StringBuilder();
                sb.append("=".repeat(50)).append("\n");
                sb.append("       ENROLLMENT RECEIPT\n");
                sb.append("=".repeat(50)).append("\n\n");
                sb.append("STUDENT DATA\n");
                sb.append("-".repeat(30)).append("\n");
                sb.append("ID: ").append(student.getStudentId()).append("\n");
                sb.append("Name: ").append(student.getFirstName()).append(" ").append(student.getLastName())
                        .append("\n");
                sb.append("Email: ").append(student.getEmail()).append("\n\n");
                sb.append("COURSE DATA\n");
                sb.append("-".repeat(30)).append("\n");
                sb.append("ID: ").append(course.getId()).append("\n");
                sb.append("Name: ").append(course.getName()).append("\n");
                sb.append("Price: $").append(course.getPrice()).append("\n");
                sb.append("Partial Exams: ").append(course.getNumPartialGrades()).append("\n");
                sb.append("Passing Grade: ").append(course.getPassingGrade()).append("\n\n");
                sb.append("-".repeat(30)).append("\n");
                sb.append("Enrollment Date: ").append(date).append("\n");
                sb.append("=".repeat(50)).append("\n");
                sb.append("\nFor PDFs, add iText to the classpath.\n");

                try (FileOutputStream fos = new FileOutputStream(file)) {
                    fos.write(sb.toString().getBytes());
                }

                showMessage("Receipt generated:\n" + file.getAbsolutePath() +
                        "\n\n(For PDFs, add iText to the classpath)");
            }
        } catch (Exception ex) {
            showError("Error generating receipt: " + ex.getMessage());
        }
    }

    private void showReport(ThrowingSupplier<List<String>> supplier, String title) {
        try {
            List<String> data = supplier.get();
            areaReports.setText(title + "\n");
            areaReports.append("=".repeat(55) + "\n\n");
            if (data.isEmpty()) {
                areaReports.append("No data to show.\n");
            } else {
                data.forEach(line -> areaReports.append("  " + line + "\n"));
            }
        } catch (Exception ex) {
            showError("Error generating report: " + ex.getMessage());
        }
    }

    private void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    @FunctionalInterface
    interface ThrowingSupplier<T> {
        T get() throws ServiceException;
    }
}
