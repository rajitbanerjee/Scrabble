package ui;

import javafx.scene.layout.VBox;

/**
 * Command line panel GUI design.
 *
 * @author Tee Chee Guan, 18202044
 * @author Katarina Cvetkovic, 18347921
 * @author Rajit Banerjee, 18202817
 * @team DarkMode
 */
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

    public void clearInputView() {
        inputView.clear();
    }

}
