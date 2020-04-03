package ui;

import javafx.scene.layout.VBox;

/**
 * Command line panel GUI design.
 *
 * @author Tee Chee Guan, 18202044
 * @author Rajit Banerjee, 18202817
 * @author Katarina Cvetkovic, 18347921
 * Team 15: DarkMode
 */
public class CLIView extends VBox {
    public static final CommandHistoryView HISTORY_VIEW = new CommandHistoryView();
    private final CommandInputView inputView = new CommandInputView();

    /**
     * Creates a new CLI view.
     */
    public CLIView() {
        super();
        setId("cli-view");
        getChildren().addAll(HISTORY_VIEW, inputView);
    }

    /**
     * Clears the CommandHistoryView.
     */
    public static void clearHistoryView() {
        HISTORY_VIEW.clear();
    }

    /**
     * Accessor for CommandInputView.
     *
     * @return inputView
     */
    public CommandInputView getInputView() {
        return inputView;
    }

    /**
     * Clears the CommandInputView.
     */
    public void clearInputView() {
        inputView.clear();
    }

}