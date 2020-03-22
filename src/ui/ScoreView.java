package ui;

import javafx.geometry.Insets;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;

/**
 * TextFlow containing scores
 *
 * @author Tee Chee Guan, 18202044
 * @author Rajit Banerjee, 18202817
 * @author Katarina Cvetkovic, 18347921
 * @team DarkMode
 */
public class ScoreView extends TextFlow {
    String p1Name;  //stores player 1's name
    String p2Name;  //stores player 2's name

    public ScoreView() {
        setTextAlignment(TextAlignment.CENTER);
        setPadding(new Insets(10));
    }

    public void initialiseNames(String name1, String name2) {
        p1Name = name1;
        p2Name = name2;
    }

    public void refreshScores(int score1, int score2) {
        getChildren().clear();
        Text p1Text = new Text(p1Name + ": " + score1 + " points\t\t");
        Text p2Text = new Text(p2Name + ": " + score2 + " points");
        getChildren().add(p1Text);
        getChildren().add(p2Text);
    }
}
