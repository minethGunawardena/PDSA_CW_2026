package algorithms.graph;

import java.util.*;

public class FordFulkersonSolver {

    private static final String SOURCE = "A";
    private static final String SINK = "T";

    public static int maxFlow(HashMap<String, HashMap<String, Integer>> graph) {

        HashMap<String, HashMap<String, Integer>> residual = new HashMap<>();

        for (String u : graph.keySet()) {
            residual.put(u, new HashMap<>(graph.get(u)));
        }

        int maxFlow = 0;

        while (true) {

            HashSet<String> visited = new HashSet<>();

            int flow = dfs(residual, SOURCE, Integer.MAX_VALUE, visited);

            if (flow == 0) {
                break;
            }

            maxFlow += flow;
        }

        return maxFlow;
    }

    // DFS to find augmenting path
    private static int dfs(HashMap<String, HashMap<String, Integer>> residual,
                           String node,
                           int flow,
                           HashSet<String> visited) {

        if (node.equals(SINK)) {
            return flow;
        }

        visited.add(node);

        for (String next : residual.get(node).keySet()) {

            int capacity = residual.get(node).get(next);

            if (!visited.contains(next) && capacity > 0) {

                int minFlow = Math.min(flow, capacity);

                int result = dfs(residual, next, minFlow, visited);

                if (result > 0) {

                    // update residual
                    residual.get(node).put(next, residual.get(node).get(next) - result);

                    residual.get(next).putIfAbsent(node, 0);
                    residual.get(next).put(node, residual.get(next).get(node) + result);

                    return result;
                }
            }
        }

        return 0;
    }
}