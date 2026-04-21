package ui.panels;

import algorithms.graph.EdmondsKarpSolver;
import algorithms.graph.FordFulkersonSolver;
import database.DBHelper;
import utils.TrafficGraphGenerator;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.util.HashMap;

public class TrafficSimulationPanel extends JPanel {

    private JTextField nameField;
    private JTextField answerField;
    private JTextArea output;

    private int currentRoundId;
    private int correctAnswer;

    private HashMap<String, HashMap<String, Integer>> graph;

    private static final Color BG_DARK      = new Color(30, 30, 30);
    private static final Color BG_DARKER    = new Color(20, 20, 20);
    private static final Color BG_PANEL     = new Color(40, 40, 40);
    private static final Color ACCENT_PURP  = new Color(155, 89, 182);
    private static final Color ACCENT_BLUE  = new Color(52, 152, 219);
    private static final Color ACCENT_GREEN = new Color(46, 204, 113);
    private static final Color ACCENT_RED   = new Color(231, 76, 60);
    private static final Color TEXT_WHITE   = Color.WHITE;
    private static final Color TEXT_DIM     = new Color(180, 180, 180);
    private static final Color BORDER_COLOR = new Color(60, 60, 60);

    public TrafficSimulationPanel() {

        setLayout(new BorderLayout(10, 10));
        setBackground(BG_DARK);
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // ================= TOP: TITLE + INPUTS =================
        JPanel topPanel = new JPanel(new BorderLayout(10, 8));
        topPanel.setBackground(BG_DARK);
        topPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        JLabel titleLabel = new JLabel("Traffic Simulation", SwingConstants.CENTER);
        titleLabel.setFont(loadPixelFont(22f));
        titleLabel.setForeground(ACCENT_PURP);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 8, 0));

        JPanel inputsRow = new JPanel(new GridLayout(1, 4, 8, 0));
        inputsRow.setBackground(BG_DARK);

        JLabel nameLabel = new JLabel("Player Name:");
        nameLabel.setFont(loadPixelFont(13f));
        nameLabel.setForeground(TEXT_DIM);

        nameField = new JTextField();
        nameField.setFont(loadPixelFont(13f));
        nameField.setBackground(new Color(50, 50, 50));
        nameField.setForeground(TEXT_WHITE);
        nameField.setCaretColor(ACCENT_PURP);
        nameField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1),
                BorderFactory.createEmptyBorder(6, 8, 6, 8)
        ));

        JLabel answerLabel = new JLabel("Max Flow (A→T):");
        answerLabel.setFont(loadPixelFont(13f));
        answerLabel.setForeground(TEXT_DIM);

        answerField = new JTextField();
        answerField.setFont(loadPixelFont(13f));
        answerField.setBackground(new Color(50, 50, 50));
        answerField.setForeground(TEXT_WHITE);
        answerField.setCaretColor(ACCENT_PURP);
        answerField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1),
                BorderFactory.createEmptyBorder(6, 8, 6, 8)
        ));

        inputsRow.add(nameLabel);
        inputsRow.add(nameField);
        inputsRow.add(answerLabel);
        inputsRow.add(answerField);

        topPanel.add(titleLabel, BorderLayout.NORTH);
        topPanel.add(inputsRow, BorderLayout.CENTER);

        add(topPanel, BorderLayout.NORTH);


        output = new JTextArea();
        output.setEditable(false);
        output.setFont(new Font("Monospaced", Font.PLAIN, 13));
        output.setBackground(BG_DARKER);
        output.setForeground(ACCENT_GREEN);
        output.setBorder(BorderFactory.createEmptyBorder(10, 12, 10, 12));
        output.setLineWrap(true);
        output.setWrapStyleWord(true);

        JScrollPane scroll = new JScrollPane(output);
        scroll.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1),
                "  Game Log  ",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                loadPixelFont(11f),
                TEXT_DIM
        ));
        scroll.getViewport().setBackground(BG_DARKER);
        scroll.getVerticalScrollBar().setBackground(BG_PANEL);

        add(scroll, BorderLayout.CENTER);


        JButton submitBtn   = styledButton("✔  Submit Answer", ACCENT_GREEN);
        JButton newRoundBtn = styledButton("▶  New Round",     ACCENT_PURP);

        JPanel bottom = new JPanel(new GridLayout(1, 2, 12, 0));
        bottom.setBackground(BG_DARK);
        bottom.setBorder(BorderFactory.createEmptyBorder(12, 0, 0, 0));

        bottom.add(submitBtn);
        bottom.add(newRoundBtn);

        add(bottom, BorderLayout.SOUTH);


        submitBtn.addActionListener(e -> checkAnswer());
        newRoundBtn.addActionListener(e -> startNewRound());

        startNewRound();
    }


    private void startNewRound() {
        try {
            output.setText("");

            graph = TrafficGraphGenerator.generate();

            currentRoundId = DBHelper.insertRound("Traffic");

            output.append("▶ NEW ROUND — TRAFFIC SIMULATION\n");
            output.append("  Source: A  →  Sink: T\n");
            output.append("────────────────────────────────────\n\n");

            displayGraph(graph);

            long start1 = System.nanoTime();
            int bfsFlow = EdmondsKarpSolver.maxFlow(cloneGraph(graph));
            long end1 = System.nanoTime();
            DBHelper.insertTime(currentRoundId, "EdmondsKarp", (end1 - start1));

            long start2 = System.nanoTime();
            int dfsFlow = FordFulkersonSolver.maxFlow(cloneGraph(graph));
            long end2 = System.nanoTime();
            DBHelper.insertTime(currentRoundId, "FordFulkerson", (end2 - start2));

            correctAnswer = bfsFlow;
            DBHelper.insertSolution(currentRoundId, correctAnswer);

            output.append("\n  Enter your guess for MAX FLOW above and press Submit!\n");
            output.append("────────────────────────────────────\n");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void displayGraph(HashMap<String, HashMap<String, Integer>> graph) {
        output.append("  NETWORK GRAPH (Capacity Map)\n\n");

        for (String from : graph.keySet()) {
            StringBuilder sb = new StringBuilder();
            sb.append("  ").append(from).append("  →  ");

            for (String to : graph.get(from).keySet()) {
                sb.append(to)
                        .append("(")
                        .append(graph.get(from).get(to))
                        .append(")  ");
            }

            output.append(sb.toString() + "\n");
        }

        output.append("\n────────────────────────────────────\n");
    }


    private void checkAnswer() {
        try {
            String name = nameField.getText().trim();
            int userAnswer = Integer.parseInt(answerField.getText().trim());

            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Enter your player name first!",
                        "Missing Name", JOptionPane.WARNING_MESSAGE);
                return;
            }

            boolean isCorrect = (userAnswer == correctAnswer);

            if (isCorrect) {
                output.append("\n  ✔ CORRECT! Well done, " + name + "!\n");
            } else {
                output.append("\n  ✘ WRONG!  Correct answer = " + correctAnswer + "\n");
            }

            int playerId = DBHelper.getOrCreatePlayer(name);
            DBHelper.savePlayerAnswer(playerId, currentRoundId, userAnswer, isCorrect);

            output.append("────────────────────────────────────\n");

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid number!",
                    "Invalid Input", JOptionPane.WARNING_MESSAGE);
        }
    }


    private HashMap<String, HashMap<String, Integer>> cloneGraph(
            HashMap<String, HashMap<String, Integer>> original) {

        HashMap<String, HashMap<String, Integer>> copy = new HashMap<>();
        for (String u : original.keySet()) {
            copy.put(u, new HashMap<>(original.get(u)));
        }
        return copy;
    }


    private JButton styledButton(String text, Color color) {
        JButton btn = new JButton(text);
        btn.setFont(loadPixelFont(14f));
        btn.setForeground(TEXT_WHITE);
        btn.setBackground(color);
        btn.setFocusPainted(false);
        btn.setOpaque(true);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createEmptyBorder(14, 20, 14, 20));

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