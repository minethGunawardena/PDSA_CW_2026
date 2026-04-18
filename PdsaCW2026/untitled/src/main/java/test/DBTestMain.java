package test;

import database.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DBTestMain {

    public static void main(String[] args) {

        System.out.println("=== DATABASE TEST START ===");

        testConnection();
        testGameRounds();
        testAlgorithmTimes();

        System.out.println("=== DATABASE TEST END ===");
    }

    // ================= TEST 1: CONNECTION =================
    private static void testConnection() {
        try {
            Connection conn = DBConnection.connect();

            if (conn != null) {
                System.out.println("[OK] Database connection successful");
            } else {
                System.out.println("[FAIL] Connection is NULL");
            }

        } catch (Exception e) {
            System.out.println("[ERROR] Connection failed");
            e.printStackTrace();
        }
    }

    // ================= TEST 2: GAME ROUNDS =================
    private static void testGameRounds() {
        try {
            Connection conn = DBConnection.connect();

            String sql = "SELECT COUNT(*) FROM game_rounds";
            PreparedStatement ps = conn.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                System.out.println("[OK] Game rounds count: " + rs.getInt(1));
            }

        } catch (Exception e) {
            System.out.println("[ERROR] game_rounds query failed");
            e.printStackTrace();
        }
    }

    // ================= TEST 3: ALGORITHM TIMES =================
    private static void testAlgorithmTimes() {
        try {
            Connection conn = DBConnection.connect();

            String sql = "SELECT game_rounds.game_type, algorithm_times.algorithm_name, algorithm_times.time_taken " +
                    "FROM algorithm_times " +
                    "JOIN game_rounds ON algorithm_times.round_id = game_rounds.id";

            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            int count = 0;

            while (rs.next()) {
                count++;

                System.out.println(
                        rs.getString("game_type") + " | " +
                                rs.getString("algorithm_name") + " | " +
                                rs.getLong("time_taken")
                );
            }

            System.out.println("[OK] Algorithm time records: " + count);

        } catch (Exception e) {
            System.out.println("[ERROR] algorithm_times query failed");
            e.printStackTrace();
        }
    }
}