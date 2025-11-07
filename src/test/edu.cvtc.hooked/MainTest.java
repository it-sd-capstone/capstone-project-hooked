package edu.cvtc.hooked;

import java.sql.*;
import java.util.Random;
import java.util.Scanner;

public class MainTest {
    public static Connection createConnection() {
        Connection result = null;
        try {
            result = DriverManager.getConnection("jdbc:sqlite:" + DATABASE_PATH);
            Statement command = result.createStatement();
            command.setQueryTimeout(TIMEOUT_STATEMENT_S);

            command.executeUpdate("""
                    CREATE TABLE IF NOT EXISTS game (
                    id INTEGER PRIMARY KEY,
                    at TEXT NOT NULL
                    );""");

            sqlInsertGame = result.prepareStatement("INSERT INTO game (at) VALUES (datetime('now'))");
            sqlFetchGameCount = result.prepareStatement("SELECT COUNT(*) FROM game");
            sqlFetchRecentGames = result.prepareStatement("SELECT COUNT(*) FROM game WHERE at > datetime('now', '-7 days')");
            sqlReset = result.prepareStatement("DELETE FROM game");
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    public static String readLine() {
        try {
            if (scanner == null) scanner = new Scanner(System.in);
            if (scanner.hasNextLine()) return scanner.nextLine();
        } catch (Exception e) {
            return "";
        }
        return "";
    }

    public static void resetScanner() {
        if (scanner != null) scanner.close();
        scanner = null;
    }

    public static ResultSet queryRaw(Connection db, String sql) {
        ResultSet result = null;
        try {
            Statement statement = db.createStatement();
            result = statement.executeQuery(sql);
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
        return result;
    }
}
