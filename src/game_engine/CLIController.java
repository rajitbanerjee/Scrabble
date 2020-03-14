package game_engine;

import constants.Constants;
import game.Board;
import game.Frame;
import game.Pool;
import javafx.scene.input.KeyCode;
import ui.CommandHistoryView;
import ui.CommandInputView;

public class CLIController {
    private CommandInputView inputView;
    private CommandHistoryView historyView;
    private Pool pool;
    private Board board;
    private Player player1, player2;
    private Constants.STATUS_CODE gameState;

    public CLIController(CommandInputView inputView, CommandHistoryView historyView) {
        this.inputView = inputView;
        this.historyView = historyView;
        pool = new Pool();
        board = new Board();
        player1 = new Player("x", new Frame(pool));
        player2 = new Player("x", new Frame(pool));
        gameState = Constants.STATUS_CODE.P1_NAME;

        setListeners();
        printWelcome();
    }

    public void startGame() {
        printToOutput(String.format("[Console] Player 1: %s", player1.getName()));
        printToOutput(String.format("[Console] Player 2: %s", player2.getName()));
    }

    public void setListeners() {
        inputView.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER) {
                historyView.printText(inputView.getText());
                processCommand(inputView.getText(), gameState);
                inputView.clear();
            }
        });
    }

    private void processCommand(String command, Constants.STATUS_CODE statusCode) {
        switch (statusCode) {
            case P1_NAME:
                try {
                    player1 = new Player(command, new Frame(pool));
                    gameState = Constants.STATUS_CODE.P2_NAME;
                    printToOutput("Player #2, please enter your name: ");
                } catch (NullPointerException ex) {
                    gameState = Constants.STATUS_CODE.P1_NAME;
                    printToOutput("Player #1, please enter your name: ");
                }
                break;
            case P2_NAME:
                try {
                    player2 = new Player(command, new Frame(pool));
                    gameState = Constants.STATUS_CODE.P1_TURN;
                    startGame();
                } catch (NullPointerException ex) {
                    gameState = Constants.STATUS_CODE.P2_NAME;
                    printToOutput("Player #2, please enter your name: ");
                }
                break;
            case P1_TURN:

                break;
        }
    }

    private void printWelcome() {
        printToOutput("Welcome to Scrabble by DarkMode.");
        printToOutput("Player #1, please enter your name: ");
    }

    public void printToOutput(String text) {
        historyView.printText(text);
    }
}
