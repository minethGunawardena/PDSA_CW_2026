package algorithms.chess;

import java.util.Arrays;


public class KnightTourDivideConquer {

    // ── standard knight moves ──────────────────────────────────────────────
    private static final int[] DR = {2, 1, -1, -2, -2, -1,  1,  2};
    private static final int[] DC = {1, 2,  2,  1, -1, -2, -2, -1};

    // ======================================================================
    //  PUBLIC ENTRY POINT
    // ======================================================================


    public static boolean solve(int N, int startR, int startC, int[][] board) {

        // initialise board to -1 (unvisited)
        for (int[] row : board) Arrays.fill(row, -1);

        // fall back to plain Warnsdorff for odd or tiny boards
        if (N % 2 != 0 || N < 8) {
            return KnightTourHeuristic.solve(N, startR, startC, board);
        }

        int half = N / 2;

        // ── Step 1: define the four quadrant origins (top-left corners) ───
        //   Q0 = top-left,    Q1 = top-right
        //   Q2 = bottom-left, Q3 = bottom-right
        int[][] origins = {
                {0,    0   },   // Q0
                {0,    half},   // Q1
                {half, 0   },   // Q2
                {half, half}    // Q3
        };

        // ── Step 2: solve each quadrant on its own sub-board ──────────────
        int[][][] subBoards = new int[4][half][half];

        // Starting corners chosen so the knight ends near the board centre
        // (inner corner of each quadrant) — improves merge success rate.
        int[][] quadStarts = {
                {half - 1, half - 1},   // Q0 inner corner
                {half - 1, 0        },  // Q1 inner corner
                {0,        half - 1 },  // Q2 inner corner
                {0,        0        }   // Q3 inner corner
        };

        for (int q = 0; q < 4; q++) {
            boolean ok = solveQuadrant(half, quadStarts[q][0], quadStarts[q][1], subBoards[q]);
            if (!ok) {
                // quadrant failed — fall back to full-board Warnsdorff
                for (int[] row : board) Arrays.fill(row, -1);
                return KnightTourHeuristic.solve(N, startR, startC, board);
            }
        }

        // ── Step 3: merge quadrants into the global board ─────────────────
        //   Merge order: Q0 → Q1 → Q3 → Q2  (Z-pattern keeps borders close)
        int[] mergeOrder = {0, 1, 3, 2};
        int   offset     = 0;

        // Write Q0 directly — no stitching needed for the first quadrant
        writeQuadrant(board, subBoards[0], origins[0], half, offset);
        offset += half * half;

        for (int m = 1; m < 4; m++) {
            int prevQ = mergeOrder[m - 1];
            int currQ = mergeOrder[m];

            boolean stitched = stitch(
                    board, N,
                    subBoards[currQ], origins[currQ], half,
                    offset
            );

            if (!stitched) {
                // stitching failed — fall back to full-board Warnsdorff
                for (int[] row : board) Arrays.fill(row, -1);
                return KnightTourHeuristic.solve(N, startR, startC, board);
            }
            offset += half * half;
        }

        return true;
    }


    private static boolean solveQuadrant(int size, int startR, int startC, int[][] sub) {
        for (int[] row : sub) Arrays.fill(row, -1);

        int r = startR;
        int c = startC;
        sub[r][c] = 0;

        for (int move = 1; move < size * size; move++) {
            int bestR = -1, bestC = -1, minDeg = Integer.MAX_VALUE;

            for (int i = 0; i < 8; i++) {
                int nr = r + DR[i];
                int nc = c + DC[i];
                if (inBounds(size, nr, nc) && sub[nr][nc] == -1) {
                    int deg = degree(size, nr, nc, sub);
                    if (deg < minDeg) {
                        minDeg = deg;
                        bestR  = nr;
                        bestC  = nc;
                    }
                }
            }

            if (bestR == -1) return false;
            r = bestR;
            c = bestC;
            sub[r][c] = move;
        }
        return true;
    }


    private static void writeQuadrant(int[][] board, int[][] sub,
                                      int[] origin, int size, int offset) {
        for (int r = 0; r < size; r++) {
            for (int c = 0; c < size; c++) {
                board[origin[0] + r][origin[1] + c] = sub[r][c] + offset;
            }
        }
    }

    private static boolean stitch(int[][] board, int N,
                                  int[][] sub, int[] origin, int size,
                                  int offset) {


        int[] last = findCell(board, N, offset - 1);
        if (last == null) return false;

        int lastR = last[0];
        int lastC = last[1];


        if (tryConnect(board, sub, origin, size, offset, lastR, lastC, false)) return true;
        reverseSub(sub, size);
        if (tryConnect(board, sub, origin, size, offset, lastR, lastC, false)) return true;

        if (tryBoundaryConnect(board, sub, origin, size, offset, lastR, lastC)) return true;

        reverseSub(sub, size);
        return tryBoundaryConnect(board, sub, origin, size, offset, lastR, lastC);
    }

    private static boolean tryConnect(int[][] board, int[][] sub, int[] origin,
                                      int size, int offset,
                                      int lastR, int lastC, boolean reversed) {

        // find where move 0 lives in the sub-board
        int[] subStart = findInSub(sub, size, 0);
        if (subStart == null) return false;

        int globalR = origin[0] + subStart[0];
        int globalC = origin[1] + subStart[1];

        // check if (lastR,lastC) can reach (globalR,globalC) by a knight move
        if (isKnightMove(lastR, lastC, globalR, globalC)) {
            writeQuadrant(board, sub, origin, size, offset);
            return true;
        }
        return false;
    }

    private static boolean tryBoundaryConnect(int[][] board, int[][] sub, int[] origin,
                                              int size, int offset,
                                              int lastR, int lastC) {
        for (int r = 0; r < size; r++) {
            for (int c = 0; c < size; c++) {
                int globalR = origin[0] + r;
                int globalC = origin[1] + c;

                if (isKnightMove(lastR, lastC, globalR, globalC)) {

                    int pivot = sub[r][c];
                    rotateSub(sub, size, pivot);
                    writeQuadrant(board, sub, origin, size, offset);
                    return true;
                }
            }
        }
        return false;
    }


    private static void reverseSub(int[][] sub, int size) {
        int total = size * size;
        for (int r = 0; r < size; r++)
            for (int c = 0; c < size; c++)
                if (sub[r][c] >= 0)
                    sub[r][c] = total - 1 - sub[r][c];
    }


    private static void rotateSub(int[][] sub, int size, int pivot) {
        int total = size * size;
        for (int r = 0; r < size; r++)
            for (int c = 0; c < size; c++)
                if (sub[r][c] >= 0)
                    sub[r][c] = (sub[r][c] - pivot + total) % total;
    }


    private static int[] findCell(int[][] board, int N, int move) {
        for (int r = 0; r < N; r++)
            for (int c = 0; c < N; c++)
                if (board[r][c] == move) return new int[]{r, c};
        return null;
    }


    private static int[] findInSub(int[][] sub, int size, int move) {
        for (int r = 0; r < size; r++)
            for (int c = 0; c < size; c++)
                if (sub[r][c] == move) return new int[]{r, c};
        return null;
    }

    private static boolean isKnightMove(int r1, int c1, int r2, int c2) {
        int dr = Math.abs(r1 - r2);
        int dc = Math.abs(c1 - c2);
        return (dr == 1 && dc == 2) || (dr == 2 && dc == 1);
    }

    private static boolean inBounds(int size, int r, int c) {
        return r >= 0 && c >= 0 && r < size && c < size;
    }

    private static int degree(int size, int r, int c, int[][] sub) {
        int count = 0;
        for (int i = 0; i < 8; i++) {
            int nr = r + DR[i];
            int nc = c + DC[i];
            if (inBounds(size, nr, nc) && sub[nr][nc] == -1) count++;
        }
        return count;
    }
}