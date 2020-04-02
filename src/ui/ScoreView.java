package ui;

import constants.UIConstants;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;

/**
 * Score display GUI design.
 *
 * @author Rajit Banerjee, 18202817
 * @author Katarina Cvetkovic, 18347921
 * @author Tee Chee Guan, 18202044
 * Team 15: DarkMode
 */
public class ScoreView extends HBox {
    private String playerOneName;
    private String playerTwoName;

    public ScoreView() {
        super();
        setId("score-view");
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
    }

    /**
     * Removes the ScoreView.
     */
    public void remove() {
        getChildren().clear();
    }

    /**
     * Updates the score display.
     *
     * @param score1 player1's score
     * @param score2 player2's score
     */
    public void update(int score1, int score2, UIConstants.STATUS_CODE turn) {
        remove();
        Button p1Button = new Button(playerOneName + ": " + score1 + " points");
        Button p2Button = new Button(playerTwoName + ": " + score2 + " points");
        if (turn == UIConstants.STATUS_CODE.P2_TURN) {
            p2Button.setId("player-turn");
            p1Button.setId("opponent-turn");

        } else {
            p1Button.setId("player-turn");
            p2Button.setId("opponent-turn");
        }
        getChildren().add(p1Button);
        getChildren().add(p2Button);
    }

}