package game_engine;

import game.Frame;
import ui.FrameView;

public class FrameController {
    private FrameView view;

    public FrameController(FrameView view) {
        this.view = view;
    }

    public void update(Frame frame) {
        view.redraw(frame);
    }
}
