package ui;

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
    public static final int SCENE_WIDTH = 1240;
    public static final int SCENE_HEIGHT = 620;
    public static final int INPUT_FIELD_WIDTH = SCENE_WIDTH / 2;
    public static final int SEPARATOR_LENGTH = (int) (INPUT_FIELD_WIDTH * 0.175);
    public static final double SQUARE_SIZE = 35;
    public static String stylesheet = "darkTheme.css";

    /**
     * Creates a new game view.
     *
     * @param cliView     the CLI view
     * @param boardView   the board view
     * @param optionsView the options view
     */
    public GameView(CLIView cliView, BoardView boardView, OptionsView optionsView) {
        super();
        // Add board view to the left side
        getChildren().add(boardView);
        // Add options view and CLI view to the right
        VBox vbox = new VBox();
        vbox.getChildren().add(optionsView);
        Line separator = new Line(optionsView.getLayoutX(), optionsView.getLayoutY(),
                optionsView.getLayoutX() + INPUT_FIELD_WIDTH, optionsView.getLayoutY());
        separator.setId("options-view-separator");
        vbox.getChildren().add(separator);
        vbox.getChildren().add(cliView);
        // Add right side view to game view
        getChildren().add(vbox);
    }

    /**
     * Switches the game theme (dark/light).
     */
    public static void switchTheme() {
        if (stylesheet.endsWith("darkTheme.css")) {
            stylesheet = "lightTheme.css";
        } else {
            stylesheet = "darkTheme.css";
        }
    }

}