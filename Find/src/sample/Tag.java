package sample;

import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;


public class Tag {
    public FlowPane Tag(String tag, Image image) {
        FlowPane pane = new FlowPane();
        pane.setOrientation(Orientation.HORIZONTAL);
        ImageView img = new ImageView();
        img.setFitHeight(25);
        img.setFitWidth(25);
        img.setImage(image);
        Label label = new Label(tag);
        label.setAlignment(Pos.CENTER);

        pane.setOnMouseEntered(e -> {
            pane.setCursor(Cursor.HAND);
        });

        pane.setStyle("-fx-border-width: 1;-fx-padding: 5 5 5 5; -fx-border-color: transparent transparent lightgrey transparent;");

        pane.getChildren().addAll(img, label);
        return pane;
    }
}
