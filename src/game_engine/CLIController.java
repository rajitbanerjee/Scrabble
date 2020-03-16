package game_engine;

import game.*;
import javafx.scene.input.KeyCode;
import ui.CommandHistoryView;
import ui.CommandInputView;

/**
 * Controller for the GUI command panel.
 *
 * @author Tee Chee Guan, 18202044
 * @author Rajit Banerjee, 18202817
 * @author Katarina Cvetkovic, 18347921
 * @team DarkMode
 */
public class CLIController {
    private CommandInputView inputView;
    private BoardController boardController;
    private ScrabbleFX game;


    public CLIController(CommandInputView inputView,
                         CommandHistoryView historyView, BoardController boardController) {
        this.inputView = inputView;
        this.boardController = boardController;
        game = new ScrabbleFX(historyView);
        setListeners();
    }

    public Board getBoard() {
        return game.getBoard();
    }

    public void setBoardController(BoardController boardController) {
        this.boardController = boardController;
    }

    public void setListeners() {
        inputView.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER) {
                game.printToOutput(inputView.getText());
                try {
                    boolean updateBoard = game.processCommand(inputView.getText());
                    if (updateBoard) {
                        boardController.update();
                    }
                } catch (InterruptedException e) {
                    System.exit(-1);
                }
                inputView.clear();
            }
        });
    }

}
