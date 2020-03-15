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
        // Initialise top-left empty block
        grid.add(BoardView.getEmptyUI(), 0, 0);
        // Draws the first row (Column Names)
        for (int k = 0; k < Constants.BOARD_SIZE; k++) {
            grid.add(BoardView.getColumnUI((char) ('A' + k)), k + 1, 0);
        }
        // Draws the first column (Row Numbers)
        for (int k = 0; k < Constants.BOARD_SIZE; k++) {
            grid.add(BoardView.getRowUI(k + 1), 0, k + 1);
        }
        // Draws the rest of the board
        for (int i = 0; i < Constants.BOARD_SIZE; i++) {
            for (int j = 0; j < Constants.BOARD_SIZE; j++) {
                grid.add(BoardView.getSquareUI(boardArray[i][j]), i + 1, j + 1);
            }
        }
        grid.setHgap(5);
        grid.setVgap(5);
    }
}
