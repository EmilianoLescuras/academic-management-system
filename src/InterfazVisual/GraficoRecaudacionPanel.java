package InterfazVisual;

import SERVICIO.CursoAlumnoService;
import EXCEPCIONES.ServiceException;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// Note: This class requires JFreeChart on the classpath
// If JFreeChart is not available, an alternative chart is shown using Swing

public class GraficoRecaudacionPanel extends JPanel {

    private CursoAlumnoService service;
    private JPanel chartPanel;

    public GraficoRecaudacionPanel() {
        this.service = new CursoAlumnoService();
        setLayout(new BorderLayout(15, 15));
        UITheme.styleMainPanel(this);

        // Title
        JLabel lblTitle = new JLabel("Revenue Chart by Course", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitle.setForeground(UITheme.PRIMARY);
        lblTitle.setBorder(new EmptyBorder(15, 0, 15, 0));

        // Chart panel
        chartPanel = new JPanel(new BorderLayout());
        chartPanel.setBackground(Color.WHITE);
        chartPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(UITheme.PRIMARY, 2),
                "Revenue",
                TitledBorder.LEFT, TitledBorder.TOP,
                UITheme.FONT_TITLE, UITheme.PRIMARY));

        // Refresh button
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setOpaque(false);
        JButton btnRefresh = UITheme.createPrimaryButton("Refresh Chart");
        btnRefresh.addActionListener(e -> refreshChart());
        buttonPanel.add(btnRefresh);

        add(lblTitle, BorderLayout.NORTH);
        add(chartPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        refreshChart();
    }

    private void refreshChart() {
        chartPanel.removeAll();

        try {
            List<String> data = service.revenueReport();

            // Try to use JFreeChart, if not available use alternative chart
            if (isJFreeChartAvailable()) {
                createJFreeChart(data);
            } else {
                createAlternativeChart(data);
            }
        } catch (ServiceException e) {
            chartPanel.add(new JLabel("Error loading data: " + e.getMessage(), SwingConstants.CENTER));
        }

        chartPanel.revalidate();
        chartPanel.repaint();
    }

    private boolean isJFreeChartAvailable() {
        try {
            Class.forName("org.jfree.chart.ChartFactory");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    private void createJFreeChart(List<String> data) {
        try {
            // Create dataset
            org.jfree.data.category.DefaultCategoryDataset dataset = new org.jfree.data.category.DefaultCategoryDataset();

            Pattern pattern = Pattern.compile(
                    "Course: (.+?) \\| Enrolled: (\\d+) \\| Price: \\$(\\d+\\.?\\d*) \\| Revenue: \\$(\\d+\\.?\\d*)");

            for (String line : data) {
                Matcher matcher = pattern.matcher(line);
                if (matcher.find()) {
                    String course = matcher.group(1);
                    double revenue = Double.parseDouble(matcher.group(4));
                    dataset.addValue(revenue, "Revenue", course);
                }
            }

            // Create chart
            org.jfree.chart.JFreeChart chart = org.jfree.chart.ChartFactory.createBarChart(
                    "Revenue by Course",
                    "Course",
                    "Revenue ($)",
                    dataset,
                    org.jfree.chart.plot.PlotOrientation.VERTICAL,
                    true, true, false);

            // Customize appearance
            chart.setBackgroundPaint(Color.WHITE);
            org.jfree.chart.plot.CategoryPlot plot = chart.getCategoryPlot();
            plot.setBackgroundPaint(new Color(240, 240, 240));
            plot.setRangeGridlinePaint(Color.GRAY);

            org.jfree.chart.renderer.category.BarRenderer renderer = (org.jfree.chart.renderer.category.BarRenderer) plot
                    .getRenderer();
            renderer.setSeriesPaint(0, UITheme.PRIMARY);

            // Create chart panel
            org.jfree.chart.ChartPanel chartPanelJFree = new org.jfree.chart.ChartPanel(chart);
            chartPanelJFree.setPreferredSize(new Dimension(800, 400));
            chartPanel.add(chartPanelJFree, BorderLayout.CENTER);

        } catch (Exception e) {
            createAlternativeChart(data);
        }
    }

    // Alternative chart if JFreeChart is not available
    private void createAlternativeChart(List<String> data) {
        JPanel barsPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                Pattern pattern = Pattern.compile(
                        "Course: (.+?) \\| Enrolled: (\\d+) \\| Price: \\$(\\d+\\.?\\d*) \\| Revenue: \\$(\\d+\\.?\\d*)");

                double maxRevenue = 0;
                for (String line : data) {
                    Matcher matcher = pattern.matcher(line);
                    if (matcher.find()) {
                        double revenue = Double.parseDouble(matcher.group(4));
                        maxRevenue = Math.max(maxRevenue, revenue);
                    }
                }

                if (maxRevenue == 0) {
                    g2d.setColor(Color.GRAY);
                    g2d.setFont(new Font("Segoe UI", Font.PLAIN, 16));
                    g2d.drawString("No revenue data is available.", 50, 100);
                    return;
                }

                int barWidth = 80;
                int gap = 30;
                int startX = 50;
                int startY = getHeight() - 80;
                int maxHeight = getHeight() - 150;

                int x = startX;
                Color[] colors = { UITheme.PRIMARY, UITheme.SECONDARY, new Color(155, 89, 182),
                        new Color(230, 126, 34), new Color(52, 152, 219) };
                int colorIndex = 0;

                for (String line : data) {
                    Matcher matcher = pattern.matcher(line);
                    if (matcher.find()) {
                        String course = matcher.group(1);
                        double revenue = Double.parseDouble(matcher.group(4));

                        int barHeight = (int) ((revenue / maxRevenue) * maxHeight);

                        // Draw bar
                        g2d.setColor(colors[colorIndex % colors.length]);
                        g2d.fillRoundRect(x, startY - barHeight, barWidth, barHeight, 10, 10);

                        // Border
                        g2d.setColor(colors[colorIndex % colors.length].darker());
                        g2d.drawRoundRect(x, startY - barHeight, barWidth, barHeight, 10, 10);

                        // Value
                        g2d.setColor(Color.BLACK);
                        g2d.setFont(new Font("Segoe UI", Font.BOLD, 12));
                        String valueStr = "$" + (int) revenue;
                        int textWidth = g2d.getFontMetrics().stringWidth(valueStr);
                        g2d.drawString(valueStr, x + (barWidth - textWidth) / 2, startY - barHeight - 10);

                        // Course name
                        g2d.setFont(new Font("Segoe UI", Font.PLAIN, 11));
                        String shortName = course.length() > 12 ? course.substring(0, 10) + ".." : course;
                        textWidth = g2d.getFontMetrics().stringWidth(shortName);
                        g2d.drawString(shortName, x + (barWidth - textWidth) / 2, startY + 20);

                        x += barWidth + gap;
                        colorIndex++;
                    }
                }

                // Y axis
                g2d.setColor(Color.DARK_GRAY);
                g2d.drawLine(40, 50, 40, startY);
                g2d.drawLine(40, startY, getWidth() - 30, startY);
            }
        };

        barsPanel.setBackground(Color.WHITE);
        barsPanel.setPreferredSize(new Dimension(800, 450));

        JLabel lblNote = new JLabel("For advanced graphics, add JFreeChart to the classpath",
                SwingConstants.CENTER);
        lblNote.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        lblNote.setForeground(Color.GRAY);

        chartPanel.add(barsPanel, BorderLayout.CENTER);
        chartPanel.add(lblNote, BorderLayout.SOUTH);
    }
}
