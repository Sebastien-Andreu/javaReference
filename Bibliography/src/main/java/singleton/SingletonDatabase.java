package singleton;

import javax.swing.text.html.ListView;
import java.io.File;
import java.sql.*;
import java.util.*;

public class SingletonDatabase {
    public static SingletonDatabase instance = new SingletonDatabase();
    public int IDFile = 0;
    public int IDFileSelected;
    public int IDTheme = 0;
    public int IDTypeOfDocument = 0;

    private SingletonDatabase(){}

    public static SingletonDatabase getInstance(){
        return instance;
    }

    private Connection connect() {
        String url = "jdbc:sqlite:BD.db";
        Connection conn = null;
        File directory = new File("BD.db");
        if (!directory.exists()) {
            System.out.println("not exist");
            createDatabase();
        }
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    public void setDetailsOfFile(code.File file) {

        String query = "select * from File where name = '" + file.getName() +"'";
        try (Connection conn = this.connect(); Statement stmt  = conn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            IDFileSelected = rs.getInt("ID");
            IDTheme = rs.getInt("ID_Theme");
            IDTypeOfDocument = rs.getInt("ID_TypeOfDocument");
            if (IDFileSelected > 0) {
                file.title = (rs.getString("title"));
                file.author = (rs.getString("author"));
                file.date = (rs.getString("date"));
                file.extension = (rs.getString("extension"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        String queryTheme = "select name from Theme where ID = '" + IDTheme +"'";
        try (Connection conn = this.connect(); Statement stmt  = conn.createStatement(); ResultSet rs = stmt.executeQuery(queryTheme)) {
            file.theme = rs.getString("name");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        String queryTypeOfDocument = "select name from TypeOfDocument where ID = '" + IDTypeOfDocument +"'";
        try (Connection conn = this.connect(); Statement stmt  = conn.createStatement(); ResultSet rs = stmt.executeQuery(queryTypeOfDocument)) {
            file.typeOfDocument = rs.getString("name");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        String query2 = "select * from KeyWord where ID_File = '" + IDFileSelected +"'";
        try (Connection conn = this.connect(); Statement stmt  = conn.createStatement(); ResultSet rs = stmt.executeQuery(query2)) {
            if (!Collections.singletonList(rs.getString("Key")).get(0).equals("[]"))  {
                file.tags = getList(rs.getString("Key"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        String query3 = "select * from Note where ID_File = '" + IDFileSelected +"'";
        try (Connection conn = this.connect(); Statement stmt  = conn.createStatement(); ResultSet rs = stmt.executeQuery(query3)) {
            if (!Collections.singletonList(rs.getString("Note")).get(0).equals("[]"))  {
                file.notes = getList(rs.getString("Note"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        String query4 = "select * from Quote where ID_File = '" + IDFileSelected +"'";
        try (Connection conn = this.connect(); Statement stmt  = conn.createStatement(); ResultSet rs = stmt.executeQuery(query4)) {
            if (!Collections.singletonList(rs.getString("Quote")).get(0).equals("[]"))  {
                file.quotes = getList(rs.getString("Quote"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void save(){
        saveTheme();
        saveTypeOfDocument();
        saveFile();
        saveKeyWord();
        saveNote();
        saveQuote();
    }

    public void saveFile(){

        String sql;
        if (SingletonController.getInstance().getController().buttonUpdateFile.isVisible()){
            sql = "update File set name = ?, extension = ? ,title = ? ,author = ?, date = ?, ID_Theme = ?, ID_TypeOfDocument = ? where ID = " + IDFileSelected;
        }else {
            sql = "INSERT INTO File(name,extension,title,author, date, ID_Theme, ID_TypeOfDocument) VALUES(?,?,?,?,?,?,?)";
        }

        String extension = SingletonController.getInstance().getController().titleOfImportFileAdd.getText().substring(
                SingletonController.getInstance().getController().titleOfImportFileAdd.getText().lastIndexOf(".") + 1
        );
        try (Connection conn = this.connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, SingletonController.getInstance().getController().titleOfImportFileAdd.getText());
            pstmt.setString(2, extension);
            pstmt.setString(3, SingletonController.getInstance().getController().titleAdd.getText());
            pstmt.setString(4, SingletonController.getInstance().getController().authorAdd.getText());
            pstmt.setString(5, SingletonController.getInstance().getController().dateAdd.getValue().toString());
            pstmt.setInt(6, IDTheme);
            pstmt.setInt(7, IDTypeOfDocument);

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
    }

    private void saveTheme(){
        IDTheme = verifyIfThemeAlreadyExist(SingletonController.getInstance().getController().themeAdd.getValue());
        if (IDTheme == 0){
            String sql;
            sql = "INSERT INTO Theme(name) VALUES(?)";
            try (Connection conn = this.connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, SingletonController.getInstance().getController().themeAdd.getValue());
                pstmt.executeUpdate();
                String sqlSelect = "SELECT max(ID) FROM Theme";
                try (Statement stmt  = conn.createStatement(); ResultSet rs = stmt.executeQuery(sqlSelect)) {
                    IDTheme = rs.getInt("max(ID)");
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void saveTypeOfDocument(){
        IDTypeOfDocument = verifyIfTypeOfDocumentAlreadyExist(SingletonController.getInstance().getController().typeOfDocumentAdd.getValue());
        if (IDTypeOfDocument == 0){
            String sql;
            sql = "INSERT INTO TypeOfDocument(name) VALUES(?)";
            try (Connection conn = this.connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, SingletonController.getInstance().getController().typeOfDocumentAdd.getValue());
                pstmt.executeUpdate();
                String sqlSelect = "SELECT max(ID) FROM TypeOfDocument";
                try (Statement stmt  = conn.createStatement(); ResultSet rs = stmt.executeQuery(sqlSelect)) {
                    IDTypeOfDocument = rs.getInt("max(ID)");
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }



    private void saveKeyWord(){
        String sql;
        if (SingletonController.getInstance().getController().buttonUpdateFile.isVisible()){
            sql = "update KeyWord set Key = ?, ID_File = ? where ID_File = " + IDFileSelected;
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
    }

    private int verifyIfThemeAlreadyExist(String theme){
        String sql = "select * from Theme where name = '" + theme +"'";
        int result = 0;
        try (Connection conn = this.connect(); Statement stmt  = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.getInt("ID") > 0)  {
                result = rs.getInt("ID");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return result;
    }

    private int verifyIfTypeOfDocumentAlreadyExist(String typeOfDocument){
        String sql = "select * from TypeOfDocument where name = '" + typeOfDocument +"'";
        int result = 0;
        try (Connection conn = this.connect(); Statement stmt  = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.getInt("ID") > 0)  {
                result = rs.getInt("ID");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return result;
    }

    public ArrayList<String> getAllTheme(){
        String query2 = "select name from Theme";
        ArrayList<String> list = new ArrayList<>();
        try (Connection conn = this.connect(); Statement stmt  = conn.createStatement(); ResultSet rs = stmt.executeQuery(query2)) {
            list.add("none");
            while (rs.next()) {
                list.add(rs.getString("name"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return list;
    }

    public ArrayList<String> getAllTypeOfDocument(){
        String query2 = "select name from TypeOfDocument";
        ArrayList<String> list = new ArrayList<>();
        try (Connection conn = this.connect(); Statement stmt  = conn.createStatement(); ResultSet rs = stmt.executeQuery(query2)) {
            list.add("none");
            while (rs.next()) {
                list.add(rs.getString("name"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return list;
    }

    private List<String> getList(String list){
        String replace = list.replace("[","");
        String replace1 = replace.replace("]","");
        return new ArrayList<String>(Arrays.asList(replace1.split(",")));
    }

    public List<String> getFileWithParams(String theme, String typeOfDocument){
        String query = "";
        if (!theme.equals("none") && typeOfDocument.equals("none")){
            query = "select F.name from File F " +
                    "inner join Theme T on T.ID == F.ID_Theme " +
                    "where T.name = '" + theme + "'";
        }
        if (theme.equals("none") && !typeOfDocument.equals("none")){
            query = "select F.name from File F " +
                    "inner join TypeOfDocument TY on TY.ID == F.ID_TypeOfDocument " +
                    "where TY.name = '" + typeOfDocument + "'";
        }
        if (!theme.equals("none") && !typeOfDocument.equals("none")){
            query = "select F.name from File F " +
                    "inner join Theme T on T.ID == F.ID_Theme " +
                    "inner join TypeOfDocument TY on TY.ID == F.ID_TypeOfDocument " +
                    "where T.name = '" + theme + "' and TY.name = '" + typeOfDocument + "'";
        }
        ArrayList<String> list = new ArrayList<>();
        try (Connection conn = this.connect(); Statement stmt  = conn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                list.add(rs.getString("name"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return list;
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
                "\t\"ID_Theme\"\tREAL NOT NULL,\n" +
                "\t\"ID_TypeOfDocument\"\tREAL NOT NULL,\n" +
                "\tFOREIGN KEY(\"ID_Theme\") REFERENCES \"Theme\"(\"ID\"),\n" +
                "\tFOREIGN KEY(\"ID_TypeOfDocument\") REFERENCES \"TypeOfDocument\"(\"ID\")\n" +
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
        String theme = "CREATE TABLE \"Theme\" (\n" +
                "\t\"ID\"\tINTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE,\n" +
                "\t\"name\"\tREAL NOT NULL\n"+
                ")";
        String typeOfDocument = "CREATE TABLE \"TypeOfDocument\" (\n" +
                "\t\"ID\"\tINTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE,\n" +
                "\t\"name\"\tREAL NOT NULL\n"+
                ")";
        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()) {
                stmt.execute(theme);
                stmt.execute(typeOfDocument);
                stmt.execute(file);
                stmt.execute(tag);
                stmt.execute(note);
                stmt.execute(quote);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

}
