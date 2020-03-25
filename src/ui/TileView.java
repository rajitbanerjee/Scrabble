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
    public TileView(Text type, Text points) {
        super();
        setId("square-with-tile");
        add(type, 1, 1);
        add(points, 2, 2);
    }

}