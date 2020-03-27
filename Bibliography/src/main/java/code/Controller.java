package code;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.json.JSONObject;
import singleton.SingletonController;
import singleton.SingletonDatabase;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class Controller {

    @FXML
    AnchorPane rootAdd;

    @FXML
    AnchorPane rootView;

    @FXML
    AnchorPane anchorPaneFile;

    @FXML
    ScrollPane scrollShowFile;

    @FXML
    VBox showFile;

    @FXML
    AnchorPane anchorPaneExtractFile;

    @FXML
    ScrollPane scrollExtractFile;

    @FXML
    VBox showExtractFile;

    @FXML
    HBox hBoxExtractFile;

    @FXML
    Button buttonExtractFile;

    @FXML
    AnchorPane anchorPaneDetails;

    @FXML
    Button modifyDetails;

    @FXML
    public Label detailsTitle;

    @FXML
    public Label detailsName;

    @FXML
    public Label detailsAuthor;

    @FXML
    public Label detailsDate;

    @FXML
    public Label detailsTheme;

    @FXML
    public Label detailsTypeOfDocument;

    @FXML
    Button closeButtonDetails;

    @FXML
    public FlowPane showTags;

    @FXML
    private VBox showNotes;

    @FXML
    private VBox showQuotes;

    @FXML
    public ImageView showIcon;

    @FXML
    public Label titleOfImportFileAdd;

    @FXML
    private TextField inputAddTags;

    @FXML
    private TextField inputAddNotes;

    @FXML
    private TextField inputAddQuotes;

    @FXML
    private FlowPane showAddTags;

    @FXML
    public FlowPane showAddNotes;

    @FXML
    public FlowPane showAddQuotes;

    @FXML
    public TextField titleAdd;

    @FXML
    public TextField authorAdd;

    @FXML
    public DatePicker dateAdd;

    @FXML
    public TextField themeAdd;

    @FXML
    public TextField typeOfDocumentAdd;

    @FXML
    Button buttonAddFile;

    @FXML
    public
    Button buttonUpdateFile;

    @FXML
    Label labelAddFile;

    public List<File> files;
    public List<File> listOfFileExtract = new ArrayList<>();

    public List<File> filesAdd;
    public String directoryDestination = "biblio";
    public String extensionOfFile;

    public ObservableList<String> stringFile = FXCollections.observableArrayList();
    public ObservableList<String> stringFileExtract = FXCollections.observableArrayList();
    public ObservableList<String> tags = FXCollections.observableArrayList();
    public ObservableList<String> notes = FXCollections.observableArrayList();
    public ObservableList<String> quotes = FXCollections.observableArrayList();

    public ObservableList<String> addTags = FXCollections.observableArrayList();
    public ObservableList<String> addNotes = FXCollections.observableArrayList();
    public ObservableList<String> addQuotes = FXCollections.observableArrayList();
    public List<Image> pictureFile = new ArrayList<>();
    public List<Image> pictureFileExtract = new ArrayList<>();

    @FXML
    public void initialize(){
        stringFile.addListener(this::eventListenerAllFile);
        stringFileExtract.addListener(this::eventListenerExtractFile);
        tags.addListener(this::eventListenerTags);
        notes.addListener(this::eventListenerNotes);
        quotes.addListener(this::eventListenerQuotes);
        addTags.addListener(this::eventListenerAddTags);
        addNotes.addListener(this::eventListenerAddNotes);
        addQuotes.addListener(this::eventListenerAddQuotes);

        setupListOfFile();

        getFile();

        SingletonController.getInstance().setController(this);
    }

    private void getFile(){
        File directory = new File(directoryDestination);
        if (!directory.exists()){
            directory.mkdir();
        } else {
            try (Stream<Path> walk = Files.walk(Paths.get(directoryDestination))) {
                files = walk.filter(Files::isRegularFile).map(Path::toFile).collect(Collectors.toList());
            } catch (IOException e) {
                e.printStackTrace();
            }

            for (File f : files){
                Icon icon = FileSystemView.getFileSystemView().getSystemIcon(f);

                BufferedImage bufferedImage = new BufferedImage(icon.getIconWidth(), icon.getIconHeight(), BufferedImage.TYPE_INT_ARGB);
                icon.paintIcon(null,bufferedImage.getGraphics(), 0, 0);
                Platform.runLater(() -> {
                    Image img = SwingFXUtils.toFXImage(bufferedImage, null);
                    pictureFile.add(img);
                    stringFile.add(f.getName());
                });
            }
        }
    }


    private void eventListenerAllFile(ListChangeListener.Change<? extends String> change){
        while (change.next()){
            if (change.wasAdded()){
                code.File file = new code.File(stringFile.get(stringFile.size()-1), pictureFile.get(pictureFile.size()-1), (stringFile.size()-1));
                file.setContainer("file");
                showFile.getChildren().add(file.getFlowPane());
            }
            if (change.wasRemoved()){
                showFile.getChildren().subList(change.getFrom(), change.getFrom() + change.getRemovedSize()).clear();
            }
        }
    }

    private void eventListenerExtractFile(ListChangeListener.Change<? extends String> change){
        while (change.next()){
            if (change.wasAdded()){
                code.File file = new code.File(stringFileExtract.get(stringFileExtract.size()-1), pictureFileExtract.get(pictureFileExtract.size()-1), (stringFileExtract.size()-1));
                file.setContainer("extract");
                showExtractFile.getChildren().add(file.getFlowPane());
            }
            if (change.wasRemoved()){
                showExtractFile.getChildren().subList(change.getFrom(), change.getFrom() + change.getRemovedSize()).clear();
            }
        }
    }

    private void eventListenerTags(ListChangeListener.Change<? extends String> change){
        while (change.next()){
            if (change.wasAdded()){
                Button result = new Button(tags.get(tags.size()-1));
                result.setPrefHeight(20);
                result.setContentDisplay(ContentDisplay.RIGHT);
                result.setStyle("-fx-padding: 5 5 5 5");

                showTags.getChildren().add(result);
            }
            if (change.wasRemoved()) {
                showTags.getChildren().subList(change.getFrom(), change.getFrom() + change.getRemovedSize()).clear();
            }
        }
    }

    private void eventListenerNotes(ListChangeListener.Change<? extends String> change){
        while (change.next()){
            if (change.wasAdded()){
                showNotes.getChildren().addAll(change.getFrom(), change.getAddedSubList().stream().map(Tag::new).collect(Collectors.toList()));
            }
            if (change.wasRemoved()) {
                showNotes.getChildren().subList(change.getFrom(), change.getFrom() + change.getRemovedSize()).clear();
            }
        }
    }

    private void eventListenerQuotes(ListChangeListener.Change<? extends String> change){
        while (change.next()){
            if (change.wasAdded()){
                showQuotes.getChildren().addAll(change.getFrom(), change.getAddedSubList().stream().map(Tag::new).collect(Collectors.toList()));
            }
            if (change.wasRemoved()) {
                showQuotes.getChildren().subList(change.getFrom(), change.getFrom() + change.getRemovedSize()).clear();
            }
        }
    }

    @FXML
    private void handleDragOver(DragEvent event){
        event.acceptTransferModes(TransferMode.ANY);
    }

    @FXML
    private void handleDropped(DragEvent event) throws IOException, ClassNotFoundException {
        try {
            int i = 0;
            JSONObject object = new JSONObject(getFile(event.getDragboard().getString()).toString());
            String name = object.getString("name");
            if (droppedIsPossible(name)){
                for (File f : files){
                    if (stringFile.get(i).equals(name)){
                        listOfFileExtract.add(f);
                        pictureFileExtract.add(pictureFile.get(i));
                        stringFileExtract.add(stringFile.get(i));
                        break;
                    }
                    ++i;
                }
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    @FXML
    private void handleDragOverRemove(DragEvent event){
        event.acceptTransferModes(TransferMode.ANY);
    }

    @FXML
    private void handleDroppedRemove(DragEvent event){
        try {
            JSONObject object = new JSONObject(getFile(event.getDragboard().getString()).toString());
            String name = object.getString("name");
            String container = object.getString("container");
            int i = 0;
            if (droppedForRemoveIsPossible(name, container)) {
                for (File f : listOfFileExtract) {
                    if (stringFileExtract.get(i).equals(name)) {
                        pictureFileExtract.remove(i);
                        stringFileExtract.remove(i);

                        listOfFileExtract.remove(f);
                        break;
                    }
                    ++i;
                }
            }
        } catch (Exception e){
            System.out.println(e.getMessage());
        }

    }

    private boolean droppedIsPossible(String name){
        boolean alreadyExist = true;
        for (String s : stringFileExtract){
            if (s.equals(name)) {
                alreadyExist = false;
                break;
            }
        }
        return alreadyExist;
    }

    private boolean droppedForRemoveIsPossible(String name, String container){
        boolean alreadyExist = false;
        if (container.equals("file"))
            return false;

        for (String f : stringFileExtract){
            if (f.equals(name)) {
                alreadyExist = true;
                break;
            }
        }
        return alreadyExist;
    }

    private Object getFile( String s ) throws IOException , ClassNotFoundException {
        byte [] data = Base64.getDecoder().decode( s );
        ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(  data ) );
        Object o  = ois.readObject();
        ois.close();
        return o;
    }

    private void setupListOfFile(){
        anchorPaneDetails.setVisible(false);
        modifyDetails.setVisible(false);
        double widthMaxSize = 870;
        anchorPaneFile.setMinWidth(widthMaxSize);
        scrollShowFile.setMinWidth(widthMaxSize);
        showFile.setMinWidth(widthMaxSize - 6);

        anchorPaneExtractFile.setMinWidth(widthMaxSize);
        scrollExtractFile.setMinWidth(widthMaxSize);
        showExtractFile.setMinWidth(widthMaxSize - 6);
        hBoxExtractFile.setMinWidth(widthMaxSize);
    }

    public void onActionCloseDetails(ActionEvent actionEvent) {
        setupListOfFile();
    }

    public void onButtonAddFileClicked(ActionEvent actionEvent) {
        rootView.setVisible(false);
        addTags.clear();
        addQuotes.clear();
        addNotes.clear();
        showIcon.setImage(null);
        titleOfImportFileAdd.setText("");
        titleAdd.setText("");
        authorAdd.setText("");
        dateAdd.setValue(null);
        themeAdd.setText("");
        typeOfDocumentAdd.setText("");
        buttonAddFile.setVisible(true);
        buttonUpdateFile.setVisible(false);
        labelAddFile.setVisible(true);
        rootAdd.setVisible(true);
        Platform.runLater( () -> rootAdd.requestFocus() );
    }







    /*-----------------------------------------------------------------------------------------------------------------*/
    /*-----------------------------------------------------------------------------------------------------------------*/
    /*-----------------------------------------------------------------------------------------------------------------*/
    /*---------------------------------------------------ADD NEW FILE--------------------------------------------------*/
    /*-----------------------------------------------------------------------------------------------------------------*/
    /*-----------------------------------------------------------------------------------------------------------------*/
    /*-----------------------------------------------------------------------------------------------------------------*/









    private void eventListenerAddTags(ListChangeListener.Change<? extends String> change){
        while (change.next()){
            if (change.wasAdded()){
                Button btn = getButtonTag(addTags.get(addTags.size()-1));
                showAddTags.getChildren().add(btn);
            }
            if (change.wasRemoved()) {
                rootAdd.requestFocus();
                showAddTags.getChildren().subList(change.getFrom(), change.getFrom() + change.getRemovedSize()).clear();
            }
        }
    }

    private void eventListenerAddNotes(ListChangeListener.Change<? extends String> change){
        while (change.next()){
            if (change.wasAdded()){
                showAddNotes.getChildren().addAll(change.getFrom(), change.getAddedSubList().stream().map(TagNote::new).collect(Collectors.toList()));
            }
            if (change.wasRemoved()) {
                rootAdd.requestFocus();
                showAddNotes.getChildren().subList(change.getFrom(), change.getFrom() + change.getRemovedSize()).clear();
            }
        }
    }

    private void eventListenerAddQuotes(ListChangeListener.Change<? extends String> change){
        while (change.next()){
            if (change.wasAdded()){
                showAddQuotes.getChildren().addAll(change.getFrom(), change.getAddedSubList().stream().map(TagQuote::new).collect(Collectors.toList()));
            }
            if (change.wasRemoved()) {
                rootAdd.requestFocus();
                showAddQuotes.getChildren().subList(change.getFrom(), change.getFrom() + change.getRemovedSize()).clear();
            }
        }
    }

    private Button getButtonTag(String tag) {
        Image image = new Image(String.valueOf(ClassLoader.getSystemResource("image/delete.png")));
        ImageView closeImg = new ImageView(image);
        Button result = new Button(tag,closeImg);
        result.setPrefHeight(20);
        result.setContentDisplay(ContentDisplay.RIGHT);

        result.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2){
                addTags.remove(tag);
            }
        });
        return result;
    }

    @FXML
    private void handleDragOverAdd(DragEvent e){
        if (!SingletonController.getInstance().getController().buttonUpdateFile.isVisible()){
            if (e.getDragboard().hasFiles()){
                e.acceptTransferModes(TransferMode.ANY);
            }
        }
    }

    @FXML
    private void handleDropAdd(DragEvent e){
        filesAdd = e.getDragboard().getFiles();
        extensionOfFile = filesAdd.get(0).getName().substring( filesAdd.get(0).getName().lastIndexOf(".") + 1);
        Icon icon = FileSystemView.getFileSystemView().getSystemIcon(filesAdd.get(0));

        BufferedImage bufferedImage = new BufferedImage(icon.getIconWidth(), icon.getIconHeight(), BufferedImage.TYPE_INT_ARGB);
        icon.paintIcon(null,bufferedImage.getGraphics(), 0, 0);
        Platform.runLater(() -> {
            Image img = SwingFXUtils.toFXImage(bufferedImage, null);
            showIcon.setImage(img);
            titleOfImportFileAdd.setText(filesAdd.get(0).getName());
        });
    }

    @FXML
    private void onEnterKeyPressTags(KeyEvent e){
        if (e.getCode() == KeyCode.ENTER && !inputAddTags.getText().isEmpty() && !addTags.contains(inputAddTags.getText())){
            addTags.add(inputAddTags.getText());
            inputAddTags.clear();
        }
    }

    @FXML
    private void onEnterKeyPressNotes(KeyEvent e){
        if (e.getCode() == KeyCode.ENTER && !inputAddNotes.getText().isEmpty() && !addNotes.contains(inputAddNotes.getText())){
            addNotes.add(inputAddNotes.getText());
            inputAddNotes.clear();
        }
    }

    @FXML
    private void onEnterKeyPressQuotes(KeyEvent e){
        if (e.getCode() == KeyCode.ENTER && !inputAddQuotes.getText().isEmpty() && !addQuotes.contains(inputAddQuotes.getText())){
            addQuotes.add(inputAddQuotes.getText());
            inputAddQuotes.clear();
        }
    }

    @FXML
    private void saveData(){
        try {
            Files.copy(Paths.get(filesAdd.get(0).getAbsolutePath()), Paths.get((directoryDestination + "\\" + titleOfImportFileAdd.getText())));
            Files.delete(Paths.get(filesAdd.get(0).getAbsolutePath()));
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        SingletonDatabase.getInstance().saveFile();
    }

    public void onCancelAddClicked(ActionEvent actionEvent) {
        rootAdd.setVisible(false);
        stringFile.clear();
        pictureFile.clear();

        getFile();

        rootView.setVisible(true);
        Platform.runLater( () -> rootView.requestFocus() );
    }

    public void onUserWantToUpdateFile(ActionEvent actionEvent) {
        SingletonDatabase.getInstance().saveFile();
        SingletonDatabase.getInstance().showDetailsOfFile(SingletonController.getInstance().getController().titleOfImportFileAdd.getText());
        rootAdd.setVisible(false);
        stringFile.clear();
        pictureFile.clear();

        getFile();

        rootView.setVisible(true);
        Platform.runLater( () -> rootView.requestFocus() );
    }
}
