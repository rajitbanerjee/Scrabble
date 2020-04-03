package ui;

import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

/**
 * Filled Tile GUI design.
 *
 * @author Rajit Banerjee, 18202817
 * @author Katarina Cvetkovic, 18347921
 * @author Tee Chee Guan, 18202044
 * Team 15: DarkMode
 */
public class TileView extends GridPane {
    /**
     * Creates a new tile view.
     *
     * @param type    the tile type/letter
     * @param points  the tile points
     * @param inFrame {@code true}, if tile is displayed on the frame
     */
    public TileView(Text type, Text points, boolean inFrame) {
        super();
        if (inFrame) {
            setId("tile-in-frame");
        } else {
            setId("tile-on-board");
        }
        add(type, 1, 1);
        add(points, 2, 2);
    }

}