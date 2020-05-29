package code.controller;

import code.File;
import code.Main;
import code.database.DatabaseView;
import code.singleton.SingletonController;
import code.singleton.SingletonDatabase;
import code.singleton.SingletonFileSelected;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.file.FileStore;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.Normalizer;
import java.text.Normalizer.Form;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ListChangeListener.Change;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.DragEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javax.swing.Icon;
import javax.swing.filechooser.FileSystemView;


import java.nio.file.Files;

import javafx.stage.FileChooser;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.BasicConfigurator;
import org.json.JSONObject;
import org.zeroturnaround.zip.ZipUtil;

public class ControllerView {
    @FXML
    public AnchorPane root;
    @FXML
    public AnchorPane rootSearch;
    @FXML
    public AnchorPane anchorPaneFile;
    @FXML
    public ScrollPane scrollShowFile;
    @FXML
    public VBox showFile;
    @FXML
    public AnchorPane anchorPaneExtractFile;
    @FXML
    public ScrollPane scrollExtractFile;
    @FXML
    public ScrollPane showDetails;
    @FXML
    public VBox showExtractFile;
    @FXML
    public HBox hBoxExtractFile;
    @FXML
    Button buttonExtractFile;
    @FXML
    public Button modifyDetails;
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
    public TextArea showNote;
    @FXML
    public TextArea showQuote;
    @FXML
    public TextField searchBar;
    @FXML
    public DatePicker dateMin;
    @FXML
    public DatePicker dateMax;
    @FXML
    public ComboBox<String> listTheme;
    @FXML
    public ComboBox<String> listTypeOfDocument;
    @FXML
    public AnchorPane showAdditional;
    @FXML
    public AnchorPane showOtherDetails;
    @FXML
    public HBox buttonEdit;

    public List<File> codeFilesExtract = new ArrayList();
    public List<File> codeFiles = new ArrayList();
    public List<java.io.File> files = new ArrayList();
    public List<Image> images = new ArrayList();
    public Path directoryDestination = ControllerSettings.getPath();
    private DatabaseView databaseView;
    public Double otherInputDefaultValueY;
    public final Set<KeyCode> pressedKeys = new HashSet();
    public List<File> listOfSelectedExtractFile = new ArrayList();
    public List<File> listOfSelectedRemoveExtractFile = new ArrayList();
    public ObservableList<String> tags = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        this.databaseView = new DatabaseView();
        this.tags.addListener(this::eventListenerTags);
        this.otherInputDefaultValueY = this.showOtherDetails.getLayoutY();
        this.root.setOnKeyPressed((e) -> {
            this.pressedKeys.add(e.getCode());
        });
        this.root.setOnKeyReleased((e) -> {
            this.pressedKeys.remove(e.getCode());
        });
        this.root.setOnMouseClicked((e) -> {
            if (e.getButton() == MouseButton.PRIMARY && !this.pressedKeys.contains(KeyCode.CONTROL)) {
                this.listOfSelectedExtractFile.clear();
                this.listOfSelectedRemoveExtractFile.clear();

                for (File f : codeFiles){
                    f.resetStyle();
                }

                for (File f : codeFilesExtract){
                    f.resetStyle();
                }
            }

        });
        this.searchBar.textProperty().addListener(this::searchBarListener);
        this.setComboBox();
        this.setupListOfFile();
        this.getFile();
        this.showFileAfterUpdate();
        BasicConfigurator.configure();
        SingletonController.getInstance().controllerView = this;
    }

    private void showFileAfterUpdate() {
        if (SingletonController.getInstance().getFile() != null) {
            for (File f : codeFiles) {
                if (f.getName().equals(SingletonController.getInstance().getFile().getName())) {
                    f.showView();
                }
            }
        }
    }

    private void setComboBox() {
        this.listTheme.getItems().clear();
        this.listTypeOfDocument.getItems().clear();
        this.listTheme.getItems().addAll(SingletonDatabase.getInstance().getAllTheme());
        this.listTypeOfDocument.getItems().addAll(SingletonDatabase.getInstance().getAllTypeOfDocument());
        this.listTheme.setValue("All");
        this.listTypeOfDocument.setValue("All");
    }

    public void getFile() {
        java.io.File directory = new java.io.File(this.directoryDestination.toString());
        this.codeFiles.clear();
        this.showFile.getChildren().clear();
        if (!directory.exists()) {
            directory.mkdir();
        } else {
            try {
                Stream<Path> walk = Files.walk(this.directoryDestination);
                try {
                    this.files = walk.filter(Files::isRegularFile).map(Path::toFile).collect(Collectors.toList());
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            for (java.io.File f : files){
                Icon icon = FileSystemView.getFileSystemView().getSystemIcon(f);
                BufferedImage bufferedImage = new BufferedImage(icon.getIconWidth(), icon.getIconHeight(), 2);
                icon.paintIcon((Component)null, bufferedImage.getGraphics(), 0, 0);
                Image img = SwingFXUtils.toFXImage(bufferedImage, null);
                this.images.add(img);
                this.codeFiles.add(new File(f, img, this));
                this.codeFiles.get(this.codeFiles.size() - 1).setContainer("file");
                this.showFile.getChildren().add(this.codeFiles.get(this.codeFiles.size() - 1).getFlowPane());
            }
        }

    }

    private void eventListenerTags(Change<? extends String> change) {
        while(change.next()) {
            if (change.wasAdded()) {
                Button result = new Button(this.tags.get(this.tags.size() - 1));
                result.setPrefHeight(20.0D);
                result.setContentDisplay(ContentDisplay.RIGHT);
                result.setStyle("-fx-padding: 5 5 5 5");
                this.showTags.getChildren().add(result);
            }

            if (change.wasRemoved()) {
                this.showTags.getChildren().subList(change.getFrom(), change.getFrom() + change.getRemovedSize()).clear();
            }
        }

    }

    private void searchBarListener(Observable observable) {
        if (!this.searchBar.getText().isEmpty()) {
            this.search(null);
        } else {
            this.getFile();
        }

    }

    public static String removeAccents(String text) {
        return text == null ? null : Normalizer.normalize(text, Form.NFD).replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
    }

    public void search(ActionEvent event) {
        this.loadDataOfCodeFile();
        if (this.listTheme.getValue().equals("All") && this.listTypeOfDocument.getValue().equals("All") && this.dateMax.getValue() == null && this.dateMin.getValue() == null) {
            this.getFile();
        }

        if (!this.listTheme.getValue().equals("All") || !this.listTypeOfDocument.getValue().equals("All")) {
            this.getFile();
            this.loadDataOfCodeFile();
            List<File> tempCodeFile = new ArrayList();
            for (File f : codeFiles){
                if (!this.listTypeOfDocument.getValue().equals("All")) {
                    if (removeAccents(f.typeOfDocument).equals(removeAccents(this.listTypeOfDocument.getValue()))) {
                        if (!this.listTheme.getValue().equals("All")) {
                            if (f.theme.contains(this.listTheme.getValue())) {
                                tempCodeFile.add(f);
                            }
                        } else {
                            tempCodeFile.add(f);
                        }
                    }
                } else if (!this.listTheme.getValue().equals("All")) {
                    if (removeAccents(f.theme).contains(removeAccents(this.listTheme.getValue()))) {
                        if (!this.listTypeOfDocument.getValue().equals("All")) {
                            if (f.typeOfDocument.equals(this.listTypeOfDocument.getValue())) {
                                tempCodeFile.add(f);
                            }
                        } else {
                            tempCodeFile.add(f);
                        }
                    }
                }
            }

            this.codeFiles.clear();
            this.showFile.getChildren().clear();
            for (File f : tempCodeFile){
                this.codeFiles.add(f);
                this.showFile.getChildren().add(f.getFlowPane());
            }
        }

        if (this.dateMax.getValue() != null || this.dateMin.getValue() != null) {
            if (this.listTheme.getValue().equals("All") && this.listTypeOfDocument.getValue().equals("All")) {
                this.getFile();
            }

            try {
                this.loadWithDate();
            } catch (Exception var5) {
                System.out.println(var5.getMessage());
            }
        }

        if (!this.searchBar.getText().isEmpty()) {
            if (this.codeFiles.isEmpty()) {
                this.getFile();
            }

            this.loadWithSearch();
        }

    }

    @FXML
    private void handleDragOver(DragEvent event) {
        event.acceptTransferModes(TransferMode.ANY);
    }

    @FXML
    private void handleDropped(DragEvent event) {
        if (this.listOfSelectedExtractFile.isEmpty()) {
            try {
                JSONObject object = new JSONObject(this.getFile(event.getDragboard().getString()).toString());
                String name = object.getString("name");
                this.dropFile(name);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        } else {
            this.dropListOfFile();
        }

    }

    public void dropListOfFile() {
        for (File f : listOfSelectedExtractFile){
            this.dropFile(f.name);
        }
        this.listOfSelectedExtractFile.clear();

        for (File f : codeFiles){
            f.resetStyle();
        }
    }

    private void dropFile(String name) {
        if (this.droppedIsPossible(name)) {
            for (File f : codeFiles){
                if (f.getName().equals(name)) {
                    this.codeFilesExtract.add(new File(f.getFile(), f.getImage(), this));
                    this.codeFilesExtract.get(this.codeFilesExtract.size() - 1).setContainer("extract");
                    this.showExtractFile.getChildren().add(((File)this.codeFilesExtract.get(this.codeFilesExtract.size() - 1)).getFlowPane());
                    break;
                }
            }
        }

        this.listOfSelectedRemoveExtractFile.clear();
    }

    @FXML
    private void handleDroppedRemove(DragEvent event) {
        if (this.listOfSelectedRemoveExtractFile.isEmpty()) {
            try {
                JSONObject object = new JSONObject(this.getFile(event.getDragboard().getString()).toString());
                String name = object.getString("name");
                String container = object.getString("container");
                this.removeExtractFile(name, container);
            } catch (Exception var5) {
                System.out.println(var5.getMessage());
            }
        } else {
            this.dropRemoveListOfFile();
        }

    }

    public void dropRemoveListOfFile() {
        for (File f : listOfSelectedRemoveExtractFile){
            this.removeExtractFile(f.name, f.container);
        }
    }

    private void removeExtractFile(String name, String container) {
        if (this.droppedForRemoveIsPossible(name, container)) {
            for (File f : codeFilesExtract){
                if (f.getName().equals(name)) {
                    f.removeInExtractFile();
                    break;
                }
            }
        }
    }

    private boolean droppedIsPossible(String name) {
        boolean alreadyExist = true;
        for (File f : codeFilesExtract){
            if (f.getName().equals(name)) {
                alreadyExist = false;
                break;
            }
        }

        return alreadyExist;
    }

    private boolean droppedForRemoveIsPossible(String name, String container) {
        boolean alreadyExist = false;
        if (container.equals("file")) {
            return false;
        } else {
            for (File f : codeFilesExtract){
                if (f.getName().equals(name)) {
                    alreadyExist = true;
                    break;
                }
            }

            return alreadyExist;
        }
    }

    private void loadWithDate() {
        this.loadDataOfCodeFile();
        List<File> tempCodeFile = new ArrayList();
        List<File> secondTempCode = new ArrayList();
        Iterator var3;
        File f;
        if (this.dateMin.getValue() != null) {
            var3 = this.codeFiles.iterator();

            label83:
            while(true) {
                do {
                    if (!var3.hasNext()) {
                        if (this.dateMax.getValue() != null) {
                            var3 = tempCodeFile.iterator();

                            while(true) {
                                do {
                                    if (!var3.hasNext()) {
                                        if (secondTempCode.isEmpty()) {
                                            secondTempCode.add(null);
                                        }
                                        break label83;
                                    }

                                    f = (File)var3.next();
                                } while(!LocalDate.parse(f.date).isBefore(this.dateMax.getValue()) && !LocalDate.parse(f.date).isEqual(this.dateMax.getValue()));

                                secondTempCode.add(f);
                            }
                        }
                        break label83;
                    }

                    f = (File)var3.next();
                } while(!LocalDate.parse(f.date).isAfter(this.dateMin.getValue()) && !LocalDate.parse(f.date).isEqual(this.dateMin.getValue()));

                tempCodeFile.add(f);
            }
        } else if (this.dateMax.getValue() != null) {
            var3 = this.codeFiles.iterator();

            label108:
            while(true) {
                do {
                    if (!var3.hasNext()) {
                        if (this.dateMin.getValue() != null) {
                            var3 = tempCodeFile.iterator();

                            while(true) {
                                do {
                                    if (!var3.hasNext()) {
                                        if (secondTempCode.isEmpty()) {
                                            secondTempCode.add(null);
                                        }
                                        break label108;
                                    }

                                    f = (File)var3.next();
                                } while(!LocalDate.parse(f.date).isAfter(this.dateMin.getValue()) && !LocalDate.parse(f.date).isEqual(this.dateMin.getValue()));

                                secondTempCode.add(f);
                            }
                        }
                        break label108;
                    }

                    f = (File)var3.next();
                } while(!LocalDate.parse(f.date).isBefore(this.dateMax.getValue()) && !LocalDate.parse(f.date).isEqual(this.dateMax.getValue()));

                tempCodeFile.add(f);
            }
        }

        this.codeFiles.clear();
        this.showFile.getChildren().clear();
        if (secondTempCode.size() == 0) {
            var3 = tempCodeFile.iterator();

            while(var3.hasNext()) {
                f = (File)var3.next();
                this.codeFiles.add(f);
                this.showFile.getChildren().add(f.getFlowPane());
            }
        } else if (secondTempCode.get(0) != null) {
            var3 = secondTempCode.iterator();

            while(var3.hasNext()) {
                f = (File)var3.next();
                this.codeFiles.add(f);
                this.showFile.getChildren().add(f.getFlowPane());
            }
        }

        this.loadDataOfCodeFile();
    }

    private void loadWithSearch() {
        this.loadDataOfCodeFile();
        List<File> tempCodeFile = new ArrayList();
        String[] input = this.searchBar.getText().split(" ");
        Iterator var3 = this.codeFiles.iterator();

        File f;
        while(var3.hasNext()) {
            f = (File)var3.next();
            String[] var5 = input;
            int var6 = input.length;

            for(int var7 = 0; var7 < var6; ++var7) {
                String s = var5[var7];
                Iterator var9 = f.tags.iterator();

                while(var9.hasNext()) {
                    String str = (String)var9.next();
                    if (this.isWordPresent(str.toLowerCase(), s.toLowerCase()) && this.addInTempCodeFile(tempCodeFile, f)) {
                        tempCodeFile.add(f);
                    }
                }

                if (this.isWordPresent(f.getName().toLowerCase(), s.toLowerCase()) && this.addInTempCodeFile(tempCodeFile, f)) {
                    tempCodeFile.add(f);
                }

                if (this.isWordPresent(f.author.toLowerCase(), s.toLowerCase()) && this.addInTempCodeFile(tempCodeFile, f)) {
                    tempCodeFile.add(f);
                }

                if (this.isWordPresent(f.title.toLowerCase(), s.toLowerCase()) && this.addInTempCodeFile(tempCodeFile, f)) {
                    tempCodeFile.add(f);
                }
            }
        }

        this.codeFiles.clear();
        this.showFile.getChildren().clear();
        var3 = tempCodeFile.iterator();

        while(var3.hasNext()) {
            f = (File)var3.next();
            this.codeFiles.add(f);
            this.showFile.getChildren().add(f.getFlowPane());
        }

    }

    private boolean addInTempCodeFile(List<File> tempCodeFile, File f) {
        boolean alreadyExist = false;
        Iterator var4 = tempCodeFile.iterator();

        while(var4.hasNext()) {
            File file = (File)var4.next();
            if (f.equals(file)) {
                alreadyExist = true;
                break;
            }
        }

        return !alreadyExist;
    }

    private boolean isWordPresent(String first, String second) {
        if (first.length() < second.length()) {
            return false;
        } else if (first.length() == second.length()) {
            return first.equals(second);
        } else {
            boolean result = false;
            String[] word = first.split("");
            int i = 0;
            String[] var6 = second.split("");
            int var7 = var6.length;

            for(int var8 = 0; var8 < var7; ++var8) {
                String c = var6[var8];
                result = c.equals(word[i]);
                if (!result) {
                    break;
                }

                ++i;
            }

            return result;
        }
    }

    private void loadDataOfCodeFile() {
        Iterator var1 = this.codeFiles.iterator();

        while(var1.hasNext()) {
            File f = (File)var1.next();
            this.databaseView.setDetailsOfFile(f);
        }

    }

    private Object getFile(String s) throws IOException, ClassNotFoundException {
        byte[] data = Base64.getDecoder().decode(s);
        ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data));
        Object o = ois.readObject();
        ois.close();
        return o;
    }

    private void setupListOfFile() {
        this.scrollShowFile.setFitToWidth(true);
        this.scrollExtractFile.setFitToWidth(true);


        this.showDetails.setVisible(false);
        this.modifyDetails.setVisible(false);
        this.buttonEdit.setVisible(false);
        double widthMaxSize = 870.0D;
        this.anchorPaneFile.setMinWidth(widthMaxSize);
        this.scrollShowFile.setMinWidth(widthMaxSize);
        this.showFile.setMinWidth(widthMaxSize - 20.0D);
        this.anchorPaneExtractFile.setMinWidth(widthMaxSize);
        this.scrollExtractFile.setMinWidth(widthMaxSize);
        this.showExtractFile.setMinWidth(widthMaxSize - 20.0D);
        this.hBoxExtractFile.setMinWidth(widthMaxSize);
    }

    @FXML
    public void onActionCloseDetails(ActionEvent actionEvent) {
        SingletonFileSelected.getInstance().reset();
        this.setupListOfFile();
        Iterator var2 = this.codeFiles.iterator();

        while(var2.hasNext()) {
            File f = (File)var2.next();
            f.resetStyle();
        }

        Platform.runLater(() -> {
            this.root.requestFocus();
        });
    }

    @FXML
    public void onUserWantToExtractFileWithMetadata(ActionEvent actionEvent) {
        if (!this.codeFilesExtract.isEmpty()) {
            java.io.File directory = this.getExtractFile();
            if (directory != null) {
                java.io.File extractMetadata = new java.io.File(directory + "\\metadata");
                extractMetadata.mkdir();
                Iterator var4 = this.codeFilesExtract.iterator();

                while(var4.hasNext()) {
                    File f = (File)var4.next();

                    try {
                        Files.copy(Paths.get(f.getFile().getAbsolutePath()), Paths.get(directory.getAbsolutePath() + "\\" + f.getName()));
                    } catch (IOException var7) {
                        var7.printStackTrace();
                    }
                }

                Path path = Paths.get(extractMetadata.getAbsolutePath() + "\\metadata.txt");
                Iterator var9 = this.codeFilesExtract.iterator();

                while(var9.hasNext()) {
                    File f = (File)var9.next();
                    f.extractMetadata(path);
                }

                this.extractFile(directory);
            }
        }

    }

    public void onUserWantToShowFile(MouseEvent event) throws IOException {
        if (!this.detailsName.getText().equals("Not available")) {
            java.io.File file = new java.io.File(this.directoryDestination + "\\" + SingletonController.getInstance().getFile().name);
            Desktop.getDesktop().open(file);
        }

    }

    public void onUserWantToExtractFile(ActionEvent event) {
        if (!this.codeFilesExtract.isEmpty()) {
            java.io.File directory = this.getExtractFile();
            if (directory != null) {
                Iterator var3 = this.codeFilesExtract.iterator();

                while(var3.hasNext()) {
                    File f = (File)var3.next();

                    try {
                        Files.copy(Paths.get(f.getFile().getAbsolutePath()), Paths.get(directory.getAbsolutePath() + "\\" + f.getName()));
                    } catch (IOException var6) {
                        var6.printStackTrace();
                    }
                }

                this.extractFile(directory);
            }
        }

    }

    public void onUserWantToExtractMetadata(ActionEvent event) {
        if (!this.codeFilesExtract.isEmpty()) {
            java.io.File directory = this.getExtractFile();
            if (directory != null) {
                Path path = Paths.get(directory.getAbsolutePath() + "\\metadata.txt");
                Iterator var4 = this.codeFilesExtract.iterator();

                while(var4.hasNext()) {
                    File f = (File)var4.next();
                    f.extractMetadata(path);
                }

                this.extractFile(directory);
            }
        }

    }

    public java.io.File getExtractFile() {
        FileChooser fileChooser = new FileChooser();
        java.io.File file = fileChooser.showSaveDialog(ControllerMenu.stageShow);
        if (file != null) {
            java.io.File directory = new java.io.File(file.getParent() + "\\" + FilenameUtils.removeExtension(file.getName()));
            if (!directory.exists()) {
                directory.mkdir();
            }

            return directory;
        } else {
            return null;
        }
    }

    public void extractFile(java.io.File directory) {
        Thread thread = new Thread(){
            @Override
            public void run() {
                super.run();
                ZipUtil.pack(new java.io.File(directory.getAbsolutePath()), new java.io.File(directory.getAbsolutePath() + ".zip"));
                try {
                    FileUtils.deleteDirectory(directory);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };

        thread.start();

        try {
            this.showExtractFile.getChildren().clear();
            this.codeFilesExtract.clear();
        } catch (Exception var3) {
            System.out.println(var3.getMessage());
        }
    }
}
