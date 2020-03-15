package game_engine;

import constants.Constants;
import game.Board;
import game.Square;
import javafx.scene.layout.GridPane;
import ui.BoardView;

public class BoardController {
    private GridPane grid;
    private Board board;

    public BoardController(GridPane grid, Board board) {
        this.grid = grid;
        this.board = board;
    }

    public void update() {
        Square[][] boardArray = board.getBoard();
        grid.getChildren().clear();
        for (int i = 0; i < Constants.BOARD_SIZE; i++) {
            for (int j = 0; j < Constants.BOARD_SIZE; j++) {
                grid.add(BoardView.getSquareUI(boardArray[i][j]), j, i);
            }
        }
        grid.setHgap(5);
        grid.setVgap(5);
    }
}
