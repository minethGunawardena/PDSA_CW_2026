package ui.panels;

import algorithms.graph.BFSSolver;
import algorithms.graph.DijkstraSolver;
import database.DBHelper;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.RoundRectangle2D;
import java.util.*;
import java.util.Timer;

public class SnakeLadderPanel extends JPanel {

    private JTextField nameField;
    private JTextArea output;
    private JButton option1, option2, option3;
    private BoardCanvas boardCanvas;
    private JLabel diceLabel;
    private Timer diceTimer;
    private boolean isRolling = false;

    private int N;
    private int[] board;
    private int correctAnswer;
    private int currentRoundId;
    private int playerScore = 0;

    // ================= THEME COLORS =================
    private static final Color BG_DARK      = new Color(30, 30, 30);
    private static final Color BG_DARKER    = new Color(20, 20, 20);
    private static final Color BG_PANEL     = new Color(40, 40, 40);
    private static final Color ACCENT_GREEN = new Color(46, 204, 113);
    private static final Color ACCENT_BLUE  = new Color(52, 152, 219);
    private static final Color ACCENT_PURP  = new Color(155, 89, 182);
    private static final Color SNAKE_RED    = new Color(231, 76, 60);
    private static final Color LADDER_YELLOW= new Color(241, 196, 15);
    private static final Color TEXT_WHITE   = Color.WHITE;
    private static final Color TEXT_DIM     = new Color(180, 180, 180);
    private static final Color BORDER_COLOR = new Color(60, 60, 60);

    // Inner class for visual board representation
    private class BoardCanvas extends JPanel {
        private int[] currentBoard;
        private int boardSize;

        public void updateBoard(int[] board, int N) {
            this.currentBoard = board;
            this.boardSize = N;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (currentBoard == null) return;

            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int cellSize = Math.min(getWidth() / boardSize, getHeight() / boardSize);
            int offsetX = (getWidth() - cellSize * boardSize) / 2;
            int offsetY = (getHeight() - cellSize * boardSize) / 2;

            for (int i = 1; i < currentBoard.length; i++) {
                int row = (i - 1) / boardSize;
                int col = (i - 1) % boardSize;

                // Alternate direction for snake pattern
                if (row % 2 == 1) {
                    col = boardSize - 1 - col;
                }

                int x = offsetX + col * cellSize;
                int y = offsetY + (boardSize - 1 - row) * cellSize;

                // Draw cell background
                g2d.setColor(new Color(45, 45, 45));
                g2d.fillRect(x, y, cellSize, cellSize);
                g2d.setColor(BORDER_COLOR);
                g2d.drawRect(x, y, cellSize, cellSize);

                // Draw number
                g2d.setColor(TEXT_WHITE);
                g2d.setFont(loadPixelFont(10f));
                FontMetrics fm = g2d.getFontMetrics();
                String num = String.valueOf(i);
                int textX = x + (cellSize - fm.stringWidth(num)) / 2;
                int textY = y + (cellSize + fm.getAscent()) / 2;
                g2d.drawString(num, textX, textY);

                // Draw snakes and ladders
                if (currentBoard[i] != -1) {
                    if (currentBoard[i] > i) {
                        // Ladder - upward
                        g2d.setColor(LADDER_YELLOW);
                        g2d.setStroke(new BasicStroke(3));
                        g2d.drawLine(x + cellSize/2, y + cellSize/2,
                                  x + cellSize/2, y + cellSize - 5);
                        // Ladder rungs
                        g2d.drawLine(x + cellSize/2 - 5, y + cellSize/2 + 5,
                                  x + cellSize/2 + 5, y + cellSize/2 + 5);
                        g2d.drawLine(x + cellSize/2 - 5, y + cellSize - 10,
                                  x + cellSize/2 + 5, y + cellSize - 10);
                    } else {
                        // Snake - downward
                        g2d.setColor(SNAKE_RED);
                        g2d.setStroke(new BasicStroke(4));
                        // Draw wavy snake body
                        int[] xPoints = {x + 5, x + cellSize/2, x + cellSize - 5, x + cellSize/2, x + 5};
                        int[] yPoints = {y + 5, y + cellSize/2, y + cellSize - 5, y + cellSize/2, y + cellSize - 5};
                        g2d.drawPolyline(xPoints, yPoints, 5);
                        // Snake head
                        g2d.fillOval(x + cellSize - 8, y + cellSize - 8, 6, 6);
                    }
                }
            }
        }
    }

    public SnakeLadderPanel() {
    private static final Color BG_DARK      = new Color(30, 30, 30);
    private static final Color BG_DARKER    = new Color(20, 20, 20);
    private static final Color BG_PANEL     = new Color(40, 40, 40);
    private static final Color ACCENT_GREEN = new Color(46, 204, 113);
    private static final Color ACCENT_BLUE  = new Color(52, 152, 219);
    private static final Color ACCENT_PURP  = new Color(155, 89, 182);
    private static final Color TEXT_WHITE   = Color.WHITE;
    private static final Color TEXT_DIM     = new Color(180, 180, 180);
    private static final Color BORDER_COLOR = new Color(60, 60, 60);

    public SnakeLadderPanel() {

        setLayout(new BorderLayout(10, 10));
        setBackground(BG_DARK);
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Top panel with title and name input
        JPanel topPanel = new JPanel(new BorderLayout(10, 8));
        topPanel.setBackground(BG_DARK);
        topPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        JLabel titleLabel = new JLabel("🐍 Snake & Ladder Adventure 🪜", SwingConstants.CENTER);
        titleLabel.setFont(loadPixelFont(24f));
        titleLabel.setForeground(ACCENT_GREEN);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 8, 0));

        JPanel nameRow = new JPanel(new BorderLayout(8, 0));
        nameRow.setBackground(BG_DARK);

        JLabel nameLabel = new JLabel("🏃 Player Name:");
        nameLabel.setForeground(TEXT_DIM);
        nameLabel.setFont(loadPixelFont(13f));

        nameField = new JTextField();
        nameField.setFont(loadPixelFont(13f));
        nameField.setBackground(new Color(50, 50, 50));
        nameField.setForeground(TEXT_WHITE);
        nameField.setCaretColor(ACCENT_GREEN);
        nameField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1),
                BorderFactory.createEmptyBorder(6, 8, 6, 8)
        ));

        nameRow.add(nameLabel, BorderLayout.WEST);
        nameRow.add(nameField, BorderLayout.CENTER);

        topPanel.add(titleLabel, BorderLayout.NORTH);
        topPanel.add(nameRow, BorderLayout.CENTER);

        add(topPanel, BorderLayout.NORTH);

        // Center panel with board and output
        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));
        centerPanel.setBackground(BG_DARK);

        // Visual board
        boardCanvas = new BoardCanvas();
        boardCanvas.setPreferredSize(new Dimension(300, 300));
        boardCanvas.setBackground(BG_DARKER);
        boardCanvas.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1),
                "  Mystic Board  ",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                loadPixelFont(11f),
                TEXT_DIM
        ));

        centerPanel.add(boardCanvas, BorderLayout.WEST);

        // Output and dice panel
        JPanel rightPanel = new JPanel(new BorderLayout(0, 10));
        rightPanel.setBackground(BG_DARK);

        output = new JTextArea();
        output.setEditable(false);
        output.setFont(new Font("Monospaced", Font.PLAIN, 12));
        output.setBackground(BG_DARKER);
        output.setForeground(ACCENT_GREEN);
        output.setBorder(BorderFactory.createEmptyBorder(10, 12, 10, 12));
        output.setLineWrap(true);
        output.setWrapStyleWord(true);

        JScrollPane scroll = new JScrollPane(output);
        scroll.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1),
                "  Adventure Log  ",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                loadPixelFont(11f),
                TEXT_DIM
        ));
        scroll.getViewport().setBackground(BG_DARKER);
        scroll.getVerticalScrollBar().setBackground(BG_PANEL);

        // Dice animation panel
        JPanel dicePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        dicePanel.setBackground(BG_DARK);
        dicePanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1),
                "  Mystic Dice  ",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                loadPixelFont(11f),
                TEXT_DIM
        ));

        diceLabel = new JLabel("🎲", SwingConstants.CENTER);
        diceLabel.setFont(new Font("Serif", Font.BOLD, 48));
        diceLabel.setForeground(ACCENT_BLUE);
        dicePanel.add(diceLabel);

        rightPanel.add(scroll, BorderLayout.CENTER);
        rightPanel.add(dicePanel, BorderLayout.SOUTH);

        centerPanel.add(rightPanel, BorderLayout.CENTER);

        add(centerPanel, BorderLayout.CENTER);


        option1 = new JButton();
        option2 = new JButton();
        option3 = new JButton();

        styleButton(option1, ACCENT_BLUE);
        styleButton(option2, ACCENT_GREEN);
        styleButton(option3, ACCENT_PURP);

        JPanel optionsWrapper = new JPanel(new BorderLayout());
        optionsWrapper.setBackground(BG_DARK);

        JLabel chooseLabel = new JLabel("🎯 Choose Your Path", SwingConstants.CENTER);
        chooseLabel.setFont(loadPixelFont(14f));
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


        option1.addActionListener(e -> handleAnswer(option1));
        option2.addActionListener(e -> handleAnswer(option2));
        option3.addActionListener(e -> handleAnswer(option3));

        startNewRound();
    }


    private void startNewRound() {
        try {
            Random rand = new Random();

            N = rand.nextInt(7) + 6;
            board = generateBoard(N);

            // Update visual board
            boardCanvas.updateBoard(board, N);

            // Roll dice animation
            rollDice();

            output.append("\n🌟 NEW ADVENTURE BEGINS 🌟\n");
            output.append("  Mystic Board Size : " + N + " x " + N + "\n");
            output.append("  Snakes: " + (N-2) + " | Ladders: " + (N-2) + "\n");

            currentRoundId = DBHelper.insertRound("Snake");

            long start1 = System.nanoTime();
            int bfsResult = BFSSolver.solve(board, N);
            long end1 = System.nanoTime();
            DBHelper.insertTime(currentRoundId, "BFS", (end1 - start1));

            long start2 = System.nanoTime();
            int dijkstraResult = DijkstraSolver.solve(board, N);
            long end2 = System.nanoTime();
            DBHelper.insertTime(currentRoundId, "Dijkstra", (end2 - start2));

            correctAnswer = bfsResult;
            DBHelper.insertSolution(currentRoundId, correctAnswer);

            generateOptions();

            output.append("  Quest ID   : " + currentRoundId + "\n");
            output.append("  🧙 Algorithms have calculated the optimal path!\n");
            output.append("  Choose wisely, brave adventurer!\n");
            output.append("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void generateOptions() {
        Random rand = new Random();
        Set<Integer> set = new HashSet<>();
        set.add(correctAnswer);

        while (set.size() < 3) {
            int fake = correctAnswer + rand.nextInt(5) - 2;
            if (fake > 0) set.add(fake);
        }

        java.util.List<Integer> options = new ArrayList<>(set);
        Collections.shuffle(options);

        String[] pathNames = {"🗡️ Warrior's Path", "🧙 Mage's Path", "🏹 Archer's Path"};
        option1.setText(pathNames[0] + " (" + options.get(0) + " moves)");
        option2.setText(pathNames[1] + " (" + options.get(1) + " moves)");
        option3.setText(pathNames[2] + " (" + options.get(2) + " moves)");
    }


    private void rollDice() {
        if (isRolling) return;
        isRolling = true;

        String[] diceFaces = {"🎲", "⚀", "⚁", "⚂", "⚃", "⚄", "⚅"};
        diceTimer = new Timer();
        diceTimer.scheduleAtFixedRate(new TimerTask() {
            int count = 0;
            @Override
            public void run() {
                SwingUtilities.invokeLater(() -> {
                    diceLabel.setText(diceFaces[new Random().nextInt(diceFaces.length)]);
                });
                count++;
                if (count >= 15) { // Roll for 1.5 seconds
                    diceTimer.cancel();
                    SwingUtilities.invokeLater(() -> {
                        diceLabel.setText("🎲");
                        isRolling = false;
                    });
                }
            }
        }, 0, 100);
    }


    private void handleAnswer(JButton btn) {
        if (isRolling) return; // Prevent answering while dice is rolling

        String name = nameField.getText().trim();

        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "🏃 Brave adventurer needs a name for the quest!",
                    "Missing Hero Name", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int selected = Integer.parseInt(btn.getText().replace(" moves", "").trim());
        boolean isCorrect = selected == correctAnswer;

        if (isCorrect) {
            playerScore++;
            output.append("  ✨ MAGNIFICENT! " + name + " conquered the challenge! ✨\n");
            output.append("  🏆 Score: " + playerScore + " victories!\n");
            // Flash the button green for success
            flashButton(btn, ACCENT_GREEN, new Color(34, 153, 84));
        } else {
            output.append("  💥 OH NO! " + name + " took a wrong turn!\n");
            output.append("  🎯 The correct path needed " + correctAnswer + " moves\n");
            // Flash the button red for wrong answer
            flashButton(btn, SNAKE_RED, new Color(192, 57, 43));
        }

        int playerId = DBHelper.getOrCreatePlayer(name);
        DBHelper.savePlayerAnswer(playerId, currentRoundId, selected, isCorrect);

        // Delay before starting new round for dramatic effect
        Timer delayTimer = new Timer();
        delayTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                SwingUtilities.invokeLater(() -> startNewRound());
            }
        }, 2000);
    }


    private void flashButton(JButton btn, Color flashColor, Color originalColor) {
        Timer flashTimer = new Timer();
        flashTimer.scheduleAtFixedRate(new TimerTask() {
            int count = 0;
            boolean isFlash = false;
            @Override
            public void run() {
                SwingUtilities.invokeLater(() -> {
                    if (isFlash) {
                        btn.setBackground(originalColor);
                    } else {
                        btn.setBackground(flashColor);
                    }
                    isFlash = !isFlash;
                });
                count++;
                if (count >= 6) { // Flash 3 times
                    flashTimer.cancel();
                    SwingUtilities.invokeLater(() -> btn.setBackground(originalColor));
                }
            }
        }, 0, 200);
    }


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