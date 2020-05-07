package code.controller.template.view;

import code.controller.template.add.TemplateController;
import code.singleton.SingletonController;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

public class ControllerViewBook {
    @FXML
    public TextArea showComment;
    @FXML
    public Label showEditor;
    @FXML
    public Label showSerie;
    @FXML
    public Label showEdition;
    @FXML
    public Label showISBN;
    @FXML
    public Label showPublisher;
    @FXML
    public Label showPublisherOffice;
    @FXML
    public Label showOnlineRef;

    @FXML
    public void initialize() {
        this.showComment.setText(SingletonController.getInstance().templateController.map.get("comment"));
        this.showEdition.setText(SingletonController.getInstance().templateController.map.get("edition"));
        this.showEditor.setText(SingletonController.getInstance().templateController.map.get("editor"));
        this.showISBN.setText(SingletonController.getInstance().templateController.map.get("ISBN"));
        this.showOnlineRef.setText(SingletonController.getInstance().templateController.map.get("reference"));
        this.showPublisher.setText(SingletonController.getInstance().templateController.map.get("publisher"));
        this.showPublisherOffice.setText(SingletonController.getInstance().templateController.map.get("office"));
        this.showSerie.setText(SingletonController.getInstance().templateController.map.get("serie"));
    }
}
