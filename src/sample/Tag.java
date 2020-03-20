package sample;

import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;

public class Tag extends FlowPane {
    public Button getTag(String tag) {
        Image image = new Image("sample/delete.png");
        ImageView closeImg = new ImageView(image);
        Button result = new Button(tag,closeImg);
        setStyle("-fx-padding: 0 2 0 0;");
        result.setPrefHeight(20);
        result.setContentDisplay(ContentDisplay.RIGHT);

        result.setOnMouseClicked(event ->  Controller.tags.remove(tag));
        return result;
    }
}
