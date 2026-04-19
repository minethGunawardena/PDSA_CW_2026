package ui;

import audio.MusicPlayer;
import ui.panels.PerformanceChartPanel;

import javax.swing.*;
import java.awt.*;

public class MainMenu extends JFrame {

    private final String musicPath = "src/audio/background.wav";
    private boolean musicOn = true;

    private JButton musicBtn;

    public MainMenu() {

        setTitle("Algorithm Game System");
        setSize(600, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(30, 30, 30));

        // ================= TITLE =================
        JLabel title = new JLabel("Algorithm Game System", SwingConstants.CENTER);
        title.setFont(loadPixelFont(26f));
        title.setForeground(Color.WHITE);
        title.setBorder(BorderFactory.createEmptyBorder(20, 10, 10, 10));

        mainPanel.add(title, BorderLayout.NORTH);

        // ================= BUTTON PANEL =================
        JPanel buttonPanel = new JPanel(new GridLayout(8, 1, 12, 12));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 60, 20, 60));
        buttonPanel.setBackground(new Color(30, 30, 30));

        // Game buttons — these open GameFrame
        buttonPanel.add(createGameButton("Minimum Cost", new Color(52, 152, 219)));
        buttonPanel.add(createGameButton("Snake & Ladder", new Color(46, 204, 113)));
        buttonPanel.add(createGameButton("Traffic Simulation", new Color(155, 89, 182)));
        buttonPanel.add(createGameButton("Knight's Tour", new Color(241, 196, 15)));
        buttonPanel.add(createGameButton("16 Queens", new Color(231, 76, 60)));

        // Non-game buttons — custom actions only
        JButton chartBtn = createPlainButton("Performance Charts", new Color(117, 60, 231));
        chartBtn.addActionListener(e -> openCharts());
        buttonPanel.add(chartBtn);

        JButton aboutBtn = createPlainButton("About", new Color(80, 80, 80));
        aboutBtn.addActionListener(e -> openAbout());
        buttonPanel.add(aboutBtn);

        musicBtn = createPlainButton("Music: ON", new Color(60, 60, 60));
        musicBtn.addActionListener(e -> toggleMusic());
        buttonPanel.add(musicBtn);

        mainPanel.add(buttonPanel, BorderLayout.CENTER);

        // ================= START MUSIC =================
        MusicPlayer.playLoop(musicPath);

        add(mainPanel);
        setVisible(true);
    }

    // ================= GAME BUTTON (opens GameFrame) =================
    private JButton createGameButton(String text, Color color) {
        JButton btn = createPlainButton(text, color);
        btn.addActionListener(e -> new GameFrame(text));
        return btn;
    }

    // ================= PLAIN BUTTON (no default action) =================
    private JButton createPlainButton(String text, Color color) {
        JButton btn = new JButton(text);

        btn.setFont(loadPixelFont(18f));
        btn.setForeground(Color.WHITE);
        btn.setBackground(color);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        Color hover = color.brighter();
        Color original = color;

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(hover);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(original);
            }
        });

        return btn;
    }

    // ================= MUSIC TOGGLE =================
    private void toggleMusic() {
        musicOn = !musicOn;
        MusicPlayer.toggle(musicPath);
        musicBtn.setText(musicOn ? "Music: ON" : "Music: OFF");
    }

    // ================= ABOUT =================
    private void openAbout() {
        JDialog dialog = new JDialog(this, "About", true);
        dialog.setSize(520, 520);
        dialog.setLocationRelativeTo(this);
        dialog.setResizable(false);

        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setBackground(new Color(30, 30, 30));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel titleLabel = new JLabel("About This Project", SwingConstants.CENTER);
        titleLabel.setFont(loadPixelFont(20f));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        JTextArea text = new JTextArea(
                "Algorithm Game System\n\n" +
                        "Assignment Project\n" +
                        "Advanced Algorithm Visualization Games\n\n" +
                        "Features:\n" +
                        "- Multiple Algorithm-Based Mini Games\n" +
                        "- MySQL Database Integration\n" +
                        "- Performance Analysis Charts\n" +
                        "- Real-time Algorithm Execution Tracking\n\n" +
                        "--------------------------------------\n" +
                        "Batch: COBSCCOMP25.2P\n\n" +
                        "Group Members:\n" +
                        "COBSCCOMP25.2P -026 - W.O.R. Vitharana\n" +
                        "COBSCCOMP25.2P -029 - K. Lahiru Fernando\n" +
                        "COBSCCOMP25.2P -042 - K M K S D Ketimingama\n" +
                        "COBSCCOMP25.2P -062 - A W W A M D Gunawardena\n" +
                        "COBSCCOMP25.2P -086 - G A P S Chamuditha\n" +
                        "COBSCCOMP25.2P -136 - W.M.T.T. Wijesundara\n\n" +
                        "--------------------------------------\n" +
                        "Project Note:\n" +
                        "This system demonstrates graph algorithms,\n" +
                        "pathfinding techniques, and optimization\n" +
                        "strategies using interactive game simulations.\n\n" +
                        "Built using Java Swing + MySQL"
        );

        text.setEditable(false);
        text.setBackground(new Color(30, 30, 30));
        text.setForeground(Color.LIGHT_GRAY);
        text.setFont(new Font("Monospaced", Font.PLAIN, 12));
        text.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        JScrollPane scrollPane = new JScrollPane(text);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(60, 60, 60)));
        scrollPane.getViewport().setBackground(new Color(30, 30, 30));
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        text.setCaretPosition(0); // ensures scroll starts at the top

        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        dialog.setContentPane(panel);
        dialog.setVisible(true);
    }

    // ================= CHARTS =================
    private void openCharts() {
        JFrame frame = new JFrame("Performance Charts");
        frame.setSize(800, 500);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        frame.setContentPane(new PerformanceChartPanel());
        frame.setVisible(true);
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