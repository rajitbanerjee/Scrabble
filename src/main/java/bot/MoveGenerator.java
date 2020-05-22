package bot;

import game.Board;
import game.Index;
import game.Square;
import game.Word;

import java.util.ArrayList;
import java.util.List;

public class MoveGenerator {
    private static final List<Word> moves = new ArrayList<>();
    private static final Board BOARD = DarkMode.getBoard();
    private static final GADDAG TREE = GADDAG.getGADDAG();
    private static Index anchor;
    private static boolean horizontalSearch;

    // Stores and returns all possible moves
    public static List<Word> getAllMoves() {
        System.out.println(TREE.inDictionary("hello")); //true test
        System.out.println(TREE.inDictionary("oxxce")); //false test
        // Searching horizontally, then vertically, for new possible moves
        getAllMoves(true);
        getAllMoves(false);
        return moves;
    }

    private static void getAllMoves(boolean horizontalSearch) {
        MoveGenerator.horizontalSearch = horizontalSearch;
        List<Index> anchors = getAnchors();
        System.out.println("Anchors, " + (horizontalSearch ? "horizontal: " : "vertical: ") + anchors);
        for (Index cor : anchors) {
            anchor = cor;
            generateMoves(0, "", DarkMode.getFrameLetters().toLowerCase(), TREE);
        }
        System.out.println("All moves: " + moves);
        // Remove move suggestions that are illegal plays
        moves.removeIf(word -> !BOARD.isWordLegal(word, DarkMode.getFrame()));
    }

    // Finds all anchor squares (first letter of all words, and also any empty squares before or after)
    private static List<Index> getAnchors() {
        List<Index> anchors = new ArrayList<>();
        for (int i = 0; i < Board.SIZE; i++) {
            boolean found = false;
            for (int j = 0; j < Board.SIZE; j++) {
                if (horizontalSearch) {
                    if (isEmpty(i, j)) {
                        found = false;
                        // Check above and below for words
                        if (!BOARD.getHorizontalWord(i - 1, j).equals("")
                                || !BOARD.getHorizontalWord(i + 1, j).equals("")) {
                            anchors.add(new Index(i, j));
                        }
                    } else if (!found) {
                        // Square is occupied and first word character not added
                        found = true;
                        anchors.add(new Index(i, j));
                    }
                } else {
                    if (isEmpty(j, i)) {
                        found = false;
                        // Check left and right for words
                        if (!BOARD.getVerticalWord(j, i - 1).equals("")
                                || !BOARD.getVerticalWord(j, i + 1).equals("")) {
                            anchors.add(new Index(j, i));
                        }
                    } else if (!found) {
                        // Square is occupied and first word character not added
                        found = true;
                        anchors.add(new Index(j, i));
                    }
                }
            }
        }
        // Add the middle square as the anchor if the board is empty
        if (BOARD.isFirstMove()) {
            anchors.add(new Index(Board.SIZE / 2, Board.SIZE / 2));
        }
        return anchors;
    }

    // -------- Basic move generation algorithm inspired by Steven A. Gordon's GADDAG algorithm ----------

    // Finds all possible next moves for the bot
    private static void generateMoves(int offset, String word, String frame, GADDAG arc) {
        if (isValidIndex(offset) && !isEmpty(offset)) {
            extendWord(offset, getLetter(offset), word, frame, arc.getSubTree(getLetter(offset)), arc);
        } else if (!frame.equals("")) {
            for (Character letter : frame.toCharArray()) {
                if (letter != '_') { // not blank tile
                    if (arc.hasPathFrom(letter) && isValidCrossSet(offset, letter)) {
                        extendWord(offset, letter, word, frame.replaceFirst("" + letter, ""),
                                arc.getSubTree(letter), arc);
                    }
                } else {
                    for (char c = 'a'; c <= 'z'; c++) {
                        if (arc.hasPathFrom(c) && isValidCrossSet(offset, c))
                            extendWord(offset, c, word, frame.replaceFirst("_", ""),
                                    arc.getSubTree(c), arc);
                    }
                }
            }
        }
    }

    // Check that invalid words are not created in the cross-set
    private static boolean isValidCrossSet(int offset, Character letter) {
        if (BOARD.isFirstMove()) {
            return true;
        } else {
            String before, after;
            if (horizontalSearch) {
                before = BOARD.getVerticalWord(anchor.getRow() - 1, anchor.getColumn() + offset);
                after = BOARD.getVerticalWord(anchor.getRow() + 1, anchor.getColumn() + offset);
            } else {
                before = BOARD.getHorizontalWord(anchor.getRow() + offset, anchor.getColumn() - 1);
                after = BOARD.getHorizontalWord(anchor.getRow() + offset, anchor.getColumn() + 1);
            }
            // Verify that the new play doesn't form invalid words
            return TREE.inDictionary(before + letter + after);
        }
    }

    // Prepend/append the given letter to the word and then records the move if needed
    private static void extendWord(int offset, Character letter, String word, String frame,
                                   GADDAG newArc, GADDAG oldArc) {
        if (offset <= 0) {
            word = letter + word;
            if (foundMove(oldArc, letter) &&
                    (!isValidIndex(offset - 1) || isEmpty(offset - 1))) {
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
                    (!isValidIndex(offset + 1) || isEmpty(offset + 1))) {
                // Store the move in main list if a dictionary word can be created
                storeMove(word);
            }
            if (newArc != null && canGrow()) {
                generateMoves(offset + 1, word, frame, newArc);
            }
        }
    }

    // Checks if the old arc contains the given letter and if that branch leads to end of word
    private static boolean foundMove(GADDAG oldArc, Character letter) {
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
    private static boolean canGrow() {
        return isEmpty(getRow(Board.SIZE - 1 - anchor.getRow()),
                getColumn(Board.SIZE - 1 - anchor.getColumn()));
    }

    // Store a possible move in the list of all moves
    private static void storeMove(String letters) {
        char column = (char) ('A' + anchor.getColumn());
        int row = anchor.getRow() + 1;
        char orientation = horizontalSearch ? 'A' : 'D';
        Word word = new Word(letters.toUpperCase(), column, row, orientation);
        if (BOARD.isWordLegal(word, DarkMode.getFrame())) {
            moves.add(word);
        }
    }

    //-------------------- access utilities ---------------------------------------

    // Returns the letter at the given offset from anchor
    private static Character getLetter(int offset) {
        return BOARD.getLetter(getRow(offset), getColumn(offset));
    }

    // Checks if index at the given offset from current anchor is valid
    private static boolean isValidIndex(int offset) {
        return Square.isValid(getRow(offset), getColumn(offset));
    }

    // Checks if board has empty spot at given row and column
    private static boolean isEmpty(int row, int column) {
        return BOARD.getBoard()[row][column].isEmpty();
    }

    // Checks if board has empty spot relative to anchor
    private static boolean isEmpty(int offset) {
        return isEmpty(getRow(offset), getColumn(offset));
    }

    // Gets the anchor row, modified by offset for vertical search
    private static int getRow(int offset) {
        return horizontalSearch ? anchor.getRow() : anchor.getRow() + offset;
    }

    // Gets the anchor column, modified by offset for horizontal search;
    private static int getColumn(int offset) {
        return horizontalSearch ? anchor.getColumn() + offset : anchor.getColumn();
    }

}