package algorithms.graph;

import java.util.*;

public class SnakeLadderSolver {

    public static int minDiceThrows(int[] board, int N) {
        boolean[] visited = new boolean[N * N + 1];

        Queue<int[]> queue = new LinkedList<>();
        queue.add(new int[]{1, 0}); // {cell, moves}

        visited[1] = true;

        while (!queue.isEmpty()) {
            int[] curr = queue.poll();
            int cell = curr[0];
            int moves = curr[1];

            if (cell == N * N) return moves;

            for (int dice = 1; dice <= 6; dice++) {
                int next = cell + dice;

                if (next > N * N) continue;

                if (!visited[next]) {
                    visited[next] = true;

                    // Apply snake or ladder
                    if (board[next] != -1)
                        next = board[next];

                    queue.add(new int[]{next, moves + 1});
                }
            }
        }

        return -1;
    }
}