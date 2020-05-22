package bot;

import game.Player;
import logic.Scrabble;

/**
 * DarkMode is a Scrabble playing bot.
 *
 * @author Rajit Banerjee, 18202817
 * @author Tee Chee Guan, 18202044
 * @author Katarina Cvetkovic, 18347921
 * Team 15: DarkMode
 */
public class DarkMode {
    static Player player;

    public static void setPlayer(Player player) {
        DarkMode.player = player;
    }

    public static String getCommand() {
        Scrabble.printToOutput("Bot's frame: " + player.getFrame().getFrame().toString());
        return "PASS";
    }

}