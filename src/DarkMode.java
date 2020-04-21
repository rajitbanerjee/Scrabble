import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class DarkMode implements BotAPI {

    private static final int ALPHABET_SIZE = 26;
    private final PlayerAPI me;
    private final OpponentAPI opponent;
    private final BoardAPI board;
    private final UserInterfaceAPI info;
    private final DictionaryAPI dictionary;
    private final GADDAG tree;
    private int turnCount;

    // NOTE: Don't need a MockBot for testing, two DarkMode objects can play each other
    DarkMode(PlayerAPI me, OpponentAPI opponent, BoardAPI board,
             UserInterfaceAPI ui, DictionaryAPI dictionary) {
        this.me = me;
        this.opponent = opponent;
        this.board = board;
        this.info = ui;
        this.dictionary = dictionary;
        turnCount = 0;
        tree = new GADDAG(null);
        tree.build();
        System.out.println(tree.getPossibleWords("AUTO", "ING"));
    }

    /**
     * Makes a decision regarding the move to be played.
     *
     * @return the DarkMode bot's move as a String
     */
    @Override
    public String getCommand() {
        String command;
        if (turnCount == 0) {
            if (me.getPrintableId() == 1) {
                command = "NAME DarkMode1";
            } else {
                command = "NAME DarkMode2";
            }
        } else if (!getFrameLetters().contains("A") || !getFrameLetters().contains("N")) {
            System.out.println(getFrameLetters());
            command = "EXCHANGE " + getFrameLetters();
        } else {
            command = "H8 A AN";
            System.out.println(getAnchorSquares(true));
        }
        turnCount++;
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
    // GADDAG algorithm -------------------------------------------------------------

    // TODO: workaround for Word methods, getNewLetters(), prepend()
    // TODO: goOn(), gen(), helper methods

    // Checks if the old arc contains the given letter and if that branch leads to end of word
    public boolean isOn(GADDAG prevArc, Character letter) {
        if (!prevArc.hasPathFrom(letter)) {
            return false;
        }
        if (prevArc.getSubTree(letter).isEndOfWord()) {
            return true;
        } else if (prevArc.getSubTree(letter).hasPathFrom('+')) {
            return prevArc.getSubTree(letter).getSubTree('+').isEndOfWord();
        }
        return false;
    }

    // Returns the letter at the given spot depending on the current anchor
    private Character getLetterOnSpot(boolean isHorizontal, Coordinate anchor, int pos) {
        if (isHorizontal) {
            return getCharAtIndex(anchor.getRow(), anchor.getColumn() + pos);
        } else {
            return getCharAtIndex(anchor.getRow() + pos, anchor.getColumn());
        }
    }

    // Searching the board -------------------------------------------------------------

    private boolean isValidIndex(int row, int column) {
        return row >= 0 && row < Board.BOARD_SIZE && column >= 0 && column < Board.BOARD_SIZE;
    }

    private boolean isEmpty(int row, int column) {
        return !board.getSquareCopy(row, column).isOccupied();
    }

    // Generates a list of all of the anchor squares needed for the current direction
    private ArrayList<Coordinate> getAnchorSquares(boolean isHorizontal) {
        ArrayList<Coordinate> anchors = new ArrayList<>();
        for (int i = 0; i < Board.BOARD_SIZE; i++) {
            boolean found = false;
            for (int j = 0; j < Board.BOARD_SIZE; j++) {
                // Horizontal anchor search
                if (isHorizontal) {
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
                    // Vertical anchors search
                    if (isEmpty(i, j)) {
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

    // Get the horizontal word if it exists, else ""
    private String getHorizontalWord(int row, int column) {
        // return empty and stop search if square is not occupied or out of bounds
        if (!isValidIndex(row, column) || isEmpty(row, column)) {
            return "";
        }
        StringBuilder word = new StringBuilder();
        // Find end and append character
        int end = column;
        while (isValidIndex(row, end) && !isEmpty(row, end)) {
            word.append(getCharAtIndex(row, end));
            end++;
        }
        // Do not include the first character
        int start = column - 1;
        while (isValidIndex(row, start) && !isEmpty(row, start)) {
            word.insert(0, start);
            start--;
        }
        // No single letter words
        if (word.length() == 1) {
            return "";
        }
        return word.toString();
    }

    // Get the vertical word if it exists, returns an empty string otherwise
    private String getVerticalWord(int row, int column) {
        // Return empty and stop search if square is not occupied or out of bounds
        if (!isValidIndex(row, column) || isEmpty(row, column)) {
            return "";
        }
        StringBuilder word = new StringBuilder();
        // Find end and append character
        int end = row;
        while (isValidIndex(end, column) && !isEmpty(end, column)) {
            word.append(getCharAtIndex(end, column));
            end++;
        }
        // Do not include the starting character
        int start = row - 1;
        while (isValidIndex(start, column) && !isEmpty(start, column)) {
            word.append(getCharAtIndex(start, column));
            start--;
        }
        // No single letter words
        if (word.length() == 1) {
            return "";
        }
        return word.toString();
    }

    // Return character at a given board position (_ for empty, + for out of bounds)
    private char getCharAtIndex(int row, int column) {
        if (!isValidIndex(row, column)) {
            return '+';
        }
        if (isEmpty(row, column)) {
            return '_';
        }
        return board.getSquareCopy(row, column).getTile().getLetter();
    }

    // Checks if the given index has an adjacent tile.
    private boolean hasNeighbour(int row, int column) {
        if (isValidIndex(row - 1, column) && !isEmpty(row - 1, column)) {
            return true;
        }
        if (isValidIndex(row + 1, column) && !isEmpty(row + 1, column)) {
            return true;
        }
        if (isValidIndex(row, column - 1) && !isEmpty(row, column - 1)) {
            return true;
        }
        return isValidIndex(row, column + 1) && !isEmpty(row, column + 1);
    }

    // Returns a String representation of the highest scoring word placement available
    private String bestWordPlacement(ArrayList<String> words) {
        String bestWord = null;
        int bestScore = 0;
        int score;
        for (String word : words) {
            score = 0;
            word = word.toUpperCase();
            char[] letters = word.toCharArray();
            for (char ch : letters) {  //calculate value of word
                Tile t = new Tile(ch);
                score += t.getValue();
            }
            if (score > bestScore) {  //update bestWord
                bestWord = word;
                bestScore = score;
            }
        }
        if (bestWord == null) {
            //the Arraylist is empty. The bot hasn't found any word placements
            throw new IllegalStateException("No words formed.");
        }
        return bestWord;
    }

    // Nested class for coordinates
    private static class Coordinate {

        private final int row;
        private final int column;

        Coordinate(int row, int column) {
            this.row = row;
            this.column = column;
        }

        int getColumn() {
            return column;
        }

        int getRow() {
            return row;
        }

        @Override
        public String toString() {
            return "(" + row + ", " + column + ")";
        }
    }

    // Build a GADDAG with dictionary words -----------------------------------------------------------------
    private static class GADDAG {

        private final Character letter;
        private final ArrayList<GADDAG> children;
        private boolean isEndOfWord;

        GADDAG(Character letter) {
            this.letter = letter;
            children = new ArrayList<>();
            isEndOfWord = false;
        }

        // Inserts words from dictionary into the GADDAG
        void build() {
            try {
                Scanner sc = new Scanner(new File("csw.txt"));
                long startTime = System.currentTimeMillis();
                while (sc.hasNextLine()) {
                    String word = sc.nextLine();
                    insert(word);
                }
                sc.close();
                System.out.println("GADDAG build time: " +
                        (System.currentTimeMillis() - startTime) / 1000);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        // GADDAG search methods to be used in move generation ----------------------------------------------

        // Check if a word is valid (present in the dictionary)
        boolean inDictionary(String word) {
            if (word.equals("")) {
                return false;
            } else {
                return find(word.charAt(0) + "+" + word.substring(1), this);
            }
        }

        // Get a list of possible words starting with a prefix and ending with a suffix
        ArrayList<String> getPossibleWords(String prefix, String suffix) {
            Set<String> prefixWords = new HashSet<>(getWordsStartingWith(prefix));
            Set<String> suffixWords = new HashSet<>(getWordsEndingWith(suffix));
            prefixWords.retainAll(suffixWords);
            return new ArrayList<>(prefixWords);
        }

        ArrayList<String> getWordsStartingWith(String prefix) {
            return getWordsStartingWith(prefix, this);
        }

        ArrayList<String> getWordsEndingWith(String suffix) {
            return getWordsEndingWith(suffix, this);
        }

        // GADDAG helper utilities ------------------------------------------------------------------

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

        private boolean find(String str, GADDAG tree) {
            if (str.length() == 0 && tree.isEndOfWord) {
                return true;
            } else if (str.length() > 0 && tree.hasPathFrom(str.charAt(0))) {
                return find(str.substring(1), tree.getSubTree(str.charAt(0)));
            } else {
                return false;
            }
        }

        // Helper to get a list of words starting with a prefix
        ArrayList<String> getWordsStartingWith(String prefix, GADDAG tree) {
            ArrayList<String> words = new ArrayList<>();
            if (tree.letter == null && prefix.equals("")) {
                for (GADDAG child : tree.children) {
                    for (String str : getWordsStartingWith("", child)) {
                        words.add(child.letter + str);
                    }
                }
            } else if (tree.letter == null) {
                for (String suffix : getWordsStartingWith(prefix.substring(0, prefix.length() - 1),
                        tree.getSubTree(prefix.charAt(prefix.length() - 1)))) {
                    words.add(prefix + suffix);
                }
            } else if (prefix.equals("")) {
                if (tree.hasPathFrom('+')) {
                    for (String str : getAllStrings(tree.getSubTree('+'))) {
                        words.add(str.substring(1)); // Substring removes the '+'
                    }
                }
            } else if (tree.hasPathFrom(prefix.charAt(prefix.length() - 1))) {
                words = getWordsStartingWith(prefix.substring(0, prefix.length() - 1),
                        tree.getSubTree(prefix.charAt(prefix.length() - 1)));
            }
            return words;
        }

        // Helper to get a list of  words ending with a given suffix
        ArrayList<String> getWordsEndingWith(String suffix, GADDAG tree) {
            ArrayList<String> wordList = new ArrayList<>();
            if (tree.letter == null && suffix.equals("")) {
                return wordList;
            } else if (tree.letter == null) {
                for (String prefix : getWordsEndingWith(suffix.substring(0, suffix.length() - 1),
                        tree.getSubTree(suffix.charAt(suffix.length() - 1)))) {
                    wordList.add(prefix + suffix);
                }
                if (inDictionary(suffix)) {
                    wordList.add(suffix);
                }
            } else if (suffix.equals("")) {
                for (GADDAG child : tree.children) {
                    for (String str : getAllStrings(child)) {
                        StringBuilder word = new StringBuilder(str.substring(0, str.indexOf('+')));
                        word.reverse();
                        if (str.indexOf('+') != str.length() - 1) {
                            word.append(str.substring(str.indexOf('+') + 1));
                        }
                        if (str.endsWith("+")) wordList.add(word.toString());
                    }
                }
            } else if (tree.hasPathFrom(suffix.charAt(suffix.length() - 1))) {
                wordList = getWordsEndingWith(suffix.substring(0, suffix.length() - 1),
                        tree.getSubTree(suffix.charAt(suffix.length() - 1)));
            }
            return wordList;
        }

        // Helper to get a list of complete Strings in the GADDAG, rev(x)+y format
        private ArrayList<String> getAllStrings(GADDAG tree) {
            ArrayList<String> list = new ArrayList<>();
            if (tree.isEndOfWord) {
                list.add(tree.letter + "");
            }
            for (GADDAG child : tree.children) {
                for (String str : getAllStrings(child)) {
                    if (tree.letter != null) {
                        list.add(tree.letter + "" + str);
                    } else {
                        list.add(str);
                    }
                }
            }
            return list;
        }

        // Checks if this level of the GADDAG has a path starting from given letter
        private boolean hasPathFrom(Character letter) {
            for (GADDAG tree : children) {
                if (letter == tree.letter) {
                    return true;
                }
            }
            return false;
        }

        // Returns the sub-tree at this level starting from given letter
        private GADDAG getSubTree(Character letter) {
            for (GADDAG tree : children) {
                if (letter == tree.letter) {
                    return tree;
                }
            }
            return null;
        }

        private boolean isEndOfWord() {
            return isEndOfWord;
        }
    }

}