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
 * Team 15: DarkMode
 */
public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        // Set up game and views
        Scrabble game = new Scrabble();
        CLIView cliView = new CLIView();
        BoardView boardView = new BoardView();
        FrameView frameView = new FrameView();
        ScoreView scoreView = new ScoreView();
        ButtonsView buttonsView = new ButtonsView();
        OptionsView optionsView = new OptionsView(frameView, scoreView, buttonsView);
        GameView gameView = new GameView(cliView, boardView, optionsView);

        // Set up scene
        Scene scene = new Scene(gameView, UIConstants.SCENE_WIDTH, UIConstants.SCENE_HEIGHT);
        scene.getStylesheets().add(UIConstants.stylesheet);

        // Set up game controller
        GameController controller = new GameController(game, cliView, boardView, frameView,
                scoreView, buttonsView, scene);
        game.setController(controller);

        // Set up stage
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        InputStream icon = classLoader.getResourceAsStream("icon.jpg");
        stage.getIcons().add(new Image(Objects.requireNonNull(icon)));
        stage.setTitle("Scrabble by DarkMode.");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

}