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

    /**
     * Creates a new score view.
     */
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
     * @param score1    player1's score
     * @param score2    player2's score
     * @param gameState the current game state
     */
    public void update(int score1, int score2, UIConstants.STATUS_CODE gameState) {
        remove();
        Button player1 = new Button(playerOneName + ": " + score1 + " points");
        Button player2 = new Button(playerTwoName + ": " + score2 + " points");
        if (gameState == UIConstants.STATUS_CODE.P1_TURN) {
            player1.setId("player-turn");
            player2.setId("opponent-turn");
        } else {
            player2.setId("player-turn");
            player1.setId("opponent-turn");
        }
        getChildren().add(player1);
        getChildren().add(player2);
    }

}