package test;

import algorithms.graph.EdmondsKarpSolver;
import algorithms.graph.FordFulkersonSolver;

import java.util.HashMap;

public class TrafficTest {

    public static void main(String[] args) {

        System.out.println("=== TRAFFIC FLOW (MAX FLOW) ALGORITHM TESTS ===\n");

        testSmallNetwork();
        testBottleneckNetwork();
        testComplexNetwork();
        testSinglePathNetwork();
        testConsistencyCheck();

        System.out.println("\n=== TESTING COMPLETED ===");
    }


    private static void testSmallNetwork() {

        System.out.println("TEST 1: Small Network");

        HashMap<String, HashMap<String, Integer>> graph = new HashMap<>();

        addEdge(graph, "A", "B", 10);
        addEdge(graph, "A", "C", 5);
        addEdge(graph, "B", "T", 10);
        addEdge(graph, "C", "T", 5);

        runTest("Small Network", graph);
    }


    private static void testBottleneckNetwork() {

        System.out.println("TEST 2: Bottleneck Network");

        HashMap<String, HashMap<String, Integer>> graph = new HashMap<>();

        addEdge(graph, "A", "B", 100);
        addEdge(graph, "B", "C", 1);   // bottleneck
        addEdge(graph, "C", "T", 100);

        runTest("Bottleneck Network", graph);
    }


    private static void testComplexNetwork() {

        System.out.println("TEST 3: Complex Network");

        HashMap<String, HashMap<String, Integer>> graph = new HashMap<>();

        addEdge(graph, "A", "B", 10);
        addEdge(graph, "A", "C", 10);
        addEdge(graph, "B", "D", 4);
        addEdge(graph, "B", "C", 2);
        addEdge(graph, "C", "D", 8);
        addEdge(graph, "D", "T", 10);

        runTest("Complex Network", graph);
    }


    private static void testSinglePathNetwork() {

        System.out.println("TEST 4: Single Path Network");

        HashMap<String, HashMap<String, Integer>> graph = new HashMap<>();

        addEdge(graph, "A", "B", 7);
        addEdge(graph, "B", "C", 5);
        addEdge(graph, "C", "T", 3);

        runTest("Single Path", graph);
    }


    private static void testConsistencyCheck() {

        System.out.println("TEST 5: Consistency Check (Repeated Runs)");

        HashMap<String, HashMap<String, Integer>> graph = new HashMap<>();

        addEdge(graph, "A", "B", 15);
        addEdge(graph, "A", "C", 10);
        addEdge(graph, "B", "T", 10);
        addEdge(graph, "C", "T", 10);

        int base1 = EdmondsKarpSolver.maxFlow(clone(graph));
        int base2 = FordFulkersonSolver.maxFlow(clone(graph));

        System.out.println("Edmonds-Karp Result : " + base1);
        System.out.println("Ford-Fulkerson Result: " + base2);

        if (base1 == base2) {
            System.out.println("STATUS: PASS (Consistent results)\n");
        } else {
            System.out.println("STATUS: FAIL (Mismatch detected)\n");
        }
    }


    private static void runTest(String name,
                                HashMap<String, HashMap<String, Integer>> graph) {

        int ek = EdmondsKarpSolver.maxFlow(clone(graph));
        int ff = FordFulkersonSolver.maxFlow(clone(graph));

        System.out.println("Test: " + name);
        System.out.println("Edmonds-Karp  : " + ek);
        System.out.println("Ford-Fulkerson: " + ff);

        if (ek == ff) {
            System.out.println("STATUS: PASS (Same max flow)\n");
        } else {
            System.out.println("STATUS: FAIL (Different results)\n");
        }
    }

 
    private static void addEdge(HashMap<String, HashMap<String, Integer>> graph,
                                String u, String v, int cap) {

        graph.putIfAbsent(u, new HashMap<>());
        graph.get(u).put(v, cap);

        graph.putIfAbsent(v, new HashMap<>());
        graph.get(v).putIfAbsent(u, 0);
    }

    private static HashMap<String, HashMap<String, Integer>> clone(
            HashMap<String, HashMap<String, Integer>> original) {

        HashMap<String, HashMap<String, Integer>> copy = new HashMap<>();

        for (String u : original.keySet()) {
            copy.put(u, new HashMap<>(original.get(u)));
        }

        return copy;
    }
}