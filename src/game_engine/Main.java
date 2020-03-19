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
        FrameView frameView = new FrameView();
        FrameController frameController = new FrameController(frameView);

        CLIView cliView = new CLIView();
        CLIController controller = new CLIController(cliView.getInputView(),
                cliView.getHistoryView(), null, frameController);

        BoardView boardView = new BoardView(controller.getBoard());
        BoardController boardController = new BoardController(boardView);
        controller.setBoardController(boardController);

        OptionsView optionsView = new OptionsView(frameView);
        GameView view = new GameView(cliView, boardView, optionsView);
        Scene scene = new Scene(view, UIConstants.getSceneWidth(), UIConstants.getSceneHeight());

        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        InputStream icon = classLoader.getResourceAsStream("resources/icon.jpg");
        stage.getIcons().add(new Image(Objects.requireNonNull(icon)));
        stage.setTitle("Scrabble by DarkMode.");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

}
