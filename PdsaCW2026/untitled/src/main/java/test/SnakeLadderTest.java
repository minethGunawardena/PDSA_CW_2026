package test;

import java.util.Arrays;

import algorithms.graph.SnakeLadderSolver;

public class SnakeLadderTest {

    public static void main(String[] args) {

        System.out.println("=== SNAKE & LADDER SOLVER TESTS ===\n");

        testCleanBoardSmall();
        testCleanBoardMedium();
        testWithSnake();
        testWithLadder();
        testMixedBoard();
        testEdgeCase2x2();
        testLargeBoard();

        System.out.println("\n=== TESTING COMPLETED ===");
    }

    private static void testCleanBoardSmall() {

        int N = 3;
        int[] board = cleanBoard(N);

        runTest("Clean Board - Small (3x3)", board, N);
    }

    private static void testCleanBoardMedium() {

        int N = 4;
        int[] board = cleanBoard(N);

        runTest("Clean Board - Medium (4x4)", board, N);
    }

    private static void testWithSnake() {

        int N = 4;
        int[] board = cleanBoard(N);

        // snake: higher → lower
        board[10] = 3;

        runTest("Board with Snake (10 -> 3)", board, N);
    }

    private static void testWithLadder() {

        int N = 4;
        int[] board = cleanBoard(N);

        // ladder: lower → higher
        board[2] = 14;

        runTest("Board with Ladder (2 -> 14)", board, N);
    }

    private static void testMixedBoard() {

        int N = 5;
        int[] board = cleanBoard(N);

        board[3] = 22;
        board[15] = 6;
        board[18] = 9;

        runTest("Mixed Board (Snakes + Ladders)", board, N);
    }

    private static void testEdgeCase2x2() {

        int N = 2;
        int[] board = cleanBoard(N);

        runTest("Edge Case - 2x2 Board", board, N);
    }

    private static void testLargeBoard() {

        int N = 10;
        int[] board = cleanBoard(N);

        board[5] = 25;
        board[30] = 12;
        board[60] = 90;

        runTest("Large Board (10x10)", board, N);
    }

    private static void runTest(String testName, int[] board, int N) {

        int[] copy = Arrays.copyOf(board, board.length);

        long start = System.nanoTime();
        int result = SnakeLadderSolver.minDiceThrows(copy, N);
        long end = System.nanoTime();

        System.out.println("Test: " + testName);
        System.out.println("Minimum Dice Throws: " + result);
        System.out.println("Time Taken (ns): " + (end - start));

        if (result >= 0) {
            System.out.println("STATUS: PASS\n");
        } else {
            System.out.println("STATUS: FAIL (No solution found)\n");
        }
    }

    private static int[] cleanBoard(int N) {

        int size = N * N;
        int[] board = new int[size + 1];

        Arrays.fill(board, -1);

        return board;
    }
}
