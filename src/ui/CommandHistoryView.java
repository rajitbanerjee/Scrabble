package ui;

import constants.UIConstants;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

/**
 * Command history GUI design.
 * TODO comments
 * @author Tee Chee Guan, 18202044
 * @author Rajit Banerjee, 18202817
 * @author Katarina Cvetkovic, 18347921
 * @team DarkMode
 */
public class CommandHistoryView extends VBox {
    // Might be changed to a Queue/LinkedList
    private ArrayList<String> history;

    public CommandHistoryView() {
        super();
        history = new ArrayList<>();
        setMinHeight(UIConstants.getSceneHeight() * 0.70);
        setMaxHeight(UIConstants.getSceneHeight() * 0.70);
        setAlignment(Pos.BOTTOM_LEFT);
        setPadding(new Insets(10));
    }

    public void printText(String text) {
        Label label = new Label(text);
        label.setFont(UIConstants.cliFont);
        getChildren().add(label);
        history.add(text);
    }

    public List<String> getHistory() {
        return history;
    }

}
