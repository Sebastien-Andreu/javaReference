package code.controller.template.add;

import code.singleton.SingletonController;
import code.singleton.SingletonDatabase;
import code.singleton.SingletonFileSelected;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
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

public class ControllerLectureNote implements TemplateController{

    @FXML
    public TextField title, univ, onlineRef, year;
    @FXML
    public TextArea comment;

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
                    objectTypeOfDocument.keySet().forEach(e -> {
                        switch (e.toString()){
                            case "title":
                                map.put("title", objectTypeOfDocument.get("title").toString());
                                break;
                            case "univ":
                                map.put("univ", objectTypeOfDocument.get("univ").toString());
                                break;
                            case "onlineRef":
                                map.put("onlineRef", objectTypeOfDocument.get("onlineRef").toString());
                                break;
                            case "year":
                                map.put("year", objectTypeOfDocument.get("year").toString());
                                break;
                            case "comment":
                                map.put("comment", objectTypeOfDocument.get("comment").toString());
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

    public JSONObject getJson() {
        JSONObject json = new JSONObject();
        if (!this.title.getText().isEmpty()){
            json.put("title", this.title.getText());
        }
        if (!this.univ.getText().isEmpty()){
            json.put("univ", this.univ.getText());
        }
        if (!this.onlineRef.getText().isEmpty()){
            json.put("onlineRef", this.onlineRef.getText());
        }
        if (!this.year.getText().isEmpty()){
            json.put("year", this.year.getText());
        }
        if (!this.comment.getText().isEmpty()){
            json.put("comment", this.comment.getText());
        }
        return json;
    }

    public AnchorPane loadView() {
        try {
            return (AnchorPane) FXMLLoader.load(ClassLoader.getSystemResource("xml/template/view/view_lecture_note.fxml"));
        } catch (IOException var2) {
            var2.printStackTrace();
            return null;
        }
    }

    public String getStringToExtract() {
        final List[] result = new List[]{new ArrayList<>()};
        map.keySet().forEach(e ->{
            switch (e.toString()){
                case "title":
                    result[0].add("\nCourse title : " + (String)map.get("title"));
                    break;
                case "univ":
                    result[0].add("University : " + (String)map.get("univ"));
                    break;
                case "onlineRef":
                    result[0].add("\nOnline reference : " + (String)map.get("onlineRef"));
                    break;
                case "year":
                    result[0].add("\nYear : " + (String)map.get("year"));
                    break;
                case "comment":
                    result[0].add("\nComment : \n" + (String)map.get("comment") + "\n");
                    break;

            }
        });
        return result[0].toString().replace(",","").replace("[", "").replace("]", "");
    }

    public void showToEdit() {
        SingletonFileSelected.getInstance().file.additionalMap.keySet().forEach(e -> {
            switch (e.toString()){
                case "title":
                    this.title.setText(SingletonFileSelected.getInstance().file.additionalMap.get("title").toString());
                    break;
                case "univ":
                    this.univ.setText(SingletonFileSelected.getInstance().file.additionalMap.get("univ").toString());
                    break;
                case "onlineRef":
                    this.onlineRef.setText(SingletonFileSelected.getInstance().file.additionalMap.get("onlineRef").toString());
                    break;
                case "year":
                    this.year.setText(SingletonFileSelected.getInstance().file.additionalMap.get("year").toString());
                    break;
                case "comment":
                    this.comment.setText(SingletonFileSelected.getInstance().file.additionalMap.get("comment").toString());
                    break;
            }
        });
    }
}
