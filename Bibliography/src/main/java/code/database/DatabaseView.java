package code.database;

import code.File;

import java.util.Arrays;

public class DatabaseView extends Database {

    public void setDetailsOfFile(File file) {
        this.clear(file);
        file.getData();
        file.title = (String)file.firstMap.get("title");
        file.author = (String)file.firstMap.get("Author");
        file.typeOfDocument = (String)file.firstMap.get("typeOfDocument");
        file.theme = ((String)file.firstMap.get("theme")).replace("\"", "").replace("[", "").replace("]", "");
        file.date = (String)file.firstMap.get("date");
        file.extension = file.name.substring(file.name.lastIndexOf(".") + 1);
        String[] tags = ((String)file.firstMap.get("keyWord")).replace("\"", "").replace("[", "").replace("]", "").split(",");

        file.tags.addAll(Arrays.asList(tags));

    }

    public void clear(File file) {
        file.title = "";
        file.author = "";
        file.typeOfDocument = "";
        file.theme = "";
        file.date = "";
        file.extension = "";
        file.tags.clear();
    }
}
