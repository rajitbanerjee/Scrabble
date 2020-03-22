package ui;

import javafx.geometry.Insets;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;

/**
 * TextFlow containing scores
 *
 * @author Katarina Cvetkovic, 18347921
 * @author Tee Chee Guan, 18202044
 * @author Rajit Banerjee, 18202817
 * @team DarkMode
 */
public class ScoreView extends TextFlow {
    private String playerOneName;
    private String playerTwoName;

    public ScoreView() {
        setTextAlignment(TextAlignment.CENTER);
        setPadding(new Insets(10));
    }

    public void initialiseNames(String playerOneName, String playerTwoName) {
        this.playerOneName = playerOneName;
        this.playerTwoName = playerTwoName;
    }

    public void refreshScores(int score1, int score2) {
        getChildren().clear();
        Text p1Text = new Text(playerOneName + ": " + score1 + " points\t\t");
        Text p2Text = new Text(playerTwoName + ": " + score2 + " points");
        getChildren().add(p1Text);
        getChildren().add(p2Text);
    }

}
