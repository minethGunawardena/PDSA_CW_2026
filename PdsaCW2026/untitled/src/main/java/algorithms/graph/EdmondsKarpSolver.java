package algorithms.graph;

import java.util.*;

public class EdmondsKarpSolver {

    private static final String SOURCE = "A";
    private static final String SINK = "T";

    public static int maxFlow(HashMap<String, HashMap<String, Integer>> graph) {

        // Residual graph
        HashMap<String, HashMap<String, Integer>> residual = new HashMap<>();

        for (String u : graph.keySet()) {
            residual.put(u, new HashMap<>(graph.get(u)));
        }

        int maxFlow = 0;

        while (true) {

            // BFS to find path
            HashMap<String, String> parent = new HashMap<>();
            Queue<String> queue = new LinkedList<>();

            queue.add(SOURCE);
            parent.put(SOURCE, null);

            while (!queue.isEmpty() && !parent.containsKey(SINK)) {

                String u = queue.poll();

                for (String v : residual.get(u).keySet()) {

                    if (!parent.containsKey(v) && residual.get(u).get(v) > 0) {
                        parent.put(v, u);
                        queue.add(v);
                    }
                }
            }

            // No path found
            if (!parent.containsKey(SINK)) {
                break;
            }

            // Find bottleneck
            int pathFlow = Integer.MAX_VALUE;

            for (String v = SINK; v != SOURCE; v = parent.get(v)) {
                String u = parent.get(v);
                pathFlow = Math.min(pathFlow, residual.get(u).get(v));
            }

            // Update residual graph
            for (String v = SINK; v != SOURCE; v = parent.get(v)) {
                String u = parent.get(v);

                residual.get(u).put(v, residual.get(u).get(v) - pathFlow);

                residual.get(v).putIfAbsent(u, 0);
                residual.get(v).put(u, residual.get(v).get(u) + pathFlow);
            }

            maxFlow += pathFlow;
        }

        return maxFlow;
    }
}