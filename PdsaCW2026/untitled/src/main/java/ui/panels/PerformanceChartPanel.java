package ui.panels;

import database.DBConnection;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class PerformanceChartPanel extends JPanel {

    private JComboBox<String> gameSelector;
    private JPanel chartContainer;

    // ================= THEME COLORS =================
    private static final Color BG_DARK      = new Color(30, 30, 30);
    private static final Color BG_DARKER    = new Color(20, 20, 20);
    private static final Color BG_PANEL     = new Color(40, 40, 40);
    private static final Color ACCENT_PURP  = new Color(117, 60, 231);
    private static final Color ACCENT_BLUE  = new Color(52, 152, 219);
    private static final Color ACCENT_GREEN = new Color(46, 204, 113);
    private static final Color ACCENT_RED   = new Color(231, 76, 60);
    private static final Color ACCENT_GOLD  = new Color(241, 196, 15);
    private static final Color TEXT_WHITE   = Color.WHITE;
    private static final Color TEXT_DIM     = new Color(180, 180, 180);
    private static final Color BORDER_COLOR = new Color(60, 60, 60);

    // Chart bar colors per algorithm slot
    private static final Color[] BAR_COLORS = {
            ACCENT_BLUE, ACCENT_GREEN, ACCENT_RED, ACCENT_GOLD, ACCENT_PURP
    };

    public PerformanceChartPanel() {

        setLayout(new BorderLayout(10, 10));
        setBackground(BG_DARK);
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // ================= TOP: TITLE + CONTROLS =================
        JPanel topPanel = new JPanel(new BorderLayout(10, 8));
        topPanel.setBackground(BG_DARK);
        topPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        JLabel titleLabel = new JLabel("Performance Charts", SwingConstants.CENTER);
        titleLabel.setFont(loadPixelFont(22f));
        titleLabel.setForeground(ACCENT_PURP);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 8, 0));

        JPanel controlsRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 0));
        controlsRow.setBackground(BG_DARK);

        JLabel selectLabel = new JLabel("Select Game:");
        selectLabel.setFont(loadPixelFont(13f));
        selectLabel.setForeground(TEXT_DIM);

        gameSelector = new JComboBox<>();
        gameSelector.setFont(loadPixelFont(13f));
        gameSelector.setBackground(new Color(50, 50, 50));
        gameSelector.setForeground(TEXT_WHITE);
        gameSelector.setBorder(BorderFactory.createLineBorder(BORDER_COLOR, 1));
        gameSelector.setPreferredSize(new Dimension(180, 36));
        gameSelector.setRenderer(new DefaultListCellRenderer() {
            public Component getListCellRendererComponent(JList<?> list, Object value,
                                                          int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                setBackground(isSelected ? ACCENT_PURP : new Color(50, 50, 50));
                setForeground(TEXT_WHITE);
                setFont(loadPixelFont(13f));
                setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));
                return this;
            }
        });

        loadGames();

        JButton loadBtn = styledButton("▶  Show Chart", ACCENT_PURP);
        loadBtn.addActionListener(e -> loadChart());

        controlsRow.add(selectLabel);
        controlsRow.add(gameSelector);
        controlsRow.add(loadBtn);

        topPanel.add(titleLabel, BorderLayout.NORTH);
        topPanel.add(controlsRow, BorderLayout.CENTER);

        add(topPanel, BorderLayout.NORTH);

        // ================= CENTER: CHART AREA =================
        chartContainer = new JPanel(new BorderLayout());
        chartContainer.setBackground(BG_DARKER);
        chartContainer.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1),
                "  Algorithm Comparison  ",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                loadPixelFont(11f),
                TEXT_DIM
        ));

        // Placeholder before a game is selected
        JLabel placeholder = new JLabel("Select a game and press Show Chart", SwingConstants.CENTER);
        placeholder.setFont(loadPixelFont(14f));
        placeholder.setForeground(new Color(80, 80, 80));
        chartContainer.add(placeholder, BorderLayout.CENTER);

        add(chartContainer, BorderLayout.CENTER);
    }

    // ================= LOAD GAMES =================
    private void loadGames() {
        try {
            Connection conn = DBConnection.connect();
            String sql = "SELECT DISTINCT game_type FROM game_rounds";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                gameSelector.addItem(rs.getString("game_type"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ================= LOAD CHART =================
    private void loadChart() {

        String selectedGame = (String) gameSelector.getSelectedItem();
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        long total = 0;
        int count = 0;
        long min = Long.MAX_VALUE;
        long max = Long.MIN_VALUE;

        try {
            Connection conn = DBConnection.connect();

            String sql =
                    "SELECT a.algorithm_name, a.time_taken " +
                            "FROM algorithm_times a " +
                            "JOIN game_rounds g ON a.round_id = g.round_id " +
                            "WHERE g.game_type = ?";

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, selectedGame);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String algo = rs.getString("algorithm_name");
                long time = rs.getLong("time_taken");

                dataset.addValue(time, algo, selectedGame);

                total += time;
                count++;
                min = Math.min(min, time);
                max = Math.max(max, time);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        // ================= BUILD CHART =================
        JFreeChart chart = ChartFactory.createBarChart(
                "",
                "Game",
                "Time (ns)",
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );

        // ---- Dark theme the chart ----
        chart.setBackgroundPaint(BG_DARKER);
        chart.getLegend().setBackgroundPaint(BG_PANEL);
        chart.getLegend().setItemPaint(TEXT_WHITE);

        CategoryPlot plot = chart.getCategoryPlot();
        plot.setBackgroundPaint(BG_DARKER);
        plot.setDomainGridlinePaint(BORDER_COLOR);
        plot.setRangeGridlinePaint(BORDER_COLOR);
        plot.setOutlinePaint(BORDER_COLOR);

        CategoryAxis domainAxis = plot.getDomainAxis();
        domainAxis.setTickLabelPaint(TEXT_DIM);
        domainAxis.setLabelPaint(TEXT_DIM);
        domainAxis.setAxisLinePaint(BORDER_COLOR);
        domainAxis.setTickLabelFont(loadPixelFont(11f));
        domainAxis.setLabelFont(loadPixelFont(12f));

        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setTickLabelPaint(TEXT_DIM);
        rangeAxis.setLabelPaint(TEXT_DIM);
        rangeAxis.setAxisLinePaint(BORDER_COLOR);
        rangeAxis.setTickLabelFont(loadPixelFont(11f));
        rangeAxis.setLabelFont(loadPixelFont(12f));

        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setSeriesPaint(0, ACCENT_BLUE);
        renderer.setSeriesPaint(1, ACCENT_GREEN);
        renderer.setSeriesPaint(2, ACCENT_RED);
        renderer.setSeriesPaint(3, ACCENT_GOLD);
        renderer.setSeriesPaint(4, ACCENT_PURP);
        renderer.setBarPainter(new org.jfree.chart.renderer.category.StandardBarPainter());
        renderer.setShadowVisible(false);

        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setBackground(BG_DARKER);

        // ================= STATS BAR =================
        double avg = (count == 0) ? 0 : (double) total / count;

        JPanel statsRow = new JPanel(new GridLayout(1, 3, 10, 0));
        statsRow.setBackground(BG_PANEL);
        statsRow.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        statsRow.add(makeStatLabel("AVG", String.format("%.0f ns", avg), ACCENT_BLUE));
        statsRow.add(makeStatLabel("MIN", min == Long.MAX_VALUE ? "—" : min + " ns", ACCENT_GREEN));
        statsRow.add(makeStatLabel("MAX", max == Long.MIN_VALUE ? "—" : max + " ns", ACCENT_RED));

        // ================= ASSEMBLE =================
        chartContainer.removeAll();
        chartContainer.add(chartPanel, BorderLayout.CENTER);
        chartContainer.add(statsRow, BorderLayout.SOUTH);
        chartContainer.revalidate();
        chartContainer.repaint();
    }

    // ================= STAT LABEL =================
    private JPanel makeStatLabel(String title, String value, Color accent) {
        JPanel cell = new JPanel(new BorderLayout(0, 4));
        cell.setBackground(BG_PANEL);
        cell.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));

        JLabel titleLbl = new JLabel(title, SwingConstants.CENTER);
        titleLbl.setFont(loadPixelFont(11f));
        titleLbl.setForeground(accent);

        JLabel valueLbl = new JLabel(value, SwingConstants.CENTER);
        valueLbl.setFont(loadPixelFont(13f));
        valueLbl.setForeground(TEXT_WHITE);

        cell.add(titleLbl, BorderLayout.NORTH);
        cell.add(valueLbl, BorderLayout.CENTER);

        return cell;
    }

    // ================= BUTTON STYLE =================
    private JButton styledButton(String text, Color color) {
        JButton btn = new JButton(text);
        btn.setFont(loadPixelFont(13f));
        btn.setForeground(TEXT_WHITE);
        btn.setBackground(color);
        btn.setFocusPainted(false);
        btn.setOpaque(true);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createEmptyBorder(10, 18, 10, 18));

        Color hover = color.brighter();

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(hover);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(color);
            }
        });

        return btn;
    }

    // ================= FONT =================
    private Font loadPixelFont(float size) {
        try {
            Font font = Font.createFont(Font.TRUETYPE_FONT,
                    new java.io.File("src/fonts/Minecraft.ttf"));
            return font.deriveFont(size);
        } catch (Exception e) {
            return new Font("Monospaced", Font.BOLD, (int) size);
        }
    }
}