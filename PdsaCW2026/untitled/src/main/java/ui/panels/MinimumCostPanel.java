package ui.panels;

import algorithms.minimumCost.GreedySolver;
import algorithms.minimumCost.HungarianSolver;
import database.DBHelper;
import utils.CostMatrixGenerator;

import javax.swing.*;
import javax.swing.border.*;
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

    // ================= THEME COLORS =================
    private static final Color BG_DARK      = new Color(30, 30, 30);
    private static final Color BG_DARKER    = new Color(20, 20, 20);
    private static final Color BG_PANEL     = new Color(40, 40, 40);
    private static final Color ACCENT_BLUE  = new Color(52, 152, 219);
    private static final Color ACCENT_GREEN = new Color(46, 204, 113);
    private static final Color ACCENT_PURP  = new Color(155, 89, 182);
    private static final Color TEXT_WHITE   = Color.WHITE;
    private static final Color TEXT_DIM     = new Color(180, 180, 180);
    private static final Color BORDER_COLOR = new Color(60, 60, 60);

    public MinimumCostPanel() {

        setLayout(new BorderLayout(10, 10));
        setBackground(BG_DARK);
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // ================= TOP: TITLE + NAME =================
        JPanel topPanel = new JPanel(new BorderLayout(10, 8));
        topPanel.setBackground(BG_DARK);
        topPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        JLabel titleLabel = new JLabel("Minimum Cost", SwingConstants.CENTER);
        titleLabel.setFont(loadPixelFont(22f));
        titleLabel.setForeground(ACCENT_BLUE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 8, 0));

        JPanel nameRow = new JPanel(new BorderLayout(8, 0));
        nameRow.setBackground(BG_DARK);

        JLabel nameLabel = new JLabel("Player Name:");
        nameLabel.setForeground(TEXT_DIM);
        nameLabel.setFont(loadPixelFont(13f));

        nameField = new JTextField();
        nameField.setFont(loadPixelFont(13f));
        nameField.setBackground(new Color(50, 50, 50));
        nameField.setForeground(TEXT_WHITE);
        nameField.setCaretColor(ACCENT_BLUE);
        nameField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1),
                BorderFactory.createEmptyBorder(6, 8, 6, 8)
        ));

        nameRow.add(nameLabel, BorderLayout.WEST);
        nameRow.add(nameField, BorderLayout.CENTER);

        topPanel.add(titleLabel, BorderLayout.NORTH);
        topPanel.add(nameRow, BorderLayout.CENTER);

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

        option1 = new JButton();
        option2 = new JButton();
        option3 = new JButton();

        styleButton(option1, ACCENT_BLUE);
        styleButton(option2, ACCENT_GREEN);
        styleButton(option3, ACCENT_PURP);

        JPanel optionsWrapper = new JPanel(new BorderLayout());
        optionsWrapper.setBackground(BG_DARK);

        JLabel chooseLabel = new JLabel("Min Cost?", SwingConstants.CENTER);
        chooseLabel.setFont(loadPixelFont(12f));
        chooseLabel.setForeground(TEXT_DIM);
        chooseLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        JPanel optionsPanel = new JPanel(new GridLayout(3, 1, 0, 12));
        optionsPanel.setBackground(BG_DARK);

        optionsPanel.add(option1);
        optionsPanel.add(option2);
        optionsPanel.add(option3);

        optionsWrapper.add(chooseLabel, BorderLayout.NORTH);
        optionsWrapper.add(optionsPanel, BorderLayout.CENTER);
        optionsWrapper.setBorder(BorderFactory.createEmptyBorder(0, 12, 0, 0));

        add(optionsWrapper, BorderLayout.EAST);

        // ================= EVENTS =================
        option1.addActionListener(e -> handleAnswer(option1));
        option2.addActionListener(e -> handleAnswer(option2));
        option3.addActionListener(e -> handleAnswer(option3));

        startNewRound();
    }

    private void startNewRound() {
        try {
            Random rand = new Random();

            N = rand.nextInt(51) + 50;
            costMatrix = CostMatrixGenerator.generate(N);

            output.append("\n▶ NEW ROUND STARTED\n");
            output.append("  Tasks/Employees : " + N + "\n");

            currentRoundId = DBHelper.insertRound("MinimumCost");

            long start1 = System.nanoTime();
            int greedyResult = GreedySolver.solve(costMatrix);
            long end1 = System.nanoTime();
            DBHelper.insertTime(currentRoundId, "Greedy", (end1 - start1));

            long start2 = System.nanoTime();
            int optimalResult = HungarianSolver.solve(copyMatrix(costMatrix));
            long end2 = System.nanoTime();
            DBHelper.insertTime(currentRoundId, "Hungarian", (end2 - start2));

            correctAnswer = optimalResult;
            DBHelper.insertSolution(currentRoundId, correctAnswer);

            output.append("  Round ID        : " + currentRoundId + "\n");
            output.append("  Pick the minimum assignment cost!\n");
            output.append("────────────────────────────────────\n");

            generateOptions(greedyResult, optimalResult);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

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

    private void handleAnswer(JButton btn) {
        try {
            String name = nameField.getText().trim();

            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Enter your player name first!",
                        "Missing Name", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int selected = Integer.parseInt(btn.getText());
            boolean isCorrect = (selected == correctAnswer);

            if (isCorrect) {
                output.append("  ✔ CORRECT! Well done, " + name + "!\n");
            } else {
                output.append("  ✘ WRONG!  Correct answer = " + correctAnswer + "\n");
            }

            int playerId = DBHelper.getOrCreatePlayer(name);
            DBHelper.savePlayerAnswer(playerId, currentRoundId, selected, isCorrect);

            output.append("────────────────────────────────────\n");

            startNewRound();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void styleButton(JButton btn, Color color) {
        btn.setFont(loadPixelFont(15f));
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
    }


    private int[][] copyMatrix(int[][] original) {
        int[][] copy = new int[original.length][original.length];
        for (int i = 0; i < original.length; i++) {
            System.arraycopy(original[i], 0, copy[i], 0, original.length);
        }
        return copy;
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