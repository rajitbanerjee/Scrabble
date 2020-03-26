package ui;

import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

import java.util.ArrayList;

/**
 * Command history GUI design.
 *
 * @author Tee Chee Guan, 18202044
 * @author Rajit Banerjee, 18202817
 * @author Katarina Cvetkovic, 18347921
 * Team 15: DarkMode
 */
public class CommandHistoryView extends ScrollPane {
    private ArrayList<String> history = new ArrayList<>();
    private VBox vbox = new VBox(); // Placed inside the ScrollPane

    public CommandHistoryView() {
        super();
        setId("history-view");
        vbox.setId("history-view-text");
        setHbarPolicy(ScrollBarPolicy.AS_NEEDED);
        setVbarPolicy(ScrollBarPolicy.ALWAYS);
        // Allows user to pan using mouse
        setPannable(true);
        // Automatically scrolls the scrollbar to the bottom
        vvalueProperty().bind(vbox.heightProperty());
        setContent(vbox);
    }

    /**
     * Prints text to the VBox.
     *
     * @param text to be printed.
     */
    public void printText(String text) {
        Label label = new Label(text);
        vbox.getChildren().add(label);
    }


    /**
     * Accesses the command entered n commands ago.
     *
     * @param n position of command relative to current command.
     * @return returns the requested command.
     */
    public String getNLastCommand(int n) {
        return history.get(history.size() - n);
    }

    /**
     * Adds a command to the history.
     *
     * @param command to be added.
     */
    public void addCommand(String command) {
        history.add(command);
    }

    /**
     * @return the size of history.
     */
    public int getHistorySize() {
        return history.size();
    }

}