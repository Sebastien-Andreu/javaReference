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
import javafx.stage.FileChooser;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.json.JSONObject;
import org.zeroturnaround.zip.ZipUtil;
import singleton.SingletonController;
import singleton.SingletonDatabase;
import tag.Tag;
import tag.TagNote;
import tag.TagQuote;

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
import java.time.LocalDate;
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
    AnchorPane rootSearch;

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
    public DatePicker dateMin;

    @FXML
    public DatePicker dateMax;

    @FXML
    public ComboBox<String> themeAdd;

    @FXML
    public ComboBox<String> typeOfDocumentAdd;

    @FXML
    public ComboBox<String> listTheme;

    @FXML
    public ComboBox<String> listTypeOfDocument;

    @FXML
    Button buttonAddFile;

    @FXML
    public
    Button buttonUpdateFile;

    @FXML
    Label labelAddFile;

    @FXML
    HBox buttonEdit;

    public List<code.File> codeFilesExtract = new ArrayList<>();
    public List<code.File> codeFiles = new ArrayList<>();

    public List<File> files = new ArrayList<>();
    public List<Image> images = new ArrayList<>();
    public List<File> filesAdd;
    public String directoryDestination = "biblio";


    public ObservableList<String> tags = FXCollections.observableArrayList();
    public ObservableList<String> notes = FXCollections.observableArrayList();
    public ObservableList<String> quotes = FXCollections.observableArrayList();

    public ObservableList<String> addTags = FXCollections.observableArrayList();
    public ObservableList<String> addNotes = FXCollections.observableArrayList();
    public ObservableList<String> addQuotes = FXCollections.observableArrayList();


    @FXML
    public void initialize(){
        tags.addListener(this::eventListenerTags);
        notes.addListener(this::eventListenerNotes);
        quotes.addListener(this::eventListenerQuotes);
        addTags.addListener(this::eventListenerAddTags);
        addNotes.addListener(this::eventListenerAddNotes);
        addQuotes.addListener(this::eventListenerAddQuotes);


        themeAdd.setEditable(true);
        themeAdd.getEditor().textProperty().addListener((obs, oldText, newText) -> {
            themeAdd.setValue(newText);
        });

        typeOfDocumentAdd.setEditable(true);
        typeOfDocumentAdd.getEditor().textProperty().addListener((obs, oldText, newText) -> {
            typeOfDocumentAdd.setValue(newText);
        });

        setComboBox();

        setupListOfFile();

        getFile();

        org.apache.log4j.BasicConfigurator.configure();
        SingletonController.getInstance().setController(this);
    }

    private void setComboBox(){
        listTheme.getItems().clear();
        listTypeOfDocument.getItems().clear();

        listTheme.getItems().addAll(SingletonDatabase.getInstance().getAllTheme());
        listTypeOfDocument.getItems().addAll(SingletonDatabase.getInstance().getAllTypeOfDocument());

        listTheme.setValue("none");
        listTypeOfDocument.setValue("none");
    }

    public void getFile(){
        File directory = new File(directoryDestination);
        codeFiles.clear();
        showFile.getChildren().clear();
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
                    images.add(img);
                    codeFiles.add(new code.File(f, img));
                    codeFiles.get(codeFiles.size()-1).setContainer("file");
                    showFile.getChildren().add(codeFiles.get(codeFiles.size()-1).getFlowPane());
                });
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
    private void handleDropped(DragEvent event){
        try {
            JSONObject object = new JSONObject(getFile(event.getDragboard().getString()).toString());
            String name = object.getString("name");
            if (droppedIsPossible(name)){
                for (code.File f : codeFiles){
                    if (f.getName().equals(name)){
                        codeFilesExtract.add(new code.File(f.getFile(), f.getImage())); // just f to remove
                        codeFilesExtract.get(codeFilesExtract.size()-1).setContainer("extract");
                        showExtractFile.getChildren().add(codeFilesExtract.get(codeFilesExtract.size()-1).getFlowPane());
                        break;
                    }
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
            if (droppedForRemoveIsPossible(name, container)) {
                for (code.File f : codeFilesExtract) {
                    if (f.getName().equals(name)) {
                        f.removeInExtractFile();
                        break;
                    }
                }
            }
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    private boolean droppedIsPossible(String name){
        boolean alreadyExist = true;
        for (code.File s : codeFilesExtract){
            if (s.getName().equals(name)) {
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

        for (code.File s : codeFilesExtract){
            if (s.getName().equals(name)) {
                alreadyExist = true;
                break;
            }
        }
        return alreadyExist;
    }

    private Object getFile( String s ) throws IOException, ClassNotFoundException {
        byte [] data = Base64.getDecoder().decode( s );
        ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(  data ) );
        Object o  = ois.readObject();
        ois.close();
        return o;
    }

    private void setupListOfFile(){
        anchorPaneDetails.setVisible(false);
        modifyDetails.setVisible(false);
        buttonEdit.setVisible(false);
        double widthMaxSize = 870;
        anchorPaneFile.setMinWidth(widthMaxSize);
        scrollShowFile.setMinWidth(widthMaxSize);
        showFile.setMinWidth(widthMaxSize - 6);

        anchorPaneExtractFile.setMinWidth(widthMaxSize);
        scrollExtractFile.setMinWidth(widthMaxSize);
        showExtractFile.setMinWidth(widthMaxSize - 6);
        hBoxExtractFile.setMinWidth(widthMaxSize);
    }

    @FXML
    public void onActionCloseDetails(ActionEvent actionEvent) {
        setupListOfFile();
    }

    @FXML
    public void onButtonAddFileClicked(ActionEvent actionEvent) {
        rootView.setVisible(false);
        rootSearch.setVisible(false);
        clearAddFile();
        buttonAddFile.setVisible(true);
        buttonUpdateFile.setVisible(false);
        labelAddFile.setVisible(true);
        rootAdd.setVisible(true);
        Platform.runLater( () -> rootAdd.requestFocus() );
    }

    private void clearAddFile(){
        addTags.clear();
        addQuotes.clear();
        addNotes.clear();
        showIcon.setImage(null);
        titleOfImportFileAdd.setText("");
        titleAdd.setText("");
        authorAdd.setText("");
        dateAdd.setValue(null);
        inputAddNotes.clear();
        inputAddQuotes.clear();
        inputAddTags.clear();
        themeAdd.getItems().clear();
        typeOfDocumentAdd.getItems().clear();

        themeAdd.getItems().addAll(SingletonDatabase.getInstance().getAllTheme());
        typeOfDocumentAdd.getItems().addAll(SingletonDatabase.getInstance().getAllTypeOfDocument());
    }

    @FXML
    public void onUserWantToExtractFile(ActionEvent actionEvent) {
        if (!codeFilesExtract.isEmpty()){
            FileChooser fileChooser = new FileChooser();

            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("ZIP files (*.zip)", "*.zip");
            fileChooser.getExtensionFilters().add(extFilter);

            File file = fileChooser.showSaveDialog(Main.stage);

            if (file != null) {
                File directory = new File(file.getParent() + "\\" + FilenameUtils.removeExtension(file.getName()));
                if (!directory.exists()) {
                    directory.mkdir();
                }
                File extractMetadata = new File(directory + "\\metadata");
                extractMetadata.mkdir();
                for (code.File f : codeFilesExtract) {
                    try {
                        Files.copy(Paths.get(f.getFile().getAbsolutePath()), Paths.get(directory.getAbsolutePath() + "\\" + f.getName()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                for (code.File f : codeFilesExtract){
                    assert extractMetadata != null;
                    f.createFileWithData(extractMetadata);
                }
                try {
                    ZipUtil.pack(new File(directory.getAbsolutePath()), new File(directory.getAbsolutePath() + ".zip"));
                    FileUtils.deleteDirectory(directory);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    showExtractFile.getChildren().clear();
                    codeFilesExtract.clear();
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }

    public void onThemeIsSelected(ActionEvent event) {
        if (listTheme.getValue().equals("none") && listTypeOfDocument.getValue().equals("none") && dateMax.getValue() == null && dateMin.getValue() == null ){
            getFile();
        }
        if (!listTheme.getValue().equals("none") || !listTypeOfDocument.getValue().equals("none")){
            List<String> list = SingletonDatabase.getInstance().getFileWithParams(listTheme.getValue(), listTypeOfDocument.getValue());

            codeFiles.clear();
            showFile.getChildren().clear();
            if (!list.isEmpty()){
                System.out.println(list);
                for (String file: list){
                    int i = 0;
                    for (File f : files){
                        if (file.equals(f.getName())){
                            codeFiles.add(new code.File(f, images.get(i)));
                            codeFiles.get(codeFiles.size()-1).setContainer("file");
                            showFile.getChildren().add(codeFiles.get(codeFiles.size()-1).getFlowPane());
                        }
                        ++i;
                    }-
                }
            }
        }
        if (dateMax.getValue() != null || dateMin.getValue() != null){
            if (listTheme.getValue().equals("none") && !listTypeOfDocument.getValue().equals("none")){
                getFile();
            }
            loadWithDate();
        }
    }

    private void loadWithDate(){
        List<code.File> tempCodeFile = new ArrayList<>();
        loadDataOfCodeFile();
        if (dateMin.getValue() != null){
            for (code.File f : codeFiles){
                if (LocalDate.parse(f.date).isAfter(dateMin.getValue())){
                    tempCodeFile.add(f);
                }
            }
        }
        if (dateMax.getValue() != null){
            for (code.File f : codeFiles){
                if (LocalDate.parse(f.date).isBefore(dateMax.getValue())){
                    tempCodeFile.add(f);
                }
            }
        }

        codeFiles.clear();
        showFile.getChildren().clear();
        for (code.File f : tempCodeFile){
            codeFiles.add(f);
            showFile.getChildren().add(f.getFlowPane());
        }
    }

    private void loadDataOfCodeFile(){
        for (code.File f : codeFiles){
            SingletonDatabase.getInstance().setDetailsOfFile(f);
        }
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

        SingletonDatabase.getInstance().save();
        clearAddFile();
    }

    @FXML
    public void onCancelAddClicked(ActionEvent actionEvent) {
        rootAdd.setVisible(false);
        clearAddFile();
        rootView.setVisible(true);
        rootSearch.setVisible(true);

        listTheme.setValue("none");
        listTypeOfDocument.setValue("none");
        getFile();
        Platform.runLater( () -> rootView.requestFocus() );
    }

    @FXML
    public void onUserWantToUpdateFile(ActionEvent actionEvent) {
        SingletonDatabase.getInstance().save();
        for (code.File f : codeFiles) {
            if (f.getName().equals(titleOfImportFileAdd.getText())) {
                f.showView();
            }
        }
        rootAdd.setVisible(false);
        clearAddFile();
        rootView.setVisible(true);
        rootSearch.setVisible(true);

        listTheme.setValue("none");
        listTypeOfDocument.setValue("none");
        getFile();
        Platform.runLater( () -> rootView.requestFocus() );
    }
}
