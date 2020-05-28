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
            case "PhD Thesis":
                result = "xml/template/add/add_phd_thesis.fxml";
                break;
            case "MSc Thesis":
                result = "xml/template/add/add_msc_thesis.fxml";
                break;
            case "Lecture note":
                result = "xml/template/add/add_lecture_note.fxml";
                break;
            case "Student report":
                result = "xml/template/add/add_student_report.fxml";
                break;
            case "AEGIS report":
                result = "xml/template/add/add_aegis_report.fxml";
                break;
            case "Newspaper":
                result = "xml/template/add/add_newspaper.fxml";
                break;
            case "Online article":
                result = "xml/template/add/add_online_article.fxml";
                break;
            case "Press release":
                result = "xml/template/add/add_press_release.fxml";
                break;
            case "Software manual":
                result = "xml/template/add/add_software_manual.fxml";
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
            case "PhD Thesis":
                SingletonController.getInstance().templateController = new ControllerPhdThesis();
                break;
            case "MSc Thesis":
                SingletonController.getInstance().templateController = new ControllerMscThesis();
                break;
            case "Lecture note":
                SingletonController.getInstance().templateController = new ControllerLectureNote();
                break;
            case "Student report":
                SingletonController.getInstance().templateController = new ControllerStudentReport();
                break;
            case "AEGIS report":
                SingletonController.getInstance().templateController = new ControllerAegisReport();
                break;
            case "Newspaper":
                SingletonController.getInstance().templateController = new ControllerNewspaper();
                break;
            case "Online article":
                SingletonController.getInstance().templateController = new ControllerOnlineArticle();
                break;
            case "Press release":
                SingletonController.getInstance().templateController = new ControllerPressRelease();
                break;
            case "Software manual":
                SingletonController.getInstance().templateController = new ControllerSoftwareManual();
                break;
        }
    }
}
