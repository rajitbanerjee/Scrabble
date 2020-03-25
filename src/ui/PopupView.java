package ui;

import game.Player;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Pop-up window on button click.
 * TODO javadoc comments
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
        window.setTitle("Invalid!");

        Label label = new Label("Cannot pass or challenge now!");
        Button button = new Button("Close");
        button.setOnAction(event -> window.close());

        VBox layout = new VBox(10);
        layout.getChildren().addAll(label, button);
        layout.setAlignment(Pos.CENTER);
        Scene scene = new Scene(layout, 250, 150);
        scene.getStylesheets().add("game_engine/stylesheet.css");
        window.setScene(scene);
        window.showAndWait();
    }

    public static void displayHelpPopup() {
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        Label label = new Label("Welcome to Scrabble!");
        String help = "Here are some supported operations:\n" + "HELP: Displays the help message.\n" +
                "PASS: Passes your current turn.\n" +
                "QUIT: Exits the game.\n" +
                "EXCHANGE [TILES]: Exchange tiles with new ones from the pool. (Replace [TILES] with the tiles)\n" +
                "CHALLENGE: Challenge the move made by the opponent in the previous turn.\n" +
                "[ROW][COLUMN] [DIRECTION] [WORD]: Places a word at a " +
                "certain location of the board\n with a specific orientation.";
        Label helpMessage = new Label(help);
        Button button = new Button("Close");
        HBox wrapper = new HBox(button);
        wrapper.setAlignment(Pos.CENTER);
        button.setOnAction(event -> window.close());
        BorderPane layout = new BorderPane();
        layout.setTop(label);
        layout.setCenter(helpMessage);
        layout.setBottom(wrapper);
        Scene scene = new Scene(layout, 750, 250);
        scene.getStylesheets().add("game_engine/stylesheet.css");
        window.setScene(scene);
        window.showAndWait();
    }

    public static void displayQuitPopup() {
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        Label label = new Label("Thanks for playing!");
        Button button = new Button("Close");
        button.setOnAction(event -> window.close());

        VBox layout = new VBox(10);
        layout.getChildren().addAll(label, button);
        layout.setAlignment(Pos.CENTER);
        Scene scene = new Scene(layout, 250, 150);
        scene.getStylesheets().add("game_engine/stylesheet.css");
        window.setScene(scene);
        window.showAndWait();
    }

    public static void displayQuitPopup(Player player1, Player player2) {
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Game over!");
        Label label = new Label("Thanks for playing!\n");
        Label p1Score = new Label(String.format("%s's score: %d", player1.getName(), player1.getScore()));
        Label p2Score = new Label(String.format("%s's score: %d", player2.getName(), player2.getScore()));

        int difference = player1.getScore() - player2.getScore();
        Label gameResult = new Label("Game is a tie!");
        if (difference != 0) {
            Player winner = (difference > 0) ? player1 : player2;
            gameResult = new Label(String.format("%s wins the game! Well done.", winner.getName()));
        }

        Button button = new Button("Close");
        button.setOnAction(event -> window.close());

        VBox layout = new VBox(10);
        layout.getChildren().addAll(label, p1Score, p2Score, gameResult, button);
        layout.setAlignment(Pos.CENTER);
        Scene scene = new Scene(layout, 250, 250);
        scene.getStylesheets().add("game_engine/stylesheet.css");
        window.setScene(scene);
        window.showAndWait();
    }

}