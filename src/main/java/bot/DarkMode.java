package bot;

import game.*;
import logic.Scoring;
import logic.Scrabble;

import java.util.List;

/**
 * DarkMode is a Scrabble playing bot.
 * // TODO javadoc comments
 *
 * @author Rajit Banerjee, 18202817
 * @author Tee Chee Guan, 18202044
 * @author Katarina Cvetkovic, 18347921
 * Team 15: DarkMode
 */
public class DarkMode {
    private static Player player;
    private static Board board;

    public static void setPlayer(Player player) {
        DarkMode.player = player;
    }

    public static String getCommand(Board board) {
        DarkMode.board = board;
        Scrabble.printToOutput("Bot's frame: " + player.getFrame().getFrame().toString());
        return getBestMove();
    }

    public static Board getBoard() {
        return board;
    }

    public static Frame getFrame() {
        return player.getFrame();
    }

    // Gets a String of only the contents of the bot's frame
    public static String getFrameLetters() {
        StringBuilder letters = new StringBuilder();
        for (Tile t : player.getFrame().getFrame()) {
            char ch = t.getType();
            if (Character.isLetter(ch) || ch == '_') {
                letters.append(ch);
            }
        }
        return letters.toString();
    }

    // Returns a String representation of the highest scoring word placement available
    private static String getBestMove() {
        board.storeHorizontalWords();
        board.storeVerticalWords();
        List<Word> moves = MoveGenerator.getAllMoves();
        if (moves.size() == 0) {
            String frame = getFrameLetters();
            // Exchange some tiles at random if no suitable moves are found
            int i1 = (int) (Math.random() * frame.length());
            int i2 = (int) (Math.random() * frame.length());
            if (i1 == i2) {
                return "EXCHANGE " + frame.charAt(0);
            } else {
                return "EXCHANGE " + frame.substring(Math.min(i1, i2), Math.max(i1, i2));
            }
        } else {
            Word best = moves.get(0);
            int bestScore = Scoring.calculateScore(best, board);
            for (Word move : moves) {
                int score = Scoring.calculateScore(move, board);
                if (score > bestScore) {
                    best = move;
                    bestScore = score;
                }
            }
            return best.toString();
        }
    }

}