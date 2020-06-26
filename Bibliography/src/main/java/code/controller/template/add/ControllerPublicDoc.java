package code.controller.template.add;

import code.singleton.SingletonController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ControllerPublicDoc implements TemplateController {

    @FXML
    public void initialize() {
        SingletonController.getInstance().templateController = this;
    }

    @Override
    public Map<String, String> getData() {
        return new HashMap<>();
    }

    @Override
    public JSONObject getJson() {
        return null;
    }

    @Override
    public AnchorPane loadView() {
        try {
            return (AnchorPane) FXMLLoader.load(ClassLoader.getSystemResource("xml/template/view/view_public_doc.fxml"));
        } catch (IOException var2) {
            var2.printStackTrace();
            return null;
        }
    }

    @Override
    public String getStringToExtract() {
        return null;
    }

    @Override
    public String getStringToFormatBibTex() {
        return null;
    }

    @Override
    public void showToEdit() {

    }
}
