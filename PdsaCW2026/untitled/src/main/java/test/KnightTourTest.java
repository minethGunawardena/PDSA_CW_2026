package test;

import algorithms.chess.KnightTourHeuristic;

public class KnightTourTest {

    public static void main(String[] args) {

        System.out.println("=== KNIGHT'S TOUR HEURISTIC TESTS ===\n");

        testCenterStart();
        testCornerStart();
        testRandomStart();
        testMultipleRuns();
        testBoardValidation();

        System.out.println("\n=== TESTING COMPLETED ===");
    }

    //test 1
    private static void testCenterStart() {

        System.out.println("TEST 1: Center Start (8x8)");

        int N = 8;
        int[][] board = new int[N][N];

        boolean result = KnightTourHeuristic.solve(N, 3, 3, board);

        System.out.println("Start Position: (3,3)");
        evaluateBoard(result, board, N);
    }

    // test 2
    private static void testCornerStart() {

        System.out.println("TEST 2: Corner Start (8x8)");

        int N = 8;
        int[][] board = new int[N][N];

        boolean result = KnightTourHeuristic.solve(N, 0, 0, board);

        System.out.println("Start Position: (0,0)");
        evaluateBoard(result, board, N);
    }

    //test 3
    private static void testRandomStart() {

        System.out.println("TEST 3: Random Start (10x10)");

        int N = 10;
        int[][] board = new int[N][N];

        boolean result = KnightTourHeuristic.solve(N, 2, 5, board);

        System.out.println("Start Position: (2,5)");
        evaluateBoard(result, board, N);
    }

    // test 4
    private static void testMultipleRuns() {

        System.out.println("TEST 4: Multiple Runs Consistency");

        int N = 8;

        for (int i = 0; i < 3; i++) {

            int[][] board = new int[N][N];
            boolean result = KnightTourHeuristic.solve(N, 0, 0, board);

            System.out.println("Run " + (i + 1) + ": " + (result ? "SUCCESS" : "FAIL"));
        }

        System.out.println();
    }

    //test 5
    private static void testBoardValidation() {

        System.out.println("TEST 5: Board Validation (Visited Check)");

        int N = 8;
        int[][] board = new int[N][N];

        boolean result = KnightTourHeuristic.solve(N, 0, 0, board);

        if (!result) {
            System.out.println("Tour failed - invalid path\n");
            return;
        }

        boolean[] visited = new boolean[N * N];
        boolean valid = true;

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {

                int val = board[i][j];

                if (val < 0 || val >= N * N || visited[val]) {
                    valid = false;
                    break;
                }

                visited[val] = true;
            }
        }

        System.out.println("All squares unique: " + valid);
        System.out.println("STATUS: " + (valid ? "PASS" : "FAIL") + "\n");
    }

    private static void evaluateBoard(boolean result, int[][] board, int N) {

        if (!result) {
            System.out.println("RESULT: FAIL (No complete tour found)\n");
            return;
        }

        boolean[] visited = new boolean[N * N];
        boolean valid = true;

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {

                int val = board[i][j];

                if (val < 0 || val >= N * N || visited[val]) {
                    valid = false;
                } else {
                    visited[val] = true;
                }
            }
        }

        System.out.println("RESULT: " + (valid ? "PASS (Valid Tour)" : "FAIL"));
        System.out.println();
    }
}
