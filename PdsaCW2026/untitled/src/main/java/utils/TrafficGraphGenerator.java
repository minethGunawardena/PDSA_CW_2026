package utils;

import java.util.Random;
import java.util.HashMap;

public class TrafficGraphGenerator {

    public static HashMap<String, HashMap<String, Integer>> generate() {

        Random rand = new Random();

        HashMap<String, HashMap<String, Integer>> graph = new HashMap<>();

        String[] nodes = {"A","B","C","D","E","F","G","H","T"};

        for (String node : nodes) {
            graph.put(node, new HashMap<>());
        }

        addEdge(graph, "A", "B", rand);
        addEdge(graph, "A", "C", rand);
        addEdge(graph, "A", "D", rand);

        addEdge(graph, "B", "E", rand);
        addEdge(graph, "B", "F", rand);

        addEdge(graph, "C", "E", rand);
        addEdge(graph, "C", "F", rand);

        addEdge(graph, "D", "F", rand);

        addEdge(graph, "E", "G", rand);
        addEdge(graph, "E", "H", rand);

        addEdge(graph, "F", "H", rand);

        addEdge(graph, "G", "T", rand);
        addEdge(graph, "H", "T", rand);

        return graph;
    }

    private static void addEdge(HashMap<String, HashMap<String, Integer>> graph,
                                String from, String to, Random rand) {

        int capacity = 5 + rand.nextInt(11); // 5–15
        graph.get(from).put(to, capacity);
    }
}