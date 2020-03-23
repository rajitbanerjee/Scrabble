package game_engine;

import javafx.scene.input.KeyCode;
import ui.CommandInputView;

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
    private GameController gameController;

    public CLIController(CommandInputView inputView, GameController gameController) {
        this.inputView = inputView;
        this.gameController = gameController;
        setListeners();
    }

    public void setListeners() {
        inputView.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER) {
                gameController.updateGame(inputView.getText());
                inputView.clear();
            }
        });
    }
}
