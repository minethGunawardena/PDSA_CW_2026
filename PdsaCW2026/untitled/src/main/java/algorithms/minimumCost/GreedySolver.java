package algorithms.minimumCost;

public class GreedySolver {

    public static int solve(int[][] cost) {

        int n = cost.length;

        boolean[] usedEmployee = new boolean[n];
        int total = 0;

        for (int i = 0; i < n; i++) {

            int best = Integer.MAX_VALUE;
            int bestJ = -1;

            for (int j = 0; j < n; j++) {
                if (!usedEmployee[j] && cost[i][j] < best) {
                    best = cost[i][j];
                    bestJ = j;
                }
            }

            usedEmployee[bestJ] = true;
            total += best;
        }

        return total;
    }
}