package ui;

import game.Player;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.InputStream;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Pop-up window on button click.
 *
 * @author Tee Chee Guan, 18202044
 * @author Rajit Banerjee, 18202817
 * @author Katarina Cvetkovic, 18347921
 * Team 15: DarkMode
 */
public class PopupView {
    public static InputStream icon = ClassLoader.getSystemClassLoader().
            getResourceAsStream("icon.png");

    /**
     * Displays the start popup.
     *
     * @return 1, if game is to be played against a bot, 0 if not, -1 for quit
     */
    public static int displayStartPopup() {
        Stage window = new Stage();
        window.setTitle("Welcome");
        window.initModality(Modality.APPLICATION_MODAL);
        window.close();
        AtomicInteger botGame = new AtomicInteger(-1);
        Label title = new Label("Scrabble by DarkMode.");
        title.setId("start-title");
        Label label = new Label("Who do you want to play against?");

        Button yes = new Button("DarkMode, the Scrabble bot");
        yes.setId("command-button");
        yes.setOnAction(event -> {
            botGame.set(1);
            window.close();
        });
        Button no = new Button("Another player");
        no.setId("command-button");
        no.setOnAction(event -> {
            botGame.set(0);
            window.close();
        });
        HBox buttons = new HBox(10);
        buttons.getChildren().addAll(yes, no);
        buttons.setAlignment(Pos.CENTER);

        VBox layout = new VBox(10);
        layout.setId("start-popup");
        layout.getChildren().addAll(title, label, buttons);
        layout.setAlignment(Pos.CENTER);
        Scene scene = new Scene(layout, 600, 300);
        scene.getStylesheets().add(GameView.stylesheet);
        window.getIcons().add(new Image(Objects.requireNonNull(icon)));
        window.setScene(scene);
        window.showAndWait();
        window.setResizable(false);
        return botGame.get();
    }

    /**
     * Displays unsupported action pop-up.
     */
    public static void displayUnsupportedActionPopup() {
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Invalid!");
        Label label = new Label("Cannot pass or challenge now!");
        Button button = new Button("Close");
        button.setId("command-button");
        button.setOnAction(event -> window.close());

        VBox layout = new VBox(10);
        layout.getChildren().addAll(label, button);
        layout.setAlignment(Pos.CENTER);
        Scene scene = new Scene(layout, 300, 150);
        scene.getStylesheets().add(GameView.stylesheet);
        window.getIcons().add(new Image(Objects.requireNonNull(icon)));
        window.setScene(scene);
        window.setResizable(false);
        window.showAndWait();
    }

    /**
     * Displays a help message, with a link to the Scrabble rules.
     */
    public static void displayHelpPopup() {
        Stage window = new Stage();
        window.setTitle("Welcome to Scrabble!");
        window.initModality(Modality.APPLICATION_MODAL);
        String help = "How to play?\n" +
                "HELP:\t\t\t\tDisplays the help message.\n" +
                "PASS:\t\t\t\tPasses your current turn.\n" +
                "QUIT:\t\t\t\tExits the game.\n" +
                "EXCHANGE <letters>:\tExchange letters with random ones from the pool. (E.g. \"EXCHANGE JK\")\n" +
                "CHALLENGE:\t\t\tChallenge the move made by the opponent in the previous turn.\n" +
                "RESTART:\t\t\t\tStarts a new game of Scrabble.\n" +
                "SWITCH THEME:\t\tSwitches the display theme (dark/light).\n" +
                "NAME <letters>:\t\tChange your display name.\n" +
                "\nValid move format (place a word starting at specified grid reference):\n" +
                "<COLUMN><ROW> <ORIENTATION (A/D)> <WORD>\n" +
                "(E.g. \"H8 A HELLO\" or \"H10 D HI\")\n" +
                "\nTo use blank tiles, simply type the desired tile in place of the blank tile.\n" +
                "(E.g. \"H8 A HELLO\" with frame having: [H, -, L, L, -]\n" +
                "\nQuick tips: \n" +
                "1. Up and Down keys allow past command navigation.\n" +
                "2. Use CTRL key to autocomplete commands.\n";
        Label text = new Label(help);

        Hyperlink link = new Hyperlink();
        link.setText("Click here to see the rules of Scrabble!");
        link.setOnAction(e -> displayScrabbleRules());

        Button button = new Button("Close");
        button.setId("command-button");
        button.setOnAction(event -> window.close());

        VBox layout = new VBox(10);
        layout.getChildren().addAll(text, link, button);
        layout.setAlignment(Pos.CENTER);
        Scene scene = new Scene(layout, 750, 500);
        scene.getStylesheets().add(GameView.stylesheet);
        window.getIcons().add(new Image(Objects.requireNonNull(icon)));
        window.setScene(scene);
        window.setResizable(false);
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
        Scene scene = new Scene(layout, GameView.SCENE_WIDTH, GameView.SCENE_HEIGHT);
        window.getIcons().add(new Image(Objects.requireNonNull(icon)));
        window.setScene(scene);
        window.setResizable(false);
        window.showAndWait();
    }

    /**
     * Displays a quit popup for when players have 0 points.
     */
    public static void displayQuitPopup() {
        Stage window = new Stage();
        window.setTitle("Quit");
        window.initModality(Modality.APPLICATION_MODAL);
        Label label = new Label("Thanks for playing!");
        Button button = new Button("Close");
        button.setId("command-button");
        button.setOnAction(event -> window.close());

        VBox layout = new VBox(10);
        layout.getChildren().addAll(label, button);
        layout.setAlignment(Pos.CENTER);
        Scene scene = new Scene(layout, 300, 150);
        scene.getStylesheets().add(GameView.stylesheet);
        window.getIcons().add(new Image(Objects.requireNonNull(icon)));
        window.setScene(scene);
        window.setResizable(false);
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
        window.setTitle("Game over!");
        window.initModality(Modality.APPLICATION_MODAL);
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
        button.setId("command-button");
        button.setOnAction(event -> window.close());

        VBox layout = new VBox(10);
        layout.getChildren().addAll(label, p1Score, p2Score, gameResult, button);
        layout.setAlignment(Pos.CENTER);
        Scene scene = new Scene(layout, 300, 150);
        scene.getStylesheets().add(GameView.stylesheet);
        window.getIcons().add(new Image(Objects.requireNonNull(icon)));
        window.setScene(scene);
        window.setResizable(false);
        window.showAndWait();
    }

    /**
     * Displays the restart popup.
     *
     * @return {@code true} if game is to be restarted
     */
    public static boolean displayRestartPopup() {
        Stage window = new Stage();
        window.setTitle("Restart");
        window.initModality(Modality.APPLICATION_MODAL);
        AtomicBoolean doRestart = new AtomicBoolean();
        Label label = new Label("Do you want to restart the game?");

        Button yes = new Button("Yes");
        yes.setId("command-button");
        yes.setOnAction(event -> {
            doRestart.set(true);
            window.close();
        });
        Button no = new Button("No");
        no.setId("command-button");
        no.setOnAction(event -> {
            doRestart.set(false);
            window.close();
        });
        HBox buttons = new HBox(10);
        buttons.getChildren().addAll(yes, no);
        buttons.setAlignment(Pos.CENTER);

        VBox layout = new VBox(10);
        layout.getChildren().addAll(label, buttons);
        layout.setAlignment(Pos.CENTER);
        Scene scene = new Scene(layout, 300, 150);
        scene.getStylesheets().add(GameView.stylesheet);
        window.getIcons().add(new Image(Objects.requireNonNull(icon)));
        window.setScene(scene);
        window.showAndWait();
        window.setResizable(false);
        return doRestart.get();
    }

}