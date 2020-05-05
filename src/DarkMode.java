import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;

/**
 * Bot to play the game of Scrabble.
 *
 * @author Rajit Banerjee, 18202817
 * @author Tee Chee Guan, 18202044
 * @author Katarina Cvetkovic, 18347921
 * Team 15: DarkMode
 */

public class DarkMode implements BotAPI {

    private final PlayerAPI me;
    private final BoardAPI board;
    private final HashMap<Coordinate, String> horizontalWords;
    private final HashMap<Coordinate, String> verticalWords;
    private final HashSet<Word> moves;
    private final GADDAG tree;
    private Coordinate anchor;
    private boolean horizontalSearch;
    private boolean firstPlay;

    DarkMode(PlayerAPI me, OpponentAPI opponent, BoardAPI board,
         UserInterfaceAPI ui, DictionaryAPI dictionary) {
        this.me = me;
        this.board = board;
        horizontalWords = new HashMap<>();
        verticalWords = new HashMap<>();
        moves = new HashSet<>();
        tree = new GADDAG(null);
        tree.build();
        firstPlay = true;
    }

    /**
     * Makes a decision regarding the move to be played.
     *
     * @return the DarkMode bot's move as a String
     */
    @Override
    public String getCommand() {
        String command;
        if (firstPlay) {
            command = "NAME " + getClass().getSimpleName();
            firstPlay = false;
        } else {
            command = getBestMove();
        }
        return command;
    }

    // Gets a String of only the contents of the bot's frame
    private String getFrameLetters() {
        StringBuilder letters = new StringBuilder();
        for (Character ch : me.getFrameAsString().toCharArray()) {
            if (Character.isLetter(ch) || ch == '_') {
                letters.append(ch);
            }
        }
        return letters.toString();
    }

    // Create the bot's frame object using given frame tiles
    private Frame frame() {
        Frame frame = new Frame();
        ArrayList<Tile> tiles = new ArrayList<>();
        for (Character ch : getFrameLetters().toCharArray()) {
            tiles.add(new Tile(ch));
        }
        frame.addTiles(tiles);
        return frame;
    }

    // Returns a String representation of the highest scoring word placement available
    private String getBestMove() {
        storeExistingBoardWords();
        HashSet<String> formattedMoves = getAllMoves();
        if (formattedMoves.size() == 0) {
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
            String bestPlacement = null;
            int bestScore = 0;
            for (String move : formattedMoves) {
                String letters = move.substring(move.lastIndexOf(' ') + 1);
                int score = 0;
                for (Character ch : letters.toCharArray()) {
                    // Calculation only considers the tile values, without multipliers
                    score += (new Tile(ch)).getValue();
                }
                if (score > bestScore) {
                    bestPlacement = move;
                    bestScore = score;
                }
            }
            return bestPlacement;
        }
    }

    private void storeExistingBoardWords() {
        horizontalWords.clear();
        verticalWords.clear();
        // Horizontal words
        for (int i = 0; i < Board.BOARD_SIZE; i++) {
            int j = 0;
            while (j < Board.BOARD_SIZE) {
                StringBuilder word = new StringBuilder();
                Coordinate start = new Coordinate(i, j);
                while (isValidIndex(i, j) && !isEmpty(i, j)) {
                    word.append(getCharAtIndex(i, j));
                    j++;
                }
                if (!word.toString().equals("")) {
                    for (int k = 0; k < word.length(); k++) {
                        horizontalWords.put(start, word.toString());
                        start = new Coordinate(i, start.getCol() + 1);
                    }
                }
                j++;
            }
        }
        // Vertical words
        for (int j = 0; j < Board.BOARD_SIZE; j++) {
            int i = 0;
            while (i < Board.BOARD_SIZE) {
                StringBuilder word = new StringBuilder();
                Coordinate start = new Coordinate(i, j);
                while (isValidIndex(i, j) && !isEmpty(i, j)) {
                    word.append(getCharAtIndex(i, j));
                    i++;
                }
                if (!word.toString().equals("")) {
                    for (int k = 0; k < word.length(); k++) {
                        verticalWords.put(start, word.toString());
                        start = new Coordinate(start.getRow() + 1, j);
                    }
                }
                i++;
            }
        }
    }

    // Gets all possible moves in the required format
    private HashSet<String> getAllMoves() {
        // Searching horizontally, then vertically, for new possible moves
        getAllMoves(true);
        getAllMoves(false);
        HashSet<String> formatted = new HashSet<>();
        for (Word word : moves) {
            String letters = word.getLetters();
            char orientation = word.isHorizontal() ? 'A' : 'D';
            String index = String.format("%s%d", (char) ('A' + word.getColumn()), word.getRow() + 1);
            formatted.add(String.format("%s %c %s", index, orientation, letters));
        }
        return formatted;
    }

    private void getAllMoves(boolean searchHorizontal) {
        this.horizontalSearch = searchHorizontal;
        ArrayList<Coordinate> anchors = getAnchors();
        for (Coordinate cor : anchors) {
            anchor = cor;
            generateMoves(0, "", getFrameLetters(), tree);
        }
        // Remove move suggestions that are illegal plays
        moves.removeIf(word -> !board.isLegalPlay(frame(), word));
    }

    // Finds all anchor squares (first letter of all words, and also any empty squares before or after)
    private ArrayList<Coordinate> getAnchors() {
        ArrayList<Coordinate> anchors = new ArrayList<>();
        for (int i = 0; i < Board.BOARD_SIZE; i++) {
            boolean found = false;
            for (int j = 0; j < Board.BOARD_SIZE; j++) {
                if (horizontalSearch) {
                    if (isEmpty(i, j)) {
                        found = false;
                        // Check above and below for words
                        if (!getHorizontalWord(i - 1, j).equals("")
                                || !getHorizontalWord(i + 1, j).equals("")) {
                            anchors.add(new Coordinate(i, j));
                        }
                    } else if (!found) {
                        // Square is occupied and first word character not added
                        found = true;
                        anchors.add(new Coordinate(i, j));
                    }
                } else {
                    if (isEmpty(j, i)) {
                        found = false;
                        // Check left and right for words
                        if (!getVerticalWord(j, i - 1).equals("")
                                || !getVerticalWord(j, i + 1).equals("")) {
                            anchors.add(new Coordinate(j, i));
                        }
                    } else if (!found) {
                        // Square is occupied and first word character not added
                        found = true;
                        anchors.add(new Coordinate(j, i));
                    }
                }
            }
        }
        // Add the middle square as the anchor if the board is empty
        if (board.isFirstPlay()) {
            anchors.add(new Coordinate(Board.BOARD_CENTRE, Board.BOARD_CENTRE));
        }
        return anchors;
    }

    // -------- Basic move generation algorithm inspired by Steven A. Gordon's GADDAG algorithm ----------

    // Finds all possible next moves for the bot
    private void generateMoves(int offset, String word, String frame, GADDAG arc) {
        if (!isEmpty(offset) && isValidIndex(offset)) {
            extendWord(offset, getLetter(offset), word, frame, arc.getSubTree(getLetter(offset)), arc);
        } else if (!frame.equals("")) {
            for (Character letter : frame.toCharArray()) {
                if (letter != '_') { // not blank tile
                    if (arc.hasPathFrom(letter) && isValidCrossSet(offset, letter)) {
                        extendWord(offset, letter, word, frame.replaceFirst("" + letter, ""),
                                arc.getSubTree(letter), arc);
                    }
                }
            }
        }
    }

    // Check that invalid words are not created in the cross-set
    private boolean isValidCrossSet(int offset, Character letter) {
        if (board.isFirstPlay()) {
            return true;
        } else {
            String before, after;
            if (horizontalSearch) {
                before = getVerticalWord(anchor.getRow() - 1, anchor.getCol() + offset);
                after = getVerticalWord(anchor.getRow() + 1, anchor.getCol() + offset);
            } else {
                before = getHorizontalWord(anchor.getRow() + offset, anchor.getCol() - 1);
                after = getHorizontalWord(anchor.getRow() + offset, anchor.getCol() + 1);
            }
            // Verify that the new play doesn't form invalid words
            return tree.inDictionary(before + letter + after);
        }
    }

    // Prepend/append the given letter to the word and then records the move if needed
    private void extendWord(int offset, Character letter, String word, String frame,
                            GADDAG newArc, GADDAG oldArc) {
        if (offset <= 0) {
            word = letter + word;
            if (foundMove(oldArc, letter) &&
                    (isEmpty(offset - 1) || !isValidIndex(offset - 1))) {
                // Store the move in main list if a dictionary word can be created
                storeMove(word);
            }
            if (newArc != null) {
                if (isEmpty(offset - 1)) {
                    generateMoves(offset - 1, word, frame, newArc);
                }
                newArc = newArc.getSubTree('+');
                if (newArc != null && isEmpty(offset - 1) && canGrow()) {
                    generateMoves(1, word, frame, newArc);
                }
            }
        } else {
            // Extending the word by appending the letter
            word += letter;
            if (foundMove(oldArc, letter) &&
                    (isEmpty(offset + 1) || !isValidIndex(offset + 1))) {
                // Store the move in main list if a dictionary word can be created
                storeMove(word);
            }
            if (newArc != null && canGrow()) {
                generateMoves(offset + 1, word, frame, newArc);
            }
        }
    }

    // Checks if the old arc contains the given letter and if that branch leads to end of word
    private boolean foundMove(GADDAG oldArc, Character letter) {
        if (!oldArc.hasPathFrom(letter)) {
            return false;
        } else if (oldArc.getSubTree(letter).isEndOfWord()) {
            return true;
        } else if (oldArc.getSubTree(letter).hasPathFrom('+')) {
            return oldArc.getSubTree(letter).getSubTree('+').isEndOfWord();
        } else {
            return false;
        }
    }

    // Checks if the word can be extended towards the right or bottom
    private boolean canGrow() {
        if (horizontalSearch) {
            return isEmpty(anchor.getRow(), Board.BOARD_SIZE - 1);
        } else {
            return isEmpty(Board.BOARD_SIZE - 1, anchor.getCol());
        }
    }

    // Store a possible move in the list of all moves
    private void storeMove(String word) {
        Word move = new Word(anchor.getRow(), anchor.getCol(), horizontalSearch, word);
        if (board.isLegalPlay(frame(), move)) {
            moves.add(move);
        }
    }

    //-------------------- Helper access methods ---------------------------------------

    // Return character at a given board position ('_' for empty, '@' for out of bounds)
    private Character getCharAtIndex(int row, int col) {
        if (!isValidIndex(row, col)) {
            return '@';
        }
        if (isEmpty(row, col)) {
            return '_';
        }
        return board.getSquareCopy(row, col).getTile().getLetter();
    }

    // Returns the letter at the given offset from anchor
    private Character getLetter(int offset) {
        if (horizontalSearch) {
            return getCharAtIndex(anchor.getRow(), anchor.getCol() + offset);
        } else {
            return getCharAtIndex(anchor.getRow() + offset, anchor.getCol());
        }
    }

    private boolean isValidIndex(int row, int col) {
        return row >= 0 && row < Board.BOARD_SIZE &&
                col >= 0 && col < Board.BOARD_SIZE;
    }

    // Checks if index at the given offset from current anchor is valid
    private boolean isValidIndex(int offset) {
        return getLetter(offset) != '@';
    }

    // Checks if board has empty spot at given row and col
    private boolean isEmpty(int row, int col) {
        return !board.getSquareCopy(row, col).isOccupied();
    }

    // Checks if board has empty spot relative to anchor
    private boolean isEmpty(int offset) {
        return getLetter(offset) == '_';
    }

    // Get the horizontal word if it exists, else ""
    private String getHorizontalWord(int row, int col) {
        Coordinate c = new Coordinate(row, col);
        return horizontalWords.getOrDefault(c, "");
    }

    // Get the vertical word if it exists, else ""
    private String getVerticalWord(int row, int col) {
        Coordinate c = new Coordinate(row, col);
        return verticalWords.getOrDefault(c, "");
    }

    // Extend existing Coordinates class to support hashCode
    private static class Coordinate extends Coordinates {

        Coordinate(int row, int col) {
            super(row, col);
        }

        @Override
        public int hashCode() {
            return (10000 * getRow()) + (10 * getCol());
        }
    }

    // ------------------- Build a GADDAG to store dictionary words ------------------------
    private static class GADDAG {

        private final Character letter;
        private final ArrayList<GADDAG> children;
        private boolean isEndOfWord;

        GADDAG(Character letter) {
            this.letter = letter;
            children = new ArrayList<>();
            isEndOfWord = false;
        }

        // Check if a word is valid (present in the dictionary)
        boolean inDictionary(String word) {
            if (word.equals("")) {
                return false;
            } else {
                return find(word.charAt(0) + "+" + word.substring(1), this);
            }
        }

        private boolean find(String s, GADDAG tree) {
            if (s.length() == 0 && tree.isEndOfWord) {
                return true;
            } else if (s.length() > 0 && tree.hasPathFrom(s.charAt(0))) {
                return find(s.substring(1), tree.getSubTree(s.charAt(0)));
            } else {
                return false;
            }
        }

        // Inserts words from dictionary into the GADDAG
        void build() {
            try {
                Scanner sc = new Scanner(new File("csw.txt"));
                while (sc.hasNextLine()) {
                    String word = sc.nextLine();
                    insert(word);
                }
                sc.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        // Converts a word xy to all possible rev(x)+y, and inserts into GADDAG
        private void insert(String word) {
            for (int i = 0; i < word.length(); i++) {
                StringBuilder x = new StringBuilder(word.substring(0, i + 1));
                x.reverse();
                String y = word.substring(i + 1);
                x.append("+").append(y);
                insertFormatted(x.toString());
            }
        }

        // Inserts a given item of the form rev(x)+y into the GADDAG
        private void insertFormatted(String item) {
            if (!item.equals("")) {
                if (!hasPathFrom(item.charAt(0))) {
                    children.add(new GADDAG(item.charAt(0)));
                }
                getSubTree(item.charAt(0)).insertFormatted(item.substring(1));
            } else {
                isEndOfWord = true;
            }
        }

        // Checks if this level of the GADDAG has a path starting from given letter
        boolean hasPathFrom(Character letter) {
            for (GADDAG tree : children) {
                if (letter == tree.letter) {
                    return true;
                }
            }
            return false;
        }

        // Returns the sub-tree at this level starting from given letter
        GADDAG getSubTree(Character letter) {
            for (GADDAG tree : children) {
                if (letter == tree.letter) {
                    return tree;
                }
            }
            return null;
        }

        boolean isEndOfWord() {
            return isEndOfWord;
        }
    }

}