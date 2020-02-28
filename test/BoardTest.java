import constants.Constants;
import game.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Board class tests.
 *
 * @author Rajit Banerjee, 18202817
 * @author Tee Chee Guan, 18202044
 * @author Katarina Cvetkovic, 18347921
 * @team DarkMode
 */

class BoardTest {
    private static final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private static final PrintStream originalOut = System.out;
    private ArrayList<Tile> f; // Stores a copy of the frame for resetting purposes
    private Board board;
    private Frame frame;

    // Helper function to reset frame, and board if required
    private void resetFrame(String letters, boolean resetBoard) {
        if (resetBoard) {
            board.reset();
        }
        for (char ch : letters.toCharArray()) {
            f.add(Tile.makeTile(ch));
        }
        frame.setFrame(f);
    }

    @BeforeEach
    void setUp() {
        board = new Board();
        frame = new Frame(new Pool());
        f = new ArrayList<>();
    }

    @Test
    void testGetBoard() {
        assertNotEquals(null, board.getBoard());
    }

    @Test
    void testBoardDimensions() {
        Square[][] b = board.getBoard();
        assertEquals(15, b.length);
        assertEquals(15, b[0].length);
    }

    @Test
    void testConstructor() {
        Square[][] b = board.getBoard();

        // Check if multipliers are set correctly
        for (int[] index : Constants.DOUBLE_LS_ARRAY) {
            assertEquals(Constants.MULTIPLIER.DOUBLE_LS, b[index[0]][index[1]].getMultiplier());
        }
        for (int[] index : Constants.TRIPLE_LS_ARRAY) {
            assertEquals(Constants.MULTIPLIER.TRIPLE_LS, b[index[0]][index[1]].getMultiplier());
        }
        for (int[] index : Constants.DOUBLE_WS_ARRAY) {
            assertEquals(Constants.MULTIPLIER.DOUBLE_WS, b[index[0]][index[1]].getMultiplier());
        }
        for (int[] index : Constants.TRIPLE_WS_ARRAY) {
            assertEquals(Constants.MULTIPLIER.TRIPLE_WS, b[index[0]][index[1]].getMultiplier());
        }
        for (int[] index : Constants.NORMAL_SQ_ARRAY) {
            assertEquals(Constants.MULTIPLIER.NORMAL, b[index[0]][index[1]].getMultiplier());
        }
        int centre = Constants.BOARD_SIZE / 2;
        assertEquals(Constants.MULTIPLIER.CENTRE, b[centre][centre].getMultiplier());
    }

    @Test
    void testReset() {
        Square[][] b = board.getBoard();
        for (int i = 0; i < Constants.BOARD_SIZE; i++) {
            for (int j = 0; j < Constants.BOARD_SIZE; j++) {
                b[i][j].setTile(Tile.makeTile('Z'));
            }
        }
        board.reset();
        b = board.getBoard();
        for (int i = 0; i < Constants.BOARD_SIZE; i++) {
            for (int j = 0; j < Constants.BOARD_SIZE; j++) {
                if (b[i][j].getTile() != null) {
                    fail("reset() doesn't work as expected");
                }
            }
        }
    }

    @Test
    void testGetTile() {
        Square[][] b = board.getBoard();
        Tile tile = Tile.makeTile('Z');
        for (char column = 'A'; column <= 'O'; column++) {
            for (int row = 1; row <= Constants.BOARD_SIZE; row++) {
                tile.setType((char) ((int) (Math.random() * 26) + 'A'));
                b[row - 1][column - 'A'].setTile(tile);
                assertEquals(tile, board.getTile(column, row));
            }
        }
        // Test if exception is thrown for square out of bounds
        try {
            board.getTile('A', 16);
            board.getTile('P', 14);
            board.getTile('B', 0);
            fail("Queried tile out of bounds.");
        } catch (Exception ignored) {
            // Test passed
        }
    }

    @Test
    void testPlaceTile() {
        Square[][] b = board.getBoard();
        Tile tile = Tile.makeTile('Z');
        for (char column = 'A'; column <= 'O'; column++) {
            for (int row = 1; row <= Constants.BOARD_SIZE; row++) {
                tile.setType((char) ((int) (Math.random() * 26) + 'A'));
                board.placeTile(column, row, tile);
                assertEquals(tile, b[row - 1][column - 'A'].getTile());
            }
        }
    }

    @Test
    void testAreMoveParametersValid() {
        resetFrame("HELLO", false);
        // Check if word is placed on a valid square
        assertTrue(board.isWordLegal(new Word("Hello", 'H', 8, 'A'), frame));
        assertFalse(board.isWordLegal(new Word("HELLO", 'R', 7, 'A'), frame));
        assertFalse(board.isWordLegal(new Word("HELLO", (char) ('A' - 1), 7, 'A'), frame));
        assertFalse(board.isWordLegal(new Word("Hello", 'H', -1, 'A'), frame));
        assertFalse(board.isWordLegal(new Word("Hello", 'H', 16, 'A'), frame));

        // Check if appropriate orientation is provided A (Across)/D (Down)
        assertTrue(board.isWordLegal(new Word("Hello", 'H', 8, 'A'), frame));
        assertTrue(board.isWordLegal(new Word("Hello", 'H', 8, 'D'), frame));
        assertTrue(board.isWordLegal(new Word("Hello", 'H', 8, 'a'), frame));
        assertTrue(board.isWordLegal(new Word("Hello", 'H', 8, 'd'), frame));
        assertFalse(board.isWordLegal(new Word("Hello", 'H', 8, 'r'), frame));
        assertFalse(board.isWordLegal(new Word("Hello", 'H', 8, 'q'), frame));

        // Check if word to be placed is too short
        assertFalse(board.isWordLegal(new Word("H", 'H', 8, 'A'), frame));
        assertTrue(board.isWordLegal(new Word("He", 'H', 8, 'A'), frame));

        // Check if word contains only alphabetical characters
        assertFalse(board.isWordLegal(new Word("123", 'H', 8, 'A'), frame));
        assertFalse(board.isWordLegal(new Word("AB?", 'H', 8, 'A'), frame));
        assertFalse(board.isWordLegal(new Word("----", 'H', 8, 'A'), frame));
        assertTrue(board.isWordLegal(new Word("Hello", 'H', 8, 'A'), frame));

        // Check that appropriate measures are taken for a null frame object
        assertFalse(board.isWordLegal(new Word("123", 'H', 8, 'A'), null));
    }

    @Test
    void testIsOverflowed() {
        resetFrame("ABCD", false);
        // Check if word placement overflows out of the board
        // Place tile at edge of board ('O', 8)
        board.placeTile('O', 8, Tile.makeTile('X'));
        // Place tile at edge of board ('H', 15)
        board.placeTile('H', Constants.BOARD_SIZE, Tile.makeTile('X'));

        // Sets firstMove to false for debugging purposes
        board.setFirstMove(false);
        assertTrue(board.isWordLegal(new Word("ABCDX", 'K', 8, 'A'), frame));
        assertTrue(board.isWordLegal(new Word("ABCDX", 'H', 11, 'D'), frame));
        assertFalse(board.isWordLegal(new Word("ABCD", 'O', 9, 'A'), frame));
        assertFalse(board.isWordLegal(new Word("ABCD", 'N', 6, 'A'), frame));
        assertFalse(board.isWordLegal(new Word("ABCD", 'I', 15, 'D'), frame));
        assertFalse(board.isWordLegal(new Word("ABCD", 'G', 13, 'D'), frame));
    }

    @Test
    void testDoesWordConflict() {
        resetFrame("ABCD", true);
        // Check for word placement conflict with existing words
        // Places a test Tile in the centre of the board ('H', 8)
        board.placeTile('H', 8, Tile.makeTile('X'));
        // Sets firstMove to false for debugging purposes
        board.setFirstMove(false);
        // Word does not conflict with existing words
        assertTrue(board.isWordLegal(new Word("XBCD", 'H', 8, 'A'), frame));
        assertTrue(board.isWordLegal(new Word("AXCD", 'G', 8, 'A'), frame));
        assertTrue(board.isWordLegal(new Word("XBCD", 'H', 8, 'D'), frame));
        assertTrue(board.isWordLegal(new Word("AXCD", 'H', 7, 'D'), frame));
        // Word conflicts with existing words
        assertFalse(board.isWordLegal(new Word("ABCD", 'H', 8, 'A'), frame));
        assertFalse(board.isWordLegal(new Word("ABCD", 'F', 8, 'A'), frame));
        assertFalse(board.isWordLegal(new Word("ABCD", 'H', 8, 'D'), frame));
        assertFalse(board.isWordLegal(new Word("ABCD", 'H', 6, 'D'), frame));
        assertFalse(board.isWordLegal(new Word("ABCD", 'H', 9, 'D'), frame));
        assertFalse(board.isWordLegal(new Word("ABCD", 'H', 5, 'D'), frame));
        assertFalse(board.isWordLegal(new Word("ABCD", 'I', 8, 'A'), frame));
        assertFalse(board.isWordLegal(new Word("ABCD", 'D', 8, 'A'), frame));
    }

    @Test
    void testDoesFrameContainTiles() {
        resetFrame("H-LL", true);
        // Check if frame contains necessary tiles for word placement (ignores filled squares)
        assertTrue(board.isWordLegal(new Word("He", 'H', 8, 'A'), frame));
        assertTrue(board.isWordLegal(new Word("lleH", 'H', 8, 'A'), frame));
        assertFalse(board.isWordLegal(new Word("Hello", 'H', 8, 'A'), frame));
        assertTrue(board.isWordLegal(new Word("He", 'H', 8, 'A'), frame));
        assertTrue(board.isWordLegal(new Word("lleH", 'H', 8, 'D'), frame));
        assertFalse(board.isWordLegal(new Word("Hello", 'H', 8, 'd'), frame));
    }

    @Test
    void testIsFrameUsed() {
        resetFrame("-CDE-", true);
        // At least one tile from the frame is used for a word placement
        board.setFirstMove(false);

        // Places a tile at  ('H', 8)
        board.placeTile('H', 8, Tile.makeTile('A'));
        // Places a tile at  ('I', 8)
        board.placeTile('I', 8, Tile.makeTile('B'));
        assertTrue(board.isWordLegal(new Word("ABC", 'H', 8, 'A'), frame));
        assertTrue(board.isWordLegal(new Word("ABX", 'H', 8, 'A'), frame));
        assertFalse(board.isWordLegal(new Word("AB", 'H', 8, 'A'), frame));
        assertTrue(board.isWordLegal(new Word("AB", 'H', 8, 'D'), frame));
        assertTrue(board.isWordLegal(new Word("XAX", 'H', 7, 'D'), frame));
        assertTrue(board.isWordLegal(new Word("XYA", 'H', 6, 'D'), frame));
        assertTrue(board.isWordLegal(new Word("LGAB", 'F', 8, 'A'), frame));
        frame.remove('-');
        frame.remove('-');
        // Test usage of other letters when empty Tile not present
        assertFalse(board.isWordLegal(new Word("ABX", 'H', 8, 'A'), frame));

    }

    @Test
    void testDoesWordCoverCentre() {
        resetFrame("-CDE-", true);
        // Check that first move covers the centre square
        board.setFirstMove(true);

        // Horizontal placement
        assertTrue(board.isWordLegal(new Word("CDE", 'H', 8, 'A'), frame));
        assertFalse(board.isWordLegal(new Word("CDE", 'I', 8, 'A'), frame));
        assertFalse(board.isWordLegal(new Word("CDE", 'A', 8, 'A'), frame));
        // Vertical placement
        assertTrue(board.isWordLegal(new Word("CDE", 'H', 8, 'D'), frame));
        assertTrue(board.isWordLegal(new Word("CDE", 'H', 6, 'D'), frame));
        assertFalse(board.isWordLegal(new Word("CDE", 'H', 5, 'D'), frame));
        assertFalse(board.isWordLegal(new Word("CDE", 'H', 9, 'D'), frame));
    }

    @Test
    void testIsWordJoined() {
        resetFrame("-CDE-", true);
        // If not first move, word connects with at least one letter on the board
        board.setFirstMove(false);

        // Places a tile at  ('H', 8)
        board.placeTile('H', 8, Tile.makeTile('X'));
        // Try top vertical connection
        assertTrue(board.isWordLegal(new Word("CDEX", 'H', 5, 'D'), frame));
        // Try top horizontal connection
        assertTrue(board.isWordLegal(new Word("CDE", 'G', 7, 'A'), frame));
        // Try left vertical connection
        assertTrue(board.isWordLegal(new Word("CDE", 'G', 7, 'D'), frame));
        // Try left horizontal connection
        assertTrue(board.isWordLegal(new Word("CDEX", 'E', 8, 'A'), frame));
        // Try bottom vertical connection
        assertTrue(board.isWordLegal(new Word("XCDE", 'H', 8, 'D'), frame));
        // Try bottom horizontal connection
        assertTrue(board.isWordLegal(new Word("CDE", 'G', 9, 'A'), frame));
        // Try right vertical connection
        assertTrue(board.isWordLegal(new Word("CDE", 'I', 7, 'D'), frame));
        // Try right horizontal connection
        assertTrue(board.isWordLegal(new Word("XCDE", 'H', 8, 'A'), frame));
        // Try to place unconnected word
        assertFalse(board.isWordLegal(new Word("CDE", 'J', 8, 'A'), frame));
    }

    @Test
    void testWordExtensions() {
        resetFrame("HeLLo", false);
        // Tests extension of words in both directions
        board.setFirstMove(false);

        // Places a test Tile in the centre of the board ('H', 8)
        board.placeTile('H', 8, Tile.makeTile('X'));
        assertTrue(board.isWordLegal(new Word("XlleH", 'H', 8, 'D'), frame));
        assertTrue(board.isWordLegal(new Word("HXllo", 'H', 7, 'D'), frame));
        assertTrue(board.isWordLegal(new Word("XlleH", 'H', 8, 'A'), frame));
        assertTrue(board.isWordLegal(new Word("HXllo", 'G', 8, 'A'), frame));
        assertTrue(board.isWordLegal(new Word("HeX", 'F', 8, 'A'), frame));

        board.placeTile('I', 13, Tile.makeTile('X'));
        assertTrue(board.isWordLegal(new Word("HeX", 'G', 13, 'A'), frame));
        assertTrue(board.isWordLegal(new Word("HeXllo", 'G', 13, 'A'), frame));
    }

    @Test
    void testFirstMovePlaceWord() {
        String word = "HELLO";
        resetFrame("HELLO", false);
        // Testing first word placement
        // Place word horizontally on H8
        board.placeWord(new Word("Hello", 'H', 8, 'A'), frame);
        for (int i = 0; i < 5; i++) {
            assertEquals(Tile.makeTile(word.charAt(i)), board.getBoard()[7]['H' - 'A' + i].getTile());
        }

        resetFrame("HELLO", true);
        // Place word vertically on H8
        board.placeWord(new Word("Hello", 'H', 8, 'D'), frame);
        for (int i = 0; i < 5; i++) {
            assertEquals(Tile.makeTile(word.charAt(i)), board.getBoard()[7 + i]['H' - 'A'].getTile());
        }

        resetFrame("HELLO", true);
        // Place word horizontally going through H8
        board.placeWord(new Word("Hello", 'F', 8, 'A'), frame);
        for (int i = 0; i < 5; i++) {
            assertEquals(Tile.makeTile(word.charAt(i)), board.getBoard()[7]['F' - 'A' + i].getTile());
        }

        resetFrame("HELLO", true);
        // Place word vertically going through H8
        board.placeWord(new Word("Hello", 'H', 6, 'D'), frame);
        for (int i = 0; i < 5; i++) {
            assertEquals(Tile.makeTile(word.charAt(i)), board.getBoard()[5 + i]['H' - 'A'].getTile());
        }
    }

    @Test
    void testPlaceWordBlankTiles() {
        String word = "HELLO";
        resetFrame("H-LL-", true);
        // Use blank tiles for word going down
        board.placeWord(new Word(word, 'H', 8, 'D'), frame);
        for (int i = 0; i < 5; i++) {
            assertEquals(word.charAt(i), board.getBoard()[7 + i]['H' - 'A'].getTile().getType());
        }
        resetFrame("H-LL-", true);
        // Use blank tiles for words going across
        board.placeWord(new Word(word, 'H', 8, 'A'), frame);
        for (int i = 0; i < 5; i++) {
            assertEquals(word.charAt(i), board.getBoard()[7]['H' - 'A' + i].getTile().getType());
        }
    }

    @Test
    void testInvalidWordPlacement() {
        resetFrame("HELLO", false);
        //Illegal first word placements
        try {
            // Place first word horizontally on H9 (doesn't go through centre square)
            board.placeWord(new Word("Hello", 'H', 9, 'A'), frame);
        } catch (IllegalArgumentException e) {
            for (int i = 0; i < 5; i++) {   //check the word was not placed
                assertNull(board.getBoard()[8]['H' - 'A' + i].getTile());
            }
        }
        try {
            // Place first word word vertically on F5 (doesn't go through centre square)
            board.placeWord(new Word("Hello", 'F', 5, 'D'), frame);
        } catch (IllegalArgumentException e) {
            for (int i = 0; i < 5; i++) {   //check the word was not placed
                assertNull(board.getBoard()[4 + i]['F' - 'A'].getTile());
            }
        }
    }

    @Test
    void testBoardDisplay() {
        System.setOut(new PrintStream(outContent));
        String expected = "----------------------------------------------------------------------------\n" +
                "|\t| A | B | C | D | E | F | G | H | I | J | K | L | M | N | O |\t   |\n" +
                "----------------------------------------------------------------------------\n" +
                "| 1\t|3xW|   |   |2xL|   |   |   |3xW|   |   |   |2xL|   |   |3xW|\t 1 |\n" +
                "----------------------------------------------------------------------------\n" +
                "| 2\t|   |2xW|   |   |   |3xL|   |   |   |3xL|   |   |   |2xW|   |\t 2 |\n" +
                "----------------------------------------------------------------------------\n" +
                "| 3\t|   |   |2xW|   |   |   |2xL|   |2xL|   |   |   |2xW|   |   |\t 3 |\n" +
                "----------------------------------------------------------------------------\n" +
                "| 4\t|2xL|   |   |2xW|   |   |   |2xL|   |   |   |2xW|   |   |2xL|\t 4 |\n" +
                "----------------------------------------------------------------------------\n" +
                "| 5\t|   |   |   |   |2xW|   |   |   |   |   |2xW|   |   |   |   |\t 5 |\n" +
                "----------------------------------------------------------------------------\n" +
                "| 6\t|   |3xL|   |   |   |3xL|   |   |   |3xL|   |   |   |3xL|   |\t 6 |\n" +
                "----------------------------------------------------------------------------\n" +
                "| 7\t|   |   |2xL|   |   |   |2xL|   |2xL|   |   |   |2xL|   |   |\t 7 |\n" +
                "----------------------------------------------------------------------------\n" +
                "| 8\t|3xW|   |   |2xL|   |   |   | * |   |   |   |2xL|   |   |3xW|\t 8 |\n" +
                "----------------------------------------------------------------------------\n" +
                "| 9\t|   |   |2xL|   |   |   |2xL|   |2xL|   |   |   |2xL|   |   |\t 9 |\n" +
                "----------------------------------------------------------------------------\n" +
                "| 10\t|   |3xL|   |   |   |3xL|   |   |   |3xL|   |   |   |3xL|   |\t 10|\n" +
                "----------------------------------------------------------------------------\n" +
                "| 11\t|   |   |   |   |2xW|   |   |   |   |   |2xW|   |   |   |   |\t 11|\n" +
                "----------------------------------------------------------------------------\n" +
                "| 12\t|2xL|   |   |2xW|   |   |   |2xL|   |   |   |2xW|   |   |2xL|\t 12|\n" +
                "----------------------------------------------------------------------------\n" +
                "| 13\t|   |   |2xW|   |   |   |2xL|   |2xL|   |   |   |2xW|   |   |\t 13|\n" +
                "----------------------------------------------------------------------------\n" +
                "| 14\t|   |2xW|   |   |   |3xL|   |   |   |3xL|   |   |   |2xW|   |\t 14|\n" +
                "----------------------------------------------------------------------------\n" +
                "| 15\t|3xW|   |   |2xL|   |   |   |3xW|   |   |   |2xL|   |   |3xW|\t 15|\n" +
                "----------------------------------------------------------------------------\n" +
                "|\t| A | B | C | D | E | F | G | H | I | J | K | L | M | N | O |\t   |\n" +
                "----------------------------------------------------------------------------";
        board.reset();
        board.display();
        // Change expected line endings from LF to CRLF before assertion
        assertEquals(expected.replaceAll("\n", "\r\n"), outContent.toString().strip());
        System.setOut(originalOut);
    }

}
