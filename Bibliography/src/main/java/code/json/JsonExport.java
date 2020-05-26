package code.json;

import code.controller.ControllerAddFile;
import code.database.DatabaseAdd;
import code.singleton.SingletonFileSelected;
import code.utils.AdditionalInput;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Iterator;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class JsonExport {
    public JsonExport() {
    }

    public void export(ControllerAddFile controller) {
        try {
            JSONObject json = new JSONObject();
            JSONObject file = new JSONObject();
            JSONArray themes = new JSONArray();

            for (String str : controller.addTheme){
                themes.add(str.replace("é", "e"));
            }

            JSONArray keyWord = new JSONArray();
            for (String str : controller.addTags){
                keyWord.add(str.replace("é", "e"));
            }

            file.put("Author", controller.inputAuthor.getValue()); // required
            file.put("date", ((LocalDate)controller.inputDate.getValue()).toString()); // required
            file.put("title", controller.inputTitle.getText()); // required
            file.put("availability", this.getAvailability(controller)); // required

            file.put("note", controller.inputNote.getText());
            file.put("quote", controller.inputQuote.getText());

            file.put("theme", themes); // required
            file.put("keyWord", keyWord); // required
            file.put("objectOfTypeOfDocument", (new AdditionalInput()).getObject());
            json.put("name", controller.titleOfImportFileAdd.getText()); // required
            json.put("typeOfDocument", controller.inputTypeOfDocument.getValue().replace("\u00e9", "e")); // required
            json.put("tag", controller.newTitleOfFile); // required
            json.put("ID", DatabaseAdd.getNumberOfFile());
            json.put("container", file);
            JSONParser parser = new JSONParser();

            try {
                JSONObject a = (JSONObject)parser.parse(new FileReader("json.json"));
                JSONArray ja = (JSONArray)a.get("Biblio");
                ja.add(json);
                File f = new File("json.json");
                Files.write(Paths.get(f.getPath()), a.toString().getBytes());
            } catch (ParseException var10) {
                var10.printStackTrace();
            }
        } catch (Exception var11) {
            System.out.println("error exportJson : export");
        }

    }

    public void edit(ControllerAddFile controller) {
        JSONParser parser = new JSONParser();

        try {
            JSONObject a = (JSONObject)parser.parse(new FileReader("json.json"));
            JSONArray ja = (JSONArray)a.get("Biblio");
            Iterator var5 = ja.iterator();

            for (Object o : ja){
                if (((JSONObject)o).get("ID").toString().equals(SingletonFileSelected.getInstance().file.firstMap.get("ID"))) {
                    JSONObject container = (JSONObject)((JSONObject)o).get("container");
                    JSONArray themes = new JSONArray();

                    for (String str : controller.addTheme){
                        themes.add(str.replace("é", "e"));
                    }

                    JSONArray keyWord = new JSONArray();

                    for (String str : controller.addTags){
                        keyWord.add(str.replace("é", "e"));
                    }

                    container.put("Author", controller.inputAuthor.getValue()); // require
                    container.put("date", ((LocalDate)controller.inputDate.getValue()).toString());// require
                    container.put("title", controller.inputTitle.getText());// require
                    container.put("availability", this.getAvailability(controller));// require

                    container.put("note", controller.inputNote.getText());
                    container.put("quote", controller.inputQuote.getText());


                    container.put("theme", themes);// require
                    container.put("keyWord", keyWord);// require
                    container.put("objectOfTypeOfDocument", (new AdditionalInput()).getObject());// require
                    ((JSONObject)o).put("name", SingletonFileSelected.getInstance().file.firstMap.get("name"));// require
                    ((JSONObject)o).put("typeOfDocument", controller.inputTypeOfDocument.getValue().replace("\u00e9", "e")); // required
                    ((JSONObject)o).put("tag", controller.newTitleOfFile);// require
                    File f = new File("json.json");
                    Files.write(Paths.get(f.getPath()), a.toString().getBytes());
                    break;
                }
            }
        } catch (IOException | ParseException var12) {
            var12.printStackTrace();
        }

    }

    private String getAvailability(ControllerAddFile controller) {
        String result = "";
        if (controller.AvailabilityElec.selectedProperty().getValue()) {
            result = result + controller.AvailabilityElec.getText() + ",";
        }

        if (controller.AvailabilityLib.selectedProperty().getValue()) {
            result = result + controller.AvailabilityLib.getText() + ",";
        }

        if (controller.AvailabilityPrint.selectedProperty().getValue()) {
            result = result + controller.AvailabilityPrint.getText() + ",";
        }

        return result.substring(0, result.length() - 1);
    }
}
