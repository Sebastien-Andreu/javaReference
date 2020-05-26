package code.singleton;

import code.File;
import javafx.scene.image.Image;

public class SingletonFileSelected {
    public static SingletonFileSelected instance = new SingletonFileSelected();
    public File file;
    public Image image;
    public String title;

    private SingletonFileSelected() {
    }

    public static SingletonFileSelected getInstance() {
        return instance;
    }

    public void reset() {
        this.file = null;
    }
}
