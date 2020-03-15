package game_engine;

import game.Board;
import ui.BoardView;

public class BoardController {
    private BoardView view;
    private Board board;

    public BoardController(BoardView view, Board board) {
        this.view = view;
        this.board = board;
    }

    public void update() {
        view.redraw();
    }
}
