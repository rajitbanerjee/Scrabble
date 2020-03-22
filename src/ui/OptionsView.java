package ui;

import constants.UIConstants;
import javafx.geometry.Pos;
import javafx.scene.layout.GridPane;

/**
 * The options panel will contain the player's frame, display each players
 * score and have buttons for pass, exchange, challenge
 */
public class OptionsView extends GridPane {
    public OptionsView(FrameView frameView, ScoreView scoreView) {
        super();
        setPrefHeight(UIConstants.getSceneHeight() * 0.25);
        setAlignment(Pos.CENTER);
        add(scoreView, 0, 0);
        add(frameView, 0, 1);
    }

}
