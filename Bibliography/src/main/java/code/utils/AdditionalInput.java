package code.utils;

import code.controller.template.add.ControllerBook;
import code.controller.template.add.ControllerCA;
import code.singleton.SingletonController;
import org.json.simple.JSONObject;

public class AdditionalInput {

    public String getAdditionalInput(String str) {
        System.out.println(str);
        String result = "";
        switch(str) {
            case "Livre":
                result = "xml/template/add/add_textbook.fxml";
                break;
            case "Article de conf\u00e9rence":
                result = "xml/template/add/add_CA.fxml";
                break;
        }
        return result;
    }

    public JSONObject getObject() {
        return SingletonController.getInstance().templateController.getJson();
    }

    public static void setAdditionalInput(String str) {
        switch(str) {
            case "Livre":
                SingletonController.getInstance().templateController = new ControllerBook();
                break;
            case "Article de conference":
                SingletonController.getInstance().templateController = new ControllerCA();
                break;
        }
    }
}
