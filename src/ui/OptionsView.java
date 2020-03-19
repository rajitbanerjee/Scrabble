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

    public OptionsView(FrameView frameView) {
        super();
        this.frameView = frameView;
        setPrefHeight(UIConstants.getSceneHeight() * 0.25);
        setAlignment(Pos.CENTER);
        add(this.frameView, 0, 0);
    }
}
