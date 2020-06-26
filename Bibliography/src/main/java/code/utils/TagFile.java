package code.utils;

import java.time.LocalDate;
import java.util.List;
import javafx.collections.ObservableList;
import javafx.scene.control.DatePicker;

public class TagFile {
    private String name;
    private List<String> theme;
    private DatePicker date;

    public TagFile() {
    }

    public String getTag(ObservableList<String> name, ObservableList<String> theme, DatePicker date) {
        this.name = name.get(0);
        this.theme = theme;
        this.date = date;
        return this.getName() + "_" + this.getTheme() + "_" + this.getDate();
    }

    private String getName() {
        Character letterName = this.name.toUpperCase().toCharArray()[0];
        String[] name = this.name.split(" ");
        if (name.length > 1){
            name[1] = name[1].substring(0, 1).toUpperCase() + name[1].substring(1);
            return letterName + "." + name[1];
        } else {
            return name[0] + ".";
        }
    }

    private String getTheme() {
        String[] theme = ((String)this.theme.get(0)).split(" ");
        StringBuilder tagTheme = new StringBuilder();
        String[] var3 = theme;
        int var4 = theme.length;

        for(int var5 = 0; var5 < var4; ++var5) {
            String str = var3[var5];
            tagTheme.append(str.substring(0, 1).toUpperCase());
        }

        return tagTheme.toString();
    }

    private String getDate() {
        LocalDate date = LocalDate.parse(((LocalDate)this.date.getValue()).toString());
        return date.getMonth().toString().substring(0, 3) + "_" + date.getYear();
    }
}
