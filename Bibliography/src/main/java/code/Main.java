package code;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Paths;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Main extends Application {
    public static Stage stage;

    public Main() {
    }

    public void start(Stage primaryStage) {
        try {
            Parent root = FXMLLoader.load(ClassLoader.getSystemResource("xml/menu.fxml"));
            stage = primaryStage;
            stage.setTitle("Bibliography");
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.setWidth(600.0D);
            stage.setHeight(430.0D);
            this.setup();
            stage.setOnCloseRequest((e) -> {
                stage.close();
                System.exit(0);
            });
            stage.show();
        } catch (Exception var3) {
            System.out.println(var3.getMessage());
        }

    }

    private void setup() {
        File file = new File("json.json");
        File folder = new File("Bibliographie");
        if (!file.exists()) {
            try {
                file.createNewFile();
                FileWriter myWriter = new FileWriter(file);
                myWriter.write("{}");
                myWriter.close();
                folder.mkdir();
                JSONParser parser = new JSONParser();

                try {
                    JSONObject fileJson = (JSONObject)parser.parse(new FileReader("json.json"));
                    JSONArray array1 = new JSONArray();
                    JSONArray array2 = new JSONArray();
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("pos", folder.getAbsolutePath());
                    array2.add(jsonObject);
                    fileJson.put("Biblio", array1);
                    fileJson.put("FileStorage", array2);
                    Files.write(Paths.get(file.getPath()), fileJson.toString().getBytes(), new OpenOption[0]);
                } catch (ParseException | IOException var9) {
                    var9.printStackTrace();
                }
            } catch (IOException var10) {
                var10.printStackTrace();
            }
        }

    }

    public static void main(String[] args) {
        launch(args);
    }
}
