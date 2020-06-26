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

public class ControllerConferencePresentation implements TemplateController{

    @FXML
    public FlowPane showAff;

    @FXML
    public ComboBox<String> inputAff;

    @FXML
    public TextField name, place, org, paperNumber, onlineRef;

    @FXML
    public TextArea comment;

    public ObservableList<String> addAff = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        SingletonController.getInstance().templateController = this;
        this.addAff.addListener(this::eventListenerAddAffiliation);
        this.inputAff.getEditor().textProperty().addListener((obs, oldText, newText) -> {
            this.inputAff.setValue(newText);
        });
        this.setComboBox();
    }

    private void setComboBox() {
        this.inputAff.getItems().addAll(SingletonDatabase.getInstance().getAllAffiliation());
        AutoIncrement autoAff = new AutoIncrement(this.inputAff);
        this.inputAff.getEditor().setOnKeyPressed((ke) -> {
            Platform.runLater(autoAff::show);
        });
    }

    public void onEnterKeyPressAffiliation(KeyEvent e) {
        if (e.getCode() == KeyCode.ENTER && !((String)this.inputAff.getValue()).isEmpty() && !this.addAff.contains(this.inputAff.getValue())) {
            this.addAff.add(this.inputAff.getValue());
            this.inputAff.setValue("");
        }
    }

    private void eventListenerAddAffiliation(ListChangeListener.Change<? extends String> change) {
        while(change.next()) {
            if (change.wasAdded()) {
                Button btn = this.getButtonTag((String)this.addAff.get(this.addAff.size() - 1));
                btn.setOnMouseClicked((event) -> {
                    if (event.getClickCount() == 2) {
                        this.addAff.remove(btn.getText());
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
                            case "paperNumber":
                                map.put("paperNumber", objectTypeOfDocument.get("paperNumber").toString());
                                break;
                            case "name":
                                map.put("name", objectTypeOfDocument.get("name").toString());
                                break;
                            case "org":
                                map.put("org", objectTypeOfDocument.get("org").toString());
                                break;
                            case "place":
                                map.put("place", objectTypeOfDocument.get("place").toString());
                                break;
                            case "onlineRef":
                                map.put("onlineRef", objectTypeOfDocument.get("onlineRef").toString());
                                break;
                            case "comment":
                                map.put("comment", objectTypeOfDocument.get("comment").toString());
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
        if (!this.paperNumber.getText().isEmpty()){
            json.put("paperNumber", this.paperNumber.getText());
        }
        if (!this.name.getText().isEmpty()){
            json.put("name", this.name.getText());
        }
        if (!this.org.getText().isEmpty()){
            json.put("org", this.org.getText());
        }
        if (!this.place.getText().isEmpty()){
            json.put("place", this.place.getText());
        }
        if (!this.onlineRef.getText().isEmpty()){
            json.put("onlineRef", this.onlineRef.getText());
        }
        if (!this.comment.getText().isEmpty()){
            json.put("comment", this.comment.getText());
        }
        if (!this.addAff.isEmpty()){
            JSONArray jsonArray = new JSONArray();
            jsonArray.addAll(this.addAff);
            json.put("affiliation", jsonArray);

            new DatabaseAdd().saveAffiliation(addAff);
        }
        System.out.println("json : " + json);
        return json;
    }

    @Override
    public AnchorPane loadView() {
        try {
            return (AnchorPane) FXMLLoader.load(ClassLoader.getSystemResource("xml/template/view/view_conference_presentation.fxml"));
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
                case "paperNumber":
                    result[0].add("\nPaper number : " + (String)map.get("paperNumber"));
                    break;
                case "name":
                    result[0].add("\nConference name : " + (String)map.get("name"));
                    break;
                case "org":
                    result[0].add("\nConference organiser : " + (String)map.get("org"));
                    break;
                case "place":
                    result[0].add("\n¨Conference place : " + (String)map.get("place"));
                    break;
                case "onlineRef":
                    result[0].add("\n¨Online reference : " + (String)map.get("onlineRef"));
                    break;
                case "affiliation":
                    String list2 = ((String) SingletonFileSelected.getInstance().file.additionalMap.get("affiliation")).replace("\"", "").replace("[", "").replace("]", "");
                    String[] ary2 = list2.split(",");
                    String[] var42 = ary2;
                    int var52 = ary2.length;
                    result[0].add("Affiliation : " + '\n');
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
    public String getStringToFormatBibTex() {
        return null;
    }

    @Override
    public void showToEdit() {
        SingletonFileSelected.getInstance().file.additionalMap.keySet().forEach(e -> {
            switch (e.toString()){
                case "paperNumber":
                    this.paperNumber.setText((String)SingletonFileSelected.getInstance().file.additionalMap.get("paperNumber"));
                    break;
                case "name":
                    this.name.setText((String)SingletonFileSelected.getInstance().file.additionalMap.get("name"));
                    break;
                case "org":
                    this.org.setText((String)SingletonFileSelected.getInstance().file.additionalMap.get("org"));
                    break;
                case "place":
                    this.place.setText((String)SingletonFileSelected.getInstance().file.additionalMap.get("place"));
                    break;
                case "onlineRef":
                    this.onlineRef.setText((String)SingletonFileSelected.getInstance().file.additionalMap.get("onlineRef"));
                    break;
                case "comment":
                    this.comment.setText((String)SingletonFileSelected.getInstance().file.additionalMap.get("comment"));
                    break;
                case "affiliation":
                    String list2 = ((String)SingletonFileSelected.getInstance().file.additionalMap.get("affiliation")).replace("\"", "").replace("[", "").replace("]", "");
                    String[] ary2 = list2.split(",");
                    for(int var5 = 0; var5 < ary2.length; ++var5) {
                        this.addAff.add(ary2[var5]);
                    }
                    break;
            }
        });
    }
}
