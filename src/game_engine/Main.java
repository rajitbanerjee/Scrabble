package game_engine;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ui.BoardView;
import ui.CLIView;
import ui.GameView;

/**
 *  Main class to run the JavaFX Application.
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
        CLIView cliView = new CLIView();
        CLIController controller = new CLIController(cliView.getInputView(),
                cliView.getHistoryView(), null);
        BoardView boardView = new BoardView(controller.getBoard());
        BoardController boardController = new BoardController(boardView, controller.getBoard());
        controller.setBoardController(boardController);
        GameView view = new GameView(cliView, boardView);
        stage.setScene(new Scene(view));
        stage.setResizable(false);
        stage.show();
    }
}
