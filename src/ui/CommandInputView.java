package ui;

import javafx.scene.control.TextField;

public class CommandInputView extends TextField {
    public CommandInputView() {
        super();
        setPromptText(">");
        setMinWidth(500);
    }
}
