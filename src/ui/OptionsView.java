package ui;

import javafx.scene.layout.GridPane;

/**
 * Contains the player's frame, display each players
 * score and have buttons for pass, exchange, challenge.
 *
 * @author Katarina Cvetkovic, 18347921
 * @author Tee Chee Guan, 18202044
 * @author Rajit Banerjee, 18202817
 * @team DarkMode
 */
public class OptionsView extends GridPane {
    public OptionsView(FrameView frameView, ScoreView scoreView, ButtonsView buttonsView) {
        super();
        setId("options-view");
        add(scoreView, 0, 0);
        add(frameView, 0, 1);
        add(buttonsView, 0, 2);
    }

}