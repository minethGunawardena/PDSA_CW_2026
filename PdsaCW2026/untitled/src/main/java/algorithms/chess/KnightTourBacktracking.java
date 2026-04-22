package algorithms.chess;

public class KnightTourBacktracking {

    private static final int[] rowMoves = {2, 1, -1, -2, -2, -1, 1, 2};
    private static final int[] colMoves = {1, 2, 2, 1, -1, -2, -2, -1};

    public static boolean solve(int N, int startR, int startC, int[][] board) {
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                board[i][j] = -1;

        board[startR][startC] = 0;
        return solveUtil(N, startR, startC, 1, board);
    }

    private static boolean solveUtil(int N, int r, int c, int move, int[][] board) {
        if (move == N * N) return true;

        for (int i = 0; i < 8; i++) {
            int nr = r + rowMoves[i];
            int nc = c + colMoves[i];

            if (isSafe(N, nr, nc, board)) {
                board[nr][nc] = move;

                if (solveUtil(N, nr, nc, move + 1, board)) return true;

                board[nr][nc] = -1; // backtrack
            }
        }
        return false;
    }

    private static boolean isSafe(int N, int r, int c, int[][] board) {
        return r >= 0 && c >= 0 && r < N && c < N && board[r][c] == -1;
    }
}