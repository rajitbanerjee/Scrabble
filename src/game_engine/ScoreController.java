package game_engine;

import ui.ScoreView;

/**
 * Controller for the GUI score display.
 *
 * @author Katarina Cvetkovic, 18347921
 * @author Tee Chee Guan, 18202044
 * @author Rajit Banerjee, 18202817
 * @team DarkMode
 */
public class ScoreController {
    ScoreView view;
    Boolean namesInitialised = false;

    public ScoreController(ScoreView view) {
        this.view = view;
    }

    public void setNames(String playerOneName, String playerTwoName) {
        view.initialiseNames(playerOneName, playerTwoName);
        namesInitialised = true;
    }

    public void update(int score1, int score2) {
        view.refreshScores(score1, score2);
    }

}
