package game_engine;

import ui.FrameView;

public class FrameController {
    private FrameView view;

    public FrameController(FrameView view) {
        this.view = view;
    }

    public void update() {
        view.redraw();
    }
}
