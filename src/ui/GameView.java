package ui;

import javafx.geometry.Pos;
import javafx.scene.layout.HBox;

/**
 * Integrate board and CLI views into a single window.
 *
 * @author Tee Chee Guan, 18202044
 * @author Katarina Cvetkovic, 18347921
 * @author Rajit Banerjee, 18202817
 * @team DarkMode
 */
public class GameView extends HBox {
    private CLIView cliView;
    private BoardView boardView;

    public GameView(CLIView cliView, BoardView boardView) {
        super();
        this.cliView = cliView;
        this.boardView = boardView;
        setAlignment(Pos.BOTTOM_CENTER);
        getChildren().add(boardView);
        getChildren().add(cliView);
    }
}
