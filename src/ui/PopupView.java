package ui;

import constants.UIConstants;
import game.Player;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Pop-up window on button click.
 *
 * @author Tee Chee Guan, 18202044
 * @author Rajit Banerjee, 18202817
 * @author Katarina Cvetkovic, 18347921
 * Team 15: DarkMode
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
        scene.getStylesheets().add(UIConstants.stylesheet);
        window.setScene(scene);
        window.showAndWait();
    }

    /**
     * Displays a help dialog.
     */
    public static void displayHelpPopup() {
        Stage window = new Stage();
        window.setTitle("Welcome to Scrabble!");
        window.initModality(Modality.APPLICATION_MODAL);
        String help = "How to play?\n" +
                "HELP:\t\t\t\tDisplays the help message.\n" +
                "PASS:\t\t\t\tPasses your current turn.\n" +
                "QUIT:\t\t\t\tExits the game.\n" +
                "EXCHANGE <letters>:\tExchange letters with random ones from the pool.\n" +
                "CHALLENGE:\t\t\tChallenge the move made by the opponent in the previous turn.\n" +
                "\nValid move format (place a word starting at specified grid reference):\n" +
                "<COLUMN><ROW> <ORIENTATION (A/D)> <WORD>\n" +
                "(E.g. \"H8 A HELLO\" or \"H10 D HI\")\n" +
                "(Quick tip: Up and Down keys allow past command navigation, CTRL key allows autocompletion.)\n";
        Label text = new Label(help);

        Hyperlink link = new Hyperlink();
        link.setText("Click here to see the rules of Scrabble!");
        link.setOnAction(e -> displayScrabbleRules());

        Button button = new Button("Close");
        button.setOnAction(event -> window.close());

        VBox layout = new VBox(10);
        layout.getChildren().addAll(text, link, button);
        layout.setAlignment(Pos.CENTER);
        Scene scene = new Scene(layout, 750, 300);
        scene.getStylesheets().add(UIConstants.stylesheet);
        window.setScene(scene);
        window.showAndWait();
    }

    /**
     * Displays the rules of Scrabble.
     */
    private static void displayScrabbleRules() {
        Stage window = new Stage();
        window.setTitle("Wikipedia: Scrabble");
        window.initModality(Modality.APPLICATION_MODAL);
        final WebView browser = new WebView();
        final WebEngine webEngine = browser.getEngine();
        webEngine.load("https://en.wikipedia.org/wiki/Scrabble#Rules");

        VBox layout = new VBox(10);
        VBox.setVgrow(browser, Priority.ALWAYS);
        layout.getChildren().addAll(browser);
        layout.setAlignment(Pos.CENTER);
        Scene scene = new Scene(layout, UIConstants.SCENE_WIDTH, UIConstants.SCENE_HEIGHT);
        window.setScene(scene);
        window.showAndWait();
    }

    /**
     * Displays a quit popup for when players have 0 points.
     */
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
        scene.getStylesheets().add(UIConstants.stylesheet);
        window.setScene(scene);
        window.showAndWait();
    }

    /**
     * Displays final scores, declares winner and exits game.
     *
     * @param player1 Scrabble player 1
     * @param player2 Scrabble player 2
     */
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
        scene.getStylesheets().add(UIConstants.stylesheet);
        window.setScene(scene);
        window.showAndWait();
    }

}