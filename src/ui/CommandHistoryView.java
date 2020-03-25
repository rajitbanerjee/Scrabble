package ui;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

import java.util.ArrayList;

/**
 * Command history GUI design.
 * TODO javadoc comments
 *
 * @author Tee Chee Guan, 18202044
 * @author Rajit Banerjee, 18202817
 * @author Katarina Cvetkovic, 18347921
 * @team DarkMode
 */
public class CommandHistoryView extends ScrollPane {
    // Might be changed to a Queue/LinkedList
    private ArrayList<String> history = new ArrayList<>();
    private VBox vbox = new VBox(); // Placed inside the ScrollPane

    public CommandHistoryView() {
        super();
        vbox.setId("history-view-box");
        vbox.setAlignment(Pos.BOTTOM_LEFT);
        setId("history-view");
        setHbarPolicy(ScrollBarPolicy.AS_NEEDED);
        setVbarPolicy(ScrollBarPolicy.ALWAYS);

        // Allows user to pan using mouse
        setPannable(true);
        // Automatically scrolls the scrollbar to the bottom
        vvalueProperty().bind(vbox.heightProperty());
        setContent(vbox);
    }

    public void printText(String text) {
        Label label = new Label(text);
        vbox.getChildren().add(label);
    }


    public String getNLastCommand(int n) {
        return history.get(history.size() - n);
    }

    public void addCommand(String command) {
        history.add(command);
    }

    public int getHistorySize() {
        return history.size();
    }

}