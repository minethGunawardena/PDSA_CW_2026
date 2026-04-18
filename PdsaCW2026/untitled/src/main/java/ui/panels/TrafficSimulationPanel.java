package ui.panels;

import algorithms.graph.EdmondsKarpSolver;
import algorithms.graph.FordFulkersonSolver;
import database.DBHelper;
import utils.TrafficGraphGenerator;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

public class TrafficSimulationPanel extends JPanel {

    private JTextField nameField;
    private JTextField answerField;
    private JTextArea output;

    private int currentRoundId;
    private int correctAnswer;

    private HashMap<String, HashMap<String, Integer>> graph;

    public TrafficSimulationPanel() {

        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // ================= TOP INPUT =================
        JPanel topPanel = new JPanel(new GridLayout(2, 2, 5, 5));
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        topPanel.add(new JLabel("Player Name:"));
        nameField = new JTextField();
        topPanel.add(nameField);

        topPanel.add(new JLabel("Max Flow (A → T):"));
        answerField = new JTextField();
        topPanel.add(answerField);

        add(topPanel, BorderLayout.NORTH);

        // ================= OUTPUT AREA =================
        output = new JTextArea();
        output.setEditable(false);
        output.setFont(new Font("Consolas", Font.PLAIN, 14));
        output.setBackground(Color.BLACK);
        output.setForeground(Color.GREEN);

        add(new JScrollPane(output), BorderLayout.CENTER);

        // ================= BUTTONS =================
        JButton submitBtn = new JButton("Submit Answer");
        JButton newRoundBtn = new JButton("New Round");

        JPanel bottom = new JPanel();
        bottom.add(submitBtn);
        bottom.add(newRoundBtn);

        add(bottom, BorderLayout.SOUTH);

        // ================= EVENTS =================
        submitBtn.addActionListener(e -> checkAnswer());
        newRoundBtn.addActionListener(e -> startNewRound());

        // start first round
        startNewRound();
    }

    // ================= START ROUND =================
    private void startNewRound() {

        try {
            output.setText("");

            graph = TrafficGraphGenerator.generate();

            currentRoundId = DBHelper.insertRound("Traffic");

            output.append("TRAFFIC SIMULATION GAME\n");
            output.append("Source: A → Sink: T\n\n");

            // SHOW GRAPH (OPTION 1)
            displayGraph(graph);

            // ================= BFS =================
            long start1 = System.nanoTime();
            int bfsFlow = EdmondsKarpSolver.maxFlow(cloneGraph(graph));
            long end1 = System.nanoTime();

            DBHelper.insertTime(currentRoundId, "EdmondsKarp", (end1 - start1));

            // ================= DFS =================
            long start2 = System.nanoTime();
            int dfsFlow = FordFulkersonSolver.maxFlow(cloneGraph(graph));
            long end2 = System.nanoTime();

            DBHelper.insertTime(currentRoundId, "FordFulkerson", (end2 - start2));

            // correct answer (use BFS result)
            correctAnswer = bfsFlow;

            DBHelper.insertSolution(currentRoundId, correctAnswer);

            output.append("\nEnter your guess for MAX FLOW:\n");
            output.append("--------------------------------\n");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ================= DISPLAY GRAPH =================
    private void displayGraph(HashMap<String, HashMap<String, Integer>> graph) {

        output.append("NETWORK GRAPH (CAPACITY MAP)\n\n");

        for (String from : graph.keySet()) {

            output.append(from + " → ");

            StringBuilder sb = new StringBuilder();

            for (String to : graph.get(from).keySet()) {
                sb.append(to)
                        .append("(")
                        .append(graph.get(from).get(to))
                        .append(") ");
            }

            output.append(sb.toString() + "\n");
        }

        output.append("\n--------------------------------\n\n");
    }

    // ================= CHECK ANSWER =================
    private void checkAnswer() {

        try {
            String name = nameField.getText().trim();
            int userAnswer = Integer.parseInt(answerField.getText().trim());

            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Enter player name!");
                return;
            }

            boolean isCorrect = (userAnswer == correctAnswer);

            if (isCorrect) {
                output.append("\nCORRECT ANSWER\n");
            } else {
                output.append("\nWRONG ANSWER\nCorrect = " + correctAnswer + "\n");
            }

            int playerId = DBHelper.getOrCreatePlayer(name);
            DBHelper.savePlayerAnswer(playerId, currentRoundId, userAnswer, isCorrect);

            output.append("--------------------------------\n");

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Enter a valid number!");
        }
    }

    // ================= CLONE GRAPH =================
    private HashMap<String, HashMap<String, Integer>> cloneGraph(
            HashMap<String, HashMap<String, Integer>> original) {

        HashMap<String, HashMap<String, Integer>> copy = new HashMap<>();

        for (String u : original.keySet()) {
            copy.put(u, new HashMap<>(original.get(u)));
        }

        return copy;
    }
}