package algorithms.chess;

import java.util.Arrays;

public class KnightTourHeuristic {

    private static final int[] rowMoves = {2, 1, -1, -2, -2, -1, 1, 2};
    private static final int[] colMoves = {1, 2, 2, 1, -1, -2, -2, -1};

    public static boolean solve(int N, int startR, int startC, int[][] board) {

        for (int i = 0; i < N; i++) {
            Arrays.fill(board[i], -1);
        }

        int r = startR;
        int c = startC;

        board[r][c] = 0;

        for (int move = 1; move < N * N; move++) {

            int nextR = -1;
            int nextC = -1;
            int minDegree = Integer.MAX_VALUE;

            for (int i = 0; i < 8; i++) {

                int nr = r + rowMoves[i];
                int nc = c + colMoves[i];

                if (isSafe(N, nr, nc, board)) {

                    int degree = countMoves(N, nr, nc, board);

                    if (degree < minDegree) {
                        minDegree = degree;
                        nextR = nr;
                        nextC = nc;
                    }
                }
            }

            // NO MOVE FOUND → FAIL
            if (nextR == -1) {
                return false;
            }

            r = nextR;
            c = nextC;
            board[r][c] = move;
        }

        return true;
    }

    private static int countMoves(int N, int r, int c, int[][] board) {

        int count = 0;

        for (int i = 0; i < 8; i++) {

            int nr = r + rowMoves[i];
            int nc = c + colMoves[i];

            if (isSafe(N, nr, nc, board)) {
                count++;
            }
        }

        return count;
    }

    private static boolean isSafe(int N, int r, int c, int[][] board) {
        return r >= 0 && c >= 0 && r < N && c < N && board[r][c] == -1;
    }
}