package sample;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.Image;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.transform.Transform;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class Controller {

    @FXML
    private AnchorPane root;

    @FXML
    VBox showFile;

    @FXML
    VBox showExtractFile;

    public static List<File> files;
    public static List<File> listOfFileExtract = new ArrayList<>();


    public ObservableList<String> stringFile = FXCollections.observableArrayList();
    public ObservableList<String> stringFileExtract = FXCollections.observableArrayList();
    public List<Image> pictureFile = new ArrayList<>();
    public List<Image> pictureFileExtract = new ArrayList<>();


    public String directoryDestination = System.getProperty("user.home") + "\\Documents\\biblio";

    @FXML
    public void initialize() {
        stringFile.addListener(this::eventListenerAllFile);
        stringFileExtract.addListener(this::eventListenerExtractFile);

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

    private void eventListenerAllFile(ListChangeListener.Change<? extends String> change){
        while (change.next()){
            if (change.wasAdded()){
                FlowPane anchorPane = new Tag().Tag(stringFile.get(stringFile.size()-1), pictureFile.get(pictureFile.size()-1));

                anchorPane.setOnDragDetected(e -> {
                    Dragboard db = anchorPane.startDragAndDrop(TransferMode.ANY);
                    SnapshotParameters sp =  new SnapshotParameters();
                    sp.setFill(Color.TRANSPARENT);
                    sp.setTransform(Transform.scale(1.5,1.5));
                    db.setDragView(anchorPane.snapshot(sp, null));

                    ClipboardContent cb = new ClipboardContent();

                    cb.putString(files.get(files.size() - 1).toString());

                    db.setContent(cb);
                    e.consume();
                });
                showFile.getChildren().add(anchorPane);
            }
            if (change.wasRemoved()){
                showFile.getChildren().subList(change.getFrom(), change.getFrom() + change.getRemovedSize()).clear();
            }
        }
    }

    private void eventListenerExtractFile(ListChangeListener.Change<? extends String> change){
        while (change.next()){
            if (change.wasAdded()){
                FlowPane anchorPane = new Tag().Tag(stringFileExtract.get(stringFileExtract.size()-1), pictureFileExtract.get(pictureFileExtract.size()-1));
                anchorPane.setOnDragDetected(e -> {
                    Dragboard db = anchorPane.startDragAndDrop(TransferMode.ANY);
                    SnapshotParameters sp =  new SnapshotParameters();
                    sp.setFill(Color.TRANSPARENT);
                    sp.setTransform(Transform.scale(1.5,1.5));
                    db.setDragView(anchorPane.snapshot(sp, null));
                    ClipboardContent cb = new ClipboardContent();

                    cb.putString(listOfFileExtract.get(listOfFileExtract.size() - 1).toString());

                    db.setContent(cb);
                    e.consume();
                });
                showExtractFile.getChildren().add(anchorPane);
            }
            if (change.wasRemoved()){
                showExtractFile.getChildren().subList(change.getFrom(), change.getFrom() + change.getRemovedSize()).clear();
            }
        }
    }

    @FXML
    private void handleDragOver(DragEvent event){
        event.acceptTransferModes(TransferMode.ANY);
    }

    @FXML
    private void handleDropped(DragEvent event){
        int i = 0;
        if (droppedIsPossible(event.getDragboard().getString())){
            for (File f : files){
                if (f.toString().equals(event.getDragboard().getString())){
                    listOfFileExtract.add(f);
                    pictureFileExtract.add(pictureFile.get(i));
                    stringFileExtract.add(stringFile.get(i));

                    pictureFile.remove(i);
                    stringFile.remove(i);

                    files.remove(f);
                    break;
                }
                ++i;
            }
        }
    }

    @FXML
    private void handleDragOverRemove(DragEvent event){
        event.acceptTransferModes(TransferMode.ANY);
    }

    @FXML
    private void handleDroppedRemove(DragEvent event){
        int i = 0;
        if (droppedFileIsPossible(event.getDragboard().getString())) {
            for (File f : listOfFileExtract) {
                if (f.toString().equals(event.getDragboard().getString())) {
                    files.add(f);
                    pictureFile.add(pictureFileExtract.get(i));
                    stringFile.add(stringFileExtract.get(i));

                    pictureFileExtract.remove(i);
                    stringFileExtract.remove(i);

                    listOfFileExtract.remove(f);
                    break;
                }
                ++i;
            }
        }
    }

    private boolean droppedIsPossible(String str){
        boolean alreadyExist = true;
        for (File f : listOfFileExtract){
            if (f.toString().equals(str)) {
                alreadyExist = false;
                break;
            }
        }
        return alreadyExist;
    }

    private boolean droppedFileIsPossible(String str){
        boolean alreadyExist = true;
        for (File f : files){
            if (f.toString().equals(str)) {
                alreadyExist = false;
                break;
            }
        }
        return alreadyExist;
    }
}
