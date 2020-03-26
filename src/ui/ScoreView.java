package ui;

import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

/**
 * TextFlow containing scores.
 *
 * @author Katarina Cvetkovic, 18347921
 * @author Tee Chee Guan, 18202044
 * @author Rajit Banerjee, 18202817
 * Team 15: DarkMode
 */
public class ScoreView extends TextFlow {
    private String playerOneName;
    private String playerTwoName;
    private boolean namesInitialised;

    public ScoreView() {
        super();
        setId("score-view");
        namesInitialised = false;
    }

    /**
     * Updates the score display.
     *
     * @param score1 player1's score
     * @param score2 player2's score
     */
    public void update(int score1, int score2) {
        getChildren().clear();
        Text p1Text = new Text(playerOneName + ": " + score1 + " points\t\t");
        Text p2Text = new Text(playerTwoName + ": " + score2 + " points");
        p1Text.setId("player-scores");
        p2Text.setId("player-scores");
        getChildren().add(p1Text);
        getChildren().add(p2Text);
    }

    /**
     * Sets the names of both players.
     *
     * @param playerOneName player1's name
     * @param playerTwoName player2's name
     */
    public void setNames(String playerOneName, String playerTwoName) {
        this.playerOneName = playerOneName;
        this.playerTwoName = playerTwoName;
        namesInitialised = true;
    }

    /**
     * Checks if names are initialised.
     *
     * @return true if names are initialised.
     */
    public boolean areNamesInitialised() {
        return namesInitialised;
    }

}