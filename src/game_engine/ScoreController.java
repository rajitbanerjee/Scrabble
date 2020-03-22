package game_engine;

import ui.ScoreView;

public class ScoreController {
    ScoreView view;
    Boolean namesInitialised = false;

    public ScoreController(ScoreView view) {
        this.view = view;
    }

    public void setNames(String name1, String name2) {
        view.initialiseNames(name1, name2);
        namesInitialised = true;
    }

    public void update(int score1, int score2) {
        view.refreshScores(score1, score2);
    }
}
