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

public class ControllerViewAegisReport {

    @FXML
    public Label track, theme;
    @FXML
    public TextArea comment;

    @FXML
    public void initialize() {

        SingletonController.getInstance().templateController.map.keySet().forEach(e -> {
            switch (e.toString()){
                case "track":
                    this.track.setText(SingletonController.getInstance().templateController.map.get("track"));
                    break;
                case "theme":
                    this.theme.setText(SingletonController.getInstance().templateController.map.get("theme"));
                    break;
                case "comment":
                    this.comment.setText(SingletonController.getInstance().templateController.map.get("comment"));
                    break;
            }
        });
    }
}
