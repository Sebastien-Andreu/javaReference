package code.controller.template.view;

import code.singleton.SingletonController;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

public class ControllerViewInternalDoc {
    @FXML
    public TextArea comment;
    @FXML
    public Label type;

    @FXML
    public void initialize() {

        SingletonController.getInstance().templateController.map.keySet().forEach(e -> {
            switch (e.toString()){
                case "type":
                    this.type.setText(SingletonController.getInstance().templateController.map.get("type"));
                    break;
                case "comment":
                    this.comment.setText(SingletonController.getInstance().templateController.map.get("comment"));
                    break;
            }
        });
    }
}
