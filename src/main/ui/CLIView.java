package ui;

import javafx.scene.layout.VBox;

public class CLIView extends VBox {
    private CommandHistoryView historyView;
    private CommandInputView inputView;

    public CLIView() {
        super();
        this.historyView = new CommandHistoryView();
        this.inputView = new CommandInputView();
        getChildren().addAll(historyView, inputView);
    }

    public CommandInputView getInputView() {
        return inputView;
    }

    public CommandHistoryView getHistoryView() {
        return historyView;
    }
}
