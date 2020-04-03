package ui;

import constants.UIConstants;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;

/**
 * Integrate Board, CLI and Options into a single window.
 *
 * @author Tee Chee Guan, 18202044
 * @author Katarina Cvetkovic, 18347921
 * @author Rajit Banerjee, 18202817
 * Team 15: DarkMode
 */
public class GameView extends HBox {
    /**
     * Creates a new game view.
     *
     * @param cliView     the CLI view
     * @param boardView   the board view
     * @param optionsView the options view
     */
    public GameView(CLIView cliView, BoardView boardView, OptionsView optionsView) {
        super();
        // Add board view to the left
        getChildren().add(boardView);
        // Create VBox for right side
        VBox vbox = new VBox();
        // Add children
        vbox.getChildren().add(optionsView);
        Line separator = new Line(optionsView.getLayoutX(), optionsView.getLayoutY(),
                optionsView.getLayoutX() + UIConstants.CMD_INPUT_WIDTH, optionsView.getLayoutY());
        separator.setId("options-view-separator");
        vbox.getChildren().add(separator);
        vbox.getChildren().add(cliView);
        // Add right view to HBox
        getChildren().add(vbox);
    }

}