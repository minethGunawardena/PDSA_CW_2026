package ui.panels;

import algorithms.graph.BFSSolver;
import algorithms.graph.DijkstraSolver;
import database.DBHelper;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class SnakeLadderPanel extends JPanel {

    // UI
    private JTextField nameField;
    private JTextArea output;
    private JButton option1, option2, option3;

    // Game state
    private int N;
    private int[] board;
    private int correctAnswer;
    private int currentRoundId;

    public SnakeLadderPanel() {

        setLayout(new BorderLayout());
        setBackground(new Color(245, 245, 245));

        // ================= TOP =================
        JPanel topPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        topPanel.setBackground(new Color(245, 245, 245));

        JLabel nameLabel = new JLabel("Player Name:");
        nameLabel.setFont(loadPixelFont(14f));

        nameField = new JTextField();
        nameField.setFont(loadPixelFont(14f));

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

            N = rand.nextInt(7) + 6; // 6–12
            board = generateBoard(N);

            output.append("\n🎲 NEW ROUND STARTED\n");
            output.append("Board Size: " + N + " x " + N + "\n");

            currentRoundId = DBHelper.insertRound("Snake");

            // BFS
            long start1 = System.nanoTime();
            int bfsResult = BFSSolver.solve(board, N);
            long end1 = System.nanoTime();

            DBHelper.insertTime(currentRoundId, "BFS", (end1 - start1));

            // Dijkstra
            long start2 = System.nanoTime();
            int dijkstraResult = DijkstraSolver.solve(board, N);
            long end2 = System.nanoTime();

            DBHelper.insertTime(currentRoundId, "Dijkstra", (end2 - start2));

            correctAnswer = bfsResult;

            DBHelper.insertSolution(currentRoundId, correctAnswer);

            generateOptions();

            output.append("Round ID: " + currentRoundId + "\n");
            output.append("Click an option 👇\n");
            output.append("--------------------------------\n");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ================= MCQ OPTIONS =================
    private void generateOptions() {

        Random rand = new Random();

        Set<Integer> set = new HashSet<>();
        set.add(correctAnswer);

        while (set.size() < 3) {
            int fake = correctAnswer + rand.nextInt(5) - 2;
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


    // ================= BOARD =================
    private int[] generateBoard(int N) {

        int size = N * N;
        int[] board = new int[size + 1];
        Arrays.fill(board, -1);

        Random rand = new Random();

        int ladders = N - 2;
        int snakes = N - 2;

        for (int i = 0; i < ladders; i++) {
            int start = rand.nextInt(size - 1) + 1;
            int end = rand.nextInt(size - start) + start + 1;
            board[start] = end;
        }

        for (int i = 0; i < snakes; i++) {
            int start = rand.nextInt(size - 1) + 1;
            int end = rand.nextInt(start - 1) + 1;
            board[start] = end;
        }

        return board;
    }


    // ================= STYLE =================
    private void styleButton(JButton btn, Color color) {
        btn.setFont(new Font("Arial", Font.BOLD, 14));
        btn.setForeground(Color.WHITE);
        btn.setBackground(color);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    }
    private Font loadPixelFont(float size) {
        try {
            Font font = Font.createFont(Font.TRUETYPE_FONT,
                    new java.io.File("src/fonts/Minecraft.ttf"));
            return font.deriveFont(size);
        } catch (Exception e) {
            e.printStackTrace();
            return new Font("Monospaced", Font.BOLD, (int) size);
        }
    }


}