package code.controller;

import code.Main;
import code.singleton.SingletonController;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.DirectoryChooser;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class ControllerSettings {
    @FXML
    public Button buttonChoose;
    @FXML
    public Label currentDirectory;
    private String directory;

    public ControllerSettings() {
    }

    @FXML
    public void initialize() {
        this.currentDirectory.setText(Objects.requireNonNull(getPath()).toString());
        this.directory = this.currentDirectory.getText();
        this.buttonChoose.setOnAction(this::getExtractFile);
    }

    private void getExtractFile(ActionEvent event) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File file = directoryChooser.showDialog(Main.stage);
        if (file != null) {
            File directory = new File(file.getParent() + "\\" + FilenameUtils.removeExtension(file.getName()));
            if (!directory.exists()) {
                directory.mkdir();
            }

            this.setNewPath(directory.toString());
            this.currentDirectory.setText(Objects.requireNonNull(getPath()).toString());
            this.copyFile(directory.toString());
            this.directory = this.currentDirectory.getText();
        }

    }

    public void onUserWantToReturn(ActionEvent event) {
        try {
            SingletonController.getInstance().setFile(null);
            Parent root = FXMLLoader.load(ClassLoader.getSystemResource("xml/menu.fxml"));
            Platform.runLater(() -> {
                Main.stage.setScene(new Scene(root));
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void setNewPath(String str) {
        JSONParser parser = new JSONParser();

        try {
            JSONObject file = (JSONObject)parser.parse(new FileReader("json.json"));
            JSONArray json = (JSONArray)file.get("FileStorage");
            JSONObject j = (JSONObject)json.get(0);
            j.put("pos", str);
            File f = new File("json.json");
            Files.write(Paths.get(f.getPath()), file.toString().getBytes(), new OpenOption[0]);
        } catch (ParseException | IOException e) {
            e.printStackTrace();
        }

    }

    public static Path getPath() {
        JSONParser parser = new JSONParser();

        try {
            JSONObject file = (JSONObject)parser.parse(new FileReader("json.json"));
            JSONArray json = (JSONArray)file.get("FileStorage");
            JSONObject j = (JSONObject)json.get(0);
            return Paths.get(j.get("pos").toString());
        } catch (ParseException | IOException var4) {
            var4.printStackTrace();
            return null;
        }
    }

    private void copyFile(String file) {
        File srcDir = new File(this.directory);
        File destDir = new File(file);

        try {
            FileUtils.copyDirectory(srcDir, destDir);
            FileUtils.deleteDirectory(srcDir);
        } catch (IOException var5) {
            var5.printStackTrace();
        }

    }
}
