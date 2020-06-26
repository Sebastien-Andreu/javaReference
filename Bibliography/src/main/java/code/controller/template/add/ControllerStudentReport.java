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

public class ControllerStudentReport implements TemplateController {

    @FXML
    public TextField supervisor, project;
    @FXML
    public TextArea comment;

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
                            case "supervisor":
                                map.put("supervisor", objectTypeOfDocument.get("supervisor").toString());
                                break;
                            case "project":
                                map.put("project", objectTypeOfDocument.get("project").toString());
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
        if (!this.supervisor.getText().isEmpty()){
            json.put("supervisor", this.supervisor.getText());
        }
        if (!this.project.getText().isEmpty()){
            json.put("project", this.project.getText());
        }
        if (!this.comment.getText().isEmpty()){
            json.put("comment", this.comment.getText());
        }
        return json;
    }

    @Override
    public AnchorPane loadView() {
        try {
            return (AnchorPane) FXMLLoader.load(ClassLoader.getSystemResource("xml/template/view/view_student_report.fxml"));
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
                case "supervisor":
                    result[0].add("\nSupervisor : " + (String)map.get("supervisor"));
                    break;
                case "project":
                    result[0].add("\nProject : " + (String)map.get("project"));
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
                case "supervisor":
                    this.supervisor.setText(SingletonFileSelected.getInstance().file.additionalMap.get("supervisor").toString());
                    break;
                case "project":
                    this.project.setText(SingletonFileSelected.getInstance().file.additionalMap.get("project").toString());
                    break;
                case "comment":
                    this.comment.setText(SingletonFileSelected.getInstance().file.additionalMap.get("comment").toString());
                    break;
            }
        });
    }
}
