package game_engine;

import javafx.scene.input.KeyCode;
import ui.*;

import static constants.UIConstants.STATUS_CODE.*;

/**
 * Main controller to manage all the UI view components.
 * TODO javadoc comments
 *
 * @author Tee Chee Guan, 18202044
 * @author Katarina Cvetkovic, 18347921
 * @author Rajit Banerjee, 18202817
 * @team DarkMode
 */
public class GameController {
    private FrameView frameView;
    private ScoreView scoreView;
    private CLIView cliView;
    private ButtonsView buttonsView;
    private BoardView boardView;
    private Scrabble game;

    public GameController(FrameView frameView, ScoreView scoreView,
                          CLIView cliView, ButtonsView buttonsView,
                          BoardView boardView, Scrabble game) {
        this.frameView = frameView;
        this.scoreView = scoreView;
        this.cliView = cliView;
        this.buttonsView = buttonsView;
        this.boardView = boardView;
        this.game = game;
        setListeners();
        setButtons();
    }

    public void setListeners() {
        cliView.getInputView().setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER) {
                updateGame(cliView.getInputView().getText());
                cliView.clearInputView();
            }
        });
    }

    public void updateGame(String command) {
        if (game.getGameState() != GAME_OVER) {
            Scrabble.printToOutput(command);
            try {
                boolean updateBoard = game.processCommand(command);
                // Update the board display
                if (updateBoard) {
                    boardView.update();
                }
                // Set the players' names
                if (!scoreView.areNamesInitialised() && game.getGameState() != P1_NAME
                        && game.getGameState() != P2_NAME) {
                    scoreView.setNames(game.getPlayer1().getName(), game.getPlayer2().getName());
                }
                // Update the frame and score display
                if (game.getGameState() == P1_TURN) {
                    scoreView.update(game.getPlayer1().getScore(), game.getPlayer2().getScore());
                    frameView.update(game.getPlayer1Frame());
                } else if (game.getGameState() != P2_NAME) {
                    scoreView.update(game.getPlayer1().getScore(), game.getPlayer2().getScore());
                    frameView.update(game.getPlayer2Frame());
                }
            } catch (RuntimeException e) {
                System.exit(-1);
            }
        }
    }

    public void setButtons() {
        buttonsView.getPassButton().setOnAction(event -> {
            if (game.getGameState() == P1_NAME ||
                    game.getGameState() == P2_NAME) {
                PopupView.displayUnsupportedActionPopup();
            } else {
                updateGame("PASS");
            }
        });
        buttonsView.getChallengeButton().setOnAction(event -> {
            if (game.getGameState() == P1_NAME ||
                    game.getGameState() == P2_NAME) {
                PopupView.displayUnsupportedActionPopup();
            } else {
                updateGame("CHALLENGE");
            }
        });
        buttonsView.getQuitButton().setOnAction(event -> {
            if (game.getGameState() == P1_NAME ||
                    game.getGameState() == P2_NAME) {
                PopupView.displayQuitPopup();
                System.exit(0);
            } else {
                updateGame("QUIT");
            }
        });
    }

}