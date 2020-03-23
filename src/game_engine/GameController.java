package game_engine;

import static constants.UIConstants.STATUS_CODE.*;

public class GameController {
    private BoardController boardController;
    private FrameController frameController;
    private ScoreController scoreController;
    private Scrabble game;

    public GameController(BoardController boardController, FrameController frameController,
                          ScoreController scoreController, Scrabble game) {
        this.boardController = boardController;
        this.frameController = frameController;
        this.scoreController = scoreController;
        this.game = game;
    }

    public void updateGame(String command) {
        if (game.getGameState() != GAME_OVER) {
            Scrabble.printToOutput(command);
            try {
                boolean updateBoard = game.processCommand(command);
                // update the board display
                if (updateBoard) {
                    boardController.update();
                }
                //set the players names for the scoreView (only needs to be done once), once names are set
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
            } catch (InterruptedException | RuntimeException e) {
                // quit game when exception encountered
                System.exit(-1);
            }
        }
    }
}
