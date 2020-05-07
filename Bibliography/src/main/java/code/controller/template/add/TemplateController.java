package code.controller.template.add;

import java.util.HashMap;
import java.util.Map;
import javafx.scene.layout.AnchorPane;
import org.json.simple.JSONObject;

public interface TemplateController {
    Map<String, String> map = new HashMap();

    Map<String, String> getData();

    JSONObject getJson();

    AnchorPane loadView();

    String getStringToExtract();

    void showToEdit();
}
