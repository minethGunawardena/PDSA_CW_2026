package ui.panels;

import algorithms.chess.KnightTourHeuristic;
import database.DBHelper;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.util.Arrays;
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

    // ================= THEME COLORS =================
    private static final Color BG_DARK      = new Color(30, 30, 30);
    private static final Color BG_DARKER    = new Color(20, 20, 20);
    private static final Color BG_PANEL     = new Color(40, 40, 40);
    private static final Color ACCENT_GOLD  = new Color(241, 196, 15);
    private static final Color ACCENT_BLUE  = new Color(52, 152, 219);
    private static final Color ACCENT_RED   = new Color(231, 76, 60);
    private static final Color TEXT_WHITE   = Color.WHITE;
    private static final Color TEXT_DIM     = new Color(180, 180, 180);
    private static final Color BORDER_COLOR = new Color(60, 60, 60);

    private static final Color CELL_LIGHT   = new Color(50, 50, 60);
    private static final Color CELL_DARK    = new Color(35, 35, 45);
    private static final Color CELL_VISITED = new Color(44, 80, 130);
    private static final Color CELL_KNIGHT  = new Color(231, 76, 60);

    public KnightTourPanel() {

        setLayout(new BorderLayout(10, 10));
        setBackground(BG_DARK);
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // ================= TOP: TITLE + CONTROLS =================
        JPanel topPanel = new JPanel(new BorderLayout(10, 8));
        topPanel.setBackground(BG_DARK);
        topPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        JLabel titleLabel = new JLabel("Knight's Tour", SwingConstants.CENTER);
        titleLabel.setFont(loadPixelFont(22f));
        titleLabel.setForeground(ACCENT_GOLD);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 8, 0));

        JPanel controlsRow = new JPanel(new GridLayout(1, 4, 8, 0));
        controlsRow.setBackground(BG_DARK);

        JLabel nameLabel = new JLabel("Player Name:");
        nameLabel.setFont(loadPixelFont(13f));
        nameLabel.setForeground(TEXT_DIM);

        nameField = new JTextField();
        nameField.setFont(loadPixelFont(13f));
        nameField.setBackground(new Color(50, 50, 50));
        nameField.setForeground(TEXT_WHITE);
        nameField.setCaretColor(ACCENT_GOLD);
        nameField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1),
                BorderFactory.createEmptyBorder(6, 8, 6, 8)
        ));

        JLabel sizeLabel = new JLabel("Board Size:");
        sizeLabel.setFont(loadPixelFont(13f));
        sizeLabel.setForeground(TEXT_DIM);

        sizeBox = new JComboBox<>(new Integer[]{8, 16});
        sizeBox.setFont(loadPixelFont(13f));
        sizeBox.setBackground(new Color(50, 50, 50));
        sizeBox.setForeground(TEXT_WHITE);
        sizeBox.setBorder(BorderFactory.createLineBorder(BORDER_COLOR, 1));
        sizeBox.setRenderer(new DefaultListCellRenderer() {
            public Component getListCellRendererComponent(JList<?> list, Object value,
                                                          int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                setBackground(isSelected ? ACCENT_GOLD : new Color(50, 50, 50));
                setForeground(isSelected ? BG_DARK : TEXT_WHITE);
                setFont(loadPixelFont(13f));
                return this;
            }
        });

        controlsRow.add(nameLabel);
        controlsRow.add(nameField);
        controlsRow.add(sizeLabel);
        controlsRow.add(sizeBox);

        topPanel.add(titleLabel, BorderLayout.NORTH);
        topPanel.add(controlsRow, BorderLayout.CENTER);

        add(topPanel, BorderLayout.NORTH);

        // ================= CENTER: BOARD =================
        boardPanel = new JPanel();
        boardPanel.setBackground(BG_DARKER);
        boardPanel.setOpaque(true);
        boardPanel.setBorder(BorderFactory.createLineBorder(BORDER_COLOR, 1));

        add(boardPanel, BorderLayout.CENTER);

        // ================= SOUTH: OUTPUT LOG =================
        output = new JTextArea(5, 30);
        output.setEditable(false);
        output.setFont(new Font("Monospaced", Font.PLAIN, 13));
        output.setBackground(BG_DARKER);
        output.setForeground(ACCENT_GOLD);
        output.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
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

        add(scroll, BorderLayout.SOUTH);

        // ================= WEST: BUTTONS =================
        JButton runBtn  = styledButton("▶  Run Tour",  ACCENT_BLUE);
        JButton animBtn = styledButton("⟳  Animate",   ACCENT_GOLD);

        JPanel btnPanel = new JPanel(new GridLayout(2, 1, 0, 12));
        btnPanel.setBackground(BG_DARK);
        btnPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 12));

        btnPanel.add(runBtn);
        btnPanel.add(animBtn);

        add(btnPanel, BorderLayout.WEST);

        runBtn.addActionListener(e -> runGame());
        animBtn.addActionListener(e -> startAnimation());

        startRound();
    }

    // ================= INIT =================
    private void startRound() {
        N = (int) sizeBox.getSelectedItem();
        board = new int[N][N];
        for (int[] row : board) Arrays.fill(row, -1); // ✔ FIX: init to -1

        currentRoundId = DBHelper.insertRound("KnightTour");

        output.setText("▶ Knight's Tour Ready (" + N + " x " + N + ")\n");
        output.append("  Select a size and press Run Tour or Animate.\n");
        output.append("────────────────────────────────────\n");

        drawBoard(-1);
    }

    // ================= RUN =================
    private void runGame() {
        try {
            String name = nameField.getText().trim();

            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Enter your player name first!",
                        "Missing Name", JOptionPane.WARNING_MESSAGE);
                return;
            }

            N = (int) sizeBox.getSelectedItem();
            board = new int[N][N];
            for (int[] row : board) Arrays.fill(row, -1); // ✔ FIX: init to -1

            int r = new Random().nextInt(N);
            int c = new Random().nextInt(N);

            output.setText("");
            output.append("▶ NEW ROUND\n");
            output.append("  Start : (" + r + ", " + c + ")\n");

            long t1 = System.nanoTime();
            boolean success = KnightTourHeuristic.solve(N, r, c, board);
            long t2 = System.nanoTime();

            DBHelper.insertTime(currentRoundId, "Heuristic", (t2 - t1));

            if (success) {
                output.append("  ✔ Full tour completed!\n");
            } else {
                output.append("  ✘ Tour incomplete.\n");
            }

            output.append("────────────────────────────────────\n");

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
        for (int[] row : board) Arrays.fill(row, -1); // ✔ FIX: init to -1

        int r = new Random().nextInt(N);
        int c = new Random().nextInt(N);

        boolean ok = KnightTourHeuristic.solve(N, r, c, board);

        if (!ok) {
            JOptionPane.showMessageDialog(this, "No full tour found — try again!",
                    "Tour Failed", JOptionPane.WARNING_MESSAGE);
            return;
        }

        output.setText("⟳ Animating Knight's Tour...\n");
        output.append("────────────────────────────────────\n");

        step = 0;
        animating = true;

        timer = new Timer(120, e -> {
            drawBoard(step);
            step++;
            if (step >= N * N) {
                timer.stop();
                animating = false;
                output.append("  ✔ Animation complete!\n");
            }
        });

        timer.start();
    }

    // ================= DRAW BOARD =================
    private void drawBoard(int highlight) {
        boardPanel.removeAll();
        boardPanel.setLayout(new GridLayout(N, N));

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {

                JLabel cell = new JLabel();
                int val = board[i][j];

                cell.setHorizontalAlignment(SwingConstants.CENTER);
                cell.setFont(loadPixelFont(N <= 8 ? 12f : 8f));
                cell.setOpaque(true);
                cell.setBorder(BorderFactory.createLineBorder(BG_DARK, 1));

                if (val == -1) {
                    // ✔ FIX: only -1 is unvisited, 0 is a valid first step
                    boolean light = (i + j) % 2 == 0;
                    cell.setBackground(light ? CELL_LIGHT : CELL_DARK);
                    cell.setForeground(TEXT_DIM);
                    cell.setText("");
                } else if (val == highlight) {
                    cell.setBackground(CELL_KNIGHT);
                    cell.setForeground(TEXT_WHITE);
                    cell.setText("♞");
                } else if (highlight == -1 || val <= highlight) {
                    cell.setBackground(CELL_VISITED);
                    cell.setForeground(TEXT_WHITE);
                    cell.setText(N <= 8 ? String.valueOf(val) : "");
                } else {
                    boolean light = (i + j) % 2 == 0;
                    cell.setBackground(light ? CELL_LIGHT : CELL_DARK);
                    cell.setForeground(TEXT_DIM);
                    cell.setText("");
                }

                boardPanel.add(cell);
            }
        }

        boardPanel.revalidate();
        boardPanel.repaint();
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
        btn.setBorder(BorderFactory.createEmptyBorder(12, 16, 12, 16));

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