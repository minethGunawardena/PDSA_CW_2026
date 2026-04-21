package ui.panels;

import database.DBConnection;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.SymbolAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;
import java.util.List;

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
    private static final Color ACCENT_CYAN  = new Color(26, 188, 156);
    private static final Color TEXT_WHITE   = Color.WHITE;
    private static final Color TEXT_DIM     = new Color(180, 180, 180);
    private static final Color BORDER_COLOR = new Color(60, 60, 60);

    private static final Color[] LINE_COLORS = {
            ACCENT_RED, ACCENT_GREEN, ACCENT_GOLD, ACCENT_BLUE, ACCENT_PURP, ACCENT_CYAN
    };


    private static final Set<String> MS_GAMES = new HashSet<>(Arrays.asList("SixteenQueens"));

    public PerformanceChartPanel() {
        setLayout(new BorderLayout(10, 10));
        setBackground(BG_DARK);
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

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
        gameSelector.setPreferredSize(new Dimension(200, 36));
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

        chartContainer = new JPanel(new BorderLayout());
        chartContainer.setBackground(BG_DARKER);
        chartContainer.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1),
                "  Algorithm Comparison  ",
                TitledBorder.LEFT, TitledBorder.TOP,
                loadPixelFont(11f), TEXT_DIM
        ));

        JLabel placeholder = new JLabel("Select a game and press Show Chart", SwingConstants.CENTER);
        placeholder.setFont(loadPixelFont(14f));
        placeholder.setForeground(new Color(80, 80, 80));
        chartContainer.add(placeholder, BorderLayout.CENTER);

        add(chartContainer, BorderLayout.CENTER);
    }

    private void loadGames() {
        try {
            Connection conn = DBConnection.connect();
            String sql = "SELECT DISTINCT game_type FROM game_rounds ORDER BY game_type";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                gameSelector.addItem(rs.getString("game_type"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadChart() {
        String selectedGame = (String) gameSelector.getSelectedItem();
        if (selectedGame == null) return;

        boolean isMs = MS_GAMES.contains(selectedGame);
        String timeUnit = isMs ? "ms" : "ns";

        Map<String, List<Long>> algoData = new LinkedHashMap<>();
        long totalSum   = 0;
        int  totalCount = 0;
        long globalMin  = Long.MAX_VALUE;
        long globalMax  = Long.MIN_VALUE;

        try {
            Connection conn = DBConnection.connect();
            String pkCol = detectPkColumn(conn);

            String sql =
                    "SELECT a.algorithm_name, a.time_taken " +
                            "FROM algorithm_times a " +
                            "JOIN game_rounds g ON a.round_id = g." + pkCol + " " +
                            "WHERE g.game_type = ? " +
                            "ORDER BY a.algorithm_name, g." + pkCol;

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, selectedGame);
            ResultSet rs = ps.executeQuery();

            boolean hasRows = false;
            while (rs.next()) {
                hasRows = true;
                String algo = rs.getString("algorithm_name");
                long   time = rs.getLong("time_taken");
                algoData.computeIfAbsent(algo, k -> new ArrayList<>()).add(time);
                totalSum += time;
                totalCount++;
                globalMin = Math.min(globalMin, time);
                globalMax = Math.max(globalMax, time);
            }

            if (!hasRows) { showNoData(selectedGame); return; }

        } catch (Exception e) {
            e.printStackTrace();
            showError("DB error: " + e.getMessage());
            return;
        }

        XYSeriesCollection dataset = new XYSeriesCollection();
        for (Map.Entry<String, List<Long>> entry : algoData.entrySet()) {
            XYSeries series = new XYSeries(entry.getKey());
            List<Long> times = entry.getValue();
            for (int i = 0; i < times.size(); i++) series.add(i + 1, times.get(i));
            dataset.addSeries(series);
        }

        int maxAttempts = algoData.values().stream().mapToInt(List::size).max().orElse(1);

        JFreeChart chart = ChartFactory.createXYLineChart(
                selectedGame + " — Algorithm Performance",
                "Attempt", "Time (" + timeUnit + ")",
                dataset, PlotOrientation.VERTICAL, true, true, false
        );

        chart.setBackgroundPaint(BG_DARKER);
        chart.getLegend().setBackgroundPaint(BG_PANEL);
        chart.getLegend().setItemPaint(TEXT_WHITE);
        chart.getLegend().setItemFont(loadPixelFont(11f));
        chart.getTitle().setPaint(TEXT_DIM);
        chart.getTitle().setFont(loadPixelFont(13f));

        XYPlot plot = chart.getXYPlot();
        plot.setBackgroundPaint(BG_DARKER);
        plot.setDomainGridlinePaint(BORDER_COLOR);
        plot.setRangeGridlinePaint(BORDER_COLOR);
        plot.setOutlinePaint(BORDER_COLOR);

        String[] attemptLabels = new String[maxAttempts + 1];
        attemptLabels[0] = "";
        for (int i = 1; i <= maxAttempts; i++) attemptLabels[i] = ordinal(i);

        SymbolAxis domainAxis = new SymbolAxis("Attempt", attemptLabels);
        domainAxis.setTickLabelPaint(TEXT_DIM);
        domainAxis.setLabelPaint(TEXT_DIM);
        domainAxis.setAxisLinePaint(BORDER_COLOR);
        domainAxis.setTickLabelFont(loadPixelFont(10f));
        domainAxis.setLabelFont(loadPixelFont(12f));
        domainAxis.setGridBandsVisible(false);
        domainAxis.setRange(0.5, maxAttempts + 0.5);
        plot.setDomainAxis(domainAxis);

        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setTickLabelPaint(TEXT_DIM);
        rangeAxis.setLabelPaint(TEXT_DIM);
        rangeAxis.setAxisLinePaint(BORDER_COLOR);
        rangeAxis.setTickLabelFont(loadPixelFont(10f));
        rangeAxis.setLabelFont(loadPixelFont(12f));

        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer(true, true);
        int idx = 0;
        for (String algoName : algoData.keySet()) {
            Color c = getAlgoColor(algoName, idx);
            renderer.setSeriesPaint(idx, c);
            renderer.setSeriesStroke(idx, new BasicStroke(2.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            renderer.setSeriesShape(idx, new Ellipse2D.Double(-5, -5, 10, 10));
            renderer.setSeriesShapesFilled(idx, true);
            idx++;
        }
        plot.setRenderer(renderer);

        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setBackground(BG_DARKER);
        chartPanel.setMouseWheelEnabled(true);

        double avg = (totalCount == 0) ? 0 : (double) totalSum / totalCount;
        JPanel statsRow = new JPanel(new GridLayout(1, 4, 10, 0));
        statsRow.setBackground(BG_PANEL);
        statsRow.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        statsRow.add(makeStatLabel("GAME", selectedGame, ACCENT_PURP));
        statsRow.add(makeStatLabel("AVG",  String.format("%.0f %s", avg, timeUnit), ACCENT_BLUE));
        statsRow.add(makeStatLabel("MIN",  globalMin == Long.MAX_VALUE ? "—" : globalMin + " " + timeUnit, ACCENT_GREEN));
        statsRow.add(makeStatLabel("MAX",  globalMax == Long.MIN_VALUE ? "—" : globalMax + " " + timeUnit, ACCENT_RED));

        chartContainer.removeAll();
        chartContainer.add(chartPanel, BorderLayout.CENTER);
        chartContainer.add(statsRow,   BorderLayout.SOUTH);
        chartContainer.revalidate();
        chartContainer.repaint();
    }

    private String detectPkColumn(Connection conn) {
        try {
            conn.prepareStatement("SELECT round_id FROM game_rounds LIMIT 1").executeQuery();
            return "round_id";
        } catch (Exception e) {
            return "id";
        }
    }

    private void showNoData(String game) {
        JLabel lbl = new JLabel("No data found for: " + game, SwingConstants.CENTER);
        lbl.setFont(loadPixelFont(13f)); lbl.setForeground(ACCENT_RED);
        chartContainer.removeAll(); chartContainer.add(lbl, BorderLayout.CENTER);
        chartContainer.revalidate(); chartContainer.repaint();
    }

    private void showError(String msg) {
        JLabel lbl = new JLabel(msg, SwingConstants.CENTER);
        lbl.setFont(loadPixelFont(12f)); lbl.setForeground(ACCENT_RED);
        chartContainer.removeAll(); chartContainer.add(lbl, BorderLayout.CENTER);
        chartContainer.revalidate(); chartContainer.repaint();
    }

    private String ordinal(int n) {
        if (n >= 11 && n <= 13) return n + "th";
        switch (n % 10) {
            case 1: return n + "st"; case 2: return n + "nd"; case 3: return n + "rd";
            default: return n + "th";
        }
    }

    private Color getAlgoColor(String name, int fallbackIndex) {
        switch (name) {
            case "BFS":        return ACCENT_BLUE;
            case "Dijkstra":   return ACCENT_GREEN;
            case "Hungarian":  return ACCENT_GOLD;
            case "Sequential": return ACCENT_PURP;
            case "Threaded":   return ACCENT_RED;
            case "Greedy":     return ACCENT_CYAN;
            default:           return LINE_COLORS[fallbackIndex % LINE_COLORS.length];
        }
    }

    private JPanel makeStatLabel(String title, String value, Color accent) {
        JPanel cell = new JPanel(new BorderLayout(0, 4));
        cell.setBackground(BG_PANEL);
        cell.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)));
        JLabel titleLbl = new JLabel(title, SwingConstants.CENTER);
        titleLbl.setFont(loadPixelFont(11f)); titleLbl.setForeground(accent);
        JLabel valueLbl = new JLabel(value, SwingConstants.CENTER);
        valueLbl.setFont(loadPixelFont(12f)); valueLbl.setForeground(TEXT_WHITE);
        cell.add(titleLbl, BorderLayout.NORTH); cell.add(valueLbl, BorderLayout.CENTER);
        return cell;
    }

    private JButton styledButton(String text, Color color) {
        JButton btn = new JButton(text);
        btn.setFont(loadPixelFont(13f)); btn.setForeground(TEXT_WHITE);
        btn.setBackground(color); btn.setFocusPainted(false);
        btn.setOpaque(true); btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createEmptyBorder(10, 18, 10, 18));
        Color hover = color.brighter();
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) { btn.setBackground(hover); }
            public void mouseExited(java.awt.event.MouseEvent evt)  { btn.setBackground(color); }
        });
        return btn;
    }

    private Font loadPixelFont(float size) {
        try {
            Font font = Font.createFont(Font.TRUETYPE_FONT, new java.io.File("src/fonts/Minecraft.ttf"));
            return font.deriveFont(size);
        } catch (Exception e) {
            return new Font("Monospaced", Font.BOLD, (int) size);
        }
    }
}