package ui.panels;

import algorithms.chess.KnightTourHeuristic;
import database.DBHelper;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class KnightTourPanel extends JPanel {

    private JTextField nameField;
    private JComboBox<Integer> sizeBox;
    private JTextArea output;
    private JPanel boardPanel;

    private int N;
    private int[][] board;

    private int currentRoundId;

    private Timer timer;
    private int step = 0;
    private boolean animating = false;

    public KnightTourPanel() {

        setLayout(new BorderLayout());
        setBackground(new Color(18, 18, 18));
        setOpaque(true);

        // ================= TOP =================
        JPanel top = new JPanel(new GridLayout(2, 2, 5, 5));
        top.setBackground(new Color(18, 18, 18));

        nameField = new JTextField();
        sizeBox = new JComboBox<>(new Integer[]{8, 16});

        styleField(nameField);
        styleField(sizeBox);

        top.add(styledLabel("Player Name:"));
        top.add(nameField);

        top.add(styledLabel("Board Size:"));
        top.add(sizeBox);

        add(top, BorderLayout.NORTH);

        // ================= BOARD =================
        boardPanel = new JPanel();
        boardPanel.setBackground(new Color(18, 18, 18));
        boardPanel.setOpaque(true);

        add(boardPanel, BorderLayout.CENTER);

        // ================= OUTPUT =================
        output = new JTextArea(5, 30);
        output.setEditable(false);
        styleOutput(output);

        JScrollPane scroll = new JScrollPane(output);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(new Color(25, 25, 25));

        add(scroll, BorderLayout.SOUTH);

        // ================= BUTTONS =================
        JButton runBtn = styledButton("Run Tour");
        JButton animBtn = styledButton("Animate");

        JPanel btnPanel = new JPanel();
        btnPanel.setBackground(new Color(18, 18, 18));

        btnPanel.add(runBtn);
        btnPanel.add(animBtn);

        add(btnPanel, BorderLayout.WEST);

        runBtn.addActionListener(e -> runGame());
        animBtn.addActionListener(e -> startAnimation());

        startRound();
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

    // ================= STYLE HELPERS =================
    private void styleField(JComponent c) {
        c.setFont(loadPixelFont(13f));
        c.setBackground(new Color(40, 40, 40));
        c.setForeground(Color.WHITE);
        c.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
    }

    private void styleOutput(JTextArea ta) {
        ta.setFont(loadPixelFont(13f));
        ta.setBackground(new Color(25, 25, 25));
        ta.setForeground(Color.WHITE);
    }

    private JButton styledButton(String text) {
        JButton b = new JButton(text);
        b.setFont(loadPixelFont(13f));
        b.setBackground(new Color(60, 60, 60));
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        return b;
    }

    // ⭐ FIXED LABEL METHOD
    private JLabel styledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(loadPixelFont(13f));
        label.setForeground(Color.WHITE);
        return label;
    }

    // ================= INIT =================
    private void startRound() {

        N = (int) sizeBox.getSelectedItem();
        board = new int[N][N];

        currentRoundId = DBHelper.insertRound("KnightTour");

        output.setText("Knight Tour Ready (" + N + "x" + N + ")\n");

        drawBoard(-1);
    }

    // ================= RUN =================
    private void runGame() {

        try {
            String name = nameField.getText().trim();

            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Enter name!");
                return;
            }

            N = (int) sizeBox.getSelectedItem();
            board = new int[N][N];

            int r = new Random().nextInt(N);
            int c = new Random().nextInt(N);

            output.setText("");
            output.append("Start: (" + r + "," + c + ")\n\n");

            long t1 = System.nanoTime();

            boolean success = KnightTourHeuristic.solve(N, r, c, board);

            long t2 = System.nanoTime();

            DBHelper.insertTime(currentRoundId, "Heuristic", (t2 - t1));

            output.append("Result: " + success + "\n");

            int playerId = DBHelper.getOrCreatePlayer(name);
            DBHelper.savePlayerAnswer(playerId, currentRoundId, 1, success);

            drawBoard(-1);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ================= ANIMATION =================
    private void startAnimation() {

        if (animating) return;

        N = (int) sizeBox.getSelectedItem();
        board = new int[N][N];

        int r = new Random().nextInt(N);
        int c = new Random().nextInt(N);

        boolean ok = KnightTourHeuristic.solve(N, r, c, board);

        if (!ok) {
            JOptionPane.showMessageDialog(this, "No full tour found!");
            return;
        }

        output.setText("Animating Knight Tour...\n");

        step = 0;
        animating = true;

        timer = new Timer(120, e -> {

            drawBoard(step);
            step++;

            if (step >= N * N) {
                timer.stop();
                animating = false;
            }
        });

        timer.start();
    }

    // ================= DRAW =================
    private void drawBoard(int highlight) {

        boardPanel.removeAll();
        boardPanel.setLayout(new GridLayout(N, N));

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {

                JLabel cell = new JLabel();

                int val = board[i][j];

                cell.setHorizontalAlignment(SwingConstants.CENTER);
                cell.setFont(loadPixelFont(12f));
                cell.setForeground(Color.WHITE);
                cell.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));

                if (val == -1) {
                    cell.setText("");
                    cell.setOpaque(true);
                    cell.setBackground(new Color(18, 18, 18));
                }
                else if (val == highlight) {
                    cell.setText("♞");
                    cell.setOpaque(true);
                    cell.setBackground(new Color(255, 60, 60));
                }
                else {
                    cell.setText(String.valueOf(val));
                    cell.setOpaque(true);
                    cell.setBackground(new Color(50, 80, 120));
                }

                boardPanel.add(cell);
            }
        }

        boardPanel.revalidate();
        boardPanel.repaint();
    }
}