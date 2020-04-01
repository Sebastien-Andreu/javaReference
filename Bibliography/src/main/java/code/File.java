package code;

import javafx.application.Platform;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import javafx.scene.transform.Transform;
import org.apache.commons.io.FilenameUtils;
import singleton.SingletonController;
import singleton.SingletonDatabase;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class File implements Serializable{


    private java.io.File file;
    private String name;
    public String container;
    private Image image;
    private FlowPane flowPane;

    public String title, author, date, theme, typeOfDocument, extension;
    public List<String> tags= new ArrayList<>(), notes= new ArrayList<>(), quotes = new ArrayList<>();

    public File(java.io.File file, Image image){
        this.file = file;
        this.name = file.getName();
        this.image = image;
        setFlowPane();
    }

    private void setFlowPane(){
        this.flowPane = new FlowPane();
        this.flowPane.setOrientation(Orientation.HORIZONTAL);
        ImageView img = new ImageView();
        img.setFitHeight(25);
        img.setFitWidth(25);
        img.setImage(this.image);
        Label label = new Label(this.name);
        label.setAlignment(Pos.CENTER);

        this.flowPane.setOnMouseEntered(e -> {
            this.flowPane.setCursor(Cursor.HAND);
        });

        flowPane.setOnMouseClicked(this::onClickListenerShowDetails);

        this.flowPane.setStyle("-fx-border-width: 1;-fx-padding: 5 5 5 5; -fx-border-color: transparent transparent lightgrey transparent;");

        this.flowPane.getChildren().addAll(img, label);

        this.flowPane.setOnDragDetected(e -> {
            Dragboard db = this.flowPane.startDragAndDrop(TransferMode.ANY);
            SnapshotParameters sp =  new SnapshotParameters();
            sp.setFill(Color.TRANSPARENT);
            sp.setTransform(Transform.scale(1.5,1.5));
            db.setDragView(this.flowPane.snapshot(sp, null));

            ClipboardContent cb = new ClipboardContent();
            try {
                cb.putString(toString(this.toString()));
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            db.setContent(cb);
            e.consume();
        });
    }

    private void onClickListenerShowDetails(MouseEvent event) {
        showView();
    }

    public void showView(){
        setSize();
        SingletonDatabase.getInstance().setDetailsOfFile(this);
        SingletonController.getInstance().getController().tags.clear();
        SingletonController.getInstance().getController().notes.clear();
        SingletonController.getInstance().getController().quotes.clear();

        for (String str : this.tags) {
            SingletonController.getInstance().getController().tags.add(str);
        }

        SingletonController.getInstance().getController().quotes.addAll(this.quotes);
        SingletonController.getInstance().getController().notes.addAll(this.notes);
        SingletonController.getInstance().getController().detailsName.setText(this.name);
        SingletonController.getInstance().getController().detailsTitle.setText(this.title);
        SingletonController.getInstance().getController().detailsAuthor.setText(this.author);
        SingletonController.getInstance().getController().detailsDate.setText(this.date);
        SingletonController.getInstance().getController().detailsTheme.setText(this.theme);
        SingletonController.getInstance().getController().detailsTypeOfDocument.setText(this.typeOfDocument);

        SingletonController.getInstance().getController().modifyDetails.setOnAction(e -> {
            SingletonController.getInstance().getController().rootView.setVisible(false);
            SingletonController.getInstance().getController().rootSearch.setVisible(false);
            SingletonController.getInstance().getController().rootAdd.setVisible(true);
            SingletonController.getInstance().getController().buttonAddFile.setVisible(false);
            SingletonController.getInstance().getController().buttonUpdateFile.setVisible(true);
            SingletonController.getInstance().getController().labelAddFile.setVisible(false);

            SingletonController.getInstance().getController().addTags.clear();
            SingletonController.getInstance().getController().addQuotes.clear();
            SingletonController.getInstance().getController().addNotes.clear();
            SingletonController.getInstance().getController().themeAdd.getItems().clear();
            SingletonController.getInstance().getController().typeOfDocumentAdd.getItems().clear();

            SingletonController.getInstance().getController().themeAdd.getItems().addAll(SingletonDatabase.getInstance().getAllTheme());
            SingletonController.getInstance().getController().typeOfDocumentAdd.getItems().addAll(SingletonDatabase.getInstance().getAllTypeOfDocument());

            for (String str : this.tags) {
                SingletonController.getInstance().getController().addTags.add(str);
            }
            SingletonController.getInstance().getController().addQuotes.addAll(this.quotes);
            SingletonController.getInstance().getController().addNotes.addAll(this.notes);
            SingletonController.getInstance().getController().showIcon.setImage(this.image);
            SingletonController.getInstance().getController().titleOfImportFileAdd.setText(this.name);
            SingletonController.getInstance().getController().titleAdd.setText(this.title);
            SingletonController.getInstance().getController().authorAdd.setText(this.author);
            SingletonController.getInstance().getController().dateAdd.setValue(LocalDate.parse(this.date));
            SingletonController.getInstance().getController().themeAdd.setValue(this.theme);
            SingletonController.getInstance().getController().typeOfDocumentAdd.setValue(this.typeOfDocument);
            Platform.runLater( () -> SingletonController.getInstance().getController().rootAdd.requestFocus() );
        });
    }

    private void setSize(){
        double widthMinSize = 268;
        SingletonController.getInstance().getController().anchorPaneFile.setMinWidth(widthMinSize);
        SingletonController.getInstance().getController().scrollShowFile.setMinWidth(widthMinSize);
        SingletonController.getInstance().getController().showFile.setMinWidth(widthMinSize - 6);

        SingletonController.getInstance().getController().anchorPaneExtractFile.setMinWidth(widthMinSize);
        SingletonController.getInstance().getController().scrollExtractFile.setMinWidth(widthMinSize);
        SingletonController.getInstance().getController().showExtractFile.setMinWidth(widthMinSize - 6);
        SingletonController.getInstance().getController().hBoxExtractFile.setMinWidth(widthMinSize);

        SingletonController.getInstance().getController().anchorPaneDetails.setVisible(true);
        SingletonController.getInstance().getController().modifyDetails.setVisible(true);
        SingletonController.getInstance().getController().buttonEdit.setVisible(true);
    }

    public void createFileWithData(java.io.File directory){
        SingletonDatabase.getInstance().setDetailsOfFile(this);

        String fileNameWithOutExt = FilenameUtils.removeExtension(this.name);

        Path path = Paths.get(directory.getAbsolutePath() + "\\" + fileNameWithOutExt + ".txt");

        try (BufferedWriter writer = Files.newBufferedWriter(path)) {
            writer.write("Title : " + this.title + '\n');
            writer.write("Author : " + this.author + '\n');
            writer.write("Date : " + this.date + '\n');
            writer.write("Theme : " + this.theme + '\n');
            writer.write("Type of document : " + this.typeOfDocument + '\n');
            writer.write("Extension : " + this.extension + '\n');
            writer.newLine();
            writer.newLine();
            writer.newLine();
            if (!this.tags.isEmpty()){
                writer.write("Key word :\n");
                for (String str : this.tags){
                    writer.write("\t" + str);
                }
            }
            if (!this.notes.isEmpty()){
                writer.write("Note :\n");
                for (String str : this.notes){
                    writer.write("\t" + str);
                }
            }
            if (!this.quotes.isEmpty()){
                writer.newLine();
                writer.write("Quote :\n");
                for (String str : this.quotes){
                    writer.write("\t" + str);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public FlowPane getFlowPane(){
        return this.flowPane;
    }

    public String getName(){
        return this.name;
    }

    public java.io.File getFile(){
        return this.file;
    }

    public Image getImage() {
        return this.image;
    }

    public void removeInExtractFile(){
        for (Node node: SingletonController.getInstance().getController().showExtractFile.getChildren()){
            if (node.getClass().equals(FlowPane.class)){
                if (node == this.flowPane){
                    SingletonController.getInstance().getController().showExtractFile.getChildren().remove(this.flowPane);
                    SingletonController.getInstance().getController().codeFilesExtract.remove(this);
                }
            }
        }
    }

    public void removeInCodeFile(){
        for (Node node: SingletonController.getInstance().getController().showFile.getChildren()){
            if (node.getClass().equals(FlowPane.class)){
                if (node == this.flowPane){
                    SingletonController.getInstance().getController().showFile.getChildren().remove(this.flowPane);
                    SingletonController.getInstance().getController().codeFiles.remove(this);
                }
            }
        }
    }

    public void setContainer(String container){
        this.container = container;
    }

    @Override
    public String toString() {
        return "{ \"name\":\"" + name + '\"' +
                ", \"container\":\"" + container +"\"}";
    }

    private static String toString(Serializable o ) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(o);
        oos.close();
        return Base64.getEncoder().encodeToString(baos.toByteArray());
    }
}
