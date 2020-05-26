//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package code.controller;

import code.database.DatabaseAdd;
import code.json.JsonExport;
import code.singleton.SingletonController;
import code.singleton.SingletonDatabase;
import code.singleton.SingletonFileSelected;
import code.utils.AdditionalInput;
import code.utils.AutoIncrement;
import code.utils.TagFile;
import java.awt.Component;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.Normalizer;
import java.text.Normalizer.Form;
import java.time.LocalDate;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ListChangeListener.Change;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javax.swing.Icon;
import javax.swing.filechooser.FileSystemView;
import org.apache.commons.io.FileUtils;

public class ControllerAddFile {
    @FXML
    AnchorPane root;
    @FXML
    AnchorPane entryTypeOfDocument;
    @FXML
    AnchorPane otherInput;
    @FXML
    public ImageView showIcon;
    @FXML
    public Label titleOfImportFileAdd;
    @FXML
    public TextField inputTitle;
    @FXML
    public DatePicker inputDate;
    @FXML
    public ComboBox<String> inputTypeOfDocument;
    @FXML
    public ComboBox<String> inputTheme;
    @FXML
    public ComboBox<String> inputKeyWord;
    @FXML
    public ComboBox<String> inputAuthor;
    @FXML
    private FlowPane showKeyWord;
    @FXML
    private FlowPane showAddTheme;
    @FXML
    public Button buttonEdit;
    @FXML
    public TextArea inputQuote;
    @FXML
    public TextArea inputNote;
    @FXML
    public Button buttonAdd;
    @FXML
    public Label labelAdd;
    @FXML
    public AnchorPane paneDragFile;
    @FXML
    public ScrollPane scrollPane;
    @FXML
    public CheckBox AvailabilityElec;
    @FXML
    public CheckBox AvailabilityLib;
    @FXML
    public CheckBox AvailabilityPrint;


    public List<File> filesAdd;
    public Path directoryDestination = ControllerSettings.getPath();
    private DatabaseAdd databaseAdd;
    public String newTitleOfFile;
    public Double otherInputDefaultValueY;
    public Double defaultHeight;
    public Double defaultValueY;
    public boolean fileIsAvailable;
    public ObservableList<String> addTags = FXCollections.observableArrayList();
    public ObservableList<String> addTheme = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        this.databaseAdd = new DatabaseAdd();
        this.addTheme.addListener(this::eventListenerAddTheme);
        this.addTags.addListener(this::eventListenerAddTags);
        this.inputTheme.getEditor().textProperty().addListener((obs, oldText, newText) -> {
            this.inputTheme.setValue(newText);
        });
        this.inputAuthor.getEditor().textProperty().addListener((obs, oldText, newText) -> {
            this.inputAuthor.setValue(newText);
        });
        this.inputKeyWord.getEditor().textProperty().addListener((obs, oldText, newText) -> {
            this.inputKeyWord.setValue(newText);
        });
        this.otherInputDefaultValueY = this.otherInput.getLayoutY();
        this.defaultHeight = this.scrollPane.getPrefHeight() + 10.0D;
        this.defaultValueY = this.scrollPane.getLayoutY();
        this.AvailabilityElec.setOnAction(this::eventListenerAvailability);
        this.AvailabilityPrint.setOnAction(this::eventListenerAvailability);
        this.AvailabilityLib.setOnAction(this::eventListenerAvailability);
        this.setComboBox();
        this.eventListenerAvailability(null);
        this.verifyIfUserEditFile();
    }

    private void verifyIfUserEditFile() {
        if (SingletonFileSelected.getInstance().file != null) {
            this.buttonAdd.setVisible(false);
            this.labelAdd.setText("Edit file");
            this.buttonEdit.setVisible(true);
            String list = SingletonFileSelected.getInstance().file.firstMap.get("keyWord").replace("\"", "").replace("[", "").replace("]", "");
            String[] ary = list.split(",");
            List<String> allKey = SingletonDatabase.getInstance().getAllKeyWord();

            for(String str : ary) {
                for (String str2: allKey) {
                    if (str.equals(removeAccents(str2))) {
                        this.addTags.add(str);
                        break;
                    }
                }
            }


            String listTheme = SingletonFileSelected.getInstance().file.firstMap.get("theme").replace("\"", "").replace("[", "").replace("]", "");
            String[] aryTheme = listTheme.split(",");
            List<String> allTheme = SingletonDatabase.getInstance().getAllTheme();


            for(String str : aryTheme) {
                for (String str2: allTheme) {
                    if (str.equals(removeAccents(str2))) {
                        this.addTheme.add(str);
                        break;
                    }
                }
            }


            this.titleOfImportFileAdd.setText(SingletonFileSelected.getInstance().title);
            this.showIcon.setImage(SingletonFileSelected.getInstance().image);
            this.inputTitle.setText(SingletonFileSelected.getInstance().file.firstMap.get("title"));
            this.inputAuthor.setValue(SingletonFileSelected.getInstance().file.firstMap.get("Author"));
            this.inputDate.setValue(LocalDate.parse(SingletonFileSelected.getInstance().file.firstMap.get("date")));
            this.inputTypeOfDocument.setValue(SingletonFileSelected.getInstance().file.firstMap.get("typeOfDocument"));
            this.inputNote.setText(SingletonFileSelected.getInstance().file.firstMap.get("note"));
            this.inputQuote.setText(SingletonFileSelected.getInstance().file.firstMap.get("quote"));

            for (String str : this.inputTypeOfDocument.getItems()){
                if (removeAccents(str).equals(SingletonFileSelected.getInstance().file.firstMap.get("typeOfDocument"))) {
                    this.inputTypeOfDocument.getSelectionModel().select(str);
                    this.eventListenerTypeOfDocument(null);
                    break;
                }
            }

            String[] split = SingletonFileSelected.getInstance().file.firstMap.get("availability").split(",");

            for(String str : split) {
                if (str.equals("Electronic")) {
                    this.AvailabilityElec.selectedProperty().setValue(true);
                }

                if (str.equals("Library")) {
                    this.AvailabilityLib.selectedProperty().setValue(true);
                }

                if (str.equals("Print/Paper")) {
                    this.AvailabilityPrint.selectedProperty().setValue(true);
                }
            }

            SingletonController.getInstance().templateController.showToEdit();
            Platform.runLater(() -> {
                this.root.requestFocus();
            });
        } else {
            this.AvailabilityElec.selectedProperty().setValue(true);
            this.eventListenerAvailability(new ActionEvent());
        }

    }

    public static String removeAccents(String text) {
        return text == null ? null : Normalizer.normalize(text, Form.NFD).replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
    }

    private void setComboBox() {
        this.inputTheme.getItems().clear();
        this.inputTypeOfDocument.getItems().clear();

        this.inputTheme.getItems().addAll(SingletonDatabase.getInstance().getAllTheme());
        this.inputTypeOfDocument.getItems().addAll(SingletonDatabase.getInstance().getAllTypeOfDocument());

        this.inputTheme.getItems().remove(0);
        this.inputTypeOfDocument.getItems().remove(0);

        this.inputAuthor.getItems().addAll(SingletonDatabase.getInstance().getAllAuthor());
        this.inputKeyWord.getItems().addAll(SingletonDatabase.getInstance().getAllKeyWord());

        AutoIncrement AutoITheme = new AutoIncrement(this.inputTheme);
        AutoIncrement AutoITypeOfDoc = new AutoIncrement(this.inputTypeOfDocument);
        AutoIncrement AutoIAuthor = new AutoIncrement(this.inputAuthor);
        AutoIncrement AutoIKey = new AutoIncrement(this.inputKeyWord);

        this.inputTheme.getEditor().setOnKeyPressed((ke) -> {
            Platform.runLater(AutoITheme::show);
        });
        this.inputTypeOfDocument.getEditor().setOnKeyPressed((ke) -> {
            Platform.runLater(AutoITypeOfDoc::show);
        });
        this.inputAuthor.getEditor().setOnKeyPressed((ke) -> {
            Platform.runLater(AutoIAuthor::show);
        });
        this.inputKeyWord.getEditor().setOnKeyPressed((ke) -> {
            Platform.runLater(AutoIKey::show);
        });
    }

    private void eventListenerAddTags(Change<? extends String> change) {
        while(change.next()) {
            if (change.wasAdded()) {
                Button btn = this.getButtonTag(this.addTags.get(this.addTags.size() - 1));
                btn.setOnMouseClicked((event) -> {
                    if (event.getClickCount() == 2) {
                        this.addTags.remove(btn.getText());
                    }

                });
                this.showKeyWord.getChildren().add(btn);
            }

            if (change.wasRemoved()) {
                this.root.requestFocus();
                this.showKeyWord.getChildren().subList(change.getFrom(), change.getFrom() + change.getRemovedSize()).clear();
            }
        }

    }

    private void eventListenerAddTheme(Change<? extends String> change) {
        while(change.next()) {
            if (change.wasAdded()) {
                Button btn = this.getButtonTag(this.addTheme.get(this.addTheme.size() - 1));
                btn.setOnMouseClicked((event) -> {
                    if (event.getClickCount() == 2) {
                        this.addTheme.remove(btn.getText());
                    }

                });
                this.showAddTheme.getChildren().add(btn);
            }

            if (change.wasRemoved()) {
                this.root.requestFocus();
                this.showAddTheme.getChildren().subList(change.getFrom(), change.getFrom() + change.getRemovedSize()).clear();
            }
        }

    }

    public void eventListenerTypeOfDocument(ActionEvent event) {
        AnchorPane content = null;

        try {
            String result = (new AdditionalInput()).getAdditionalInput(this.inputTypeOfDocument.getValue());
            content = FXMLLoader.load(ClassLoader.getSystemResource(result));
            this.entryTypeOfDocument.getChildren().clear();
            this.entryTypeOfDocument.getChildren().addAll(content);
            this.otherInput.setLayoutY(this.otherInputDefaultValueY);
            this.otherInput.setLayoutY(content.getPrefHeight() + this.otherInput.getLayoutY());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    public void eventListenerAvailability(ActionEvent event) {
        if (this.AvailabilityElec.selectedProperty().getValue()) {
            this.fileIsAvailable = true;
            this.paneDragFile.setVisible(true);
            this.scrollPane.setLayoutY(this.defaultValueY);
            this.scrollPane.setPrefHeight(this.defaultHeight);
        } else if (this.AvailabilityLib.selectedProperty().getValue() || this.AvailabilityPrint.selectedProperty().getValue()) {
            this.fileIsAvailable = false;
            this.paneDragFile.setVisible(false);
            this.scrollPane.setLayoutY(0.0D);
            this.scrollPane.setPrefHeight(this.root.getPrefHeight() + 10.0D);
        }

    }

    private Button getButtonTag(String tag) {
        Image image = new Image(String.valueOf(ClassLoader.getSystemResource("image/delete.png")));
        ImageView closeImg = new ImageView(image);
        Button result = new Button(tag, closeImg);
        result.setPrefHeight(20.0D);
        result.setContentDisplay(ContentDisplay.RIGHT);
        return result;
    }

    public void handleDropAdd(DragEvent e) {
        this.filesAdd = e.getDragboard().getFiles();
        Icon icon = FileSystemView.getFileSystemView().getSystemIcon((File)this.filesAdd.get(0));
        BufferedImage bufferedImage = new BufferedImage(icon.getIconWidth(), icon.getIconHeight(), 2);
        icon.paintIcon(null, bufferedImage.getGraphics(), 0, 0);
        Platform.runLater(() -> {
            Image img = SwingFXUtils.toFXImage(bufferedImage, null);
            this.showIcon.setImage(img);
            this.titleOfImportFileAdd.setText(this.filesAdd.get(0).getName());
        });
    }

    @FXML
    private void saveData() {
        String tag = (new TagFile()).getTag(this.inputAuthor.getValue(), this.addTheme, this.inputDate);
        File str;
        if (this.fileIsAvailable) {
            String extension = this.titleOfImportFileAdd.getText().substring(this.titleOfImportFileAdd.getText().lastIndexOf(".") + 1);
            try {
                Stream<Path> walk = Files.walk(this.directoryDestination);
                try {
                    List<File> files = walk.filter(Files::isRegularFile).map(Path::toFile).collect(Collectors.toList());
                    if (!files.isEmpty()) {
                        int id = 0;
                        String tempTag = tag;
                        for (File file : files){
                            if (file.getName().equals(tag + ".txt")) {
                                tag = tempTag + '_' + ++id;
                            }
                        }
                    }
                    this.newTitleOfFile = tag + "." + extension;
                    File source = new File(this.filesAdd.get(0).getAbsolutePath());
                    str = new File(this.directoryDestination + "\\" + tag + "." + extension);
                    FileUtils.copyFile(source, str);
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        } else {
            this.titleOfImportFileAdd.setText("Not available");
            try {
                Stream<Path> walk = Files.walk(this.directoryDestination);
                try {
                    List<File> files = walk.filter(Files::isRegularFile).map(Path::toFile).collect(Collectors.toList());
                    if (!files.isEmpty()) {
                        int id = 0;
                        String tempTag = tag;
                        for (File file : files){
                            if (file.getName().equals(tag + ".txt")) {
                                tag = tempTag + '_' + ++id;
                            }
                        }
                    }
                    this.newTitleOfFile = tag + ".txt";
                    File file = new File(this.directoryDestination + "\\" + this.newTitleOfFile);
                    file.createNewFile();
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }

        this.databaseAdd.save(this);
        (new JsonExport()).export(this);
        this.clearAddFile();
    }

    @FXML
    public void onUserWantToUpdateFile(ActionEvent actionEvent) {
        String tag = (new TagFile()).getTag(this.inputAuthor.getValue(), this.addTheme, this.inputDate);
        if (!SingletonFileSelected.getInstance().file.name.equals(this.titleOfImportFileAdd.getText())) {
            try {
                Files.delete(Paths.get(this.directoryDestination + "\\" + SingletonFileSelected.getInstance().file.getName()));
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }

            String extension = this.titleOfImportFileAdd.getText().substring(this.titleOfImportFileAdd.getText().lastIndexOf(".") + 1);
            SingletonFileSelected.getInstance().file.firstMap.put("name", this.titleOfImportFileAdd.getText());
            try {
                Stream<Path> walk = Files.walk(this.directoryDestination);
                try {
                    List<File> files = walk.filter(Files::isRegularFile).map(Path::toFile).collect(Collectors.toList());
                    if (!files.isEmpty()) {
                        int id = 0;
                        for (File file : files){
                            if (file.getName().equals(tag + "." + extension)) {
                                ++id;
                            }
                        }
                        if (id != 0) {
                            tag = tag + "_" + id;
                        }
                    }
                    this.newTitleOfFile = tag + "." + extension;
                    File source = new File(this.filesAdd.get(0).getAbsolutePath());
                    File dest = new File(this.directoryDestination + "\\" + tag + "." + extension);
                    FileUtils.copyFile(source, dest);
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            } catch (IOException var24) {
                System.out.println(var24.getMessage());
            }
        } else {
            this.newTitleOfFile = SingletonFileSelected.getInstance().file.name;
        }

        this.databaseAdd.save(this);
        (new JsonExport()).edit(this);

        try {
            Parent root = FXMLLoader.load(ClassLoader.getSystemResource("xml/view.fxml"));
            ControllerMenu.stageShow.setScene(new Scene(root));
        } catch (IOException var19) {
            var19.printStackTrace();
        }

    }

    private void clearAddFile() {
        if (this.fileIsAvailable) {
            this.showIcon.setImage(null);
            this.titleOfImportFileAdd.setText("");
        }

        this.otherInput.setLayoutY(this.otherInputDefaultValueY);
        this.entryTypeOfDocument.getChildren().clear();
        this.inputTitle.setText("");
        this.inputDate.setValue(null);
        this.inputNote.clear();
        this.inputQuote.clear();
        this.addTheme.clear();
        this.addTags.clear();
        this.AvailabilityPrint.selectedProperty().setValue(false);
        this.AvailabilityLib.selectedProperty().setValue(false);
        this.AvailabilityElec.selectedProperty().setValue(false);
        this.inputTheme.getItems().clear();
        this.inputTypeOfDocument.getItems().clear();
        this.inputAuthor.getItems().clear();
        this.inputKeyWord.getItems().clear();
        this.inputTheme.getItems().addAll(SingletonDatabase.getInstance().getAllTheme());
        this.inputTypeOfDocument.getItems().addAll(SingletonDatabase.getInstance().getAllTypeOfDocument());
        this.inputAuthor.getItems().addAll(SingletonDatabase.getInstance().getAllAuthor());
        this.inputKeyWord.getItems().addAll(SingletonDatabase.getInstance().getAllKeyWord());
    }

    public void handleDragOverAdd(DragEvent e) {
        if (SingletonFileSelected.getInstance().file == null) {
            e.acceptTransferModes(TransferMode.ANY);
        } else if (!SingletonFileSelected.getInstance().file.firstMap.get("name").equals("Not available") && e.getDragboard().hasFiles()) {
            e.acceptTransferModes(TransferMode.ANY);
        }

    }

    public void onEnterKeyPressTheme(KeyEvent e) {
        if (e.getCode() == KeyCode.ENTER && !this.inputTheme.getValue().isEmpty() && !this.addTheme.contains(this.inputTheme.getValue())) {
            this.addTheme.add(this.inputTheme.getValue());
            this.inputTheme.setValue("");
        }

    }

    public void onEnterKeyPressKeyWord(KeyEvent e) {
        if (e.getCode() == KeyCode.ENTER && !this.inputKeyWord.getValue().isEmpty() && !this.addTags.contains(this.inputKeyWord.getValue())) {
            this.addTags.add(this.inputKeyWord.getValue());
            this.inputKeyWord.setValue("");
        }
    }
}
