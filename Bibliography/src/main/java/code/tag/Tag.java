package code.tag;

import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;

public class Tag extends FlowPane {
    public Tag(String tag) {
        Label result = new Label(tag);
        result.setPrefHeight(20.0D);
        result.setContentDisplay(ContentDisplay.RIGHT);
        this.setStyle("-fx-border-width: 1;-fx-padding: 5 5 5 5; -fx-border-color: transparent transparent lightgrey transparent;");
        result.setMinWidth(this.getMinWidth());
        this.getChildren().add(result);
    }
}
