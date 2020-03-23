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
        Line separator = new Line(optionsView.getLayoutX(), optionsView.getLayoutY(),
                optionsView.getLayoutX() + UIConstants.CMD_INPUT_WIDTH, optionsView.getLayoutY());
        separator.setStrokeWidth(0.25);
        vbox.getChildren().add(separator);
        vbox.getChildren().add(cliView);
        // Add right view to HBox
        getChildren().add(vbox);
    }

}
