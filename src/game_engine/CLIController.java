package game_engine;

import ui.CommandHistoryView;
import ui.CommandInputView;

public class CLIController {
    private CommandInputView inputView;
    private CommandHistoryView historyView;

    public CLIController(CommandInputView inputView, CommandHistoryView historyView) {
        this.inputView = inputView;
        this.historyView = historyView;
    }
}
