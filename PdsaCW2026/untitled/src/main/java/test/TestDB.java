package test;

import database.DBConnection;
import java.sql.Connection;

public class TestDB {

    public static void main(String[] args) {
        Connection conn = DBConnection.connect();

        if (conn != null) {
            System.out.println("Connection successful!");
        } else {
            System.out.println("Connection failed!");
        }
    }
}