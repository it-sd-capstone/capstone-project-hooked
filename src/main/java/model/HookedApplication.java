package model;

import java.sql.*;
import java.util.Random;
import java.util.Scanner;

public class HookedApplication {

    public static final String DATABASE_NAME = "hooked";
    public static final String DATABASE_PATH = DATABASE_NAME + ".db";
    private static final int TIMEOUT_STATEMENT_S = 5;

    private static Scanner scanner;
    private static Random rng;

    private static PreparedStatement InsertIntoCatches;
    private static PreparedStatement InsertIntoSpecies;
    private static PreparedStatement InsertIntoUsers;
    private static PreparedStatement InsertIntoLocations;
    private static PreparedStatement InsertIntoBaits;

    public static Connection createConnection() {
        Connection result = null;
        try {
            result = DriverManager.getConnection("jdbc:sqlite:" + DATABASE_PATH);
            Statement command = result.createStatement();
            command.setQueryTimeout(TIMEOUT_STATEMENT_S);

            command.executeUpdate("""
                    CREATE TABLE IF NOT EXISTS Catches (
                    CatchID INTEGER PRIMARY KEY AUTOINCREMENT,
                    UserID INTEGER NOT NULL,
                    SpeciesID INTEGER NOT NULL,
                    LocationID INTEGER NOT NULL,
                    BaitID INTEGER NOT NULL,
                    DateCaught date, 
                    Notes TEXT,
                    FOREIGN KEY (userID) REFERENCES User(userID),
                    FOREIGN KEY (SpeciesID) REFERENCES Species(SpeciesID),
                    FOREIGN KEY (LocationID) REFERENCES Location(LocationID),
                    FOREIGN KEY (BaitID) REFERENCES Bait(BaitID)
                    );""");

            command.executeUpdate("""
                    CREATE TABLE IF NOT EXISTS Species (
                    SpeciesID INTEGER PRIMARY KEY AUTOINCREMENT,
                    SpeciesName STRING NOT NULL,
                    length DOUBLE NOT NULL,
                    weight DOUBLE NOT NULL
                    );""");

            command.executeUpdate("""
                    CREATE TABLE IF NOT EXISTS Users (
                    UserID INTEGER PRIMARY KEY AUTOINCREMENT,
                    firstName STRING NOT NULL,
                    lastName STRING NOT NULL,
                    UserName STRING NOT NULL,
                    password STRING NOT NULL
                    );""");

            command.executeUpdate("""
                    CREATE TABLE IF NOT EXISTS Locations (
                    LocationID INTEGER PRIMARY KEY AUTOINCREMENT,
                    LocationName STRING NOT NULL,
                    State STRING NOT NULL
                    );""");

            command.executeUpdate("""
                    CREATE TABLE IF NOT EXISTS Baits (
                    BaitID INTEGER PRIMARY KEY AUTOINCREMENT,
                    BaitType STRING NOT NULL
                    );""");

        } catch (Exception e) {
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
        } catch (Exception e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
        return result;
    }
}