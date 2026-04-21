package ui.panels;

import algorithms.queens.SequentialQueensSolver;
import algorithms.queens.ThreadedQueensSolver;
import database.DBHelper;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.util.HashMap;

public class QueensPanel extends JPanel {

    private JTextField nameField;
    private JTextArea outputArea;
    private JPanel boardPanel;
    private JButton startBtn;
    private JButton submitBtn;
    private JProgressBar progressBar;
    private JLabel queensLeftLabel;

    private int roundId;
    private int totalSolutions;
    private int foundCount = 0;
    private int queensPlaced = 0;

    // stores boardKey -> playerName for duplicate checking
    private HashMap<String, String> foundSolutions = new HashMap<>();

    // current player board - index = row, value = col of queen (-1 = empty)
    private int[] playerBoard = new int[16];
    private boolean gameActive = false;

    // ================= THEME COLORS (same as KnightTourPanel) =================
    private static final Color BG_DARK      = new Color(30, 30, 30);
    private static final Color BG_DARKER    = new Color(20, 20, 20);
    private static final Color BG_PANEL     = new Color(40, 40, 40);
    private static final Color ACCENT_GOLD  = new Color(241, 196, 15);
    private static final Color ACCENT_BLUE  = new Color(52, 152, 219);
    private static final Color ACCENT_RED   = new Color(231, 76, 60);
    private static final Color ACCENT_GREEN = new Color(39, 174, 96);
    private static final Color TEXT_WHITE   = Color.WHITE;
    private static final Color TEXT_DIM     = new Color(180, 180, 180);
    private static final Color BORDER_COLOR = new Color(60, 60, 60);
    private static final Color CELL_LIGHT   = new Color(50, 50, 60);
    private static final Color CELL_DARK    = new Color(35, 35, 45);
    private static final Color CELL_QUEEN   = new Color(44, 80, 130);

    public QueensPanel() {
        setLayout(new BorderLayout(10, 10));
        setBackground(BG_DARK);
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel topPanel = new JPanel(new BorderLayout(10, 8));
        topPanel.setBackground(BG_DARK);
        topPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        JLabel titleLabel = new JLabel("Sixteen Queens Puzzle", SwingConstants.CENTER);
        titleLabel.setFont(loadPixelFont(22f));
        titleLabel.setForeground(ACCENT_GOLD);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 8, 0));

        JPanel controlsRow = new JPanel(new GridLayout(1, 3, 8, 0));
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

        startBtn = styledButton("▶  Start Game", ACCENT_BLUE);
        startBtn.addActionListener(e -> startGame());

        controlsRow.add(nameLabel);
        controlsRow.add(nameField);
        controlsRow.add(startBtn);

        topPanel.add(titleLabel, BorderLayout.NORTH);
        topPanel.add(controlsRow, BorderLayout.CENTER);
        add(topPanel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new BorderLayout(5, 5));
        centerPanel.setBackground(BG_DARK);

        boardPanel = new JPanel(new GridLayout(16, 16));
        boardPanel.setPreferredSize(new Dimension(380, 380));
        boardPanel.setBackground(BG_DARKER);
        boardPanel.setBorder(BorderFactory.createLineBorder(BORDER_COLOR, 1));
        drawBoard();
        centerPanel.add(boardPanel, BorderLayout.CENTER);

        queensLeftLabel = new JLabel("Queens left to place: 16", SwingConstants.CENTER);
        queensLeftLabel.setForeground(ACCENT_GOLD);
        queensLeftLabel.setFont(loadPixelFont(12f));
        queensLeftLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
        centerPanel.add(queensLeftLabel, BorderLayout.SOUTH);

        add(centerPanel, BorderLayout.CENTER);

        JPanel rightPanel = new JPanel(new BorderLayout(5, 8));
        rightPanel.setBackground(BG_DARK);
        rightPanel.setPreferredSize(new Dimension(310, 0));

        outputArea = new JTextArea();
        outputArea.setEditable(false);
        outputArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        outputArea.setBackground(BG_DARKER);
        outputArea.setForeground(ACCENT_GOLD);
        outputArea.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));
        outputArea.setLineWrap(true);
        outputArea.setWrapStyleWord(true);
        outputArea.setText("Press Start Game to begin.\n");

        JScrollPane scrollPane = new JScrollPane(outputArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1),
                "  Game Log  ",
                TitledBorder.LEFT, TitledBorder.TOP,
                loadPixelFont(11f), TEXT_DIM
        ));
        scrollPane.getViewport().setBackground(BG_DARKER);
        scrollPane.getVerticalScrollBar().setBackground(BG_PANEL);
        rightPanel.add(scrollPane, BorderLayout.CENTER);

        // bottom of right panel - progress bar + submit button
        JPanel bottomRight = new JPanel(new GridLayout(3, 1, 0, 5));
        bottomRight.setBackground(BG_DARK);
        bottomRight.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));

        progressBar = new JProgressBar(0, 100);
        progressBar.setValue(0);
        progressBar.setStringPainted(true);
        progressBar.setString("Waiting...");
        progressBar.setForeground(ACCENT_GREEN);
        progressBar.setBackground(BG_DARKER);

        submitBtn = styledButton("✔  Submit Solution", ACCENT_GREEN);
        submitBtn.setEnabled(false);
        submitBtn.addActionListener(e -> checkSolution());

        bottomRight.add(new JLabel()); // spacer
        bottomRight.add(progressBar);
        bottomRight.add(submitBtn);
        rightPanel.add(bottomRight, BorderLayout.SOUTH);

        add(rightPanel, BorderLayout.EAST);
    }

    private void startGame() {
        String name = nameField.getText().trim();
        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter your name!",
                    "Missing Name", JOptionPane.WARNING_MESSAGE);
            return;
        }

        startBtn.setEnabled(false);
        submitBtn.setEnabled(false);
        outputArea.setText("Running algorithms, please wait...\n");
        progressBar.setString("Running...");
        progressBar.setValue(0);

        new Thread(() -> {
            try {
                roundId = DBHelper.insertRound("SixteenQueens");

                // run sequential
                SwingUtilities.invokeLater(() -> {
                    progressBar.setValue(10);
                    progressBar.setString("Running Sequential...");
                });

                long t1 = System.currentTimeMillis();
                int seqResult = SequentialQueensSolver.solve();
                long seqTime = System.currentTimeMillis() - t1;
                DBHelper.insertTime(roundId, "Sequential", seqTime);

                SwingUtilities.invokeLater(() -> {
                    progressBar.setValue(55);
                    progressBar.setString("Running Threaded...");
                    outputArea.append("Sequential done: " + seqTime + " ms\n");
                });

                // run threaded
                long t2 = System.currentTimeMillis();
                int thrResult = ThreadedQueensSolver.solve();
                long thrTime = System.currentTimeMillis() - t2;
                DBHelper.insertTime(roundId, "Threaded", thrTime);

                SwingUtilities.invokeLater(() -> {
                    progressBar.setValue(90);
                    progressBar.setString("Saving to DB...");
                    outputArea.append("Threaded done: " + thrTime + " ms\n");
                });

                totalSolutions = seqResult;
                DBHelper.insertSolution(roundId, totalSolutions);

                foundCount = 0;
                queensPlaced = 0;
                foundSolutions.clear();
                resetPlayerBoard();
                gameActive = true;

                SwingUtilities.invokeLater(() -> {
                    progressBar.setValue(100);
                    progressBar.setString("Done!");

                    outputArea.setText("");
                    outputArea.append("=== Algorithm Results ===\n");
                    outputArea.append("Sequential: " + seqResult + " solutions\n");
                    outputArea.append("Time: " + seqTime + " ms\n\n");
                    outputArea.append("Threaded: " + thrResult + " solutions\n");
                    outputArea.append("Time: " + thrTime + " ms\n\n");

                    if (seqTime > thrTime)
                        outputArea.append(">> Threaded was faster!\n\n");
                    else if (thrTime > seqTime)
                        outputArea.append(">> Sequential was faster!\n\n");
                    else
                        outputArea.append(">> Both took the same time!\n\n");

                    outputArea.append("Total solutions: " + totalSolutions + "\n");
                    outputArea.append("Found so far: " + foundCount + "\n");
                    outputArea.append("────────────────────────\n");
                    outputArea.append("Click cells to place queens\nthen press Submit.\n");

                    updateQueensLeftLabel();
                    drawBoard();
                    submitBtn.setEnabled(true);
                    startBtn.setEnabled(true);
                });

            } catch (Exception ex) {
                SwingUtilities.invokeLater(() -> {
                    outputArea.append("Error: " + ex.getMessage() + "\n");
                    progressBar.setString("Error!");
                    startBtn.setEnabled(true);
                });
            }
        }).start();
    }


    private void checkSolution() {
        if (!gameActive) {
            JOptionPane.showMessageDialog(this, "Start a game first!");
            return;
        }

        String name = nameField.getText().trim();
        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter your name!");
            return;
        }


        for (int i = 0; i < 16; i++) {
            if (playerBoard[i] == -1) {
                JOptionPane.showMessageDialog(this, "Row " + (i + 1) + " has no queen!");
                return;
            }
        }


        if (!isValid(playerBoard)) {
            outputArea.append("[" + name + "] Invalid - queens attacking each other!\n");
            return;
        }


        String key = boardToKey(playerBoard);
        if (foundSolutions.containsKey(key)) {
            String who = foundSolutions.get(key);
            outputArea.append("Already found by " + who + "! Try again.\n");
            JOptionPane.showMessageDialog(this,
                    "This solution was already found by " + who + "!\nTry a different one.");
            return;
        }


        foundCount++;
        foundSolutions.put(key, name);

        int playerId = DBHelper.getOrCreatePlayer(name);
        DBHelper.savePlayerAnswer(playerId, roundId, foundCount, true);
        DBHelper.insertUniqueSolution("SixteenQueens", key);

        outputArea.append("[" + name + "] Correct! Solution #" + foundCount + "\n");
        outputArea.append("Found: " + foundCount + " / " + totalSolutions + "\n\n");

        if (foundCount >= totalSolutions) {
            outputArea.append("ALL SOLUTIONS FOUND!\nResetting for next players...\n");
            foundSolutions.clear();
            DBHelper.clearUniqueSolutions("SixteenQueens");
            gameActive = false;
            submitBtn.setEnabled(false);
            progressBar.setString("All done!");
            JOptionPane.showMessageDialog(this, "All solutions found! Game complete!");
        } else {
            int left = totalSolutions - foundCount;
            JOptionPane.showMessageDialog(this,
                    "Correct! " + left + " solution(s) remaining.\nClear board and try another!");
            queensPlaced = 0;
            resetPlayerBoard();
            updateQueensLeftLabel();
            drawBoard();
        }
    }


    private void drawBoard() {
        boardPanel.removeAll();

        for (int row = 0; row < 16; row++) {
            for (int col = 0; col < 16; col++) {
                final int r = row, c = col;
                JButton cell = new JButton();
                cell.setMargin(new Insets(0, 0, 0, 0));
                cell.setFont(loadPixelFont(10f));
                cell.setFocusPainted(false);
                cell.setBorderPainted(false);
                cell.setOpaque(true);


                boolean light = (row + col) % 2 == 0;

                if (playerBoard[row] == col) {
                    cell.setBackground(CELL_QUEEN);
                    cell.setForeground(TEXT_WHITE);
                    cell.setText("Q");
                } else {
                    cell.setBackground(light ? CELL_LIGHT : CELL_DARK);
                    cell.setForeground(TEXT_DIM);
                    cell.setText("");
                }

                cell.addActionListener(e -> {
                    if (!gameActive) return;
                    // clicking same cell removes queen, else place queen in that row
                    if (playerBoard[r] == c) {
                        playerBoard[r] = -1;
                        queensPlaced--;
                    } else {
                        if (playerBoard[r] != -1) {
                            // replacing existing queen in this row - count stays same
                        } else {
                            queensPlaced++;
                        }
                        playerBoard[r] = c;
                    }
                    updateQueensLeftLabel();
                    drawBoard();
                });

                boardPanel.add(cell);
            }
        }

        boardPanel.revalidate();
        boardPanel.repaint();
    }

    private void updateQueensLeftLabel() {
        int left = 16 - queensPlaced;
        queensLeftLabel.setText("Queens placed: " + queensPlaced + " / 16  |  Left: " + left);
        if (left == 0)
            queensLeftLabel.setForeground(ACCENT_GREEN);
        else
            queensLeftLabel.setForeground(ACCENT_GOLD);
    }

    private void resetPlayerBoard() {
        for (int i = 0; i < 16; i++)
            playerBoard[i] = -1;
    }

    static boolean isValid(int[] board) {
        for (int i = 0; i < 16; i++) {
            for (int j = i + 1; j < 16; j++) {
                if (board[i] == board[j]) return false;
                if (Math.abs(board[i] - board[j]) == Math.abs(i - j)) return false;
            }
        }
        return true;
    }

    static String boardToKey(int[] board) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < board.length; i++) {
            if (i > 0) sb.append(",");
            sb.append(board[i]);
        }
        return sb.toString();
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
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) { btn.setBackground(hover); }
            public void mouseExited(java.awt.event.MouseEvent evt)  { btn.setBackground(color); }
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