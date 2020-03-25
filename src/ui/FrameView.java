package ui;

import constants.GameConstants;
import constants.UIConstants;
import game.Frame;
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
 * TODO javadoc comments
 *
 * @author Katarina Cvetkovic, 18347921
 * @author Tee Chee Guan, 18202044
 * @author Rajit Banerjee, 18202817
 * @team DarkMode
 */
public class FrameView extends GridPane {
    private double tileSize = UIConstants.SQUARE_SIZE;

    public FrameView() {
        super();
        setAlignment(Pos.CENTER);
        setMinWidth(UIConstants.CMD_INPUT_WIDTH);
        setHgap(UIConstants.BOARD_HGAP);
        setVgap(UIConstants.BOARD_VGAP);
    }

    public void update(Frame frame) {
        for (int i = 0; i < GameConstants.FRAME_LIMIT; i++) {
            GridPane tile = new GridPane();
            tile.setMinSize(tileSize, tileSize);
            tile.setMaxSize(tileSize, tileSize);
            tile.setPadding(new Insets(6, 6, 6, 6));

            Text text = new Text();
            Text points = new Text();
            if (i < frame.getFrame().size()) {
                text = new Text(Character.toString(frame.getFrame().get(i).getType()));
                points = new Text(Integer.toString((frame.getFrame().get(i).getPoints())));
            }
            points.setFont(UIConstants.pointsFont);
            tile.setAlignment(Pos.CENTER);
            tile.add(text, 1, 1);
            tile.add(points, 2, 2);
            tile.setBackground(new Background(new BackgroundFill(Color.LIGHTYELLOW, CornerRadii.EMPTY, Insets.EMPTY)));
            add(tile, i, 1);
        }
    }

}
