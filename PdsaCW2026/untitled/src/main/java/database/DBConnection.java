package database;
import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {

    private static final String URL = "jdbc:mysql://localhost:3306/game_system";
    private static final String USER = "root";
    private static final String PASSWORD = "root"; // change this

    public static Connection connect() {
        try {
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Connected to MySQL!");
            return conn;
        } catch (Exception e) {
            System.out.println("Connection failed!");
            e.printStackTrace();
            return null;
        }
    }
}
