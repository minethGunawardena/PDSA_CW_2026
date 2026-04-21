package test;

import java.util.Arrays;

import algorithms.minimumCost.GreedySolver;
import algorithms.minimumCost.HungarianSolver;

public class MinimumCostTest {

    public static void main(String[] args) {

        System.out.println("=== MINIMUM COST ALGORITHM TESTS ===\n");

        testSmallMatrix();
        testEqualValues();
        testIncreasingMatrix();
        testRandomMatrix();
        testEdgeCase1x1();

        System.out.println("\n=== TESTING COMPLETED ===");
    }

    private static void testSmallMatrix() {

        int[][] cost = {
            {9, 2, 7},
            {6, 4, 3},
            {5, 8, 1}
        };

        runTest("Small Matrix", cost);
    }

    private static void testEqualValues() {

        int[][] cost = {
            {5, 5, 5},
            {5, 5, 5},
            {5, 5, 5}
        };

        runTest("Equal Values Matrix", cost);
    }

    private static void testIncreasingMatrix() {

        int[][] cost = {
            {1, 2, 3},
            {2, 3, 4},
            {3, 4, 5}
        };

        runTest("Increasing Pattern Matrix", cost);
    }

    private static void testRandomMatrix() {

        int[][] cost = {
            {4, 1, 7},
            {2, 6, 5},
            {9, 3, 8}
        };

        runTest("Random Matrix", cost);
    }

    private static void testEdgeCase1x1() {

        int[][] cost = {
            {10}
        };

        runTest("Edge Case 1x1 Matrix", cost);
    }

    private static void runTest(String testName, int[][] original) {

        int[][] copy1 = deepCopy(original);
        int[][] copy2 = deepCopy(original);

        int greedy = GreedySolver.solve(copy1);
        int hungarian = HungarianSolver.solve(copy2);

        System.out.println("Test: " + testName);
        System.out.println("Greedy Result     : " + greedy);
        System.out.println("Hungarian Result  : " + hungarian);

        if (hungarian <= greedy) {
            System.out.println("STATUS: PASS (Hungarian optimal or equal)\n");
        } else {
            System.out.println("STATUS: FAIL (Greedy better than expected - check logic)\n");
        }
    }

    private static int[][] deepCopy(int[][] matrix) {

        int[][] copy = new int[matrix.length][matrix.length];

        for (int i = 0; i < matrix.length; i++) {
            copy[i] = Arrays.copyOf(matrix[i], matrix[i].length);
        }

        return copy;
    }
}
