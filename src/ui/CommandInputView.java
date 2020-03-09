package ui;

import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;

import java.util.ArrayList;
import java.util.List;

public class CommandInputView extends TextField {
    // Might be changed to a Queue/LinkedList
    private ArrayList<String> history;

    public CommandInputView() {
        super();
        history = new ArrayList<>();
        setPromptText(">");
        // Set listener for Enter key
        setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER) {
                history.add(getText());
                clear();
            }
        });
    }

    public List<String> getHistory() {
        return history;
    }
}
