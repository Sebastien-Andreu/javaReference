package code.controller.template.view;

import code.controller.template.add.TemplateController;
import code.singleton.SingletonController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ListChangeListener.Change;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.FlowPane;

public class ControllerViewCA {
    @FXML
    public TextArea comment;
    @FXML
    public Label name;
    @FXML
    public Label org;
    @FXML
    public Label code;
    @FXML
    public Label ref;
    @FXML
    public FlowPane showAff;
    @FXML
    public Label place;


    public ObservableList<String> affiliation = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        SingletonController.getInstance().templateController.map.keySet().forEach(e -> {
            switch (e.toString()){
                case "comment":
                    this.comment.setText(SingletonController.getInstance().templateController.map.get("comment"));
                    break;
                case "confName":
                    this.name.setText(SingletonController.getInstance().templateController.map.get("confName"));
                    break;
                case "confOrg":
                    this.org.setText(SingletonController.getInstance().templateController.map.get("confOrg"));
                    break;
                case "paperCode":
                    this.code.setText(SingletonController.getInstance().templateController.map.get("paperCode"));
                    break;
                case "onlineRef":
                    this.ref.setText(SingletonController.getInstance().templateController.map.get("onlineRef"));
                    break;
                case "confPlace":
                    this.place.setText(SingletonController.getInstance().templateController.map.get("confPlace"));
                    break;
                case "affiliation":
                    String list = (SingletonController.getInstance().templateController.map.get("affiliation")).replace("\"", "").replace("[", "").replace("]", "");
                    String[] ary = list.split(",");
                    this.affiliation.addListener(this::eventListenerAffiliation);

                    for(String str: ary) {
                        this.affiliation.add(str);
                    }
                    break;
            }
        });
    }

    private void eventListenerAffiliation(Change<? extends String> change) {
        while(change.next()) {
            if (change.wasAdded()) {
                Button result = new Button((String)this.affiliation.get(this.affiliation.size() - 1));
                result.setPrefHeight(20.0D);
                result.setContentDisplay(ContentDisplay.RIGHT);
                result.setStyle("-fx-padding: 5 5 5 5");
                this.showAff.getChildren().add(result);
            }

            if (change.wasRemoved()) {
                this.showAff.getChildren().subList(change.getFrom(), change.getFrom() + change.getRemovedSize()).clear();
            }
        }
    }
}
