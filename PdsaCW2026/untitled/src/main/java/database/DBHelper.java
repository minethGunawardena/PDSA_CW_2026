package database;

import java.sql.*;

public class DBHelper {

    public static int insertRound(String gameType) {
        try (Connection conn = DBConnection.connect()) {

            String sql = "INSERT INTO game_rounds (game_type) VALUES (?)";
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            ps.setString(1, gameType);
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) return rs.getInt(1);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static void insertTime(int roundId, String algo, long time) {
        try (Connection conn = DBConnection.connect()) {

            String sql = "INSERT INTO algorithm_times (round_id, algorithm_name, time_taken) VALUES (?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, roundId);
            ps.setString(2, algo);
            ps.setLong(3, time);

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void insertSolution(int roundId, int answer) {
        try (Connection conn = DBConnection.connect()) {

            String sql = "INSERT INTO solutions (round_id, correct_answer) VALUES (?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, roundId);
            ps.setInt(2, answer);

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static int getOrCreatePlayer(String name) {
        try (Connection conn = DBConnection.connect()) {

            String check = "SELECT player_id FROM players WHERE name = ?";
            PreparedStatement ps = conn.prepareStatement(check);
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) return rs.getInt("player_id");

            String insert = "INSERT INTO players (name) VALUES (?)";
            PreparedStatement ps2 = conn.prepareStatement(insert, Statement.RETURN_GENERATED_KEYS);
            ps2.setString(1, name);
            ps2.executeUpdate();

            ResultSet rs2 = ps2.getGeneratedKeys();
            if (rs2.next()) return rs2.getInt(1);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static void savePlayerAnswer(int playerId, int roundId, int answer, boolean isCorrect) {
        try (Connection conn = DBConnection.connect()) {

            String sql = "INSERT INTO player_answers (player_id, round_id, answer, is_correct) VALUES (?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, playerId);
            ps.setInt(2, roundId);
            ps.setInt(3, answer);
            ps.setBoolean(4, isCorrect);

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static ResultSet getAllTimes() {
        try {
            Connection conn = DBConnection.connect();

            String sql = "SELECT g.game_type AS game_name, a.algorithm_name AS algorithm, a.time_taken AS time_ns " +
                    "FROM algorithm_times a " +
                    "JOIN game_rounds g ON a.round_id = g.id";

            PreparedStatement ps = conn.prepareStatement(sql);
            return ps.executeQuery();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}