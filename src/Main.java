import java.io.FileNotFoundException;

public class Main {

    public static boolean BOT_GAME = true;
    public static int BOT_DELAY = 0; // ms

    public static void main(String[] args) throws FileNotFoundException {
        Scrabble scrabble = new Scrabble();
        UserInterface ui = new UserInterface(scrabble);
        Bots bots = new Bots(scrabble, ui, args);
        ui.setBots(bots);
        ui.playGame();
    }

}