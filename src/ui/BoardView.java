package ui;

import constants.GameConstants;
import constants.UIConstants;
import game.Board;
import game.Square;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

/**
 * Board GUI design.
 * TODO comments
 *
 * @author Tee Chee Guan, 18202044
 * @author Rajit Banerjee, 18202817
 * @author Katarina Cvetkovic, 18347921
 * @team DarkMode
 */
public class BoardView extends GridPane {
    private Board board;
    private static double squareSize;

    public BoardView(Board board) {
        super();
        squareSize = UIConstants.SQUARE_SIZE;
        this.board = board;
        setAlignment(Pos.TOP_LEFT);
        setHgap(UIConstants.BOARD_HGAP);
        setVgap(UIConstants.BOARD_VGAP);
        redraw();
    }

    public void update() {
        redraw();
    }

    // add(column, row)
    public void redraw() {
        Square[][] boardArray = board.getBoard();
        // Initialise top-left empty block
        add(getEmptyUI(), 0, 0);
        // Draws the first row (Column Names)
        for (int k = 0; k < GameConstants.BOARD_SIZE; k++) {
            add(getColumnUI((char) ('A' + k)), k + 1, 0);
        }
        // Draws the first column (Row Numbers)
        for (int k = 0; k < GameConstants.BOARD_SIZE; k++) {
            add(getRowUI(k + 1), 0, k + 1);
        }
        // Draws the rest of the board
        for (int i = 0; i < GameConstants.BOARD_SIZE; i++) {
            for (int j = 0; j < GameConstants.BOARD_SIZE; j++) {
                add(getSquareUI(boardArray[i][j]), j + 1, i + 1);
            }
        }
    }

    public static Node getSquareUI(Square square) {
        Color col = null;
        switch (square.getMultiplier()) {
            case CENTRE:
            case DOUBLE_WS:
                col = Color.LIGHTPINK;
                break;
            case DOUBLE_LS:
                col = Color.LIGHTBLUE;
                break;
            case NORMAL:
                col = Color.GHOSTWHITE;
                break;
            case TRIPLE_LS:
                col = Color.CADETBLUE;
                break;
            case TRIPLE_WS:
                col = Color.INDIANRED;
                break;
        }
        Text text;
        if (square.isEmpty()) {
            StackPane pane = new StackPane();
            pane.setBackground(new Background(new BackgroundFill(col, CornerRadii.EMPTY, Insets.EMPTY)));
            text = new Text(square.toString());
            pane.getChildren().add(text);
            return pane;
        } else {
            GridPane gridPane = new GridPane();
            gridPane.setMinSize(squareSize, squareSize);
            gridPane.setPadding(new Insets(6));
            gridPane.setHgap(UIConstants.BOARD_HGAP);

            text = new Text(Character.toString(square.getTile().getType()));
            Text points = new Text(Integer.toString((square.getTile().getPoints())));
            points.setFont(UIConstants.pointsFont);
            gridPane.add(text, 1, 1);
            gridPane.add(points, 2, 2);

            col = Color.LIGHTYELLOW;
            gridPane.setBackground(new Background(new BackgroundFill(col, CornerRadii.EMPTY, Insets.EMPTY)));
            return gridPane;
        }
    }

    public static Node getColumnUI(char ch) {
        StackPane pane = new StackPane();
        Rectangle rect = new Rectangle(squareSize, squareSize);
        rect.setFill(Color.GHOSTWHITE);
        Text text = new Text(Character.toString(ch));
        pane.getChildren().add(rect);
        pane.getChildren().add(text);
        return pane;
    }

    public static Node getRowUI(int i) {
        StackPane pane = new StackPane();
        Rectangle rect = new Rectangle(squareSize, squareSize);
        rect.setFill(Color.GHOSTWHITE);
        Text text = new Text(Integer.toString(i));
        pane.getChildren().add(rect);
        pane.getChildren().add(text);
        return pane;
    }

    public static Node getEmptyUI() {
        StackPane pane = new StackPane();
        Rectangle rect = new Rectangle(squareSize, squareSize);
        rect.setFill(Color.GHOSTWHITE);
        pane.getChildren().add(rect);
        return pane;
    }

}
