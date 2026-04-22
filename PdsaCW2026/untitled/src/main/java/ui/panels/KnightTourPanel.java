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


    private final JTextField         nameField;
    private final JComboBox<Integer> sizeBox;
    private final JComboBox<String>  algoBox;
    private final JTextArea          output;
    private final JPanel             boardPanel;


    private int     N;
    private int[][] board;
    private int     currentRoundId;
    private Timer   timer;
    private int     step      = 0;
    private boolean animating = false;

    
    private static final Color BG_DARK      = new Color(30, 30, 30);
    private static final Color BG_DARKER    = new Color(20, 20, 20);
    private static final Color BG_PANEL     = new Color(40, 40, 40);
    private static final Color ACCENT_GOLD  = new Color(241, 196, 15);
    private static final Color ACCENT_BLUE  = new Color(52, 152, 219);
    private static final Color ACCENT_GREEN = new Color(46, 204, 113);
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

        // ── title ──────────────────────────────────────────────────────────
        JLabel titleLabel = new JLabel("Knight's Tour", SwingConstants.CENTER);
        titleLabel.setFont(loadPixelFont(22f));
        titleLabel.setForeground(ACCENT_GOLD);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 8, 0));

        // ── controls row ───────────────────────────────────────────────────
        JPanel controlsRow = new JPanel(new GridLayout(1, 6, 8, 0));
        controlsRow.setBackground(BG_DARK);

        nameField = new JTextField();
        styleTextField(nameField);

        sizeBox = new JComboBox<>(new Integer[]{8, 16});
        styleCombo(sizeBox);

        // Algorithm options: Both = compare Heuristic vs Divide & Conquer
        algoBox = new JComboBox<>(new String[]{"Both", "Heuristic", "Divide & Conquer"});
        styleCombo(algoBox);

        controlsRow.add(makeLabel("Player Name:"));
        controlsRow.add(nameField);
        controlsRow.add(makeLabel("Board Size:"));
        controlsRow.add(sizeBox);
        controlsRow.add(makeLabel("Algorithm:"));
        controlsRow.add(algoBox);

        JPanel topPanel = new JPanel(new BorderLayout(10, 8));
        topPanel.setBackground(BG_DARK);
        topPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        topPanel.add(titleLabel,  BorderLayout.NORTH);
        topPanel.add(controlsRow, BorderLayout.CENTER);
        add(topPanel, BorderLayout.NORTH);

        // ── board ──────────────────────────────────────────────────────────
        boardPanel = new JPanel();
        boardPanel.setBackground(BG_DARKER);
        boardPanel.setOpaque(true);
        boardPanel.setBorder(BorderFactory.createLineBorder(BORDER_COLOR, 1));
        add(boardPanel, BorderLayout.CENTER);

        // ── game log (no progress bar) ─────────────────────────────────────
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
                TitledBorder.LEFT, TitledBorder.TOP,
                loadPixelFont(11f), TEXT_DIM
        ));
        scroll.getViewport().setBackground(BG_DARKER);
        scroll.getVerticalScrollBar().setBackground(BG_PANEL);
        add(scroll, BorderLayout.SOUTH);

        // ── buttons ────────────────────────────────────────────────────────
        JButton runBtn  = styledButton("▶  Run Tour", ACCENT_BLUE);
        JButton animBtn = styledButton("⟳  Animate",  ACCENT_GOLD);
        JButton bothBtn = styledButton("⚡  Compare",  ACCENT_GREEN);

        JPanel btnPanel = new JPanel(new GridLayout(3, 1, 0, 12));
        btnPanel.setBackground(BG_DARK);
        btnPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 12));
        btnPanel.add(runBtn);
        btnPanel.add(animBtn);
        btnPanel.add(bothBtn);
        add(btnPanel, BorderLayout.WEST);

        runBtn.addActionListener(ignored  -> runGame());
        animBtn.addActionListener(ignored -> startAnimation());
        bothBtn.addActionListener(ignored -> runBothAndCompare());

        startRound();
    }

    private void startRound() {
        N              = getBoardSize();
        board          = emptyBoard(N);
        currentRoundId = DBHelper.insertRound("KnightTour");

        output.setText("▶ Knight's Tour Ready (" + N + " x " + N + ")\n");
        output.append("  Algorithms: Heuristic (Warnsdorff) vs Divide & Conquer\n");
        output.append("  Select an algorithm and press Run Tour, Animate, or Compare.\n");
        output.append("────────────────────────────────────\n");
        drawBoard(-1);
    }


    private void runGame() {
        String name = nameField.getText().trim();
        if (name.isEmpty()) { showWarn("Enter your player name first!", "Missing Name"); return; }

        String algo = getSelectedAlgo();
        if ("Both".equals(algo)) { runBothAndCompare(); return; }

        N     = getBoardSize();
        board = emptyBoard(N);

        final int    startR    = new Random().nextInt(N);
        final int    startC    = new Random().nextInt(N);
        final String finalAlgo = algo;

        output.setText("");
        output.append("▶ NEW ROUND  [" + finalAlgo + "]\n");
        output.append("  Board : " + N + " × " + N + "\n");
        output.append("  Start : (" + startR + ", " + startC + ")\n");
        output.append("  Solving...\n");

        new SwingWorker<Boolean, Void>() {
            long elapsed;

            @Override
            protected Boolean doInBackground() {
                long t1 = System.nanoTime();
                boolean ok = isDivideAndConquer(finalAlgo)
                        ? KnightTourDivideConquer.solve(N, startR, startC, board)
                        : KnightTourHeuristic.solve(N, startR, startC, board);
                elapsed = System.nanoTime() - t1;
                return ok;
            }

            @Override
            protected void done() {
                try {
                    boolean success = get();

                    DBHelper.insertTime(currentRoundId, finalAlgo, elapsed);
                    int playerId = DBHelper.getOrCreatePlayer(name);
                    DBHelper.savePlayerAnswer(playerId, currentRoundId, 1, success);

                    output.append(success ? "  ✔ Full tour completed!\n" : "  ✘ Tour incomplete.\n");
                    output.append("  Time  : " + formatNs(elapsed) + "\n");
                    output.append("────────────────────────────────────\n");
                    drawBoard(-1);

                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                    LOG.severe("runGame interrupted: " + ex.getMessage());
                } catch (ExecutionException ex) {
                    LOG.severe("runGame failed: " + ex.getMessage());
                }
            }
        }.execute();
    }


    private void runBothAndCompare() {
        String name = nameField.getText().trim();
        if (name.isEmpty()) { showWarn("Enter your player name first!", "Missing Name"); return; }

        N = getBoardSize();
        final int startR = new Random().nextInt(N);
        final int startC = new Random().nextInt(N);

        output.setText("");
        output.append("⚡ COMPARING BOTH ALGORITHMS\n");
        output.append("  Board : " + N + " × " + N + "\n");
        output.append("  Start : (" + startR + ", " + startC + ")\n");
        output.append("────────────────────────────────────\n");
        output.append("  Running Heuristic...\n");

        new SwingWorker<Void, String>() {
            final int[][] hBoard = emptyBoard(N);   // Heuristic board
            final int[][] dBoard = emptyBoard(N);   // Divide & Conquer board
            boolean hOk, dOk;
            long    hTime, dTime;

            @Override
            protected Void doInBackground() {
                // ── 1. Heuristic (Warnsdorff) ──────────────────────────────
                long t1 = System.nanoTime();
                hOk   = KnightTourHeuristic.solve(N, startR, startC, hBoard);
                hTime = System.nanoTime() - t1;

                publish("Running Divide & Conquer...");

                // ── 2. Divide & Conquer ────────────────────────────────────
                long t2 = System.nanoTime();
                dOk   = KnightTourDivideConquer.solve(N, startR, startC, dBoard);
                dTime = System.nanoTime() - t2;

                return null;
            }

            @Override
            protected void process(List<String> chunks) {
                output.append("  " + chunks.get(chunks.size() - 1) + "\n");
            }

            @Override
            protected void done() {
                try {
                    get();

                    // ── save both timings to DB ─────────────────────────────
                    DBHelper.insertTime(currentRoundId, "Heuristic",       hTime);
                    DBHelper.insertTime(currentRoundId, "Divide&Conquer",  dTime);

                    int playerId = DBHelper.getOrCreatePlayer(name);
                    DBHelper.savePlayerAnswer(playerId, currentRoundId, 1, hOk || dOk);

                    // ── log results ────────────────────────────────────────
                    output.append("\n");
                    output.append("  [Heuristic — Warnsdorff]\n");
                    output.append("    Result : " + (hOk ? "✔ Complete" : "✘ Incomplete") + "\n");
                    output.append("    Time   : " + formatNs(hTime) + "\n\n");

                    output.append("  [Divide & Conquer]\n");
                    output.append("    Result : " + (dOk ? "✔ Complete" : "✘ Incomplete") + "\n");
                    output.append("    Time   : " + formatNs(dTime) + "\n\n");

                    // ── winner ─────────────────────────────────────────────
                    if (hOk && dOk) {
                        String faster = hTime <= dTime ? "Heuristic" : "Divide & Conquer";
                        output.append("  ⚡ Faster  : " + faster + "\n");
                    } else if (hOk) {
                        output.append("  ⚡ Only Heuristic found a full tour.\n");
                    } else if (dOk) {
                        output.append("  ⚡ Only Divide & Conquer found a full tour.\n");
                    } else {
                        output.append("  ⚡ Neither algorithm completed the tour.\n");
                    }
                    output.append("────────────────────────────────────\n");

                    // show whichever board succeeded (prefer Heuristic)
                    board = hOk ? hBoard : dBoard;
                    drawBoard(-1);

                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                    LOG.severe("compare interrupted: " + ex.getMessage());
                } catch (ExecutionException ex) {
                    LOG.severe("compare failed: " + ex.getMessage());
                }
            }
        }.execute();
    }


    private void startAnimation() {
        if (animating) return;

        String algo = getSelectedAlgo();
        if ("Both".equals(algo)) algo = "Heuristic"; // animate Heuristic when Both selected

        N     = getBoardSize();
        board = emptyBoard(N);

        final int    startR    = new Random().nextInt(N);
        final int    startC    = new Random().nextInt(N);
        final String finalAlgo = algo;

        output.setText("⟳ Solving for animation  [" + finalAlgo + "]...\n");

        new SwingWorker<Boolean, Void>() {
            @Override
            protected Boolean doInBackground() {
                return isDivideAndConquer(finalAlgo)
                        ? KnightTourDivideConquer.solve(N, startR, startC, board)
                        : KnightTourHeuristic.solve(N, startR, startC, board);
            }

            @Override
            protected void done() {
                try {
                    boolean ok = get();
                    if (!ok) {
                        showWarn("No full tour found — try again!", "Tour Failed");
                        return;
                    }

                    output.append("────────────────────────────────────\n");
                    step      = 0;
                    animating = true;
                    final int total = N * N;

                    timer = new Timer(120, ignored -> {
                        drawBoard(step);
                        step++;
                        if (step >= total) {
                            timer.stop();
                            animating = false;
                            output.append("  ✔ Animation complete!\n");
                        }
                    });
                    timer.start();

                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                    LOG.severe("animation interrupted: " + ex.getMessage());
                } catch (ExecutionException ex) {
                    LOG.severe("animation failed: " + ex.getMessage());
                }
            }
        }.execute();
    }

    private void drawBoard(int highlight) {
        boardPanel.removeAll();
        boardPanel.setLayout(new GridLayout(N, N));

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                JLabel  cell  = new JLabel();
                int     val   = board[i][j];
                boolean light = (i + j) % 2 == 0;

                cell.setHorizontalAlignment(SwingConstants.CENTER);
                cell.setFont(loadPixelFont(N <= 8 ? 12f : 8f));
                cell.setOpaque(true);
                cell.setBorder(BorderFactory.createLineBorder(BG_DARK, 1));

                if (val == -1) {
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


    private boolean isDivideAndConquer(String algo) {
        return "Divide & Conquer".equals(algo);
    }

    private int getBoardSize() {
        Object sel = sizeBox.getSelectedItem();
        return (sel instanceof Integer) ? (Integer) sel : 8;
    }

    private String getSelectedAlgo() {
        Object sel = algoBox.getSelectedItem();
        return (sel != null) ? sel.toString() : "Heuristic";
    }

    private static int[][] emptyBoard(int size) {
        int[][] b = new int[size][size];
        for (int[] row : b) Arrays.fill(row, -1);
        return b;
    }

    private static String formatNs(long ns) {
        if (ns < 1_000_000L)     return (ns / 1_000L) + " µs";
        if (ns < 1_000_000_000L) return (ns / 1_000_000L) + " ms";
        return String.format("%.2f s", ns / 1_000_000_000.0);
    }

    private void showWarn(String msg, String title) {
        JOptionPane.showMessageDialog(this, msg, title, JOptionPane.WARNING_MESSAGE);
    }

    private JLabel makeLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(loadPixelFont(13f));
        lbl.setForeground(TEXT_DIM);
        return lbl;
    }

    private void styleTextField(JTextField tf) {
        tf.setFont(loadPixelFont(13f));
        tf.setBackground(new Color(50, 50, 50));
        tf.setForeground(TEXT_WHITE);
        tf.setCaretColor(ACCENT_GOLD);
        tf.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1),
                BorderFactory.createEmptyBorder(6, 8, 6, 8)
        ));
    }

    private <T> void styleCombo(JComboBox<T> box) {
        box.setFont(loadPixelFont(13f));
        box.setBackground(new Color(50, 50, 50));
        box.setForeground(TEXT_WHITE);
        box.setBorder(BorderFactory.createLineBorder(BORDER_COLOR, 1));
        box.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value,
                                                          int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                setBackground(isSelected ? ACCENT_GOLD : new Color(50, 50, 50));
                setForeground(isSelected ? BG_DARK : TEXT_WHITE);
                setFont(loadPixelFont(13f));
                return this;
            }
        });
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
        btn.setBorder(BorderFactory.createEmptyBorder(12, 16, 12, 16));

        Color hover = color.brighter();
        btn.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) { btn.setBackground(hover); }
            @Override public void mouseExited(MouseEvent e)  { btn.setBackground(color); }
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