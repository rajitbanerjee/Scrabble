package game_engine;

import game.Frame;
import ui.FrameView;

/**
 * Controller for the GUI frame.
 *
 * @author Katarina Cvetkovic, 18347921
 * @author Tee Chee Guan, 18202044
 * @author Rajit Banerjee, 18202817
 * @team DarkMode
 */
public class FrameController {
    private FrameView view;

    public FrameController(FrameView view) {
        this.view = view;
    }

    public void update(Frame frame) {
        view.redraw(frame);
    }

}
