package code.utils;

import code.controller.template.add.*;
import code.singleton.SingletonController;
import org.json.simple.JSONObject;

public class AdditionalInput {

    public String getAdditionalInput(String str) {
        String result = "";
        switch(str) {
            case "Livre":
                result = "xml/template/add/add_textbook.fxml";
                break;
            case "Article de conf\u00e9rence":
                result = "xml/template/add/add_CA.fxml";
                break;
            case "Book chapter":
                result = "xml/template/add/add_book_chapter.fxml";
                break;
            case "Patent":
                result = "xml/template/add/add_patent.fxml";
                break;
            case "Standard":
                result = "xml/template/add/add_standard.fxml";
                break;
            case "Technical report":
                result = "xml/template/add/add_technical_report.fxml";
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
            case "Book chapter":
                SingletonController.getInstance().templateController = new ControllerBookChapter();
                break;
            case "Patent":
                SingletonController.getInstance().templateController = new ControllerPatent();
                break;
            case "Standard":
                SingletonController.getInstance().templateController = new ControllerStandard();
                break;
            case "Technical report":
                SingletonController.getInstance().templateController = new ControllerTechnicalReport();
                break;
        }
    }
}
