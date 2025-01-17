package code.controller;

import code.Main;
import java.io.IOException;

import code.singleton.SingletonFileSelected;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ControllerMenu {
    public boolean addFileIsShowing = false;
    public boolean showFileIsShowing = false;
//    public boolean aboutIsShowing = false;
    public static Stage stageShow;
//    public static Stage stageAbout;
    public static Stage stageAdd;

    public ControllerMenu() {
    }

    public void onUserWantToAddFile(ActionEvent event) {
        try {
            SingletonFileSelected.getInstance().file = null;
            if (!this.addFileIsShowing) {
                this.addFileIsShowing = true;
                Parent root = FXMLLoader.load(ClassLoader.getSystemResource("xml/view_add.fxml"));
                stageAdd = new Stage();
                stageAdd.setResizable(false);
                stageAdd.setTitle("Bibliography");
                stageAdd.setScene(new Scene(root));
                stageAdd.setWidth(945.0D);
                stageAdd.setOnCloseRequest((e) -> {
                    stageAdd.close();
                    this.addFileIsShowing = false;
                });
                stageAdd.show();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void onUserWantToShowFile(ActionEvent event) {
        try {
            if (!this.showFileIsShowing) {
                this.showFileIsShowing = true;
                Parent root = FXMLLoader.load(ClassLoader.getSystemResource("xml/view.fxml"));
                stageShow = new Stage();
                stageShow.setTitle("Bibliography");
                stageShow.setScene(new Scene(root));
                stageShow.setResizable(false);
                stageShow.setWidth(945.0D);
                stageShow.setOnCloseRequest((e) -> {
                    stageShow.close();
                    this.showFileIsShowing = false;
                });
                stageShow.show();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void onUserWantToShowAbout(ActionEvent event) {
        try {
            String[] cmdArray = {"cmd", "/c", "start", "about/About.pdf"}; // just change folder
            java.lang.Runtime.getRuntime().exec(cmdArray);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void onUserWantToShowSettings(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(ClassLoader.getSystemResource("xml/settings.fxml"));
            Platform.runLater(() -> {
                Main.stage.setScene(new Scene(root));
                Main.stage.setResizable(false);
                Main.stage.setWidth(600.0D);
                Main.stage.setHeight(430.0D);
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
