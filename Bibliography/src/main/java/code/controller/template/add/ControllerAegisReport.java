package code.controller.template.add;

import code.singleton.SingletonController;
import code.singleton.SingletonDatabase;
import code.singleton.SingletonFileSelected;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
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

public class ControllerAegisReport implements TemplateController {

    @FXML
    public TextField track, theme;
    @FXML
    public TextArea comment;

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
                            case "track":
                                map.put("track", objectTypeOfDocument.get("track").toString());
                                break;
                            case "theme":
                                map.put("theme", objectTypeOfDocument.get("theme").toString());
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

    @Override
    public JSONObject getJson() {
        JSONObject json = new JSONObject();
        if (!this.track.getText().isEmpty()){
            json.put("track", this.track.getText());
        }
        if (!this.theme.getText().isEmpty()){
            json.put("theme", this.theme.getText());
        }
        if (!this.comment.getText().isEmpty()){
            json.put("comment", this.comment.getText());
        }
        return json;
    }

    @Override
    public AnchorPane loadView() {
        try {
            return (AnchorPane) FXMLLoader.load(ClassLoader.getSystemResource("xml/template/view/view_aegis_report.fxml"));
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
                case "track":
                    result[0].add("\nTrack : " + (String)map.get("track"));
                    break;
                case "comment":
                    result[0].add("\nComment : \n" + (String)map.get("comment") + "\n");
                    break;
                case "theme":
                    result[0].add("\nRoadmap theme : \n" + (String)map.get("theme") + "\n");
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
                case "track":
                    this.track.setText(SingletonFileSelected.getInstance().file.additionalMap.get("track").toString());
                    break;
                case "theme":
                    this.theme.setText(SingletonFileSelected.getInstance().file.additionalMap.get("theme").toString());
                    break;
                case "comment":
                    this.comment.setText(SingletonFileSelected.getInstance().file.additionalMap.get("comment").toString());
                    break;
            }
        });
    }
}
