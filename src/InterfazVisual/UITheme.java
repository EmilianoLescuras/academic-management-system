package InterfazVisual;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Utility class to apply a modern theme to Swing components.
 */
public class UITheme {

    // Theme colors
    public static final Color PRIMARY = new Color(41, 128, 185); // Main blue
    public static final Color PRIMARY_DARK = new Color(31, 97, 141); // Dark blue (hover)
    public static final Color SECONDARY = new Color(46, 204, 113); // Green
    public static final Color SECONDARY_DARK = new Color(39, 174, 96); // Dark green (hover)
    public static final Color DANGER = new Color(231, 76, 60); // Red
    public static final Color DANGER_DARK = new Color(192, 57, 43); // Dark red (hover)
    public static final Color WARNING = new Color(241, 196, 15); // Yellow
    public static final Color BACKGROUND = new Color(236, 240, 241); // Light gray background
    public static final Color PANEL_BG = new Color(255, 255, 255); // White
    public static final Color TEXT_PRIMARY = new Color(44, 62, 80); // Dark text
    public static final Color TEXT_SECONDARY = new Color(127, 140, 141); // Gray text
    public static final Color TABLE_HEADER_BG = new Color(52, 73, 94); // Table header
    public static final Color TABLE_ROW_ALT = new Color(245, 248, 250); // Alternating row

    // Fonts
    public static final Font FONT_TITLE = new Font("Segoe UI", Font.BOLD, 18);
    public static final Font FONT_LABEL = new Font("Segoe UI", Font.PLAIN, 14);
    public static final Font FONT_BUTTON = new Font("Segoe UI", Font.BOLD, 13);
    public static final Font FONT_TABLE = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font FONT_TABLE_HEADER = new Font("Segoe UI", Font.BOLD, 13);

    /**
     * Creates a styled button with hover effect
     */
    public static JButton createButton(String text, Color bgColor, Color hoverColor) {
        JButton button = new JButton(text);
        button.setFont(FONT_BUTTON);
        button.setForeground(Color.WHITE);
        button.setBackground(bgColor);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(new EmptyBorder(10, 20, 10, 20));

        // Hover effect
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(hoverColor);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(bgColor);
            }
        });

        return button;
    }

    /**
     * Creates a primary button (blue)
     */
    public static JButton createPrimaryButton(String text) {
        return createButton(text, PRIMARY, PRIMARY_DARK);
    }

    /**
     * Creates a secondary button (green)
     */
    public static JButton createSecondaryButton(String text) {
        return createButton(text, SECONDARY, SECONDARY_DARK);
    }

    /**
     * Creates a danger button (red)
     */
    public static JButton createDangerButton(String text) {
        return createButton(text, DANGER, DANGER_DARK);
    }

    /**
     * Styles a JLabel
     */
    public static JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(FONT_LABEL);
        label.setForeground(TEXT_PRIMARY);
        return label;
    }

    /**
     * Styles a JTextField
     */
    public static JTextField createTextField() {
        JTextField field = new JTextField();
        field.setFont(FONT_LABEL);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(189, 195, 199), 1),
                new EmptyBorder(8, 10, 8, 10)));
        return field;
    }

    /**
     * Styles a JTable
     */
    public static void styleTable(JTable table) {
        table.setFont(FONT_TABLE);
        table.setRowHeight(35);
        table.setSelectionBackground(PRIMARY);
        table.setSelectionForeground(Color.WHITE);
        table.setGridColor(new Color(220, 220, 220));
        table.setShowGrid(true);
        table.setIntercellSpacing(new Dimension(1, 1));

        // Style header
        JTableHeader header = table.getTableHeader();
        header.setFont(FONT_TABLE_HEADER);
        header.setBackground(TABLE_HEADER_BG);
        header.setForeground(Color.WHITE);
        header.setPreferredSize(new Dimension(header.getWidth(), 40));

        // Center cell content
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
    }

    /**
     * Styles a panel with background
     */
    public static void stylePanel(JPanel panel) {
        panel.setBackground(PANEL_BG);
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));
    }

    /**
     * Styles the main panel with gray background
     */
    public static void styleMainPanel(JPanel panel) {
        panel.setBackground(BACKGROUND);
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
    }
}
