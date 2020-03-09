package ui;

import constants.Constants;
import game.Board;
import game.Square;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class BoardView extends GridPane {
    private Board board;

    public BoardView(Board board) {
        this.board = board;
        init();
    }

    private static Node getSquareUI(Square square) {
        StackPane pane = new StackPane();
        Rectangle rect = new Rectangle(50, 50);
        switch (square.getMultiplier()) {
            case CENTRE:
            case DOUBLE_WS:
                rect.setFill(Color.LIGHTPINK);
                break;
            case DOUBLE_LS:
                rect.setFill(Color.LIGHTBLUE);
                break;
            case NORMAL:
                rect.setFill(Color.GHOSTWHITE);
                break;
            case TRIPLE_LS:
                rect.setFill(Color.CADETBLUE);
                break;
            case TRIPLE_WS:
                rect.setFill(Color.INDIANRED);
                break;
        }
        Text text;
        if (square.getTile() == null) {
            text = new Text(square.toString());
        } else {
            text = new Text(Character.toString(square.getTile().getType()));
        }
        pane.getChildren().add(rect);
        pane.getChildren().add(text);
        return pane;
    }

    private void init() {
        Square[][] boardArray = board.getBoard();
        for (int i = 0; i < Constants.BOARD_SIZE; i++) {
            for (int j = 0; j < Constants.BOARD_SIZE; j++) {
                add(getSquareUI(boardArray[i][j]), j, i);
            }
        }
        setHgap(5);
        setVgap(5);
    }
}
