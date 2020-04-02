package ui;

import game.Frame;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

/**
 * Frame GUI design.
 *
 * @author Katarina Cvetkovic, 18347921
 * @author Tee Chee Guan, 18202044
 * @author Rajit Banerjee, 18202817
 * Team 15: DarkMode
 */
public class FrameView extends GridPane {
    public FrameView() {
        super();
        setId("frame-view");
    }

    /**
     * Removes the FrameView.
     */
    public void remove() {
        getChildren().clear();
    }

    /**
     * Updates the frame being displayed.
     *
     * @param frame the Frame to be displayed (frame currently in use)
     */
    public void update(Frame frame) {
        remove();
        for (int i = 0; i < frame.getFrame().size(); i++) {
            Text text = new Text(frame.getTile(i).toString());
            Text points = new Text(Integer.toString((frame.getTile(i).getPoints())));
            points.setId("tile-points");
            TileView tile = new TileView(text, points, true);
            add(tile, i, 1);
        }
    }

}