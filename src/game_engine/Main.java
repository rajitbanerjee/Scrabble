package game_engine;

import constants.UIConstants;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import ui.*;

import java.io.InputStream;
import java.util.Objects;

/**
 * Main class to run the JavaFX Application.
 *
 * @author Tee Chee Guan, 18202044
 * @author Rajit Banerjee, 18202817
 * @author Katarina Cvetkovic, 18347921
 * @team DarkMode
 */
public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        // Set up game and Views
        FrameView frameView = new FrameView();
        ScoreView scoreView = new ScoreView();
        CLIView cliView = new CLIView();
        ButtonsView buttonsView = new ButtonsView();

        Scrabble game = new Scrabble(cliView.getHistoryView());
        BoardView boardView = new BoardView(game.getBoard());
        OptionsView optionsView = new OptionsView(frameView, scoreView, buttonsView);
        GameView gameView = new GameView(cliView, boardView, optionsView);

        // Set up game controller
        new GameController(frameView, scoreView, cliView, buttonsView, boardView, game);

        // Set up scene
        Scene scene = new Scene(gameView, UIConstants.getSceneWidth(), UIConstants.getSceneHeight());
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        InputStream icon = classLoader.getResourceAsStream("resources/icon.jpg");

        stage.getIcons().add(new Image(Objects.requireNonNull(icon)));
        stage.setTitle("Scrabble by DarkMode.");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

}
