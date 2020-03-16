package ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

/**
 * Command history GUI design.
 *
 * @author Tee Chee Guan, 18202044
 * @author Katarina Cvetkovic, 18347921
 * @author Rajit Banerjee, 18202817
 * @team DarkMode
 */
public class CommandHistoryView extends VBox {
    // Might be changed to a Queue/LinkedList
    private ArrayList<String> history;

    public CommandHistoryView() {
        super();
        history = new ArrayList<>();
        setMinHeight(700);
        setAlignment(Pos.BOTTOM_LEFT);
        setPadding(new Insets(10));
    }

    public void printText(String text) {
        getChildren().add(new Label(text));
        history.add(text);
    }

    public List<String> getHistory() {
        return history;
    }

}
