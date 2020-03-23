package game_engine;

import constants.UIConstants;
import ui.ButtonsView;
import ui.PopupView;

public class ButtonsController {
    private ButtonsView buttonsView;
    private GameController gameController;
    private Scrabble game;

    public ButtonsController(ButtonsView buttonsView, GameController gameController, Scrabble game) {
        this.buttonsView = buttonsView;
        this.gameController = gameController;
        this.game = game;
        initControllers();
    }

    public void initControllers() {
        buttonsView.getPassButton().setOnAction(event -> {
            if (game.getGameState() == UIConstants.STATUS_CODE.P1_NAME ||
                    game.getGameState() == UIConstants.STATUS_CODE.P2_NAME) {
                PopupView.displayUnsupportedActionPopup();
            } else {
                gameController.updateGame("PASS");
            }
        });
        buttonsView.getChallengeButton().setOnAction(event -> {
            if (game.getGameState() == UIConstants.STATUS_CODE.P1_NAME ||
                    game.getGameState() == UIConstants.STATUS_CODE.P2_NAME) {
                PopupView.displayUnsupportedActionPopup();
            } else {
                gameController.updateGame("CHALLENGE");
            }
        });
        buttonsView.getQuitButton().setOnAction(event -> {
            if (game.getGameState() == UIConstants.STATUS_CODE.P1_NAME ||
                    game.getGameState() == UIConstants.STATUS_CODE.P2_NAME) {
                PopupView.displayQuitPopup();
                System.exit(0);
            } else {
                gameController.updateGame("QUIT");
            }
        });
    }
}
