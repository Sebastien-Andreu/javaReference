package code.controller.template.add;

import code.singleton.SingletonController;
import code.singleton.SingletonDatabase;
import code.singleton.SingletonFileSelected;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
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
                    map.put("reference", objectTypeOfDocument.get("reference").toString());
                    map.put("editor", objectTypeOfDocument.get("editor").toString());
                    map.put("ISBN", objectTypeOfDocument.get("ISBN").toString());
                    map.put("serie", objectTypeOfDocument.get("serie").toString());
                    map.put("edition", objectTypeOfDocument.get("edition").toString());
                    map.put("publisher", objectTypeOfDocument.get("publisher").toString());
                    map.put("comment", objectTypeOfDocument.get("comment").toString());
                    map.put("office", objectTypeOfDocument.get("office").toString());
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
        json.put("editor", this.editor.getText());
        json.put("serie", this.serie.getText());
        json.put("ISBN", this.ISBN.getText());
        json.put("edition", this.edition.getText());
        json.put("office", this.office.getText());
        json.put("publisher", this.publisher.getText());
        json.put("reference", this.reference.getText());
        json.put("comment", this.comment.getText());
        return json;
    }

    public AnchorPane loadView() {
        try {
            return (AnchorPane)FXMLLoader.load(ClassLoader.getSystemResource("xml/template/view/view_textbook.fxml"));
        } catch (IOException var2) {
            var2.printStackTrace();
            return null;
        }
    }

    public String getStringToExtract() {
        return "Editor : " + (String)map.get("editor") + "\nSerie : " + (String)map.get("serie") + "\nISBN : " + (String)map.get("ISBN") + "\nEdition : " + (String)map.get("edition") + "\nOffice : " + (String)map.get("office") + "\nPublisher : " + (String)map.get("publisher") + "\nReference : " + (String)map.get("reference") + "\nComment : \n" + (String)map.get("comment") + "\n";
    }

    public void showToEdit() {
        this.editor.setText((String)SingletonFileSelected.getInstance().file.additionalMap.get("editor"));
        this.serie.setText((String)SingletonFileSelected.getInstance().file.additionalMap.get("serie"));
        this.ISBN.setText((String)SingletonFileSelected.getInstance().file.additionalMap.get("ISBN"));
        this.edition.setText((String)SingletonFileSelected.getInstance().file.additionalMap.get("edition"));
        this.office.setText((String)SingletonFileSelected.getInstance().file.additionalMap.get("office"));
        this.publisher.setText((String)SingletonFileSelected.getInstance().file.additionalMap.get("publisher"));
        this.reference.setText((String)SingletonFileSelected.getInstance().file.additionalMap.get("reference"));
        this.comment.setText((String)SingletonFileSelected.getInstance().file.additionalMap.get("comment"));
    }
}
