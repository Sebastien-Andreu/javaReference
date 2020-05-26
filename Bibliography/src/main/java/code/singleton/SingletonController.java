package code.singleton;

import code.File;
import code.controller.ControllerView;
import code.controller.template.add.TemplateController;

public class SingletonController {
    public static SingletonController instance = new SingletonController();
    public ControllerView controllerView;
    public TemplateController templateController;
    private File file;

    private SingletonController() {
    }

    public static SingletonController getInstance() {
        return instance;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public File getFile() {
        return this.file;
    }
}
