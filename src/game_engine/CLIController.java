package game_engine;

import game.Board;
import javafx.scene.input.KeyCode;
import ui.CommandHistoryView;
import ui.CommandInputView;

import static constants.UIConstants.STATUS_CODE.*;

/**
 * Controller for the GUI command panel.
 * TODO comments
 *
 * @author Tee Chee Guan, 18202044
 * @author Rajit Banerjee, 18202817
 * @author Katarina Cvetkovic, 18347921
 * @team DarkMode
 */
public class CLIController {
    private CommandInputView inputView;
    private BoardController boardController;
    private FrameController frameController;
    private ScoreController scoreController;
    private Scrabble game;

    public CLIController(CommandInputView inputView,
                         CommandHistoryView historyView, BoardController boardController,
                         FrameController frameController, ScoreController scoreController) {
        this.inputView = inputView;
        this.boardController = boardController;
        this.frameController = frameController;
        this.scoreController = scoreController;
        game = new Scrabble(historyView);
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
                if (game.getGameState() != GAME_OVER) {
                    Scrabble.printToOutput(inputView.getText());
                    try {
                        boolean updateBoard = game.processCommand(inputView.getText());
                        // update the board display
                        if (updateBoard) {
                            boardController.update();
                        }
                        //set the players names for the scoreView(only needs to be done once)
                        if (!scoreController.namesInitialised && game.getGameState() != P1_NAME
                                && game.getGameState() != P2_NAME) {
                            scoreController.setNames(game.getPlayer1().getName(), game.getPlayer2().getName());
                        }
                        // update the frame and score display
                        if (game.getGameState() == P1_TURN) {
                            scoreController.update(game.getPlayer1().getScore(), game.getPlayer2().getScore());
                            frameController.update(game.getPlayer1Frame());
                        } else if (game.getGameState() != P2_NAME) {
                            scoreController.update(game.getPlayer1().getScore(), game.getPlayer2().getScore());
                            frameController.update(game.getPlayer2Frame());
                        }
                        inputView.clear();
                    } catch (InterruptedException e) {
                        System.exit(-1);
                    } catch (RuntimeException e) {
                        // FIXME Exception thrown by wait() in Scrabble
                        inputView.clear();
                    }
                }
            }
        });
    }

}
