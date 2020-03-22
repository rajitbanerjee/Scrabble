package ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;

public class ButtonsView extends HBox {
    public Button pass;
    public Button challenge;

    public ButtonsView() {
        pass = new Button("PASS");
        challenge = new Button("CHALLENGE");
        getChildren().addAll(pass, challenge);
        setAlignment(Pos.CENTER);
        setSpacing(30);
        setPadding(new Insets(10));
    }
}
