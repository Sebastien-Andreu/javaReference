package code.controller.template.add;

import code.singleton.SingletonController;
import code.singleton.SingletonDatabase;
import code.singleton.SingletonFileSelected;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class ControllerBook implements TemplateController {
    @FXML
    public TextField editor;
    @FXML
    public TextField serie;
    @FXML
    public TextField ISBN;
    @FXML
    public TextField edition;
    @FXML
    public TextField office;
    @FXML
    public TextField publisher;
    @FXML
    public TextField reference;
    @FXML
    public TextArea comment;

    public ControllerBook() {
    }

    @FXML
    public void initialize() {
        SingletonController.getInstance().templateController = this;
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
                            case "reference":
                                map.put("reference", objectTypeOfDocument.get("reference").toString());
                                break;
                            case "editor":
                                map.put("editor", objectTypeOfDocument.get("editor").toString());
                                break;
                            case "ISBN":
                                map.put("ISBN", objectTypeOfDocument.get("ISBN").toString());
                                break;
                            case "serie":
                                map.put("serie", objectTypeOfDocument.get("serie").toString());
                                break;
                            case "edition":
                                map.put("edition", objectTypeOfDocument.get("edition").toString());
                                break;
                            case "publisher":
                                map.put("publisher", objectTypeOfDocument.get("publisher").toString());
                                break;
                            case "comment":
                                map.put("comment", objectTypeOfDocument.get("comment").toString());
                                break;
                            case "office":
                                map.put("office", objectTypeOfDocument.get("office").toString());
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
        if (!this.serie.getText().isEmpty()){
            json.put("serie", this.serie.getText());
        }
        if (!this.ISBN.getText().isEmpty()){
            json.put("ISBN", this.ISBN.getText());
        }
        if (!this.edition.getText().isEmpty()){
            json.put("edition", this.edition.getText());
        }
        if (!this.office.getText().isEmpty()){
            json.put("office", this.office.getText());
        }
        if (!this.publisher.getText().isEmpty()){
            json.put("publisher", this.publisher.getText());
        }
        if (!this.reference.getText().isEmpty()){
            json.put("reference", this.reference.getText());
        }
        if (!this.comment.getText().isEmpty()){
            json.put("comment", this.comment.getText());
        }
        return json;
    }

    @Override
    public AnchorPane loadView() {
        try {
            return (AnchorPane)FXMLLoader.load(ClassLoader.getSystemResource("xml/template/view/view_textbook.fxml"));
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
                case "reference":
                    result[0].add("\nReference : " + (String)map.get("reference"));
                    break;
                case "editor":
                    result[0].add("Editor : " + (String)map.get("editor"));
                    break;
                case "ISBN":
                    result[0].add("\nISBN : " + (String)map.get("ISBN"));
                    break;
                case "serie":
                    result[0].add("\nSerie : " + (String)map.get("serie"));
                    break;
                case "edition":
                    result[0].add("\nEdition : " + (String)map.get("edition"));
                    break;
                case "publisher":
                    result[0].add("\nPublisher : " + (String)map.get("publisher"));
                    break;
                case "office":
                    result[0].add("\nOffice : " + (String)map.get("office"));
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

    public void showToEdit() {
        SingletonFileSelected.getInstance().file.additionalMap.keySet().forEach(e -> {
            switch (e.toString()){
                case "reference":
                    this.reference.setText(SingletonFileSelected.getInstance().file.additionalMap.get("reference").toString());
                    break;
                case "editor":
                    this.editor.setText(SingletonFileSelected.getInstance().file.additionalMap.get("editor").toString());
                    break;
                case "ISBN":
                    this.ISBN.setText(SingletonFileSelected.getInstance().file.additionalMap.get("ISBN").toString());
                    break;
                case "serie":
                    this.serie.setText(SingletonFileSelected.getInstance().file.additionalMap.get("serie").toString());
                    break;
                case "edition":
                    this.edition.setText(SingletonFileSelected.getInstance().file.additionalMap.get("edition").toString());
                    break;
                case "publisher":
                    this.publisher.setText(SingletonFileSelected.getInstance().file.additionalMap.get("publisher").toString());
                    break;
                case "comment":
                    this.comment.setText(SingletonFileSelected.getInstance().file.additionalMap.get("comment").toString());
                    break;
                case "office":
                    this.office.setText(SingletonFileSelected.getInstance().file.additionalMap.get("office").toString());
                    break;
            }
        });
    }
}
