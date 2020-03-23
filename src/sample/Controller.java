package sample;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;

import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.DragEvent;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.awt.image.BufferedImage;
import java.io.File;
import java.sql.*;
import java.util.List;
import java.util.stream.Collectors;

import org.sqlite.JDBC;

public class Controller {

    @FXML
    private ImageView showIcon;

    @FXML
    private AnchorPane root;

    @FXML
    private Label titleOfImportFile;

    @FXML
    private TextField inputTags;

    @FXML
    private TextField inputNotes;

    @FXML
    private TextField inputQuotes;

    @FXML
    private FlowPane showTags;

    @FXML
    private FlowPane showNotes;

    @FXML
    private FlowPane showQuotes;

    @FXML
    private TextField title;

    @FXML
    private TextField author;

    @FXML
    private DatePicker date;

    @FXML
    private TextField theme;

    @FXML
    private TextField typeOfDocument;


    public static ObservableList<String> tags = FXCollections.observableArrayList();
    public static ObservableList<String> notes = FXCollections.observableArrayList();
    public static ObservableList<String> quotes = FXCollections.observableArrayList();
    public String extensionOfFile;
    public int IDFile = 0;

    public Controller() throws SQLException {
    }


    @FXML
    public void initialize() {
        tags.addListener(this::eventListenerTags);
        notes.addListener(this::eventListenerNotes);
        quotes.addListener(this::eventListenerQuotes);

        Platform.runLater( () -> root.requestFocus() );
    }

    @FXML
    private void handleDragOver(DragEvent e){
        if (e.getDragboard().hasFiles()){
            e.acceptTransferModes(TransferMode.ANY);
        }
    }

    @FXML
    private void handleDrop(DragEvent e){
        List<File> files = e.getDragboard().getFiles();
        extensionOfFile = files.get(0).getName().substring( files.get(0).getName().lastIndexOf(".") + 1);
        Icon icon = FileSystemView.getFileSystemView().getSystemIcon(files.get(0));

        BufferedImage bufferedImage = new BufferedImage(icon.getIconWidth(), icon.getIconHeight(), BufferedImage.TYPE_INT_ARGB);
        icon.paintIcon(null,bufferedImage.getGraphics(), 0, 0);
        Platform.runLater(() -> {
            Image img = SwingFXUtils.toFXImage(bufferedImage, null);
            showIcon.setImage(img);
            titleOfImportFile.setText(files.get(0).getName());
        });
    }

    @FXML
    private void onEnterKeyPressTags(KeyEvent e){
        if (e.getCode() == KeyCode.ENTER && !inputTags.getText().isEmpty() && !tags.contains(inputTags.getText())){
            tags.add(inputTags.getText());
            System.out.println(tags);
            inputTags.clear();
        }
    }

    @FXML
    private void onEnterKeyPressNotes(KeyEvent e){
        if (e.getCode() == KeyCode.ENTER && !inputNotes.getText().isEmpty() && !notes.contains(inputNotes.getText())){
            notes.add(inputNotes.getText());
            inputNotes.clear();
        }
    }

    @FXML
    private void onEnterKeyPressQuotes(KeyEvent e){
        if (e.getCode() == KeyCode.ENTER && !inputQuotes.getText().isEmpty() && !quotes.contains(inputQuotes.getText())){
            quotes.add(inputQuotes.getText());
            inputQuotes.clear();
        }
    }

    @FXML
    private void saveData(){
        saveFile();
        saveKeyWord();
        saveNote();
        saveQuote();
    }

    private void saveFile(){
        String sql = "INSERT INTO File(name,extension,title,author, date, theme, typeOfDocument) VALUES(?,?,?,?,?,?,?)";

        try (Connection conn = this.connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, titleOfImportFile.getText());
            pstmt.setString(2, extensionOfFile);
            pstmt.setString(3, title.getText());
            pstmt.setString(4, author.getText());
            pstmt.setString(5, date.getValue().toString());
            pstmt.setString(6, theme.getText());
            pstmt.setString(7, typeOfDocument.getText());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        String sqlSelect = "SELECT max(ID) FROM File";
        try (Connection conn = this.connect(); Statement stmt  = conn.createStatement(); ResultSet rs = stmt.executeQuery(sqlSelect)) {
            IDFile = rs.getInt("max(ID)");
            System.out.println(IDFile);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private void saveKeyWord(){
        String sqlTag = "INSERT INTO KeyWord(Key, ID_File) VALUES(?,?)";
        try (Connection conn = this.connect(); PreparedStatement pstmt = conn.prepareStatement(sqlTag)) {
            pstmt.setString(1, String.valueOf(tags));
            pstmt.setInt(2, IDFile);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private void saveNote(){
        String sqlNote = "INSERT INTO Note(Note, ID_File) VALUES(?,?)";
        try (Connection conn = this.connect(); PreparedStatement pstmt = conn.prepareStatement(sqlNote)) {
            pstmt.setString(1, String.valueOf(notes));
            pstmt.setInt(2, IDFile);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private void saveQuote(){
        String sqlQuote = "INSERT INTO Quote(Quote, ID_File) VALUES(?,?)";
        try (Connection conn = this.connect(); PreparedStatement pstmt = conn.prepareStatement(sqlQuote)) {
            pstmt.setString(1, String.valueOf(quotes));
            pstmt.setInt(2, IDFile);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private void eventListenerTags(ListChangeListener.Change<? extends String> change){
        while (change.next()){
            if (change.wasAdded()){
                Button btn = getButtonTag(tags.get(tags.size()-1));
                showTags.getChildren().add(btn);
            }
            if (change.wasRemoved()) {
                root.requestFocus();
                showTags.getChildren().subList(change.getFrom(), change.getFrom() + change.getRemovedSize()).clear();
            }
        }
    }

    private void eventListenerNotes(ListChangeListener.Change<? extends String> change){
        while (change.next()){
            if (change.wasAdded()){
                showNotes.getChildren().addAll(change.getFrom(), change.getAddedSubList().stream().map(TagNote::new).collect(Collectors.toList()));
            }
            if (change.wasRemoved()) {
                root.requestFocus();
                showNotes.getChildren().subList(change.getFrom(), change.getFrom() + change.getRemovedSize()).clear();
            }
        }
    }

    private void eventListenerQuotes(ListChangeListener.Change<? extends String> change){
        while (change.next()){
            if (change.wasAdded()){
                showQuotes.getChildren().addAll(change.getFrom(), change.getAddedSubList().stream().map(TagQuote::new).collect(Collectors.toList()));
            }
            if (change.wasRemoved()) {
                root.requestFocus();
                showQuotes.getChildren().subList(change.getFrom(), change.getFrom() + change.getRemovedSize()).clear();
            }
        }
    }

    private Button getButtonTag(String tag) {
        Image image = new Image("sample/delete.png");
        ImageView closeImg = new ImageView(image);
        Button result = new Button(tag,closeImg);
        result.setPrefHeight(20);
        result.setContentDisplay(ContentDisplay.RIGHT);

        result.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2){
                Controller.tags.remove(tag);
            }
        });
        return result;
    }

    private Connection connect() {
        String path = System.getProperty("user.dir") + "\\src\\sample\\Database\\BD.db";
        String url = "jdbc:sqlite:" + path;
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
            System.out.println(conn);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }
}
