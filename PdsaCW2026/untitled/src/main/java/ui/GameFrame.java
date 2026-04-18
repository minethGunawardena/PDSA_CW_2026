package ui;

import ui.panels.*;

import javax.swing.*;
import java.awt.*;

public class GameFrame extends JFrame {

    public GameFrame(String gameName) {

        setTitle(gameName);
        setSize(900, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel panel = getPanel(gameName);

        add(panel, BorderLayout.CENTER);

        setVisible(true);
    }

    // ================= PANEL ROUTER =================
    private JPanel getPanel(String gameName) {

        switch (gameName) {

            case "Minimum Cost":
                return new MinimumCostPanel();

            case "Snake & Ladder":
                return new SnakeLadderPanel();

            case "Traffic Simulation":
                return new TrafficSimulationPanel();

            case "Knight's Tour":
                return new KnightTourPanel();

            case "16 Queens":
                // return new QueensPanel();
                return placeholder("16 Queens coming soon...");

            case "Performance Charts":
                return new ui.panels.PerformanceChartPanel();

            default:
                return placeholder("Unknown Game: " + gameName);
        }
    }

    // ================= FALLBACK UI =================
    private JPanel placeholder(String text) {

        JPanel p = new JPanel();
        p.setBackground(new Color(30, 30, 30));
        p.setLayout(new BorderLayout());

        JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Arial", Font.BOLD, 20));

        p.add(label, BorderLayout.CENTER);

        return p;
    }
}