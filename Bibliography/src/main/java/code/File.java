package code;

import code.controller.ControllerMenu;
import code.controller.ControllerView;
import code.database.DatabaseView;
import code.json.JsonReader;
import code.singleton.SingletonController;
import code.singleton.SingletonDatabase;
import code.singleton.SingletonFileSelected;
import java.io.ByteArrayOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import javafx.scene.transform.Transform;

public class File implements Serializable {
    private java.io.File file;
    public Image image;
    private FlowPane flowPane;
    private ControllerView controller;
    private DatabaseView databaseView;
    public String name;
    public String container;
    public String title;
    public String author;
    public String date;
    public String theme;
    public String typeOfDocument;
    public String extension;
    public List<String> tags = new ArrayList();
    public Map<String, String> additionalMap = null;
    public Map<String, String> firstMap = null;

    public File(java.io.File file, Image image, ControllerView controller) {
        this.file = file;
        this.name = file.getName();
        this.image = image;
        this.controller = controller;
        this.databaseView = new DatabaseView();
        this.setFlowPane();
    }

    private void setFlowPane() {
        this.flowPane = new FlowPane();
        this.flowPane.setOrientation(Orientation.HORIZONTAL);
        ImageView img = new ImageView();
        img.setFitHeight(25.0D);
        img.setFitWidth(25.0D);
        img.setImage(this.image);
        Label label = new Label(this.name);
        label.setAlignment(Pos.CENTER);
        this.flowPane.setOnMouseEntered((e) -> {
            this.flowPane.setCursor(Cursor.HAND);
        });
        this.flowPane.setOnMouseClicked((e) -> {
            if (e.getButton() == MouseButton.PRIMARY && this.controller.pressedKeys.contains(KeyCode.CONTROL)) {
                if (this.container.equals("file")) {
                    if (!this.controller.listOfSelectedExtractFile.contains(this)) {
                        this.flowPane.setStyle("-fx-background-color: #AE8B67;-fx-border-width: 1;-fx-padding: 5 5 5 5; -fx-border-color: transparent transparent #AE8B67 transparent;");
                        this.controller.listOfSelectedExtractFile.add(this);
                    } else {
                        this.controller.listOfSelectedExtractFile.remove(this);
                        this.flowPane.setStyle("-fx-border-width: 1;-fx-padding: 5 5 5 5; -fx-border-color: transparent transparent lightgrey transparent;");
                    }

                    for (File f : this.controller.codeFilesExtract){
                        f.resetStyle();
                    }
                }

                if (this.container.equals("extract")) {
                    if (!this.controller.listOfSelectedRemoveExtractFile.contains(this)) {
                        this.flowPane.setStyle("-fx-background-color: #AE8B67;-fx-border-width: 1;-fx-padding: 5 5 5 5; -fx-border-color: transparent transparent #AE8B67 transparent;");
                        this.controller.listOfSelectedRemoveExtractFile.add(this);
                    } else {
                        this.controller.listOfSelectedRemoveExtractFile.remove(this);
                        this.flowPane.setStyle("-fx-border-width: 1;-fx-padding: 5 5 5 5; -fx-border-color: transparent transparent lightgrey transparent;");
                    }

                    for (File f : this.controller.codeFiles){
                        f.resetStyle();
                    }
                }
            } else {
                this.controller.listOfSelectedExtractFile.clear();
                this.showView();
            }

        });
        this.flowPane.setStyle("-fx-border-width: 1;-fx-padding: 5 5 5 5; -fx-border-color: transparent transparent lightgrey transparent;");
        this.flowPane.getChildren().addAll(img, label);
        this.flowPane.setOnDragDetected((e) -> {
            Dragboard db = this.flowPane.startDragAndDrop(TransferMode.ANY);
            SnapshotParameters sp = new SnapshotParameters();
            sp.setFill(Color.TRANSPARENT);
            sp.setTransform(Transform.scale(1.5D, 1.5D));
            db.setDragView(this.flowPane.snapshot(sp, (WritableImage)null));
            ClipboardContent cb = new ClipboardContent();

            try {
                cb.putString(toString(this.toString()));
            } catch (IOException var6) {
                var6.printStackTrace();
            }

            db.setContent(cb);
            e.consume();
        });
    }

    public void getData() {
        JsonReader jsonReader = new JsonReader();
        this.firstMap = jsonReader.read(this.name);
        SingletonFileSelected.getInstance().file = this;
        SingletonDatabase.getInstance().IDSelected = Integer.parseInt((String)this.firstMap.get("ID"));
        SingletonFileSelected.getInstance().image = this.image;
        SingletonFileSelected.getInstance().title = this.name;
        this.additionalMap = SingletonController.getInstance().templateController.getData();
    }


    public void showView() {
        this.getData();
        this.setSize();
        SingletonController.getInstance().setFile(this);
        this.databaseView.setDetailsOfFile(this);
        this.controller.tags.clear();
        String list = this.firstMap.get("keyWord").replace("\"", "").replace("[", "").replace("]", "");
        String[] ary = list.split(",");

        for(String str : ary){
            this.controller.tags.add(str);
        }

        this.controller.detailsName.setText(this.firstMap.get("name"));
        this.controller.detailsTitle.setText(this.firstMap.get("title"));
        this.controller.detailsAuthor.setText(this.firstMap.get("Author"));
        this.controller.detailsDate.setText(this.firstMap.get("date"));
        this.controller.detailsTheme.setText(this.firstMap.get("theme").replace("[", "").replace("]", "").replace("\"", "").replace(",", ", "));
        this.controller.detailsTypeOfDocument.setText(this.firstMap.get("typeOfDocument"));
        this.controller.showNote.setText(this.firstMap.get("note"));
        this.controller.showQuote.setText(this.firstMap.get("quote"));
        AnchorPane pane = SingletonController.getInstance().templateController.loadView();
        this.controller.showAdditional.getChildren().clear();
        this.controller.showAdditional.getChildren().addAll(pane);
        this.controller.showOtherDetails.setLayoutY(this.controller.otherInputDefaultValueY);
        this.controller.showOtherDetails.setLayoutY(pane.getPrefHeight() + this.controller.showAdditional.getLayoutY());
        this.controller.detailsName.setOnMouseEntered((e) -> {
            this.controller.detailsName.setCursor(Cursor.HAND);
        });
        this.controller.detailsName.setOnMouseExited((e) -> {
            this.controller.detailsName.setCursor(Cursor.DEFAULT);
        });
        this.controller.modifyDetails.setOnAction((e) -> {
            try {
                Parent root = FXMLLoader.load(ClassLoader.getSystemResource("xml/view_add.fxml"));
                ControllerMenu.stageShow.setScene(new Scene(root));
            } catch (IOException var2) {
                var2.printStackTrace();
            }

        });
    }

    private void setSize() {
        double widthMinSize = 270.0D;
        this.controller.anchorPaneFile.setMinWidth(widthMinSize);
        this.controller.scrollShowFile.setMinWidth(widthMinSize);
        this.controller.showFile.setMinWidth(widthMinSize - 20.0D);
        this.controller.anchorPaneExtractFile.setMinWidth(widthMinSize);
        this.controller.scrollExtractFile.setMinWidth(widthMinSize);
        this.controller.showExtractFile.setMinWidth(widthMinSize - 20.0D);
        this.controller.hBoxExtractFile.setMinWidth(widthMinSize);
        this.controller.showDetails.setVisible(true);
        this.controller.modifyDetails.setVisible(true);
        this.controller.buttonEdit.setVisible(true);
    }

    public void extractMetadata(Path path) {
        this.databaseView.setDetailsOfFile(this);

        try {
            PrintWriter writer = new PrintWriter(new FileWriter(String.valueOf(path), true));

            try {
                writer.println("Title : " + this.title);
                writer.println("Author : " + this.author);
                writer.println("Date : " + this.date);
                writer.println("Theme : " + this.theme);
                writer.println("Type of document : " + this.typeOfDocument);
                writer.println("Extension : " + this.extension);
                writer.println("");
                writer.println("");
                writer.write(SingletonController.getInstance().templateController.getStringToExtract());
                writer.println("");
                writer.println("");
                writer.println("Key word :\t");
                String[] ary = this.firstMap.get("keyWord").replace("\"", "").replace("[", "").replace("]", "").split(",");

                for(String str : ary) {
                    writer.write("\t" + str);
                }

                writer.println("");
                writer.println("");

                if (!this.firstMap.get("note").equals("")){
                    writer.println("Note :");
                    writer.println(this.firstMap.get("note"));
                    writer.println("");
                    writer.println("");
                }
                if (!this.firstMap.get("quote").equals("")){
                    writer.println("Quote :");
                    writer.println(this.firstMap.get("quote"));
                    writer.println("");
                    writer.println("");
                    writer.println("");
                }

                writer.println("-----------------------------------------------------------------------------------------------------------------------");
                writer.println("");
                writer.println("");
            } catch (Exception e) {
                System.out.println(e.getMessage());
            } finally {
                writer.close();
            }
        } catch (Exception var18) {
            System.out.println(var18.getMessage());
        }

    }

    public FlowPane getFlowPane() {
        return this.flowPane;
    }

    public String getName() {
        return this.name;
    }

    public java.io.File getFile() {
        return this.file;
    }

    public Image getImage() {
        return this.image;
    }

    public void removeInExtractFile() {
        for (Node node : this.controller.showExtractFile.getChildren()){
            if (node.getClass().equals(FlowPane.class) && node == this.flowPane) {
                this.controller.codeFilesExtract.remove(this);
                this.controller.showExtractFile.getChildren().remove(this.flowPane);
                break;
            }
        }
    }

    public void setContainer(String container) {
        this.container = container;
    }

    public String toString() {
        return "{ \"name\":\"" + this.name + '"' + ", \"container\":\"" + this.container + "\"}";
    }

    private static String toString(Serializable o) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(o);
        oos.close();
        return Base64.getEncoder().encodeToString(baos.toByteArray());
    }

    public void resetStyle() {
        this.flowPane.setStyle("-fx-border-width: 1;-fx-padding: 5 5 5 5; -fx-border-color: transparent transparent lightgrey transparent;");
    }
}
