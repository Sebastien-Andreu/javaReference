package singleton;

import code.Controller;

public class SingletonController {
    public static SingletonController instance = new SingletonController();
    private Controller controller;

    private SingletonController(){}

    public static SingletonController getInstance(){
        return instance;
    }

    public void setController(Controller controller){
        this.controller = controller;
    }

    public Controller getController(){
        return this.controller;
    }
}
