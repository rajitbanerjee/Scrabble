import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class DarkMode implements BotAPI {

    private final PlayerAPI me;
    private final OpponentAPI opponent;
    private final BoardAPI board;
    private final UserInterfaceAPI info;
    private final DictionaryAPI dictionary;
    private final GADDAG tree;
    private final HashSet<Word> moves;
    private Coordinate anchor;
    private boolean searchHorizontal;
    private int turnCount;
    private final HashMap<Coordinate, String> horizontalWords;
    private final HashMap<Coordinate, String> verticalWords;

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
        searchHorizontal = true;
        moves = new HashSet<>();
        horizontalWords = new HashMap<>();
        verticalWords = new HashMap<>();
    }

    /**
     * Makes a decision regarding the move to be played.
     *
     * @return the DarkMode bot's move as a String
     */
    @Override
    public String getCommand() {
        String command;
        generateWordLists();
        if (turnCount == 0) {
            if (me.getPrintableId() == 1) {
                command = "NAME DarkMode1";
            } else {
                command = "NAME DarkMode2";
            }
        } else {
            command = getBestMove();
            System.out.println("Best move: " + command + "\n");
        }
        turnCount++;
        return command;
    }

    private void generateWordLists() {
        horizontalWords.clear();
        verticalWords.clear();

        // Horizontal words.
        for (int i = 0; i < Board.BOARD_SIZE; i++) {
            int j = 0;
            while (j < Board.BOARD_SIZE) {
                StringBuilder word = new StringBuilder();
                Coordinate start = new Coordinate(i, j);
                while (isValidIndex(i, j) && board.getSquareCopy(i, j).isOccupied()) {
                    word.append(getCharAtIndex(i, j));
                    j++;
                }
                if (!word.toString().equals("")) {
                    for (int k = 0; k < word.length(); k++) {
                        horizontalWords.put(start, word.toString());
                        start = new Coordinate(i, start.getColumn() + 1);
                    }
                }
                j++;
            }
        }

        // Vertical words.
        for (int j = 0; j < Board.BOARD_SIZE; j++) {
            int i = 0;
            while (i < Board.BOARD_SIZE) {
                StringBuilder word = new StringBuilder();
                Coordinate start = new Coordinate(i, j);
                while (isValidIndex(i, j) && board.getSquareCopy(i, j).isOccupied()) {
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
    // FIXME
    private String getBestMove() {
        HashSet<String> moves = format(getAllMoves());
        System.out.println("test: " + getAllMoves());
        System.out.println("my frame: " + frame());
        System.out.println("all moves: " + moves);
        if (moves.size() == 0) {
            return "EXCHANGE " + getFrameLetters();
        } else {
            String bestPlacement = null;
            int bestScore = 0;
            int score;
            for (String move : moves) {
                String letters = move.substring(move.indexOf(' ', move.indexOf(' ') + 1) + 1);
                letters = letters.substring(0, letters.length() - countBlanks(letters) - 1);
//                System.out.println(letters);
                score = 0;
                for (Character ch : letters.toCharArray()) {  // calculate value of word
//                    System.out.println("ch: " + ch);
                    score += (new Tile(ch)).getValue();
                }
                if (score > bestScore) {  // update best word
                    bestPlacement = move;
                    bestScore = score;
                }
            }
            if (bestPlacement == null || !(bestPlacement.length() > 2)) {
                throw new IllegalStateException("No words formed.");
            }
            return bestPlacement;
        }
    }

    private int countBlanks(String letters) {
        int count = 0;
        for (Character ch : letters.toCharArray()) {
            if (ch == '_') {
                count++;
            }
        }
        return count;
    }

    // Get best command in required format
    // FIXME?
    private HashSet<String> format(HashSet<Word> moves) {
        HashSet<String> ans = new HashSet<>();
        for (Word word : moves) {
            StringBuilder designations = new StringBuilder();
            String letters = word.getLetters();
            char orientation = word.isHorizontal() ? 'A' : 'D';
            String index = String.format("%s%d", (char) ('A' + word.getColumn()), word.getRow() + 1);
            ans.add(String.format("%s %c %s", index, orientation, letters));
        }
        return ans;
    }

    // Move generation algorithm -------------------------------------------------------------

    // Gets all possible moves, searching both horizontally and vertically
    private HashSet<Word> getAllMoves() {
        getAllMoves(true);
        getAllMoves(false);
        return moves;
    }

    // FIXME?
    private void getAllMoves(boolean searchHorizontal) {
        this.searchHorizontal = searchHorizontal;
        ArrayList<Coordinate> anchors = getAnchors();
        if (searchHorizontal) {
            System.out.println("Horizontal search, anchors: " + anchors);
        } else {
            System.out.println("Vertical search, anchors: " + anchors);
        }
        for (Coordinate cor : anchors) {
            anchor = cor;
            generate(0, "", getFrameLetters(), tree);
        }
        moves.removeIf(word -> !board.isLegalPlay(frame(), word));
    }

    // GADDAG move generation algorithm by Steven A. Gordon
    // FIXME
    private void generate(int pos, String word, String frame, GADDAG arc) {
//        System.out.println("generate " + pos + ", " + word + ", " + frame);
        if (!isEmpty(pos) && getLetterAtOffset(pos) != '+') {
            goOn(pos, getLetterAtOffset(pos), word, frame, arc.getSubTree(getLetterAtOffset(pos)), arc);
        } else if (!frame.equals("")) {
            for (Character letter : frame.toCharArray()) {
                if (letter != '_') { // not blank tile
//                    System.out.println("arc has path from " + letter + ": " + arc.hasPathFrom(letter));
//                    System.out.println("arc can grow from " + pos + ", " + letter + ": " + canGrow(pos, letter));
                    if (arc.hasPathFrom(letter) && allowedOn(pos, letter)) {
                        goOn(pos, letter, word, frame.replaceFirst("" + letter, ""),
                                arc.getSubTree(letter), arc);
                    }
                } else {
                    for (char i = 'A'; i < 'Z'; i++) {
                        if (arc.hasPathFrom(i) && allowedOn(pos, i)) {
                            goOn(pos, i, word, frame.replaceFirst("_", ""),
                                    arc.getSubTree(i), arc);
                        }
                    }
                }
            }
        }
    }

    // Append/prepend the given letter to the word and then records the move if needed
    // FIXME?
    private void goOn(int pos, Character letter, String word, String frame, GADDAG newArc, GADDAG prevArc) {
        if (pos <= 0) {
            word = letter + word;
            if (isOn(prevArc, letter) && (isEmpty(pos - 1) || getLetterAtOffset(pos - 1) == '+')) {
                storeMove(word);
            }
            if (newArc != null) {
                if (isEmpty(pos - 1)) {
                    generate(pos - 1, word, frame, newArc);
                }
                newArc = newArc.getSubTree('+');
                if (newArc != null && isEmpty(pos - 1) && rightOrBottomFree()) {
                    generate(1, word, frame, newArc);
                }
            }
        } else {
            word += letter;
            if (isOn(prevArc, letter) && (isEmpty(pos + 1) || getLetterAtOffset(pos + 1) == '+')) {
                storeMove(word);
            }
            if (newArc != null && rightOrBottomFree()) {
                generate(pos + 1, word, frame, newArc);
            }
        }
    }

    // Checks if the old arc contains the given letter and if that branch leads to end of word
    private boolean isOn(GADDAG prevArc, Character letter) {
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

    // Check that invalid words are not created in the cross-set
    private boolean allowedOn(int pos, Character letter) {
        String top;
        String bottom;
        if (searchHorizontal) {
            top = getVerticalWord(anchor.getRow() - 1, anchor.getColumn() + pos);
            bottom = getVerticalWord(anchor.getRow() + 1, anchor.getColumn() + pos);
        } else {
            top = getHorizontalWord(anchor.getRow() + pos, anchor.getColumn() - 1);
            bottom = getHorizontalWord(anchor.getRow() + pos, anchor.getColumn() + 1);
        }
        // FIXME? Doesn't work without this
        if (board.isFirstPlay()) {
            return true;
        }
        return tree.inDictionary(top + letter + bottom);
    }

    // TODO improve?
    private void storeMove(String word) {
        int row = anchor.getRow();
        int col = anchor.getColumn();
        // Include code below or not? Change to word.length() != 0?
//        if (word.length() == 0) {
//            if (searchHorizontal) {
//                col++;
//            } else {
//                row++;
//            }
//        }
        Word move = new Word(row, col, searchHorizontal, word);
        if (board.isLegalPlay(frame(), move)) {
            moves.add(move);
        }
    }

    // Checks if the rightmost column or bottommost row have available empty squares
    private boolean rightOrBottomFree() {
        if (searchHorizontal) {
            return isEmpty(anchor.getRow(), Board.BOARD_SIZE - 1);
        } else {
            return isEmpty(Board.BOARD_SIZE - 1, anchor.getColumn());
        }
    }


    // Searching the board -------------------------------------------------------------

    // Returns the letter at the given offset from anchor
    private Character getLetterAtOffset(int pos) {
        if (searchHorizontal) {
            return getCharAtIndex(anchor.getRow(), anchor.getColumn() + pos);
        } else {
            return getCharAtIndex(anchor.getRow() + pos, anchor.getColumn());
        }
    }

    // Return character at a given board position ('_' for empty, + for out of bounds)
    private Character getCharAtIndex(int row, int column) {
        if (!isValidIndex(row, column)) {
            return '+';
        }
        if (isEmpty(row, column)) {
            return '_';
        }
        return board.getSquareCopy(row, column).getTile().getLetter();
    }

    private boolean isValidIndex(int row, int column) {
        return row >= 0 && row < Board.BOARD_SIZE && column >= 0 && column < Board.BOARD_SIZE;
    }

    // Checks if board has empty spot at given row and column
    private boolean isEmpty(int row, int column) {
        return !board.getSquareCopy(row, column).isOccupied();
    }

    // Checks if board has empty spot relative to anchor
    private boolean isEmpty(int pos) {
        return getLetterAtOffset(pos) == '_';
    }

    /**
     * Generates a list of all of the anchor squares needed when working in the
     * current direction.  Anchor squares consist of squares containing the first
     * letter of all the words in the given direction as well as any squares
     * directly above or below an occupied square for horizontal and directly
     * right or left of an occupied square for vertical.  These are the squares
     * that the GADDAG algorithm can base its search out from.
     */
    private ArrayList<Coordinate> getAnchors() {
        ArrayList<Coordinate> anchors = new ArrayList<>();
        for (int i = 0; i < Board.BOARD_SIZE; i++) {
            boolean found = false;
            for (int j = 0; j < Board.BOARD_SIZE; j++) {
                // Horizontal anchor search
                if (searchHorizontal) {
                    if (isEmpty(i, j)) {
                        found = false;
                        // Check above and below for words
                        if (!getHorizontalWord(i - 1, j).equals("")
                                || !getHorizontalWord(i + 1, j).equals("") && isEmpty(i, j)) {
                            anchors.add(new Coordinate(i, j));
                        }
                    } else if (!found) {
                        // Square is occupied and first word character not added
                        found = true;
                        anchors.add(new Coordinate(i, j));
                    }
                } else {
                    // Vertical anchors search
                    if (isEmpty(j, i)) {
                        found = false;
                        // Check left and right for words
                        if (!getVerticalWord(j, i - 1).equals("")
                                || !getVerticalWord(j, i + 1).equals("") && isEmpty(j, i)) {
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
        Coordinate c = new Coordinate(row, column);
        if (horizontalWords.containsKey(c)) {
            return horizontalWords.get(c);
        }
        return "";
    }

    // Get the vertical word if it exists, else ""
    private String getVerticalWord(int row, int column) {
        Coordinate c = new Coordinate(row, column);
        if (verticalWords.containsKey(c)) {
            return verticalWords.get(c);
        }
        return "";
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

        @Override
        public boolean equals(Object c) {
            if (!(c instanceof Coordinate)) {
                return false;
            }
            return (row == ((Coordinate) c).getRow() && column == ((Coordinate) c).getColumn());
        }

        @Override
        public int hashCode() {
            return (10000 * row) + (10 * column);
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