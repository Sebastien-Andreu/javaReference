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

public class ControllerViewStudentReport {

    @FXML
    public Label supervisor, project;
    @FXML
    public TextArea comment;

    @FXML
    public void initialize() {

        SingletonController.getInstance().templateController.map.keySet().forEach(e -> {
            switch (e.toString()){
                case "supervisor":
                    this.supervisor.setText(SingletonController.getInstance().templateController.map.get("supervisor"));
                    break;
                case "project":
                    this.project.setText(SingletonController.getInstance().templateController.map.get("project"));
                    break;
                case "comment":
                    this.comment.setText(SingletonController.getInstance().templateController.map.get("comment"));
                    break;
            }
        });
    }
}
