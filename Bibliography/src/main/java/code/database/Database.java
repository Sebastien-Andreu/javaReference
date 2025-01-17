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
            System.out.println("error verifyIfThemeAlreadyExist");
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

    protected int verifyIfAffiliationExist(String name) {
        String sql = "select * from Affiliation where name = '" + name + "'";
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
        String query2 = "select name from Theme order by name";
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
        String query2 = "select name from TypeOfDocument order by name";
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
        String query2 = "select * from Author order by author";
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
        String query2 = "select Key from KeyWord order by Key";
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

    public ArrayList<String> getAllAffiliation() {
        String query2 = "select name from affiliation order by name";
        ArrayList list = new ArrayList();

        try (Connection conn = connect(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query2)){
            while(rs.next()) {
                list.add(rs.getString("name"));
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
        String affiliation = "CREATE TABLE \"Affiliation\" (\n\t\"ID\"\tINTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE,\n\t\"name\"\tREAL NOT NULL\n)";
        String typeOfDocument = "CREATE TABLE \"TypeOfDocument\" (\n\t\"ID\"\tINTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE,\n\t\"name\"\tREAL NOT NULL\n)";
        String importLivre = "INSERT INTO TypeOfDocument (name) VALUES(\"Book\")";
        String importAC = "INSERT INTO TypeOfDocument (name) VALUES(\"Conference article\")";
        String importBookChapter = "INSERT INTO TypeOfDocument (name) VALUES(\"Book chapter\")";
        String importStandard = "INSERT INTO TypeOfDocument (name) VALUES(\"Standard\")";
        String importPatent = "INSERT INTO TypeOfDocument (name) VALUES(\"Patent\")";
        String importTechnicalReport = "INSERT INTO TypeOfDocument (name) VALUES(\"Technical report\")";
        String importPhdThesis = "INSERT INTO TypeOfDocument (name) VALUES(\"PhD Thesis\")";
        String importMscThesis = "INSERT INTO TypeOfDocument (name) VALUES(\"MSc Thesis\")";
        String importStudentReport = "INSERT INTO TypeOfDocument (name) VALUES(\"Student report\")";
        String importAEGISReport = "INSERT INTO TypeOfDocument (name) VALUES(\"AEGIS report\")";
        String importNewspaper = "INSERT INTO TypeOfDocument (name) VALUES(\"Newspaper\")";
        String importOnlineArticle = "INSERT INTO TypeOfDocument (name) VALUES(\"Online article\")";
        String importPressRelease = "INSERT INTO TypeOfDocument (name) VALUES(\"Press release\")";
        String importSoftwareManual = "INSERT INTO TypeOfDocument (name) VALUES(\"Software manual\")";
        String importLectureNote = "INSERT INTO TypeOfDocument (name) VALUES(\"Lecture note\")";
        String importConferencePresentation = "INSERT INTO TypeOfDocument (name) VALUES(\"Conference presentation\")";
        String importJournalArticle = "INSERT INTO TypeOfDocument (name) VALUES(\"Journal article\")";
        String importInternalDoc = "INSERT INTO TypeOfDocument (name) VALUES(\"Internal document\")";
        String importPublicDoc = "INSERT INTO TypeOfDocument (name) VALUES(\"Public document\")";
        String importTest = "INSERT INTO TypeOfDocument (name) VALUES(\"Test\")";

        String author = "CREATE TABLE \"Author\" (\n\t\"ID\"\tINTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE,\n\t\"author\"\tREAL NOT NULL\n)";

        try (Connection conn = DriverManager.getConnection(url); Statement stmt = conn.createStatement()){
            stmt.execute(author);
            stmt.execute(theme);
            stmt.execute(typeOfDocument);
            stmt.execute(affiliation);
            stmt.execute(tag);
            stmt.execute(numberOfFile);
            stmt.execute(importLivre);
            stmt.execute(importAC);
            stmt.execute(importNumber);
            stmt.execute(importBookChapter);
            stmt.execute(importStandard);
            stmt.execute(importPatent);
            stmt.execute(importTechnicalReport);
            stmt.execute(importPhdThesis);
            stmt.execute(importMscThesis);
            stmt.execute(importLectureNote);
            stmt.execute(importStudentReport);
            stmt.execute(importAEGISReport);
            stmt.execute(importNewspaper);
            stmt.execute(importOnlineArticle);
            stmt.execute(importPressRelease);
            stmt.execute(importSoftwareManual);
            stmt.execute(importConferencePresentation);
            stmt.execute(importJournalArticle);
            stmt.execute(importInternalDoc);
            stmt.execute(importPublicDoc);
            stmt.execute(importTest);
        } catch (SQLException var41) {
            System.out.println(var41.getMessage());
        }
    }
}
