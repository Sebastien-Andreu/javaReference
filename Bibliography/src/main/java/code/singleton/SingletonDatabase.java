package code.singleton;

import code.database.Database;

public class SingletonDatabase extends Database {
    public static SingletonDatabase instance = new SingletonDatabase();
    public int IDSelected;

    private SingletonDatabase() {
    }

    public static SingletonDatabase getInstance() {
        return instance;
    }
}
