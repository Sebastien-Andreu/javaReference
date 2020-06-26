package code.controller.template.add;

import code.database.DatabaseAdd;
import code.singleton.SingletonController;
import code.singleton.SingletonDatabase;
import code.singleton.SingletonFileSelected;
import code.utils.BibTexFormat;
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
import java.time.LocalDate;
import java.util.*;

public class ControllerJournalArticle implements TemplateController{

    @FXML
    public FlowPane showAff;

    @FXML
    public ComboBox<String> inputAff;

    @FXML
    public TextField DOI, journalTitle, journalVol, journalNumber, pub, page, onlineRef;

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
                            case "journalTitle":
                                map.put("journalTitle", objectTypeOfDocument.get("journalTitle").toString());
                                break;
                            case "DOI":
                                map.put("DOI", objectTypeOfDocument.get("DOI").toString());
                                break;
                            case "journalVol":
                                map.put("journalVol", objectTypeOfDocument.get("journalVol").toString());
                                break;
                            case "journalNumber":
                                map.put("journalNumber", objectTypeOfDocument.get("journalNumber").toString());
                                break;
                            case "pub":
                                map.put("pub", objectTypeOfDocument.get("pub").toString());
                                break;
                            case "page":
                                map.put("page", objectTypeOfDocument.get("page").toString());
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
        if (!this.DOI.getText().isEmpty()){
            json.put("DOI", this.DOI.getText());
        }
        if (!this.journalTitle.getText().isEmpty()){
            json.put("journalTitle", this.journalTitle.getText());
        }
        if (!this.journalVol.getText().isEmpty()){
            json.put("journalVol", this.journalVol.getText());
        }
        if (!this.journalNumber.getText().isEmpty()){
            json.put("journalNumber", this.journalNumber.getText());
        }
        if (!this.pub.getText().isEmpty()){
            json.put("pub", this.pub.getText());
        }
        if (!this.page.getText().isEmpty()){
            json.put("page", this.page.getText());
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
        return json;
    }

    @Override
    public AnchorPane loadView() {
        try {
            return (AnchorPane) FXMLLoader.load(ClassLoader.getSystemResource("xml/template/view/view_journal_article.fxml"));
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
                case "DOI":
                    result[0].add("\nDOI : " + (String)map.get("DOI"));
                    break;
                case "journalTitle":
                    result[0].add("\nJournal title : " + (String)map.get("journalTitle"));
                    break;
                case "journalVol":
                    result[0].add("\nJournal volume : " + (String)map.get("journalVol"));
                    break;
                case "journalNumber":
                    result[0].add("\nJournal number : " + (String)map.get("journalNumber"));
                    break;
                case "pub":
                    result[0].add("\nPublisher : " + (String)map.get("pub"));
                    break;
                case "page":
                    result[0].add("\nPage : " + (String)map.get("page"));
                    break;
                case "onlineRef":
                    result[0].add("\nOnline reference : " + (String)map.get("onlineRef"));
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
                    result[0].add("\n Comment : " + (String)map.get("comment"));
                    break;
            }
        });
        return result[0].toString().replace("[", "").replace("]", "");
    }

    @Override
    public String getStringToFormatBibTex(){
        final List[] result = new List[]{new ArrayList<>()};

        result[0].add("@Article{" + SingletonFileSelected.getInstance().file.firstMap.get("tag"));

        result[0].add("\n AUTHOR = {" + BibTexFormat.getAuthor() + "}");

        result[0].add("\n TITLE = {" + SingletonFileSelected.getInstance().file.firstMap.get("title")+ "}");

        if (map.containsKey("journalTitle")){
            result[0].add("\n JOURNAL = {" + (String)map.get("journalTitle") + "}");
        } else {
            result[0].add("\n JOURNAL = { JOURNAL MISSING !!!!!!}");
        }

        if (map.containsKey("journalVol")){
            result[0].add("\n VOLUME  = {" + (String)map.get("journalVol") + "}");
        } else {
            result[0].add("\n VOLUME = { JOURNAL VOLUME MISSING !!!!!!}");

        }

        LocalDate date = LocalDate.parse(SingletonFileSelected.getInstance().file.firstMap.get("date"));

        result[0].add("\n YEAR = {" + date.getYear() + "}");

        if (map.containsKey("journalNumber")){
            result[0].add("\n NUMBER  = {" + (String)map.get("journalNumber") + "}");
        } else {
            result[0].add("\n NUMBER = { NUMBER MISSING !!!!!!}");
        }

        String str =  result[0].toString().replace("[", "").replace("]", "");
        return str + "\n}";
    }

    @Override
    public void showToEdit() {
        SingletonFileSelected.getInstance().file.additionalMap.keySet().forEach(e -> {
            switch (e.toString()){
                case "DOI":
                    this.DOI.setText((String)SingletonFileSelected.getInstance().file.additionalMap.get("DOI"));
                    break;
                case "journalTitle":
                    this.journalTitle.setText((String)SingletonFileSelected.getInstance().file.additionalMap.get("journalTitle"));
                    break;
                case "journalVol":
                    this.journalVol.setText((String)SingletonFileSelected.getInstance().file.additionalMap.get("journalVol"));
                    break;
                case "journalNumber":
                    this.journalNumber.setText((String)SingletonFileSelected.getInstance().file.additionalMap.get("journalNumber"));
                    break;
                case "pub":
                    this.pub.setText((String)SingletonFileSelected.getInstance().file.additionalMap.get("pub"));
                    break;
                case "page":
                    this.page.setText((String)SingletonFileSelected.getInstance().file.additionalMap.get("page"));
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
