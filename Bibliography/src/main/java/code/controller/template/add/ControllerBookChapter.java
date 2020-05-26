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

public class ControllerBookChapter implements TemplateController{

    @FXML
    public TextField editor;
    @FXML
    public TextField series;
    @FXML
    public TextField isbn;
    @FXML
    public TextField edition;
    @FXML
    public TextField publisherOffice;
    @FXML
    public TextField publisher;
    @FXML
    public TextField title;
    @FXML
    public TextArea comment;
    @FXML
    public ComboBox<String> inputAuthor;
    @FXML
    public FlowPane showAuthor;

    public ObservableList<String> addAuthor = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        SingletonController.getInstance().templateController = this;
        this.addAuthor.addListener(this::eventListenerAddAuthor);
        this.inputAuthor.getEditor().textProperty().addListener((obs, oldText, newText) -> {
            this.inputAuthor.setValue(newText);
        });
        this.setComboBox();
    }

    private void setComboBox() {
        this.inputAuthor.getItems().addAll(SingletonDatabase.getInstance().getAllAuthor());
        AutoIncrement autoAuthor = new AutoIncrement(this.inputAuthor);
        this.inputAuthor.getEditor().setOnKeyPressed((ke) -> {
            Platform.runLater(autoAuthor::show);
        });
    }

    public void onEnterKeyPressAuthor(KeyEvent e) {
        if (e.getCode() == KeyCode.ENTER && !((String)this.inputAuthor.getValue()).isEmpty() && !this.addAuthor.contains(this.inputAuthor.getValue())) {
            this.addAuthor.add(this.inputAuthor.getValue());
            this.inputAuthor.setValue("");
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
                            case "series":
                                map.put("series", objectTypeOfDocument.get("series").toString());
                                break;
                            case "isbn":
                                map.put("isbn", objectTypeOfDocument.get("isbn").toString());
                                break;
                            case "edition":
                                map.put("edition", objectTypeOfDocument.get("edition").toString());
                                break;
                            case "publisherOffice":
                                map.put("publisherOffice", objectTypeOfDocument.get("publisherOffice").toString());
                                break;
                            case "publisher":
                                map.put("publisher", objectTypeOfDocument.get("publisher").toString());
                                break;
                            case "editor":
                                map.put("editor", objectTypeOfDocument.get("editor").toString());
                                break;
                            case "comment":
                                map.put("comment", objectTypeOfDocument.get("comment").toString());
                                break;
                            case "author":
                                map.put("author", objectTypeOfDocument.get("author").toString());
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
        if (!this.editor.getText().isEmpty()){
            json.put("editor", this.editor.getText());
        }
        if (!this.series.getText().isEmpty()){
            json.put("series", this.series.getText());
        }
        if (!this.isbn.getText().isEmpty()){
            json.put("isbn", this.isbn.getText());
        }
        if (!this.edition.getText().isEmpty()){
            json.put("edition", this.edition.getText());
        }
        if (!this.publisherOffice.getText().isEmpty()){
            json.put("publisherOffice", this.publisherOffice.getText());
        }
        if (!this.publisher.getText().isEmpty()){
            json.put("publisher", this.publisher.getText());
        }
        if (!this.title.getText().isEmpty()){
            json.put("title", this.title.getText());
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
        return json;
    }

    @Override
    public AnchorPane loadView() {
        try {
            return (AnchorPane) FXMLLoader.load(ClassLoader.getSystemResource("xml/template/view/view_book_chapter.fxml"));
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
                case "editor":
                    result[0].add("\nEditor : " + (String)map.get("editor"));
                    break;
                case "isbn":
                    result[0].add("\nISBN : " + (String)map.get("isbn"));
                    break;
                case "edition":
                    result[0].add("\nEdition : " + (String)map.get("edition"));
                    break;
                case "publisherOffice":
                    result[0].add("\n¨Publisher office : " + (String)map.get("publisherOffice"));
                    break;
                case "publisher":
                    result[0].add("\n¨Publisher : " + (String)map.get("publisher"));
                    break;
                case "series":
                    result[0].add("\n¨Series : " + (String)map.get("series"));
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
                case "isbn":
                    this.isbn.setText((String)SingletonFileSelected.getInstance().file.additionalMap.get("isbn"));
                    break;
                case "edition":
                    this.edition.setText((String)SingletonFileSelected.getInstance().file.additionalMap.get("edition"));
                    break;
                case "editor":
                    this.editor.setText((String)SingletonFileSelected.getInstance().file.additionalMap.get("editor"));
                    break;
                case "publisherOffice":
                    this.publisherOffice.setText((String)SingletonFileSelected.getInstance().file.additionalMap.get("publisherOffice"));
                    break;
                case "publisher":
                    this.publisher.setText((String)SingletonFileSelected.getInstance().file.additionalMap.get("publisher"));
                    break;
                case "series":
                    this.series.setText((String)SingletonFileSelected.getInstance().file.additionalMap.get("series"));
                    break;
                case "comment":
                    this.comment.setText((String)SingletonFileSelected.getInstance().file.additionalMap.get("comment"));
                    break;
                case "author":
                    String list = ((String)SingletonFileSelected.getInstance().file.additionalMap.get("author")).replace("\"", "").replace("[", "").replace("]", "");
                    String[] ary = list.split(",");
                    String[] var3 = ary;
                    int var4 = ary.length;

                    for(int var5 = 0; var5 < var4; ++var5) {
                        String str = var3[var5];
                        this.addAuthor.add(str);
                    }
                    break;
            }
        });
    }
}
