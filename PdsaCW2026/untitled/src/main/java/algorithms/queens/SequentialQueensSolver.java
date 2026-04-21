package algorithms.queens;

public class SequentialQueensSolver {

    private static final int N = 16;
    private static int solutionCount;

    public static int solve() {
        solutionCount = 0;
        int[] board = new int[N];

        solveRecursive(board, 0);

        return solutionCount;
    }

    private static void solveRecursive(int[] board, int row) {

        if (row == N) {
            solutionCount++;
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