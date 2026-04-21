package algorithms.queens;

import java.util.concurrent.atomic.AtomicInteger;

public class ThreadedQueensSolver {

    private static final int N = 16;
    private static AtomicInteger solutionCount = new AtomicInteger(0);

    public static int solve() {

        solutionCount.set(0);
        Thread[] threads = new Thread[N];

        for (int col = 0; col < N; col++) {
            final int firstCol = col;

            threads[col] = new Thread(() -> {
                int[] board = new int[N];
                board[0] = firstCol;
                solveRecursive(board, 1);
            });

            threads[col].start();
        }

        for (Thread t : threads) {
            try {
                t.join();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return solutionCount.get();
    }

    private static void solveRecursive(int[] board, int row) {

        if (row == N) {
            solutionCount.incrementAndGet();
            return;
        }

        for (int col = 0; col < N; col++) {
            if (isSafe(board, row, col)) {
                board[row] = col;
                solveRecursive(board, row + 1);
            }
        }
    }

    private static boolean isSafe(int[] board, int row, int col) {
        for (int i = 0; i < row; i++) {
            if (board[i] == col ||
                    Math.abs(board[i] - col) == Math.abs(i - row)) {
                return false;
            }
        }
        return true;
    }
}