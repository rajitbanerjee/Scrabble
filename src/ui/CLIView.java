package ui;

import javafx.scene.layout.VBox;

/**
 * Command line panel GUI design.
 * TODO javadoc comments
 *
 * @author Tee Chee Guan, 18202044
 * @author Rajit Banerjee, 18202817
 * @author Katarina Cvetkovic, 18347921
 * @team DarkMode
 */
public class CLIView extends VBox {
    public static CommandHistoryView historyView = new CommandHistoryView();
    private CommandInputView inputView = new CommandInputView();

    public CLIView() {
        super();
        setId("cli-view");
        getChildren().addAll(historyView, inputView);
    }

    public CommandInputView getInputView() {
        return inputView;
    }

    public void clearInputView() {
        inputView.clear();
    }

    public CommandHistoryView getHistoryView() {
        return historyView;
    }

}