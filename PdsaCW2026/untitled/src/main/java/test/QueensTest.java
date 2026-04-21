package test;
import algorithms.queens.SequentialQueensSolver;
import algorithms.queens.ThreadedQueensSolver;

import java.util.Arrays;

public class QueensTest {

    public static void main(String[] args) {

        System.out.println("=== 16 QUEENS ALGORITHM TESTS ===\n");

        testSequentialVsThreaded();
        testMultipleRunsConsistency();
        testPerformanceComparison();
        testThreadSafetyCheck();

        System.out.println("\n=== TESTING COMPLETED ===");
    }

    // test 1
    private static void testSequentialVsThreaded() {

        System.out.println("TEST 1: Sequential vs Threaded Output\n");

        int seqResult = SequentialQueensSolver.solve();
        int thrResult = ThreadedQueensSolver.solve();

        System.out.println("Sequential Solutions : " + seqResult);
        System.out.println("Threaded Solutions   : " + thrResult);

        if (seqResult == thrResult) {
            System.out.println("STATUS: PASS (Both give same solution count)\n");
        } else {
            System.out.println("STATUS: FAIL (Mismatch detected!)\n");
        }
    }

    //test 2
    private static void testMultipleRunsConsistency() {

        System.out.println("TEST 2: Consistency Across Multiple Runs\n");

        int prev = -1;
        boolean consistent = true;

        for (int i = 0; i < 3; i++) {

            int result = SequentialQueensSolver.solve();

            System.out.println("Run " + (i + 1) + " result: " + result);

            if (prev != -1 && result != prev) {
                consistent = false;
            }

            prev = result;
        }

        if (consistent) {
            System.out.println("STATUS: PASS (Consistent results)\n");
        } else {
            System.out.println("STATUS: FAIL (Inconsistent results!)\n");
        }
    }

    // test 3
    private static void testPerformanceComparison() {

        System.out.println("TEST 3: Performance Comparison\n");

        long startSeq = System.nanoTime();
        int seq = SequentialQueensSolver.solve();
        long endSeq = System.nanoTime();

        long seqTime = endSeq - startSeq;

        long startThr = System.nanoTime();
        int thr = ThreadedQueensSolver.solve();
        long endThr = System.nanoTime();

        long thrTime = endThr - startThr;

        System.out.println("Sequential Time : " + seqTime + " ns");
        System.out.println("Threaded Time   : " + thrTime + " ns");

        System.out.println("Solutions: " + seq + " (Seq) | " + thr + " (Thr)");

        if (thrTime < seqTime) {
            System.out.println("STATUS: Threaded is faster\n");
        } else {
            System.out.println("STATUS: Sequential is faster (or JVM overhead)\n");
        }
    }

    // test 4
    private static void testThreadSafetyCheck() {

        System.out.println("TEST 4: Thread Safety Check\n");

        int[] results = new int[5];

        for (int i = 0; i < 5; i++) {
            results[i] = ThreadedQueensSolver.solve();
        }

        boolean same = true;

        for (int i = 1; i < results.length; i++) {
            if (results[i] != results[0]) {
                same = false;
                break;
            }
        }

        System.out.println("Runs: " + Arrays.toString(results));

        if (same) {
            System.out.println("STATUS: PASS (Thread-safe consistent results)\n");
        } else {
            System.out.println("STATUS: FAIL (Race condition or inconsistency)\n");
        }
    }
}