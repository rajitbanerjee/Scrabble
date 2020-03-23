package game_engine;

import javafx.scene.Node;
import javafx.scene.control.Button;
import ui.ButtonsView;

import java.util.List;

public class ButtonsController {
    private ButtonsView buttonsView;
    private GameController gameController;

    public ButtonsController(ButtonsView buttonsView, GameController gameController) {
        this.buttonsView = buttonsView;
        this.gameController = gameController;
        initControllers();
    }

    public void initControllers() {
        List<Node> nodeList = buttonsView.getChildren();
        if (nodeList.get(0) instanceof Button && nodeList.get(1) instanceof Button) {
            // Safe casts
            Button passButton = (Button) nodeList.get(0);
            Button challengeButton = (Button) nodeList.get(1);
            passButton.setOnAction(event -> gameController.updateGame("PASS"));
            challengeButton.setOnAction(event -> gameController.updateGame("CHALLENGE"));

        } else {
            throw new RuntimeException("Cannot setup button controllers");
        }
    }
}
