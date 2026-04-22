package ui.panels;

import algorithms.chess.KnightTourDivideConquer;
import algorithms.chess.KnightTourHeuristic;
import database.DBHelper;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;

public class KnightTourPanel extends JPanel {

    private static final Logger LOG = Logger.getLogger(KnightTourPanel.class.getName());

    private final JTextField nameField;
    private final JComboBox<Integer> sizeBox;
    private final JComboBox<String>  algoBox;
    private final JTextArea          output;
    private final JPanel             boardPanel;

    private final JTextField guessStartRow;
    private final JTextField guessStartCol;
    private final JTextField guessEndRow;
    private final JTextField guessEndCol;
    private final JButton    guessBtn;
    private final JPanel     guessPanel;

    private int     N;
    private int[][] board;
    private int     currentRoundId;
    private int     currentPlayerId = -1;
    private Timer   timer;
    private int     step = 0;
    private boolean animating = false;

    private boolean waitingForGuess = false;
    private int answerStartR = -1;
    private int answerStartC = -1;
    private int answerEndR   = -1;
    private int answerEndC   = -1;

    // ── Theme Colors (matching QueensPanel) ─────────────────────────────────────────
    private static final Color BG_DARK      = new Color(30,  30,  30);
    private static final Color BG_DARKER    = new Color(20,  20,  20);
    private static final Color BG_PANEL     = new Color(40,  40,  40);
    private static final Color ACCENT_GOLD  = new Color(241, 196, 15);
    private static final Color ACCENT_BLUE  = new Color(52,  152, 219);
    private static final Color ACCENT_RED   = new Color(231, 76,  60);
    private static final Color ACCENT_GREEN = new Color(39,  174, 96);
    private static final Color TEXT_WHITE   = Color.WHITE;
    private static final Color TEXT_DIM     = new Color(180, 180, 180);
    private static final Color BORDER_COLOR = new Color(60,  60,  60);
    private static final Color CELL_LIGHT   = new Color(50,  50,  60);
    private static final Color CELL_DARK    = new Color(35,  35,  45);
    private static final Color CELL_VISITED = new Color(44,  80,  130);
    private static final Color CELL_KNIGHT  = new Color(231, 76,  60);
    private static final Color CELL_CORRECT = new Color(39,  174, 96);
    private static final Color CELL_WRONG   = new Color(192, 57,  43);

    public KnightTourPanel() {

        setLayout(new BorderLayout(10,10));
        setBackground(BG_DARK);
        setBorder(BorderFactory.createEmptyBorder(15,15,15,15));

        // ── Top Panel with Title ──
        JPanel topContainer = new JPanel(new BorderLayout(10, 8));
        topContainer.setBackground(BG_DARK);
        topContainer.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        JLabel titleLabel = new JLabel("⚔ Knight's Tour ⚔", SwingConstants.CENTER);
        titleLabel.setFont(loadPixelFont(20f));
        titleLabel.setForeground(ACCENT_GOLD);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 8, 0));

        // ── Controls Panel (styled like QueensPanel) ──
        JPanel controlsRow = new JPanel(new GridLayout(1, 6, 8, 0));
        controlsRow.setBackground(BG_DARK);

        JLabel nameLabel = new JLabel("Player:");
        nameLabel.setFont(loadPixelFont(12f));
        nameLabel.setForeground(TEXT_DIM);

        nameField = new JTextField();
        nameField.setFont(loadPixelFont(13f));
        nameField.setBackground(new Color(50, 50, 50));
        nameField.setForeground(TEXT_WHITE);
        nameField.setCaretColor(ACCENT_GOLD);
        nameField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1),
                BorderFactory.createEmptyBorder(6, 8, 6, 8)));

        JLabel sizeLabel = new JLabel("Size:");
        sizeLabel.setFont(loadPixelFont(12f));
        sizeLabel.setForeground(TEXT_DIM);

        sizeBox = new JComboBox<>(new Integer[]{8, 16});
        sizeBox.setFont(loadPixelFont(13f));
        sizeBox.setBackground(new Color(50, 50, 50));
        sizeBox.setForeground(TEXT_WHITE);
        sizeBox.setBorder(BorderFactory.createLineBorder(BORDER_COLOR, 1));

        JLabel algoLabel = new JLabel("Algo:");
        algoLabel.setFont(loadPixelFont(12f));
        algoLabel.setForeground(TEXT_DIM);

        algoBox = new JComboBox<>(new String[]{"Both", "Heuristic", "Divide & Conquer"});
        algoBox.setFont(loadPixelFont(13f));
        algoBox.setBackground(new Color(50, 50, 50));
        algoBox.setForeground(TEXT_WHITE);
        algoBox.setBorder(BorderFactory.createLineBorder(BORDER_COLOR, 1));

        controlsRow.add(nameLabel);
        controlsRow.add(nameField);
        controlsRow.add(sizeLabel);
        controlsRow.add(sizeBox);
        controlsRow.add(algoLabel);
        controlsRow.add(algoBox);

        // ── Guess Panel (styled) ──
        guessStartRow = new JTextField(3);
        guessStartCol = new JTextField(3);
        guessEndRow   = new JTextField(3);
        guessEndCol   = new JTextField(3);

        Font guessFont = loadPixelFont(13f);
        Color guessBg = new Color(50, 50, 50);

        for (JTextField tf : new JTextField[]{guessStartRow, guessStartCol, guessEndRow, guessEndCol}) {
            tf.setFont(guessFont);
            tf.setBackground(guessBg);
            tf.setForeground(TEXT_WHITE);
            tf.setCaretColor(ACCENT_GOLD);
            tf.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(BORDER_COLOR, 1),
                    BorderFactory.createEmptyBorder(6, 8, 6, 8)));
            tf.setHorizontalAlignment(JTextField.CENTER);
        }

        guessBtn = styledButton("✔ Submit Guess", ACCENT_GREEN);
        guessBtn.addActionListener(e -> submitGuess());

        guessPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 5));
        guessPanel.setBackground(BG_DARK);
        guessPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(ACCENT_GOLD, 1),
                "  Guess Start & End Positions  ",
                TitledBorder.CENTER, TitledBorder.TOP,
                loadPixelFont(11f), ACCENT_GOLD));

        JLabel startLabel = new JLabel("START →");
        startLabel.setFont(loadPixelFont(11f));
        startLabel.setForeground(ACCENT_BLUE);
        
        JLabel endLabel = new JLabel("END →");
        endLabel.setFont(loadPixelFont(11f));
        endLabel.setForeground(ACCENT_RED);

        guessPanel.add(startLabel);
        guessPanel.add(new JLabel("R:"));
        guessPanel.add(guessStartRow);
        guessPanel.add(new JLabel("C:"));
        guessPanel.add(guessStartCol);
        guessPanel.add(Box.createHorizontalStrut(15));
        guessPanel.add(endLabel);
        guessPanel.add(new JLabel("R:"));
        guessPanel.add(guessEndRow);
        guessPanel.add(new JLabel("C:"));
        guessPanel.add(guessEndCol);
        guessPanel.add(guessBtn);
        guessPanel.setVisible(false);

        topContainer.add(titleLabel, BorderLayout.NORTH);
        topContainer.add(controlsRow, BorderLayout.CENTER);
        topContainer.add(guessPanel, BorderLayout.SOUTH);

        add(topContainer, BorderLayout.NORTH);

        // ── Board Panel (centered with border) ──
        boardPanel = new JPanel();
        boardPanel.setBackground(BG_DARKER);
        boardPanel.setBorder(BorderFactory.createLineBorder(BORDER_COLOR, 1));
        add(boardPanel, BorderLayout.CENTER);

        // ── Output Area (styled like QueensPanel) ──
        output = new JTextArea(5, 30);
        output.setEditable(false);
        output.setFont(new Font("Monospaced", Font.PLAIN, 12));
        output.setBackground(BG_DARKER);
        output.setForeground(ACCENT_GOLD);
        output.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));
        output.setLineWrap(true);
        output.setWrapStyleWord(true);

        JScrollPane outputScroll = new JScrollPane(output);
        outputScroll.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1),
                "  Game Log  ",
                TitledBorder.LEFT, TitledBorder.TOP,
                loadPixelFont(11f), TEXT_DIM));
        outputScroll.getViewport().setBackground(BG_DARKER);
        outputScroll.getVerticalScrollBar().setBackground(BG_PANEL);

        // ── Run Button (styled) ──
        JButton runBtn = styledButton("▶ Start Knight's Tour", ACCENT_BLUE);
        runBtn.addActionListener(e -> runGame());

        JPanel southPanel = new JPanel(new BorderLayout(8, 8));
        southPanel.setBackground(BG_DARK);
        southPanel.add(outputScroll, BorderLayout.CENTER);
        
        JPanel runPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        runPanel.setBackground(BG_DARK);
        runPanel.add(runBtn);
        southPanel.add(runPanel, BorderLayout.SOUTH);

        add(southPanel, BorderLayout.SOUTH);

        startRound();
    }

    private void startRound() {
        N = (Integer) sizeBox.getSelectedItem();
        board = new int[N][N];
        for (int[] r : board) Arrays.fill(r, -1);
        drawBoard();
    }

    private void runGame() {

        String name = nameField.getText().trim();
        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter your name!",
                    "Missing Name", JOptionPane.WARNING_MESSAGE);
            return;
        }

        currentPlayerId = DBHelper.getOrCreatePlayer(name);
        currentRoundId  = DBHelper.insertRound("KnightTour");

        N = (Integer) sizeBox.getSelectedItem();
        board = new int[N][N];
        for (int[] r : board) Arrays.fill(r, -1);

        int startR = new Random().nextInt(N);
        int startC = new Random().nextInt(N);

        output.setText("");
        output.append("┌─────────────────────────────────────────┐\n");
        output.append("│   Starting Knight's Tour                │\n");
        output.append("│   Board Size : " + N + "×" + N + String.format("%27s", "│\n"));
        output.append("│   Start Pos  : (" + startR + "," + startC + ")" + String.format("%28s", "│\n"));
        output.append("└─────────────────────────────────────────┘\n\n");

        boolean ok = algoBox.getSelectedItem().toString().equals("Divide & Conquer")
                ? KnightTourDivideConquer.solve(N, startR, startC, board)
                : KnightTourHeuristic.solve(N, startR, startC, board);

        if (!ok) {
            output.append("❌ Failed to find a solution!\n");
            return;
        }

        answerStartR = startR;
        answerStartC = startC;

        int max = -1;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (board[i][j] > max) {
                    max = board[i][j];
                    answerEndR = i;
                    answerEndC = j;
                }
            }
        }

        output.append("✓ Solution found!\n");
        output.append("  Start: (" + answerStartR + "," + answerStartC + ")\n");
        output.append("  End  : (" + answerEndR + "," + answerEndC + ")\n\n");
        output.append("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n");
        output.append("Now guess the START and END positions!\n");

        waitingForGuess = true;
        guessPanel.setVisible(true);

        drawHidden();
    }

    private void submitGuess() {

        try {
            int sR = Integer.parseInt(guessStartRow.getText().trim());
            int sC = Integer.parseInt(guessStartCol.getText().trim());
            int eR = Integer.parseInt(guessEndRow.getText().trim());
            int eC = Integer.parseInt(guessEndCol.getText().trim());

            if (sR < 0 || sR >= N || sC < 0 || sC >= N || eR < 0 || eR >= N || eC < 0 || eC >= N) {
                JOptionPane.showMessageDialog(this,
                        "Coordinates must be between 0 and " + (N - 1),
                        "Invalid Input", JOptionPane.WARNING_MESSAGE);
                return;
            }

            boolean correctStart = (sR == answerStartR && sC == answerStartC);
            boolean correctEnd   = (eR == answerEndR && eC == answerEndC);

            int encodedStart = sR * N + sC;
            int encodedEnd   = eR * N + eC;

            DBHelper.savePlayerAnswer(currentPlayerId, currentRoundId, encodedStart, correctStart);
            DBHelper.savePlayerAnswer(currentPlayerId, currentRoundId, encodedEnd, correctEnd);

            output.append("\n┌───────────── RESULTS ─────────────┐\n");
            output.append("│ Your Guess:                      │\n");
            output.append("│   Start (" + sR + "," + sC + ") : " + (correctStart ? "✓ CORRECT" : "✗ WRONG") + String.format("%16s", "│\n"));
            output.append("│   End   (" + eR + "," + eC + ") : " + (correctEnd ? "✓ CORRECT" : "✗ WRONG") + String.format("%16s", "│\n"));
            output.append("└───────────────────────────────────┘\n");

            if (correctStart && correctEnd) {
                output.append("\n🎉 PERFECT! You guessed both correctly! 🎉\n");
            } else if (correctStart || correctEnd) {
                output.append("\n👍 Good! You got one of them right!\n");
            } else {
                output.append("\n💀 Better luck next time!\n");
            }

            waitingForGuess = false;
            guessPanel.setVisible(false);

            drawResult(sR, sC, eR, eC, correctStart, correctEnd);

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "Please enter valid numbers for coordinates!",
                    "Invalid Input", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void drawBoard() {
        boardPanel.removeAll();
        boardPanel.setLayout(new GridLayout(N, N));

        int cellSize = Math.min(480 / N, 40);
        boardPanel.setPreferredSize(new Dimension(N * cellSize, N * cellSize));

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                JPanel cell = createStyledCell();
                boolean light = (i + j) % 2 == 0;
                cell.setBackground(light ? CELL_LIGHT : CELL_DARK);
                
                if (board[i][j] >= 0) {
                    JLabel label = new JLabel(String.valueOf(board[i][j]), SwingConstants.CENTER);
                    label.setFont(loadPixelFont(Math.min(14f, 36f / N)));
                    label.setForeground(TEXT_WHITE);
                    cell.setBackground(CELL_VISITED);
                    cell.setLayout(new BorderLayout());
                    cell.add(label);
                }
                
                boardPanel.add(cell);
            }
        }
        revalidate();
        repaint();
    }

    private void drawHidden() {
        boardPanel.removeAll();
        boardPanel.setLayout(new GridLayout(N, N));

        int cellSize = Math.min(480 / N, 40);
        boardPanel.setPreferredSize(new Dimension(N * cellSize, N * cellSize));

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                JPanel cell = createStyledCell();
                boolean light = (i + j) % 2 == 0;
                cell.setBackground(light ? CELL_LIGHT : CELL_DARK);
                
                JLabel label = new JLabel("?", SwingConstants.CENTER);
                label.setFont(loadPixelFont(Math.min(16f, 38f / N)));
                label.setForeground(TEXT_DIM);
                cell.setLayout(new BorderLayout());
                cell.add(label);
                
                boardPanel.add(cell);
            }
        }
        revalidate();
        repaint();
    }

    private void drawResult(int sr, int sc, int er, int ec, boolean cs, boolean ce) {
        boardPanel.removeAll();
        boardPanel.setLayout(new GridLayout(N, N));

        int cellSize = Math.min(480 / N, 40);
        boardPanel.setPreferredSize(new Dimension(N * cellSize, N * cellSize));

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                JPanel cell = createStyledCell();
                boolean light = (i + j) % 2 == 0;
                
                JLabel label = new JLabel("", SwingConstants.CENTER);
                label.setFont(loadPixelFont(Math.min(16f, 38f / N)));

                if (i == sr && j == sc) {
                    cell.setBackground(cs ? CELL_CORRECT : CELL_WRONG);
                    label.setText(cs ? "✓" : "✗");
                    label.setForeground(TEXT_WHITE);
                } else if (i == er && j == ec) {
                    cell.setBackground(ce ? CELL_CORRECT : CELL_WRONG);
                    label.setText(ce ? "✓" : "✗");
                    label.setForeground(TEXT_WHITE);
                } else if (i == answerStartR && j == answerStartC) {
                    cell.setBackground(ACCENT_BLUE);
                    label.setText("S");
                    label.setForeground(TEXT_WHITE);
                    label.setFont(loadPixelFont(Math.min(18f, 40f / N)));
                } else if (i == answerEndR && j == answerEndC) {
                    cell.setBackground(ACCENT_RED);
                    label.setText("E");
                    label.setForeground(TEXT_WHITE);
                    label.setFont(loadPixelFont(Math.min(18f, 40f / N)));
                } else {
                    cell.setBackground(light ? CELL_LIGHT : CELL_DARK);
                    label.setText("");
                }
                
                cell.setLayout(new BorderLayout());
                cell.add(label);
                boardPanel.add(cell);
            }
        }
        revalidate();
        repaint();
    }

    private JPanel createStyledCell() {
        JPanel cell = new JPanel();
        cell.setBorder(BorderFactory.createLineBorder(BORDER_COLOR, 1));
        cell.setPreferredSize(new Dimension(40, 40));
        return cell;
    }

    private JButton styledButton(String text, Color color) {
        JButton btn = new JButton(text);
        btn.setFont(loadPixelFont(13f));
        btn.setForeground(TEXT_WHITE);
        btn.setBackground(color);
        btn.setFocusPainted(false);
        btn.setOpaque(true);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createEmptyBorder(10, 14, 10, 14));

        Color hover = color.brighter();
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btn.setBackground(hover);  }
            public void mouseExited(MouseEvent e) { btn.setBackground(color);  }
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