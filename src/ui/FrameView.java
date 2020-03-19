package ui;


import constants.UIConstants;
import game.Frame;
import game.Pool;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

/**
 * Frame GUI design.
 *
 * @author Tee Chee Guan, 18202044
 * @author Rajit Banerjee, 18202817
 * @author Katarina Cvetkovic, 18347921
 * @team DarkMode
 */
public class FrameView extends GridPane {
    private static double tileSize;
    Pool pool = new Pool();
    Frame frame = new Frame(pool);

    public FrameView() {
        tileSize = UIConstants.SQUARE_SIZE;
        setAlignment(Pos.CENTER);
        setMinWidth(UIConstants.CMD_INPUT_WIDTH);
        setHgap(UIConstants.BOARD_HGAP);
        setVgap(UIConstants.BOARD_VGAP);
    }

    public void redraw(Frame frame) {
        for (int i = 0; i < frame.getFrame().size(); i++) {
            GridPane tile = new GridPane();
            tile.setMinSize(tileSize, tileSize);
            tile.setMaxSize(tileSize, tileSize);
            tile.setPadding(new Insets(6, 6, 6, 6));

            Text text = new Text(Character.toString(frame.getFrame().get(i).getType()));
            Text points = new Text(Integer.toString((frame.getFrame().get(i).getPoints())));
            points.setFont(UIConstants.pointsFont);
            tile.setAlignment(Pos.CENTER);
            tile.add(text, 1, 1);
            tile.add(points, 2, 2);
            tile.setBackground(new Background(new BackgroundFill(Color.LIGHTYELLOW, CornerRadii.EMPTY, Insets.EMPTY)));

            add(tile, i, 1);
        }
    }
}