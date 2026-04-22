package algorithms.graph;

import java.util.*;

public class DijkstraSolver {

    /** Maximum value on a standard dice. */
    private static final int MAX_DICE = 6;

    /** Starting cell of the Snake and Ladder board. */
    private static final int START_CELL = 1;

    /**
     * Finds the minimum number of dice throws to reach the last cell
     * of a Snake and Ladder board using Dijkstra's algorithm.
     *
     * @param board  1D board array; board[i] = -1 if no snake/ladder at cell i,
     *               otherwise board[i] = destination cell.
     * @param N      Board dimension (board is N x N).
     * @return       Minimum dice throws to reach cell N*N from cell 1,
     *               or -1 if no path exists.
     */
    public static int solve(int[] board, int N) {
        int size = N * N;

        // dist[i] = minimum dice throws to reach cell i
        int[] dist = new int[size + 1];
        Arrays.fill(dist, Integer.MAX_VALUE);

        // Min-heap ordered by distance
        PriorityQueue<int[]> pq = new PriorityQueue<>(Comparator.comparingInt(a -> a[1]));

        dist[START_CELL] = 0;
        pq.add(new int[]{START_CELL, 0});

        while (!pq.isEmpty()) {
            int[] curr = pq.poll();
            int cell = curr[0];

            // Reached the final cell — return minimum throws
            if (cell == size) return dist[cell];

            // Simulate each possible dice roll
            for (int dice = 1; dice <= MAX_DICE; dice++) {
                int next = cell + dice;
                if (next > size) continue;

                // Apply snake or ladder if present
                if (board[next] != -1)
                    next = board[next];

                // Relax the distance if a shorter path is found
                if (dist[next] > dist[cell] + 1) {
                    dist[next] = dist[cell] + 1;
                    pq.add(new int[]{next, dist[next]});
                }
            }
        }

        return -1;
    }
}