import javafx.application.Application;
import javafx.stage.Stage;

import java.io.FileNotFoundException;

public class Main extends Application {

    public static boolean BOT_GAME = true;
    public static int BOT_DELAY = 2; // TODO change for debugging, originally 3s

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws FileNotFoundException {
        Scrabble scrabble = new Scrabble();
        UserInterface ui = new UserInterface(scrabble);
        Bots bots = new Bots(scrabble, ui, getParameters());
        ui.setBots(bots);
        ui.displayStage(primaryStage);
    }

}