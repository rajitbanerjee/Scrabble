package ui;

import game.Player;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Pop-up window on button click.
 *
 * @author Tee Chee Guan, 18202044
 * @author Rajit Banerjee, 18202817
 * @author Katarina Cvetkovic, 18347921
 * @team DarkMode
 */
public class PopupView {
    public static void displayUnsupportedActionPopup() {
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Unsupported action");

        Label label = new Label("Cannot pass or challenge now!");
        Button button = new Button("Close");
        button.setOnAction(event -> window.close());

        VBox layout = new VBox(10);
        layout.getChildren().addAll(label, button);
        layout.setAlignment(Pos.CENTER);
        Scene scene = new Scene(layout, 250, 150);
        window.setScene(scene);
        window.showAndWait();
    }

    public static void displayQuitPopup() {
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Thanks for playing!");
        Label label = new Label("Thanks for playing!");
        Button button = new Button("Close");
        button.setOnAction(event -> window.close());

        VBox layout = new VBox(10);
        layout.getChildren().addAll(label, button);
        layout.setAlignment(Pos.CENTER);
        Scene scene = new Scene(layout, 250, 150);
        window.setScene(scene);
        window.showAndWait();
    }

    public static void displayQuitPopup(Player p1, Player p2) {
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Thanks for playing!");
        Label label = new Label("Thanks for playing!\nFinal Scores:");
        Label p1Score = new Label(String.format("%s's score: %d", p1.getName(), p1.getScore()));
        Label p2Score = new Label(String.format("%s's score: %d", p2.getName(), p2.getScore()));

        int difference = p1.getScore() - p2.getScore();
        Label gameResult = new Label("Game is a tie!");
        if (difference != 0) {
            Player winner = (difference > 0) ? p1 : p2;
            gameResult = new Label(String.format("%s wins the game! Well done.", winner.getName()));
        }

        Button button = new Button("Close");
        button.setOnAction(event -> window.close());

        VBox layout = new VBox(10);
        layout.getChildren().addAll(label, p1Score, p2Score, gameResult, button);
        layout.setAlignment(Pos.CENTER);
        Scene scene = new Scene(layout, 250, 250);
        window.setScene(scene);
        window.showAndWait();
    }
}
