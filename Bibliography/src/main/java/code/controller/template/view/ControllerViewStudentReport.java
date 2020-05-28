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
    public Label title, univ, project, year;
    @FXML
    public TextArea comment;
    @FXML
    public FlowPane showAuthor;

    public ObservableList<String> addAuthor = FXCollections.observableArrayList();

    @FXML
    public void initialize() {

        SingletonController.getInstance().templateController.map.keySet().forEach(e -> {
            switch (e.toString()){
                case "title":
                    this.title.setText(SingletonController.getInstance().templateController.map.get("title"));
                    break;
                case "univ":
                    this.univ.setText(SingletonController.getInstance().templateController.map.get("univ"));
                    break;
                case "project":
                    this.project.setText(SingletonController.getInstance().templateController.map.get("project"));
                    break;
                case "year":
                    this.year.setText(SingletonController.getInstance().templateController.map.get("year"));
                    break;
                case "comment":
                    this.comment.setText(SingletonController.getInstance().templateController.map.get("comment"));
                    break;
                case "author":
                    String list2 = (SingletonController.getInstance().templateController.map.get("author")).replace("\"", "").replace("[", "").replace("]", "");
                    String[] ary2 = list2.split(",");
                    this.addAuthor.addListener(this::eventListenerAuthor);

                    for(String str: ary2) {
                        this.addAuthor.add(str);
                    }
                    break;
            }
        });
    }

    private void eventListenerAuthor(ListChangeListener.Change<? extends String> change) {
        while(change.next()) {
            if (change.wasAdded()) {
                Button result = new Button((String)this.addAuthor.get(this.addAuthor.size() - 1));
                result.setPrefHeight(20.0D);
                result.setContentDisplay(ContentDisplay.RIGHT);
                result.setStyle("-fx-padding: 5 5 5 5");
                this.showAuthor.getChildren().add(result);
            }

            if (change.wasRemoved()) {
                this.showAuthor.getChildren().subList(change.getFrom(), change.getFrom() + change.getRemovedSize()).clear();
            }
        }
    }
}
