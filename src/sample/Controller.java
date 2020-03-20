package sample;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;
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
import java.util.List;

public class Controller {

    @FXML
    private ImageView showIcon;

    @FXML
    private AnchorPane root;

    @FXML
    private Label showTitle;

    @FXML
    private TextField inputTags;

    @FXML
    private FlowPane showTags;

    public static ObservableList<String> tags = FXCollections.observableArrayList();;

    @FXML
    public void initialize() {
        tags.addListener(this::eventListenerTags);
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
        Icon icon = FileSystemView.getFileSystemView().getSystemIcon(files.get(0));

        BufferedImage bufferedImage = new BufferedImage(icon.getIconWidth(), icon.getIconHeight(), BufferedImage.TYPE_INT_ARGB);
        icon.paintIcon(null,bufferedImage.getGraphics(), 0, 0);
        Platform.runLater(() -> {
            Image img = SwingFXUtils.toFXImage(bufferedImage, null);
            showIcon.setImage(img);
            showTitle.setText(files.get(0).getName());
        });
    }

    @FXML
    private void onEnterKeyPressTags(KeyEvent e){
        if (e.getCode() == KeyCode.ENTER && !inputTags.getText().isEmpty() && !tags.contains(inputTags.getText())){
            tags.add(inputTags.getText());
            System.out.println(inputTags.getText());
            inputTags.clear();
        }
    }

    private void eventListenerTags(ListChangeListener.Change<? extends String> change){
        while (change.next()){
            if (change.wasAdded()){
                showTags.getChildren().add(new Tag().getTag(tags.get(tags.size()-1)));
            }
            if (change.wasRemoved()) {
                root.requestFocus();
                showTags.getChildren().subList(change.getFrom(), change.getFrom() + change.getRemovedSize()).clear();
            }
        }
    }
}
