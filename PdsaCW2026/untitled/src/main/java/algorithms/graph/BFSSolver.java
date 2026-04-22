package algorithms.graph;

import java.util.*;

public class BFSSolver {

    /** Maximum value on a standard dice. */
    private static final int MAX_DICE = 6;

    /** Starting cell of the Snake and Ladder board. */
    private static final int START_CELL = 1;

    /**
     * Finds the minimum number of dice throws to reach the last cell
     * of a Snake and Ladder board using Breadth-First Search.
     *
     * @param board  1D board array; board[i] = -1 if no snake/ladder at cell i,
     *               otherwise board[i] = destination cell.
     * @param N      Board dimension (board is N x N).
     * @return       Minimum dice throws to reach cell N*N from cell 1,
     *               or -1 if no path exists.
     */
    public static int solve(int[] board, int N) {
        boolean[] visited = new boolean[N * N + 1];
        Queue<int[]> queue = new LinkedList<>();

        // Start at cell 1 with 0 moves
        queue.add(new int[]{START_CELL, 0});
        visited[START_CELL] = true;

        while (!queue.isEmpty()) {
            int[] curr = queue.poll();
            int cell = curr[0];
            int moves = curr[1];

            // Reached the final cell — return the move count
            if (cell == N * N) return moves;

            // Simulate each possible dice roll
            for (int dice = 1; dice <= MAX_DICE; dice++) {
                int next = cell + dice;
                if (next > N * N) continue;

                if (!visited[next]) {
                    visited[next] = true;

                    // Apply snake or ladder if present
                    if (board[next] != -1)
                        next = board[next];

                    queue.add(new int[]{next, moves + 1});
                }
            }
        }

        return -1;
    }
}