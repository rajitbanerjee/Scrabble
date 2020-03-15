package ui;

import javafx.geometry.Pos;
import javafx.scene.layout.HBox;

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
