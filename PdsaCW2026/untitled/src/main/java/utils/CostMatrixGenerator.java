package utils;

import java.util.Random;

public class CostMatrixGenerator {

    public static int[][] generate(int n) {

        Random rand = new Random();
        int[][] cost = new int[n][n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                cost[i][j] = 20 + rand.nextInt(181); // 20–200
            }
        }

        return cost;
    }
}