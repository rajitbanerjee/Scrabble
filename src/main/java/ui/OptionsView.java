package ui;

import javafx.scene.layout.GridPane;

/**
 * Contains the player's frame, displays each player's score and has
 * buttons for help, pass, challenge, quit, restart, switch theme.
 *
 * @author Katarina Cvetkovic, 18347921
 * @author Tee Chee Guan, 18202044
 * @author Rajit Banerjee, 18202817
 * Team 15: DarkMode
 */
public class OptionsView extends GridPane {
    /**
     * Creates a new options view.
     *
     * @param frameView   the frame view
     * @param scoreView   the score view
     * @param buttonsView the buttons view
     */
    public OptionsView(FrameView frameView, ScoreView scoreView, ButtonsView buttonsView) {
        super();
        setId("options-view");
        add(scoreView, 0, 0);
        add(frameView, 0, 1);
        add(buttonsView, 0, 2);
    }

}