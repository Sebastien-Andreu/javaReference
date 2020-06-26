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

public class ControllerViewJournalArticle {
    @FXML
    public TextArea comment;
    @FXML
    public Label title, DOI, journalTitle, journalVol, journalNumber, pub, page, onlineRef;

    @FXML
    public FlowPane showAff;

    public ObservableList<String> addAffiliation = FXCollections.observableArrayList();

    @FXML
    public void initialize() {

        SingletonController.getInstance().templateController.map.keySet().forEach(e -> {
            switch (e.toString()){
                case "title":
                    this.title.setText(SingletonController.getInstance().templateController.map.get("title"));
                    break;
                case "DOI":
                    this.DOI.setText(SingletonController.getInstance().templateController.map.get("DOI"));
                    break;
                case "journalTitle":
                    this.journalTitle.setText(SingletonController.getInstance().templateController.map.get("journalTitle"));
                    break;
                case "journalVol":
                    this.journalVol.setText(SingletonController.getInstance().templateController.map.get("journalVol"));
                    break;
                case "journalNumber":
                    this.journalNumber.setText(SingletonController.getInstance().templateController.map.get("journalNumber"));
                    break;
                case "pub":
                    this.pub.setText(SingletonController.getInstance().templateController.map.get("pub"));
                    break;
                case "page":
                    this.page.setText(SingletonController.getInstance().templateController.map.get("page"));
                    break;
                case "onlineRef":
                    this.onlineRef.setText(SingletonController.getInstance().templateController.map.get("onlineRef"));
                    break;
                case "comment":
                    this.comment.setText(SingletonController.getInstance().templateController.map.get("comment"));
                    break;
                case "affiliation":
                    String list = (SingletonController.getInstance().templateController.map.get("affiliation")).replace("\"", "").replace("[", "").replace("]", "");
                    String[] ary = list.split(",");
                    this.addAffiliation.addListener(this::eventListenerAffiliation);

                    for(String str: ary) {
                        this.addAffiliation.add(str);
                    }
                    break;
            }
        });
    }

    private void eventListenerAffiliation(ListChangeListener.Change<? extends String> change) {
        while(change.next()) {
            if (change.wasAdded()) {
                Button result = new Button((String)this.addAffiliation.get(this.addAffiliation.size() - 1));
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
