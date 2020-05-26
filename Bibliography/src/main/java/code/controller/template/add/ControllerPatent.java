package code.controller.template.add;

import code.database.DatabaseAdd;
import code.singleton.SingletonController;
import code.singleton.SingletonDatabase;
import code.singleton.SingletonFileSelected;
import code.utils.AutoIncrement;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ControllerPatent implements TemplateController {

    @FXML
    public FlowPane showAff, showAuthor;

    @FXML
    public ComboBox<String> inputAff, inputAuthor;

    @FXML
    public TextField number, onlineRef, title;

    @FXML
    public TextArea comment;

    public ObservableList<String> addAuthor = FXCollections.observableArrayList();
    public ObservableList<String> addAffiliation = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        SingletonController.getInstance().templateController = this;
        this.addAuthor.addListener(this::eventListenerAddAuthor);
        this.inputAuthor.getEditor().textProperty().addListener((obs, oldText, newText) -> {
            this.inputAuthor.setValue(newText);
        });
        this.addAffiliation.addListener(this::eventListenerAddAffiliation);
        this.inputAff.getEditor().textProperty().addListener((obs, oldText, newText) -> {
            this.inputAff.setValue(newText);
        });
        this.setComboBox();
    }

    private void setComboBox() {
        this.inputAuthor.getItems().addAll(SingletonDatabase.getInstance().getAllAuthor());
        AutoIncrement autoAuthor = new AutoIncrement(this.inputAuthor);
        this.inputAuthor.getEditor().setOnKeyPressed((ke) -> {
            Platform.runLater(autoAuthor::show);
        });

        this.inputAff.getItems().addAll(SingletonDatabase.getInstance().getAllAuthor());
        AutoIncrement autoAff = new AutoIncrement(this.inputAff);
        this.inputAff.getEditor().setOnKeyPressed((ke) -> {
            Platform.runLater(autoAff::show);
        });
    }

    public void onEnterKeyPressAuthor(KeyEvent e) {
        if (e.getCode() == KeyCode.ENTER && !((String)this.inputAuthor.getValue()).isEmpty() && !this.addAuthor.contains(this.inputAuthor.getValue())) {
            this.addAuthor.add(this.inputAuthor.getValue());
            this.inputAuthor.setValue("");
        }
    }

    public void onEnterKeyPressAffiliation(KeyEvent e) {
        if (e.getCode() == KeyCode.ENTER && !((String)this.inputAff.getValue()).isEmpty() && !this.addAffiliation.contains(this.inputAff.getValue())) {
            this.addAffiliation.add(this.inputAff.getValue());
            this.inputAff.setValue("");
        }
    }

    private void eventListenerAddAuthor(ListChangeListener.Change<? extends String> change) {
        while(change.next()) {
            if (change.wasAdded()) {
                Button btn = this.getButtonTag((String)this.addAuthor.get(this.addAuthor.size() - 1));
                btn.setOnMouseClicked((event) -> {
                    if (event.getClickCount() == 2) {
                        this.addAuthor.remove(btn.getText());
                    }

                });
                this.showAuthor.getChildren().add(btn);
            }

            if (change.wasRemoved()) {
                this.showAuthor.getChildren().subList(change.getFrom(), change.getFrom() + change.getRemovedSize()).clear();
            }
        }
    }

    private void eventListenerAddAffiliation(ListChangeListener.Change<? extends String> change) {
        while(change.next()) {
            if (change.wasAdded()) {
                Button btn = this.getButtonTag((String)this.addAffiliation.get(this.addAffiliation.size() - 1));
                btn.setOnMouseClicked((event) -> {
                    if (event.getClickCount() == 2) {
                        this.addAffiliation.remove(btn.getText());
                    }

                });
                this.showAff.getChildren().add(btn);
            }

            if (change.wasRemoved()) {
                this.showAff.getChildren().subList(change.getFrom(), change.getFrom() + change.getRemovedSize()).clear();
            }
        }
    }

    private Button getButtonTag(String tag) {
        Image image = new Image(String.valueOf(ClassLoader.getSystemResource("image/delete.png")));
        ImageView closeImg = new ImageView(image);
        Button result = new Button(tag, closeImg);
        result.setPrefHeight(20.0D);
        result.setContentDisplay(ContentDisplay.RIGHT);
        return result;
    }


    @Override
    public Map<String, String> getData() {
        JSONParser parser = new JSONParser();
        map.clear();

        try {
            JSONObject file = (JSONObject)parser.parse(new FileReader("json.json"));
            JSONArray json = (JSONArray)file.get("Biblio");
            Iterator var4 = json.iterator();

            while(var4.hasNext()) {
                Object jsonObject = var4.next();
                JSONObject j = (JSONObject)jsonObject;
                if (Integer.parseInt(j.get("ID").toString()) == SingletonDatabase.getInstance().IDSelected) {
                    JSONObject object = (JSONObject)j.get("container");
                    JSONObject objectTypeOfDocument = (JSONObject)object.get("objectOfTypeOfDocument");
                    objectTypeOfDocument.keySet().forEach(e -> {
                        switch (e.toString()){
                            case "title":
                                map.put("title", objectTypeOfDocument.get("title").toString());
                                break;
                            case "number":
                                map.put("number", objectTypeOfDocument.get("number").toString());
                                break;
                            case "onlineRef":
                                map.put("onlineRef", objectTypeOfDocument.get("onlineRef").toString());
                                break;
                            case "comment":
                                map.put("comment", objectTypeOfDocument.get("comment").toString());
                                break;
                            case "author":
                                map.put("author", objectTypeOfDocument.get("author").toString());
                                break;
                            case "affiliation":
                                map.put("affiliation", objectTypeOfDocument.get("affiliation").toString());
                                break;
                        }
                    });
                    break;
                }
            }

            return map;
        } catch (IOException | ParseException var9) {
            var9.printStackTrace();
            return null;
        }
    }

    @Override
    public JSONObject getJson() {
        JSONObject json = new JSONObject();
        if (!this.title.getText().isEmpty()){
            json.put("title", this.title.getText());
        }
        if (!this.number.getText().isEmpty()){
            json.put("number", this.number.getText());
        }
        if (!this.onlineRef.getText().isEmpty()){
            json.put("onlineRef", this.onlineRef.getText());
        }
        if (!this.comment.getText().isEmpty()){
            json.put("comment", this.comment.getText());
        }
        if (!this.addAuthor.isEmpty()){
            JSONArray jsonArray = new JSONArray();
            jsonArray.addAll(this.addAuthor);
            json.put("author", jsonArray);

            new DatabaseAdd().saveAffiliation(addAuthor);
        }
        if (!this.addAffiliation.isEmpty()){
            JSONArray jsonArray = new JSONArray();
            jsonArray.addAll(this.addAffiliation);
            json.put("affiliation", jsonArray);

            new DatabaseAdd().saveAffiliation(addAffiliation);
        }
        return json;
    }

    @Override
    public AnchorPane loadView() {
        try {
            return (AnchorPane) FXMLLoader.load(ClassLoader.getSystemResource("xml/template/view/view_patent.fxml"));
        } catch (IOException var2) {
            var2.printStackTrace();
            return null;
        }
    }

    @Override
    public String getStringToExtract() {
        final List[] result = new List[]{new ArrayList<>()};
        map.keySet().forEach(e ->{
            switch (e.toString()){
                case "title":
                    result[0].add("\nTitle : " + (String)map.get("title"));
                    break;
                case "number":
                    result[0].add("\nPatent number : " + (String)map.get("number"));
                    break;
                case "onlineRef":
                    result[0].add("\nOnline reference : " + (String)map.get("onlineRef"));
                    break;
                case "author":
                    String list = ((String) SingletonFileSelected.getInstance().file.additionalMap.get("author")).replace("\"", "").replace("[", "").replace("]", "");
                    String[] ary = list.split(",");
                    String[] var4 = ary;
                    int var5 = ary.length;
                    for(int var6 = 0; var6 < var5; ++var6) {
                        String str = var4[var6];
                        result[0].add(str + "\n");
                    }
                    break;
                case "affiliation":
                    String list2 = ((String) SingletonFileSelected.getInstance().file.additionalMap.get("affiliation")).replace("\"", "").replace("[", "").replace("]", "");
                    String[] ary2 = list2.split(",");
                    String[] var42 = ary2;
                    int var52 = ary2.length;
                    for(int var6 = 0; var6 < var52; ++var6) {
                        String str = var42[var6];
                        result[0].add(str + "\n");
                    }
                    break;
                case "comment":
                    result[0].add("\nComment : \n" + (String)map.get("comment") + "\n");
                    break;

            }
        });
        return result[0].toString().replace(",","").replace("[", "").replace("]", "");
    }

    @Override
    public void showToEdit() {
        SingletonFileSelected.getInstance().file.additionalMap.keySet().forEach(e -> {
            switch (e.toString()){
                case "title":
                    this.title.setText((String)SingletonFileSelected.getInstance().file.additionalMap.get("title"));
                    break;
                case "number":
                    this.number.setText((String)SingletonFileSelected.getInstance().file.additionalMap.get("number"));
                    break;
                case "onlineRef":
                    this.onlineRef.setText((String)SingletonFileSelected.getInstance().file.additionalMap.get("onlineRef"));
                    break;
                case "comment":
                    this.comment.setText((String)SingletonFileSelected.getInstance().file.additionalMap.get("comment"));
                    break;
                case "author":
                    String list = ((String)SingletonFileSelected.getInstance().file.additionalMap.get("author")).replace("\"", "").replace("[", "").replace("]", "");
                    String[] ary = list.split(",");
                    for(int var5 = 0; var5 < ary.length; ++var5) {
                        this.addAuthor.add(ary[var5]);
                    }
                    break;
                case "affiliation":
                    String list2 = ((String)SingletonFileSelected.getInstance().file.additionalMap.get("affiliation")).replace("\"", "").replace("[", "").replace("]", "");
                    String[] ary2 = list2.split(",");
                    for(int var5 = 0; var5 < ary2.length; ++var5) {
                        this.addAffiliation.add(ary2[var5]);
                    }
                    break;
            }
        });
    }
}