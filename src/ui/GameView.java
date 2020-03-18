package ui;

import javafx.geometry.Pos;
import javafx.scene.layout.GridPane;

/**
 * Integrate board and CLI views into a single window.
 *
 * @author Tee Chee Guan, 18202044
 * @author Katarina Cvetkovic, 18347921
 * @author Rajit Banerjee, 18202817
 * @team DarkMode
 */
public class GameView extends GridPane {
    public GameView(CLIView cliView, BoardView boardView, OptionsView optionsView) {
        super();
        setAlignment(Pos.TOP_LEFT);
        add(boardView, 0, 0, 4, 4);
        add(optionsView, 4, 0, 1, 1);
        add(cliView, 4, 1, 3, 1);
    }

}
