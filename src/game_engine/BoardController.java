package game_engine;

import game.Board;
import javafx.scene.layout.GridPane;

public class BoardController {
    private GridPane grid;
    private Board board;

    public BoardController(GridPane grid, Board board) {
        this.grid = grid;
        this.board = board;
    }

    public void update() {
        //TODO: update board when changed
    }
}
