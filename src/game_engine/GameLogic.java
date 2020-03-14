package game_engine;

public class GameLogic {
    private Scrabble game;
    private CLIController controller;

    public GameLogic(CLIController controller) {
        this.controller = controller;
        //this.game = new Scrabble(controller);
    }

    public void start() {
        controller.printToOutput("\nWelcome to Scrabble by DarkMode.");
        controller.printToOutput("Player #1, please enter your name: ");
        //game.setGameStatus(Constants.STATUS_CODE.P1_NAME);


    }
}
