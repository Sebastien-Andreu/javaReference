package code.controller.template.view;

import code.singleton.SingletonController;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

public class ControllerViewBook {
    @FXML
    public TextArea showComment;
    @FXML
    public Label showEditor, showSerie, showEdition, showISBN, showPublisher, showPublisherOffice,showOnlineRef;

    @FXML
    public void initialize() {

        SingletonController.getInstance().templateController.map.keySet().forEach(e -> {
            switch (e.toString()){
                case "reference":
                    this.showOnlineRef.setText(SingletonController.getInstance().templateController.map.get("reference"));
                    break;
                case "editor":
                    this.showEditor.setText(SingletonController.getInstance().templateController.map.get("editor"));
                    break;
                case "ISBN":
                    this.showISBN.setText(SingletonController.getInstance().templateController.map.get("ISBN"));
                    break;
                case "serie":
                    this.showSerie.setText(SingletonController.getInstance().templateController.map.get("serie"));
                    break;
                case "edition":
                    this.showEdition.setText(SingletonController.getInstance().templateController.map.get("edition"));
                    break;
                case "publisher":
                    this.showPublisher.setText(SingletonController.getInstance().templateController.map.get("publisher"));
                    break;
                case "comment":
                    this.showComment.setText(SingletonController.getInstance().templateController.map.get("comment"));
                    break;
                case "office":
                    this.showPublisherOffice.setText(SingletonController.getInstance().templateController.map.get("office"));
                    break;
            }
        });
    }
}
