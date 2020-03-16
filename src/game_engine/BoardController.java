package game_engine;

import game.Board;
import ui.BoardView;

/**
 *  Controller for the GUI board display.
 *
 * @author Tee Chee Guan, 18202044
 * @author Rajit Banerjee, 18202817
 * @author Katarina Cvetkovic, 18347921
 * @team DarkMode
 */
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
