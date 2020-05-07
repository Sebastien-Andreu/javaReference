package code.controller.template.add;

import code.singleton.SingletonController;
import code.singleton.SingletonDatabase;
import code.singleton.SingletonFileSelected;
import code.utils.AutoIncrement;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ListChangeListener.Change;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
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

public class ControllerCA implements TemplateController {
    @FXML
    public TextField name;
    @FXML
    public TextField org;
    @FXML
    public TextField place;
    @FXML
    public TextField code;
    @FXML
    public TextField ref;
    @FXML
    public ComboBox<String> inputAff;
    @FXML
    public FlowPane showAff;
    @FXML
    public TextArea comment;
    public ObservableList<String> addAffiliation = FXCollections.observableArrayList();

    public ControllerCA() {
    }

    @FXML
    public void initialize() {
        SingletonController.getInstance().templateController = this;
        this.addAffiliation.addListener(this::eventListenerAddAffiliation);
        this.inputAff.getEditor().textProperty().addListener((obs, oldText, newText) -> {
            this.inputAff.setValue(newText);
        });
        this.setComboBox();
    }

    private void setComboBox() {
        this.inputAff.getItems().addAll(SingletonDatabase.getInstance().getAllAuthor());
        AutoIncrement autoIAffiliation = new AutoIncrement(this.inputAff);
        this.inputAff.getEditor().setOnKeyPressed((ke) -> {
            Platform.runLater(autoIAffiliation::show);
        });
    }

    public void onEnterKeyPressAffiliation(KeyEvent e) {
        if (e.getCode() == KeyCode.ENTER && !((String)this.inputAff.getValue()).isEmpty() && !this.addAffiliation.contains(this.inputAff.getValue())) {
            this.addAffiliation.add(this.inputAff.getValue());
            this.inputAff.setValue("");
        }

    }

    private void eventListenerAddAffiliation(Change<? extends String> change) {
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
                    map.put("confName", objectTypeOfDocument.get("confName").toString());
                    map.put("confOrg", objectTypeOfDocument.get("confOrg").toString());
                    map.put("confPlace", objectTypeOfDocument.get("confPlace").toString());
                    map.put("paperCode", objectTypeOfDocument.get("paperCode").toString());
                    map.put("onlineRef", objectTypeOfDocument.get("onlineRef").toString());
                    map.put("comment", objectTypeOfDocument.get("comment").toString());
                    map.put("affiliation", objectTypeOfDocument.get("affiliation").toString());
                    break;
                }
            }

            return map;
        } catch (IOException | ParseException var9) {
            var9.printStackTrace();
            return null;
        }
    }

    public JSONObject getJson() {
        JSONObject json = new JSONObject();
        json.put("confName", this.name.getText());
        json.put("confOrg", this.org.getText());
        json.put("confPlace", this.place.getText());
        json.put("paperCode", this.code.getText());
        json.put("onlineRef", this.ref.getText());
        json.put("comment", this.comment.getText());
        JSONArray jsonArray = new JSONArray();
        jsonArray.addAll(this.addAffiliation);
        json.put("affiliation", jsonArray);
        return json;
    }

    public AnchorPane loadView() {
        try {
            return (AnchorPane)FXMLLoader.load(ClassLoader.getSystemResource("xml/template/view/view_CA.fxml"));
        } catch (IOException var2) {
            var2.printStackTrace();
            return null;
        }
    }

    public String getStringToExtract() {
        StringBuilder extract = new StringBuilder();
        extract.append("Conference name : ").append((String)map.get("confName")).append("\n");
        extract.append("Conference organization : ").append((String)map.get("confOrg")).append("\n");
        extract.append("Conference place : ").append((String)map.get("confPlace")).append("\n");
        extract.append("Paper code : ").append((String)map.get("paperCode")).append("\n");
        extract.append("Online reference : ").append((String)map.get("onlineRef")).append("\n");
        String list = ((String)SingletonFileSelected.getInstance().file.additionalMap.get("affiliation")).replace("\"", "").replace("[", "").replace("]", "");
        String[] ary = list.split(",");
        String[] var4 = ary;
        int var5 = ary.length;

        for(int var6 = 0; var6 < var5; ++var6) {
            String str = var4[var6];
            extract.append(str).append("\n");
        }

        extract.append("\n");
        extract.append("Comment : ").append((String)map.get("comment")).append("\n");
        return extract.toString();
    }

    public void showToEdit() {
        this.name.setText((String)SingletonFileSelected.getInstance().file.additionalMap.get("confName"));
        this.org.setText((String)SingletonFileSelected.getInstance().file.additionalMap.get("confOrg"));
        this.place.setText((String)SingletonFileSelected.getInstance().file.additionalMap.get("confPlace"));
        this.code.setText((String)SingletonFileSelected.getInstance().file.additionalMap.get("paperCode"));
        this.ref.setText((String)SingletonFileSelected.getInstance().file.additionalMap.get("onlineRef"));
        this.comment.setText((String)SingletonFileSelected.getInstance().file.additionalMap.get("comment"));
        String list = ((String)SingletonFileSelected.getInstance().file.additionalMap.get("affiliation")).replace("\"", "").replace("[", "").replace("]", "");
        String[] ary = list.split(",");
        String[] var3 = ary;
        int var4 = ary.length;

        for(int var5 = 0; var5 < var4; ++var5) {
            String str = var3[var5];
            this.addAffiliation.add(str);
        }

    }
}
