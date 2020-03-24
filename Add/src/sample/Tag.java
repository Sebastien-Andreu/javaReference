package sample;

import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.FlowPane;


class TagNote extends FlowPane{

    public TagNote(String tag) {
        Image image = new Image("sample/delete.png");
        ImageView closeImg = new ImageView(image);
        Label result = new Label(tag,closeImg);
        result.setPrefHeight(20);
        result.setContentDisplay(ContentDisplay.RIGHT);

        result.setOnMouseClicked(event -> {
            if(event.getButton().equals(MouseButton.PRIMARY)) {
                if (event.getClickCount() == 2) {
                    Controller.notes.remove(tag);
                }
            }
        });

        getChildren().add(result);
    }
}

class TagQuote extends FlowPane{

    public TagQuote(String tag) {
        Image image = new Image("sample/delete.png");
        ImageView closeImg = new ImageView(image);
        Label result = new Label(tag,closeImg);
        result.setPrefHeight(20);
        result.setContentDisplay(ContentDisplay.RIGHT);

        result.setOnMouseClicked(event -> {
            if(event.getButton().equals(MouseButton.PRIMARY)) {
                if (event.getClickCount() == 2) {
                    Controller.quotes.remove(tag);
                }
            }
        });

        getChildren().add(result);
    }
}