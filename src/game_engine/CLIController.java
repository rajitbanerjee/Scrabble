package game_engine;

import javafx.scene.input.KeyCode;
import ui.CommandHistoryView;
import ui.CommandInputView;

public class CLIController {
    private CommandInputView inputView;
    private CommandHistoryView historyView;

    public CLIController(CommandInputView inputView, CommandHistoryView historyView) {
        this.inputView = inputView;
        this.historyView = historyView;
        setListeners();
    }

    public void setListeners() {
        inputView.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER) {
                historyView.addRow(inputView.getText());
                inputView.clear();
            }
        });
    }
}
