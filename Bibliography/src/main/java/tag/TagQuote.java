package tag;

import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.FlowPane;
import singleton.SingletonController;

public class TagQuote extends FlowPane {

    public TagQuote(String tag) {
        Image image = new Image(String.valueOf(ClassLoader.getSystemResource("image/delete.png")));
        ImageView closeImg = new ImageView(image);
        Label result = new Label(tag,closeImg);
        result.setPrefHeight(20);
        result.setContentDisplay(ContentDisplay.RIGHT);

        setStyle("-fx-border-width: 1;-fx-padding: 5 5 5 5; -fx-border-color: transparent transparent lightgrey transparent;");
        result.setMinWidth(SingletonController.getInstance().getController().showAddQuotes.getPrefWidth());

        result.setOnMouseClicked(event -> {
            if(event.getButton().equals(MouseButton.PRIMARY)) {
                if (event.getClickCount() == 2) {
                    SingletonController.getInstance().getController().addQuotes.remove(tag);
                }
            }
        });

        getChildren().add(result);
    }
}