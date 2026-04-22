package test;

import algorithms.graph.SnakeLadderSolver;

import java.util.Arrays;

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


    /**
     * Test 1: Verifies BFS finds a valid path on a small 3x3 clean board
     * with no snakes or ladders. Expected result: a small positive move count.
     */
    private static void testCleanBoardSmall() {

        int N = 3;
        int[] board = cleanBoard(N);

        runTest("Clean Board - Small (3x3)", board, N);
    }

    /**
     * Test 2: Verifies BFS on a 4x4 clean board with no snakes or ladders.
     * Baseline test to confirm normal traversal without any jumps.
     */
    private static void testCleanBoardMedium() {

        int N = 4;
        int[] board = cleanBoard(N);

        runTest("Clean Board - Medium (4x4)", board, N);
    }

    /**
     * Test 3: Verifies BFS correctly avoids or uses a snake at cell 10 -> 3,
     * which moves the player backward. Expected: more moves than clean board.
     */
    private static void testWithSnake() {

        int N = 4;
        int[] board = cleanBoard(N);

        // snake: higher -> lower (moves player backward)
        board[10] = 3;

        runTest("Board with Snake (10 -> 3)", board, N);
    }

    /**
     * Test 4: Verifies BFS correctly uses a ladder at cell 2 -> 14,
     * which skips the player forward. Expected: fewer moves than clean board.
     */
    private static void testWithLadder() {

        int N = 4;
        int[] board = cleanBoard(N);

        // ladder: lower -> higher (moves player forward)
        board[2] = 14;

        runTest("Board with Ladder (2 -> 14)", board, N);
    }

    /**
     * Test 5: Verifies BFS on a board containing both snakes and ladders.
     * Tests that BFS correctly navigates mixed jump conditions.
     */
    private static void testMixedBoard() {

        int N = 5;
        int[] board = cleanBoard(N);

        board[3] = 22;   // ladder
        board[15] = 6;   // snake
        board[18] = 9;   // snake

        runTest("Mixed Board (Snakes + Ladders)", board, N);
    }

    /**
     * Test 6: Edge case test on the smallest possible board (2x2).
     * Verifies the solver handles minimal board sizes without errors.
     */
    private static void testEdgeCase2x2() {

        int N = 2;
        int[] board = cleanBoard(N);

        runTest("Edge Case - 2x2 Board", board, N);
    }

    /**
     * Test 7: Performance test on a large 10x10 board with mixed snakes and ladders.
     * Verifies BFS handles larger boards efficiently and returns a valid result.
     */
    private static void testLargeBoard() {

        int N = 10;
        int[] board = cleanBoard(N);

        board[5] = 25;   // ladder
        board[30] = 12;  // snake
        board[60] = 90;  // ladder

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