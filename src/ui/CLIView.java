package ui;

import javafx.scene.layout.VBox;

public class CLIView extends VBox {
    public CLIView() {
        super();
        getChildren().addAll(new CommandHistoryView(), new CommandInputView());
    }
}
