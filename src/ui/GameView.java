package ui;

import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * Integrate board and CLI views into a single window.
 *
 * @author Tee Chee Guan, 18202044
 * @author Katarina Cvetkovic, 18347921
 * @author Rajit Banerjee, 18202817
 * @team DarkMode
 */
public class GameView extends HBox {
    public GameView(CLIView cliView, BoardView boardView, OptionsView optionsView) {
        super();
        // Add board view to the left
        getChildren().add(boardView);
        // Create VBox for right side
        VBox vbox = new VBox();
        // Add children
        vbox.getChildren().add(optionsView);
        vbox.getChildren().add(cliView);
        // Add right view to HBox
        getChildren().add(vbox);
    }

}
