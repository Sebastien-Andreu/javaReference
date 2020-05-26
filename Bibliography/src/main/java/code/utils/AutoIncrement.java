package code.utils;

import com.jfoenix.controls.JFXAutoCompletePopup;
import javafx.scene.control.ComboBox;

public class AutoIncrement {
    private ComboBox<String> myNode;
    private JFXAutoCompletePopup<String> myAutoCompletePopup = new JFXAutoCompletePopup();

    public AutoIncrement(ComboBox<String> node) {
        this.myNode = node;
    }

    public void show() {
        this.myAutoCompletePopup.getSuggestions().clear();
        this.myAutoCompletePopup.getSuggestions().addAll(this.myNode.getItems());
        if (!this.myAutoCompletePopup.getFilteredSuggestions().isEmpty()) {
            this.myAutoCompletePopup.hide();
            this.myAutoCompletePopup.setPrefWidth(344.0D);
            this.myAutoCompletePopup.setSelectionHandler((e) -> {
                this.myNode.setValue(e.getObject());
                this.myNode.getEditor().positionCaret(((String)this.myNode.getValue()).length());
            });

            try {
                this.myAutoCompletePopup.filter((string) -> {
                    return string.toLowerCase().contains(((String)this.myNode.getValue()).toLowerCase());
                });
                if (!this.myAutoCompletePopup.getFilteredSuggestions().isEmpty() && !((String)this.myNode.getValue()).isEmpty()) {
                    this.myAutoCompletePopup.show(this.myNode);
                    if (((String)this.myAutoCompletePopup.getFilteredSuggestions().get(0)).toLowerCase().equals(((String)this.myNode.getValue()).toLowerCase())) {
                        this.myNode.setValue(this.myAutoCompletePopup.getFilteredSuggestions().get(0));
                        this.myNode.getEditor().positionCaret(((String)this.myNode.getValue()).length());
                    }
                } else {
                    this.myAutoCompletePopup.hide();
                }
            } catch (Exception var2) {
                System.out.println(var2.getMessage());
            }
        }

    }
}
