package ui.panels;

import algorithms.minimumCost.GreedySolver;
import algorithms.minimumCost.HungarianSolver;
import database.DBHelper;
import utils.CostMatrixGenerator;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class MinimumCostPanel extends JPanel {

    // UI
    private JTextField nameField;
    private JTextArea output;

    private JButton option1, option2, option3;

    // Game state
    private int N;
    private int[][] costMatrix;
    private int correctAnswer;
    private int currentRoundId;

    public MinimumCostPanel() {

        setLayout(new BorderLayout());
        setBackground(new Color(245, 245, 245));

        // ================= TOP =================
        JPanel topPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        topPanel.setBackground(new Color(245, 245, 245));

        JLabel nameLabel = new JLabel("Player Name:");
        nameLabel.setFont(new Font("Arial", Font.BOLD, 14));

        nameField = new JTextField();
        nameField.setFont(new Font("Arial", Font.PLAIN, 14));

        topPanel.add(nameLabel);
        topPanel.add(nameField);

        add(topPanel, BorderLayout.NORTH);

        // ================= CENTER =================
        output = new JTextArea();
        output.setEditable(false);
        output.setFont(new Font("Consolas", Font.PLAIN, 14));
        output.setBackground(new Color(30, 30, 30));
        output.setForeground(new Color(0, 255, 120));
        output.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JScrollPane scroll = new JScrollPane(output);
        scroll.setBorder(BorderFactory.createTitledBorder("Game Log"));
        add(scroll, BorderLayout.CENTER);

        // ================= OPTIONS =================
        option1 = new JButton();
        option2 = new JButton();
        option3 = new JButton();

        styleButton(option1, new Color(52, 152, 219));
        styleButton(option2, new Color(46, 204, 113));
        styleButton(option3, new Color(155, 89, 182));

        JPanel optionsPanel = new JPanel(new GridLayout(3, 1, 10, 10));
        optionsPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        optionsPanel.setBackground(new Color(245, 245, 245));

        optionsPanel.add(option1);
        optionsPanel.add(option2);
        optionsPanel.add(option3);

        add(optionsPanel, BorderLayout.EAST);

        // ================= EVENTS =================
        option1.addActionListener(e -> handleAnswer(option1));
        option2.addActionListener(e -> handleAnswer(option2));
        option3.addActionListener(e -> handleAnswer(option3));

        // ================= START =================
        startNewRound();
    }

    // ================= ROUND =================
    private void startNewRound() {

        try {
            Random rand = new Random();

            N = rand.nextInt(51) + 50; // 50–100
            costMatrix = CostMatrixGenerator.generate(N);

            output.append("\n🧮 NEW ROUND STARTED\n");
            output.append("Tasks/Employees: " + N + "\n");

            currentRoundId = DBHelper.insertRound("MinimumCost");

            // ================= GREEDY =================
            long start1 = System.nanoTime();
            int greedyResult = GreedySolver.solve(costMatrix);
            long end1 = System.nanoTime();

            DBHelper.insertTime(currentRoundId, "Greedy", (end1 - start1));

            // ================= HUNGARIAN =================
            long start2 = System.nanoTime();
            int optimalResult = HungarianSolver.solve(copyMatrix(costMatrix));
            long end2 = System.nanoTime();

            DBHelper.insertTime(currentRoundId, "Hungarian", (end2 - start2));

            correctAnswer = optimalResult;

            DBHelper.insertSolution(currentRoundId, correctAnswer);

            output.append("Round ID: " + currentRoundId + "\n");
            output.append("Choose the minimum cost 👇\n");
            output.append("--------------------------------\n");

            generateOptions(greedyResult, optimalResult);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ================= OPTIONS =================
    private void generateOptions(int greedy, int optimal) {

        Set<Integer> set = new HashSet<>();
        set.add(optimal);
        set.add(greedy);

        Random rand = new Random();

        while (set.size() < 3) {
            int fake = optimal + rand.nextInt(200) - 100;
            if (fake > 0) set.add(fake);
        }

        List<Integer> options = new ArrayList<>(set);
        Collections.shuffle(options);

        option1.setText(String.valueOf(options.get(0)));
        option2.setText(String.valueOf(options.get(1)));
        option3.setText(String.valueOf(options.get(2)));
    }

    // ================= ANSWER =================
    private void handleAnswer(JButton btn) {

        try {
            String name = nameField.getText().trim();

            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Enter player name!");
                return;
            }

            int selected = Integer.parseInt(btn.getText());
            boolean isCorrect = (selected == correctAnswer);

            if (isCorrect) {
                output.append("✅ Correct!\n");
            } else {
                output.append("❌ Wrong! Correct = " + correctAnswer + "\n");
            }

            int playerId = DBHelper.getOrCreatePlayer(name);
            DBHelper.savePlayerAnswer(playerId, currentRoundId, selected, isCorrect);

            output.append("--------------------------------\n");

            startNewRound();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ================= STYLE =================
    private void styleButton(JButton btn, Color color) {
        btn.setFont(new Font("Arial", Font.BOLD, 14));
        btn.setForeground(Color.WHITE);
        btn.setBackground(color);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    }

    // ================= UTILITY =================
    private int[][] copyMatrix(int[][] original) {
        int[][] copy = new int[original.length][original.length];
        for (int i = 0; i < original.length; i++) {
            System.arraycopy(original[i], 0, copy[i], 0, original.length);
        }
        return copy;
    }
}