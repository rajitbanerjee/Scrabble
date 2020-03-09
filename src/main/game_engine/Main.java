package game_engine;

import game.Board;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ui.CLIView;


public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        Board board = new Board();
        CLIView view = new CLIView();
        CLIController controller = new CLIController(view.getInputView(), view.getHistoryView());
        stage.setScene(new Scene(view));
        stage.show();
    }
}
