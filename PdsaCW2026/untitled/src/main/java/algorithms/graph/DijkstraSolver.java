package algorithms.graph;

import java.util.*;

public class DijkstraSolver {

    public static int solve(int[] board, int N) {
        int size = N * N;
        int[] dist = new int[size + 1];
        Arrays.fill(dist, Integer.MAX_VALUE);

        PriorityQueue<int[]> pq = new PriorityQueue<>(Comparator.comparingInt(a -> a[1]));

        dist[1] = 0;
        pq.add(new int[]{1, 0});

        while (!pq.isEmpty()) {
            int[] curr = pq.poll();
            int cell = curr[0];

            if (cell == size) return dist[cell];

            for (int i = 1; i <= 6; i++) {
                int next = cell + i;
                if (next > size) continue;

                if (board[next] != -1)
                    next = board[next];

                if (dist[next] > dist[cell] + 1) {
                    dist[next] = dist[cell] + 1;
                    pq.add(new int[]{next, dist[next]});
                }
            }
        }

        return -1;
    }
}