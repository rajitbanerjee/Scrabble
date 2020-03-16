package game_engine;

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

    public BoardController(BoardView view) {
        this.view = view;
    }

    public void update() {
        view.redraw();
    }

}
