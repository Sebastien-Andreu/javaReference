package code.controller.template.view;

import code.singleton.SingletonController;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.FlowPane;

public class ControllerViewBookChapter {
    @FXML
    public TextArea comment;
    @FXML
    public Label title, editor, isbn, edition, publisher, publisherOffice, series;

    @FXML
    public void initialize() {

        SingletonController.getInstance().templateController.map.keySet().forEach(e -> {
            switch (e.toString()){
                case "title":
                    this.title.setText(SingletonController.getInstance().templateController.map.get("title"));
                    break;
                case "editor":
                    this.editor.setText(SingletonController.getInstance().templateController.map.get("editor"));
                    break;
                case "isbn":
                    this.isbn.setText(SingletonController.getInstance().templateController.map.get("isbn"));
                    break;
                case "edition":
                    this.edition.setText(SingletonController.getInstance().templateController.map.get("edition"));
                    break;
                case "publisher":
                    this.publisher.setText(SingletonController.getInstance().templateController.map.get("publisher"));
                    break;
                case "publisherOffice":
                    this.publisherOffice.setText(SingletonController.getInstance().templateController.map.get("publisherOffice"));
                    break;
                case "series":
                    this.series.setText(SingletonController.getInstance().templateController.map.get("series"));
                    break;
                case "comment":
                    this.comment.setText(SingletonController.getInstance().templateController.map.get("comment"));
                    break;
            }
        });
    }
}
