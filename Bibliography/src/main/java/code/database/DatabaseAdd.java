package code.database;

import code.controller.ControllerAddFile;
import code.singleton.SingletonFileSelected;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseAdd extends Database {
    private ControllerAddFile controller;
    private String queryKey;
    private String queryTheme;
    public int IDFile = 0;
    public int IDTheme = 0;
    public int IDAuthor = 0;
    public int IDKey = 0;

    public DatabaseAdd() {
    }

    public void save(ControllerAddFile controller) {
        this.controller = controller;
        this.saveAuthor();
        this.saveTheme();
        this.saveKeyWord();
        this.saveNumberOfFile();
    }

    public void saveNumberOfFile() {
        if (SingletonFileSelected.getInstance().file == null) {
            int id = getNumberOfFile();
            try (Connection conn = connect();PreparedStatement pstmt = conn.prepareStatement("update NumberOfFile set number = '" + id + "' where ID = 1")){
                pstmt.executeUpdate();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }

    }

    private void saveTheme() {
        for (String str : controller.addTheme){
            this.IDTheme = this.verifyIfThemeAlreadyExist(str);
            if (this.IDTheme == 0) {
                try (Connection conn = connect();PreparedStatement pstmt = conn.prepareStatement("INSERT INTO Theme(name) VALUES(?)");Statement stmt = conn.createStatement()){
                    pstmt.setString(1, str);
                    pstmt.executeUpdate();
                    try (ResultSet rs = stmt.executeQuery("SELECT max(ID) FROM Theme")){
                        this.IDTheme = rs.getInt("max(ID)");
                    } catch (SQLException e) {
                        System.out.println(e.getMessage());
                    }
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }

    private void saveAuthor() {
        this.IDAuthor = this.verifyIfAuthorExist((String)this.controller.inputAuthor.getValue());
        if (this.IDAuthor == 0) {
            String sql = "INSERT INTO Author(author) VALUES(?)";

            try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)){
                pstmt.setString(1, (String)this.controller.inputAuthor.getValue());
                pstmt.executeUpdate();
                String sqlSelect = "SELECT max(ID) FROM Author";
                try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sqlSelect)){
                    this.IDAuthor = rs.getInt("max(ID)");
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void saveKeyWord() {
        for (String str : controller.addTags){
            this.IDKey = this.verifyIfKeyWordExist(str);
            if (this.IDKey == 0) {
                try (Connection conn = connect();PreparedStatement pstmt = conn.prepareStatement("INSERT INTO KeyWord(Key) VALUES(?)");Statement stmt = conn.createStatement()){
                    pstmt.setString(1, str);
                    pstmt.executeUpdate();
                    try (ResultSet rs = stmt.executeQuery("SELECT max(ID) FROM KeyWord")){
                        this.IDKey = rs.getInt("max(ID)");
                    } catch (SQLException e) {
                        System.out.println(e.getMessage());
                    }
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }
}
