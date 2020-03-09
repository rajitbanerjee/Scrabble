package game_engine;

import game.Board;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ui.BoardView;


public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Board board = new Board();
        Scene scene = new Scene(new BoardView(board));
        stage.setScene(scene);
        stage.show();
    }
}
