package singleton;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SingletonDatabase {
    public static SingletonDatabase instance = new SingletonDatabase();
    public int IDFile = 0;
    public int IDFileSelected;

    private SingletonDatabase(){}

    public static SingletonDatabase getInstance(){
        return instance;
    }

    private Connection connect() {
        String url = "jdbc:sqlite:BD.db";
        Connection conn = null;
        File directory = new File(url);
        if (!directory.exists()) {
            System.out.println("existe pas");
            createDatabase();
        }
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    public void showDetailsOfFile(String name) {
        String query = "select * from File where name = '" +name +"'";
        try (Connection conn = this.connect(); Statement stmt  = conn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            IDFileSelected = rs.getInt("ID");
            if (IDFileSelected > 0) {
                SingletonController.getInstance().getController().detailsTitle.setText(rs.getString("title"));
                SingletonController.getInstance().getController().detailsName.setText(rs.getString("name"));
                SingletonController.getInstance().getController().detailsAuthor.setText(rs.getString("author"));
                SingletonController.getInstance().getController().detailsDate.setText(rs.getString("date"));
                SingletonController.getInstance().getController().detailsTheme.setText(rs.getString("theme"));
                SingletonController.getInstance().getController().detailsTypeOfDocument.setText(rs.getString("typeOfDocument"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        String query2 = "select * from KeyWord where ID_File = '" + IDFileSelected +"'";
        SingletonController.getInstance().getController().tags.clear();
        try (Connection conn = this.connect(); Statement stmt  = conn.createStatement(); ResultSet rs = stmt.executeQuery(query2)) {
            int id = rs.getInt("ID");
            if (id > 0 && (!Collections.singletonList(rs.getString("Key")).get(0).equals("[]")))  {
                for (String str : getList(rs.getString("Key"))){
                    SingletonController.getInstance().getController().tags.add(str);
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        String query3 = "select * from Note where ID_File = '" + IDFileSelected +"'";
        SingletonController.getInstance().getController().notes.clear();
        try (Connection conn = this.connect(); Statement stmt  = conn.createStatement(); ResultSet rs = stmt.executeQuery(query3)) {
            int id = rs.getInt("ID");
            if (id > 0 && (!Collections.singletonList(rs.getString("Note")).get(0).equals("[]")))  {
                for (String str : getList(rs.getString("Note"))){
                    SingletonController.getInstance().getController().notes.add(str);
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        String query4 = "select * from Quote where ID_File = '" + IDFileSelected +"'";
        SingletonController.getInstance().getController().quotes.clear();
        try (Connection conn = this.connect(); Statement stmt  = conn.createStatement(); ResultSet rs = stmt.executeQuery(query4)) {
            int id = rs.getInt("ID");
            if (id > 0 && (!Collections.singletonList(rs.getString("Quote")).get(0).equals("[]")))  {
                for (String str : getList(rs.getString("Quote"))){
                    SingletonController.getInstance().getController().quotes.add(str);
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void saveFile(){
        String sql;
        if (SingletonController.getInstance().getController().buttonUpdateFile.isVisible()){
            sql = "update File set name = ?, extension = ? ,title = ? ,author = ?, date = ?, theme = ?, typeOfDocument = ? where ID = " + IDFileSelected;
        }else {
            sql = "INSERT INTO File(name,extension,title,author, date, theme, typeOfDocument) VALUES(?,?,?,?,?,?,?)";
        }

        try (Connection conn = this.connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, SingletonController.getInstance().getController().titleOfImportFileAdd.getText());
            pstmt.setString(2, SingletonController.getInstance().getController().extensionOfFile);
            pstmt.setString(3, SingletonController.getInstance().getController().titleAdd.getText());
            pstmt.setString(4, SingletonController.getInstance().getController().authorAdd.getText());
            pstmt.setString(5, SingletonController.getInstance().getController().dateAdd.getValue().toString());
            pstmt.setString(6, SingletonController.getInstance().getController().themeAdd.getText());
            pstmt.setString(7, SingletonController.getInstance().getController().typeOfDocumentAdd.getText());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        String sqlSelect = "SELECT max(ID) FROM File";
        try (Connection conn = this.connect(); Statement stmt  = conn.createStatement(); ResultSet rs = stmt.executeQuery(sqlSelect)) {
            IDFile = rs.getInt("max(ID)");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        SingletonController.getInstance().getController().showIcon.setImage(null);
        SingletonController.getInstance().getController().titleOfImportFileAdd.setText("");
        SingletonController.getInstance().getController().titleAdd.setText("");
        SingletonController.getInstance().getController().authorAdd.setText("");
        SingletonController.getInstance().getController().dateAdd.setValue(null);
        SingletonController.getInstance().getController().themeAdd.setText("");
        SingletonController.getInstance().getController().typeOfDocumentAdd.setText("");
        saveKeyWord();
    }

    private void saveKeyWord(){
        String sql;
        if (SingletonController.getInstance().getController().buttonUpdateFile.isVisible()){
            sql = "update KeyWord set Key = ?, ID_File = ? where ID_File = " + IDFileSelected;
            IDFile = IDFileSelected;
        }else {
            sql = "INSERT INTO KeyWord(Key, ID_File) VALUES(?,?)";
        }
        try (Connection conn = this.connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, String.valueOf(SingletonController.getInstance().getController().addTags));
            pstmt.setInt(2, IDFile);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        SingletonController.getInstance().getController().addTags.clear();
        saveNote();
    }

    private void saveNote(){
        String sql;
        if (SingletonController.getInstance().getController().buttonUpdateFile.isVisible()){
            sql = "update Note set Note = ?, ID_File = ? where ID_File = " + IDFileSelected;
        }else {
            sql = "INSERT INTO Note(Note, ID_File) VALUES(?,?)";
        }

        try (Connection conn = this.connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, String.valueOf(SingletonController.getInstance().getController().addNotes));
            pstmt.setInt(2, IDFile);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        SingletonController.getInstance().getController().addNotes.clear();
        saveQuote();
    }

    private void saveQuote(){
        String sql;
        if (SingletonController.getInstance().getController().buttonUpdateFile.isVisible()){
            sql = "update Quote set Quote = ?, ID_File = ? where ID_File = " + IDFileSelected;
        }else {
            sql = "INSERT INTO Quote(Quote, ID_File) VALUES(?,?)";
        }
        try (Connection conn = this.connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, String.valueOf(SingletonController.getInstance().getController().addQuotes));
            pstmt.setInt(2, IDFile);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        SingletonController.getInstance().getController().addQuotes.clear();
    }

    private List<String> getList(String list){
        String replace = list.replace("[","");
        String replace1 = replace.replace("]","");
        return new ArrayList<String>(Arrays.asList(replace1.split(",")));
    }

    private void createDatabase() {
        String url = "jdbc:sqlite:BD.db";
        String file = "CREATE TABLE IF NOT EXISTS \"File\" (\n" +
                "\t\"ID\"\tINTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE,\n" +
                "\t\"name\"\tREAL NOT NULL,\n" +
                "\t\"extension\"\tREAL NOT NULL,\n" +
                "\t\"title\"\tREAL NOT NULL,\n" +
                "\t\"author\"\tREAL NOT NULL,\n" +
                "\t\"date\"\tREAL NOT NULL,\n" +
                "\t\"theme\"\tREAL NOT NULL,\n" +
                "\t\"typeOfDocument\"\tREAL NOT NULL\n" +
                ");";
        String tag = "CREATE TABLE IF NOT EXISTS \"KeyWord\" (\n" +
                "\t\"ID\"\tINTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE,\n" +
                "\t\"Key\"\tREAL NOT NULL,\n" +
                "\t\"ID_File\"\tINTEGER NOT NULL,\n" +
                "\tFOREIGN KEY(\"ID_File\") REFERENCES \"File\"(\"ID\")\n" +
                ")";
        String note = "CREATE TABLE IF NOT EXISTS \"Note\" (\n" +
                "\t\"ID\"\tINTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE,\n" +
                "\t\"Note\"\tREAL NOT NULL,\n" +
                "\t\"ID_File\"\tINTEGER NOT NULL,\n" +
                "\tFOREIGN KEY(\"ID_File\") REFERENCES \"File\"(\"ID\")\n" +
                ")";
        String quote = "CREATE TABLE IF NOT EXISTS \"Quote\" (\n" +
                "\t\"ID\"\tINTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE,\n" +
                "\t\"Quote\"\tREAL NOT NULL,\n" +
                "\t\"ID_File\"\tINTEGER NOT NULL,\n" +
                "\tFOREIGN KEY(\"ID_File\") REFERENCES \"File\"(\"ID\")\n" +
                ")";
        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()) {
            stmt.execute(file);
            stmt.execute(tag);
            stmt.execute(note);
            stmt.execute(quote);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

}
