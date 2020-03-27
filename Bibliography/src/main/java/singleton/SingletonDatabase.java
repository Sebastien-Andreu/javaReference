package singleton;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SingletonDatabase {
    public static SingletonDatabase instance = new SingletonDatabase();
    public int IDFile = 0;

    private SingletonDatabase(){}

    public static SingletonDatabase getInstance(){
        return instance;
    }

    private Connection connect() {
        String url = "jdbc:sqlite:BD.db";
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    public void showDetailsOfFile(String name) {
        String query = "select * from File where name = '" +name +"'";
        int id = 0;
        try (Connection conn = this.connect(); Statement stmt  = conn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            id = rs.getInt("ID");
            if (id > 0) {
                SingletonController.getInstance().getController().detailsTitle.setText(rs.getString("name"));
                SingletonController.getInstance().getController().detailsName.setText(rs.getString("title"));
                SingletonController.getInstance().getController().detailsAuthor.setText(rs.getString("author"));
                SingletonController.getInstance().getController().detailsDate.setText("date");
                SingletonController.getInstance().getController().detailsTheme.setText(rs.getString("theme"));
                SingletonController.getInstance().getController().detailsTypeOfDocument.setText(rs.getString("typeOfDocument"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        String query2 = "select * from KeyWord where ID_File = '" + id +"'";
        try (Connection conn = this.connect(); Statement stmt  = conn.createStatement(); ResultSet rs = stmt.executeQuery(query2)) {
            int idKey = rs.getInt("ID");
            SingletonController.getInstance().getController().tags.clear();
            if (idKey > 0 && (!Collections.singletonList(rs.getString("Key")).get(0).isEmpty()))  {
                for (String str : getList(rs.getString("Key"))){
                    SingletonController.getInstance().getController().tags.add(str);
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        String query3 = "select * from Note where ID_File = '" + id +"'";
        try (Connection conn = this.connect(); Statement stmt  = conn.createStatement(); ResultSet rs = stmt.executeQuery(query3)) {
            int idKey = rs.getInt("ID");
            SingletonController.getInstance().getController().notes.clear();
            if (idKey > 0 && (!Collections.singletonList(rs.getString("Note")).get(0).isEmpty())) {
                for (String str : getList(rs.getString("Note"))){
                    SingletonController.getInstance().getController().notes.add(str);
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        String query4 = "select * from Quote where ID_File = '" + id +"'";
        try (Connection conn = this.connect(); Statement stmt  = conn.createStatement(); ResultSet rs = stmt.executeQuery(query4)) {
            int idKey = rs.getInt("ID");
            SingletonController.getInstance().getController().quotes.clear();
            if (idKey > 0 && (!Collections.singletonList(rs.getString("Quote")).get(0).isEmpty()))  {
                for (String str : getList(rs.getString("Quote"))){
                    SingletonController.getInstance().getController().quotes.add(str);
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void saveFile(){
        String sql = "INSERT INTO File(name,extension,title,author, date, theme, typeOfDocument) VALUES(?,?,?,?,?,?,?)";

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
        String sqlTag = "INSERT INTO KeyWord(Key, ID_File) VALUES(?,?)";
        try (Connection conn = this.connect(); PreparedStatement pstmt = conn.prepareStatement(sqlTag)) {
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
        String sqlNote = "INSERT INTO Note(Note, ID_File) VALUES(?,?)";
        try (Connection conn = this.connect(); PreparedStatement pstmt = conn.prepareStatement(sqlNote)) {
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
        String sqlQuote = "INSERT INTO Quote(Quote, ID_File) VALUES(?,?)";
        try (Connection conn = this.connect(); PreparedStatement pstmt = conn.prepareStatement(sqlQuote)) {
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
}
