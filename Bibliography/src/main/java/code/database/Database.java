package code.database;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public abstract class Database {

    public static Connection connect() {
        String url = "jdbc:sqlite:BD.db";
        Connection conn = null;
        File directory = new File("BD.db");
        if (!directory.exists()) {
            createDatabase(url);
        }
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException var4) {
            System.out.println(var4.getMessage());
        }

        return conn;
    }

    protected int verifyIfThemeAlreadyExist(String theme) {
        String sql = "select * from Theme where name = '" + theme + "'";
        int result = 0;
        try (Connection conn = connect(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)){
            if (rs.getInt("ID") > 0) {
                result = rs.getInt("ID");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return result;
    }

    protected int verifyIfAuthorExist(String author) {
        String sql = "select * from Author where author = '" + author + "'";
        int result = 0;

        try (Connection conn = connect(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)){
            if (rs.getInt("ID") > 0) {
                result = rs.getInt("ID");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return result;
    }

    protected int verifyIfKeyWordExist(String key) {
        String sql = "select * from KeyWord where Key = '" + key + "'";
        int result = 0;

        try (Connection conn = connect(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)){
            if (rs.getInt("ID") > 0) {
                result = rs.getInt("ID");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return result;
    }

    public ArrayList<String> getAllTheme() {
        String query2 = "select name from Theme";
        ArrayList list = new ArrayList();

        try (Connection conn = connect();Statement stmt = conn.createStatement();ResultSet rs = stmt.executeQuery(query2)){
            list.add("All");
            while(rs.next()) {
                list.add(rs.getString("name"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return list;
    }

    public ArrayList<String> getAllTypeOfDocument() {
        String query2 = "select name from TypeOfDocument";
        ArrayList list = new ArrayList();

        try (Connection conn = connect(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query2)){
            list.add("All");

            while(rs.next()) {
                list.add(rs.getString("name"));
            }
        } catch (SQLException var61) {
            System.out.println(var61.getMessage());
        }

        return list;
    }

    public ArrayList<String> getAllAuthor() {
        String query2 = "select * from Author";
        ArrayList list = new ArrayList();

        try (Connection conn = connect(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query2)){
            while(rs.next()) {
                list.add(rs.getString("author"));
            }
        } catch (SQLException var61) {
            System.out.println(var61.getMessage());
        }

        return list;
    }

    public ArrayList<String> getAllKeyWord() {
        String query2 = "select Key from KeyWord";
        ArrayList list = new ArrayList();

        try (Connection conn = connect(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query2)){
            while(rs.next()) {
                list.add(rs.getString("Key"));
            }
        } catch (SQLException var61) {
            System.out.println(var61.getMessage());
        }

        return list;
    }

    public static int getNumberOfFile() {
        int number = 0;

        try (Connection conn = connect(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery("select number from NumberOfFile where ID = 1")){
            number = rs.getInt("number");
        } catch (SQLException var59) {
            System.out.println(var59.getMessage());
        }

        return number + 1;
    }

    private static void createDatabase(String url) {
        String tag = "CREATE TABLE IF NOT EXISTS \"KeyWord\" (\n\t\"ID\"\tINTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE,\n\t\"Key\"\tREAL NOT NULL\n)";
        String numberOfFile = "CREATE TABLE IF NOT EXISTS \"NumberOfFile\" (\n\t\"number\"\tINTEGER NOT NULL DEFAULT 0,\n\t\"ID\"\tINTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE)";
        String importNumber = "INSERT INTO NumberOfFile (number) VALUES(0)";
        String theme = "CREATE TABLE \"Theme\" (\n\t\"ID\"\tINTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE,\n\t\"name\"\tREAL NOT NULL\n)";
        String typeOfDocument = "CREATE TABLE \"TypeOfDocument\" (\n\t\"ID\"\tINTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE,\n\t\"name\"\tREAL NOT NULL\n)";
        String importLivre = "INSERT INTO TypeOfDocument (name) VALUES(\"Livre\")";
        String importAC = "INSERT INTO TypeOfDocument (name) VALUES(\"Article de conf\u00e9rence\")";
        String author = "CREATE TABLE \"Author\" (\n\t\"ID\"\tINTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE,\n\t\"author\"\tREAL NOT NULL\n)";

        try (Connection conn = DriverManager.getConnection(url); Statement stmt = conn.createStatement()){
            stmt.execute(author);
            stmt.execute(theme);
            stmt.execute(typeOfDocument);
            stmt.execute(tag);
            stmt.execute(numberOfFile);
            stmt.execute(importLivre);
            stmt.execute(importAC);
            stmt.execute(importNumber);
        } catch (SQLException var41) {
            System.out.println(var41.getMessage());
        }
    }
}
