package ui;

import constants.UIConstants;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

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
    private VBox vbox = new VBox(); // placed inside the ScrollPane

    public CommandHistoryView() {
        super();
        vbox.setAlignment(Pos.BOTTOM_LEFT);
        vbox.setMinHeight(UIConstants.getSceneHeight() * 0.75 - 20);
        setPrefHeight(UIConstants.getSceneHeight() * 0.75);
        setPadding(new Insets(5));
        setStyle("-fx-background-color:transparent;");
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
        history.add(text);
    }

    public List<String> getHistory() {
        return history;
    }

}
