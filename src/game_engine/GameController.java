package game_engine;

import constants.UIConstants;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import ui.*;

import static constants.UIConstants.STATUS_CODE.*;

/**
 * Main controller to manage all the UI view components.
 *
 * @author Tee Chee Guan, 18202044
 * @author Katarina Cvetkovic, 18347921
 * @author Rajit Banerjee, 18202817
 * Team 15: DarkMode
 */
public class GameController {
    private FrameView frameView;
    private ScoreView scoreView;
    private CLIView cliView;
    private ButtonsView buttonsView;
    private BoardView boardView;
    private Scrabble game;
    private Scene scene;
    private int nLastCommand;

    public GameController(Scrabble game, CLIView cliView, BoardView boardView, FrameView frameView,
                          ScoreView scoreView, ButtonsView buttonsView, Scene scene) {
        this.game = game;
        this.cliView = cliView;
        this.boardView = boardView;
        this.frameView = frameView;
        this.scoreView = scoreView;
        this.buttonsView = buttonsView;
        this.scene = scene;
        setListeners();
        setButtons();
    }

    /**
     * Sets the listeners for GameController.
     */
    public void setListeners() {
        cliView.getInputView().setOnKeyPressed(keyEvent -> {
            CommandHistoryView historyView = CLIView.historyView;
            CommandInputView inputView = cliView.getInputView();
            if (keyEvent.getCode() == KeyCode.ENTER) {
                updateGame(inputView.getText());
                historyView.addCommand(inputView.getText());
                nLastCommand = 0;
                cliView.clearInputView();
            } else if (keyEvent.getCode() == KeyCode.UP) {
                if (historyView.getHistorySize() != 0) {
                    nLastCommand++;
                    correctNLast();
                    inputView.setText(historyView.getNLastCommand(nLastCommand));
                    Platform.runLater(inputView::end);
                }
            } else if (keyEvent.getCode() == KeyCode.DOWN) {
                if (nLastCommand == 0) {
                    cliView.clearInputView();
                } else if (historyView.getHistorySize() != 0) {
                    nLastCommand--;
                    if (nLastCommand == 0) {
                        inputView.setText("");
                    } else {
                        correctNLast();
                        inputView.setText(historyView.getNLastCommand(nLastCommand));
                    }
                }
            } else if (keyEvent.getCode() == KeyCode.CONTROL) {
                // Only start autocomplete if game has started and input is not empty
                if ((game.getGameState() == P1_TURN || game.getGameState() == P2_TURN) &&
                        !inputView.getText().trim().isEmpty()) {
                    inputView.setText(getAutoCompletedText(inputView.getText()));
                    inputView.end();
                }
            }
        });
    }

    // Returns the original phrase if no matches
    private String getAutoCompletedText(String phrase) {
        String prefix = phrase.toUpperCase().trim();
        // Array of supported commands
        String[] supportedCommands = {"HELP", "PASS", "CHALLENGE", "QUIT", "RESTART"};
        for (String s : supportedCommands) {
            if (s.startsWith(prefix)) {
                return s;
            }
        }
        // If no matches return original phrase
        return phrase;
    }

    // Corrects the variable nLastCommand
    private void correctNLast() {
        CommandHistoryView historyView = CLIView.historyView;
        if (nLastCommand > historyView.getHistorySize()) {
            nLastCommand = historyView.getHistorySize();
        } else if (nLastCommand < 1) {
            nLastCommand = 1;
        }
    }

    /**
     * Updates the game display according to the last command entered by the user.
     *
     * @param command the command entered by the player
     */
    public void updateGame(String command) {
        if (game.getGameState() != GAME_OVER) {
            try {
                boolean updateBoard = game.processCommand(command.trim());
                // Update the board display
                if (updateBoard) {
                    boardView.update(game.getBoard());
                }
                // Update the score and frame display
                boolean arePlayersReady = game.arePlayersReady();
                if (arePlayersReady && game.getGameState() != P1_NAME
                        && game.getGameState() != P2_NAME) {
                    scoreView.setNames(game.getPlayer1().getName(), game.getPlayer2().getName());
                }
                if (!arePlayersReady) {
                    // Remove score and frame views when game has been restarted
                    scoreView.remove();
                    frameView.remove();
                } else {
                    // Update scores
                    scoreView.update(game.getPlayer1().getScore(),
                            game.getPlayer2().getScore(), game.getGameState());
                    // Update frame
                    if (game.getGameState() == P1_TURN) {
                        frameView.update(game.getPlayer1().getFrame());
                    } else if (game.getGameState() == P2_TURN) {
                        frameView.update(game.getPlayer2().getFrame());
                    }
                }
            } catch (RuntimeException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Sets actions for buttons: PASS, CHALLENGE, QUIT, HELP, RESTART
     */
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
        buttonsView.getHelpButton().setOnAction(event -> updateGame("HELP"));
        buttonsView.getRestartButton().setOnAction(event -> updateGame("RESTART"));
        buttonsView.getThemeButton().setOnAction(event -> {
            UIConstants.switchTheme();
            scene.getStylesheets().clear();
            scene.getStylesheets().add(UIConstants.stylesheet);
        });
    }

}