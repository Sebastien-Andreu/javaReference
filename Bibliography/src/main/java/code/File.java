package code;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
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
import singleton.SingletonController;
import singleton.SingletonDatabase;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Base64;

public class File implements Serializable{

    private String name, container;
    private Image image;
    private int id;
    private FlowPane flowPane;

    public File(String name, Image image, int id){
        this.name = name;
        this.image = image;
        this.id = id;

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
        SingletonDatabase.getInstance().showDetailsOfFile(this.name);
    }

    private void showView(){
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

        SingletonController.getInstance().getController().modifyDetails.setOnAction(e -> {
            SingletonController.getInstance().getController().rootView.setVisible(false);
            SingletonController.getInstance().getController().rootAdd.setVisible(true);
            SingletonController.getInstance().getController().buttonAddFile.setVisible(false);
            SingletonController.getInstance().getController().buttonUpdateFile.setVisible(true);
            SingletonController.getInstance().getController().labelAddFile.setVisible(false);

            SingletonController.getInstance().getController().addTags.clear();
            SingletonController.getInstance().getController().addQuotes.clear();
            SingletonController.getInstance().getController().addNotes.clear();

            for (String str : SingletonController.getInstance().getController().tags) {
                SingletonController.getInstance().getController().addTags.add(str);
            }
            SingletonController.getInstance().getController().addQuotes.addAll(SingletonController.getInstance().getController().quotes);
            SingletonController.getInstance().getController().addNotes.addAll(SingletonController.getInstance().getController().notes);
            SingletonController.getInstance().getController().showIcon.setImage(this.image);
            SingletonController.getInstance().getController().titleOfImportFileAdd.setText(this.name);
            SingletonController.getInstance().getController().titleAdd.setText(SingletonController.getInstance().getController().detailsTitle.getText());
            SingletonController.getInstance().getController().authorAdd.setText(SingletonController.getInstance().getController().detailsAuthor.getText());
            SingletonController.getInstance().getController().dateAdd.setValue(LocalDate.parse(SingletonController.getInstance().getController().detailsDate.getText()));
            SingletonController.getInstance().getController().themeAdd.setText(SingletonController.getInstance().getController().detailsTheme.getText());
            SingletonController.getInstance().getController().typeOfDocumentAdd.setText(SingletonController.getInstance().getController().detailsTypeOfDocument.getText());
            Platform.runLater( () -> SingletonController.getInstance().getController().rootAdd.requestFocus() );
        });
    }

    public FlowPane getFlowPane(){
        return this.flowPane;
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
        ObjectOutputStream oos = new ObjectOutputStream( baos );
        oos.writeObject( o );
        oos.close();
        return Base64.getEncoder().encodeToString(baos.toByteArray());
    }
}
