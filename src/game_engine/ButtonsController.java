package game_engine;

import ui.ButtonsView;

public class ButtonsController {
    private ButtonsView buttonsView;
    private GameController gameController;

    public ButtonsController(ButtonsView buttonsView, GameController gameController) {
        this.buttonsView = buttonsView;
        this.gameController = gameController;
        initControllers();
    }

    public void initControllers() {
        buttonsView.getPassButton().setOnAction(event -> gameController.updateGame("PASS"));
        buttonsView.getChallengeButton().setOnAction(event -> gameController.updateGame("CHALLENGE"));
        buttonsView.getQuitButton().setOnAction(event -> gameController.updateGame("QUIT"));
    }
}
