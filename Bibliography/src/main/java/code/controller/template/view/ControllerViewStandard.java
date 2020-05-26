package code.controller.template.view;

import code.singleton.SingletonController;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

public class ControllerViewStandard {
    @FXML
    public TextArea comment;
    @FXML
    public Label title, onlineRef;

    @FXML
    public void initialize() {

        SingletonController.getInstance().templateController.map.keySet().forEach(e -> {
            switch (e.toString()){
                case "title":
                    this.title.setText(SingletonController.getInstance().templateController.map.get("title"));
                    break;
                case "onlineRef":
                    this.onlineRef.setText(SingletonController.getInstance().templateController.map.get("onlineRef"));
                    break;
                case "comment":
                    this.comment.setText(SingletonController.getInstance().templateController.map.get("comment"));
                    break;
            }
        });
    }
}
