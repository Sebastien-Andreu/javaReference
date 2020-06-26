package code.database;

import code.controller.ControllerAddFile;
import code.singleton.SingletonFileSelected;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseAdd extends Database {
    private ControllerAddFile controller;

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
            if (this.verifyIfThemeAlreadyExist(str) == 0) {
                try (Connection conn = connect();PreparedStatement pstmt = conn.prepareStatement("INSERT INTO Theme(name) VALUES(?)");Statement stmt = conn.createStatement()){
                    pstmt.setString(1, str);
                    pstmt.executeUpdate();
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }

    private void saveAuthor() {
        for (String str : controller.addAuthor){
            if (this.verifyIfAuthorExist((String)str) == 0) {
                try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement("INSERT INTO Author(author) VALUES(?)")){
                    pstmt.setString(1, str);
                    pstmt.executeUpdate();
                } catch (SQLException e) {
                    System.out.println("error saveAuthor");
                }
            }
        }
    }

    public void saveAffiliation(ObservableList<String> aff) {
        for (String str : aff){
            if (this.verifyIfAffiliationExist(str) == 0) {
                try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement("INSERT INTO Affiliation(name) VALUES(?)")){
                    pstmt.setString(1, str);
                    pstmt.executeUpdate();
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }

    private void saveKeyWord() {
        for (String str : controller.addTags){
            if (this.verifyIfKeyWordExist(str) == 0) {
                try (Connection conn = connect();PreparedStatement pstmt = conn.prepareStatement("INSERT INTO KeyWord(Key) VALUES(?)");Statement stmt = conn.createStatement()){
                    pstmt.setString(1, str);
                    pstmt.executeUpdate();
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }
}
