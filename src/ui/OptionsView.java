package ui;

import constants.UIConstants;
import javafx.geometry.Pos;
import javafx.scene.layout.GridPane;

/**
 * The options panel will contain the player's frame, display each players
 * score and have buttons for pass, exchange, challenge
 */
public class OptionsView extends GridPane {
    private FrameView frameView;

    public OptionsView() {
        super();
        this.frameView = new FrameView();
        setMinHeight(UIConstants.getSceneHeight() * 0.25);
        setMaxHeight(UIConstants.getSceneHeight() * 0.25);
        setAlignment(Pos.CENTER);
        add(frameView, 0, 0);
    }
}
