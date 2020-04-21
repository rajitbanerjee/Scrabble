import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class DarkMode implements BotAPI {

    // The public API of Bot must not change
    // This is ONLY class that you can edit in the program
    // Rename Bot to the name of your team. Use camel case.
    // Bot may not alter the state of the game objects
    // It may only inspect the state of the board and the player objects

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
        tree = new GADDAG(null);
        turnCount = 0;
        setUpDictionaryGADDAG();
    }

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

    // Dictionary processing: build GADDAG ------------------------------------------

    // Inserts words from dictionary into the GADDAG
    private void setUpDictionaryGADDAG() {
        try (Scanner sc = new Scanner(new File("csw.txt"))) {
            while (sc.hasNextLine()) {
                insert(sc.nextLine());
            }
            // Test GADDAG
            System.out.println(getPossibleWords("AUTO", "ING", tree));
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
            tree.insertFormatted(x.toString());
        }
    }

    // Word search methods and GADDAG set-up --------------------------------

    // Check if a word is valid (present in the dictionary)
    private boolean isValidWord(String word) {
        if (word.equals("")) {
            return false;
        } else {
            return find(word.charAt(0) + "+" + word.substring(1), tree);
        }
    }

    private boolean find(String str, GADDAG tree) {
        if (str.length() == 0 && tree.isEndOfWord()) {
            return true;
        } else if (str.length() > 0 && tree.hasPathFrom(str.charAt(0))) {
            return find(str.substring(1), tree.getSubTree(str.charAt(0)));
        } else {
            return false;
        }
    }

    // Get a list of possible words starting with a prefix and ending with a suffix
    private ArrayList<String> getPossibleWords(String prefix, String suffix, GADDAG tree) {
        Set<String> prefixWords = new HashSet<>(getWordsStartingWith(prefix, tree));
        Set<String> suffixWords = new HashSet<>(getWordsEndingWith(suffix, tree));
        prefixWords.retainAll(suffixWords);
        return new ArrayList<>(prefixWords);
    }

    // Get a list of words starting with a prefix
    private ArrayList<String> getWordsStartingWith(String prefix, GADDAG tree) {
        ArrayList<String> words = new ArrayList<>();
        if (tree.getLetter() == null && prefix.equals("")) {
            for (GADDAG child : tree.getChildren()) {
                for (String str : getWordsStartingWith("", child)) {
                    words.add(child.getLetter() + str);
                }
            }
        } else if (tree.getLetter() == null) {
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

    // Get a list of  words ending with a given suffix
    private ArrayList<String> getWordsEndingWith(String suffix, GADDAG tree) {
        ArrayList<String> wordList = new ArrayList<>();
        if (tree.getLetter() == null && suffix.equals("")) {
            return wordList;
        } else if (tree.getLetter() == null) {
            for (String prefix : getWordsEndingWith(suffix.substring(0, suffix.length() - 1),
                    tree.getSubTree(suffix.charAt(suffix.length() - 1)))) {
                wordList.add(prefix + suffix);
            }
            if (isValidWord(suffix)) {
                wordList.add(suffix);
            }
        } else if (suffix.equals("")) {
            for (GADDAG child : tree.getChildren()) {
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
        if (tree.isEndOfWord()) {
            list.add(tree.getLetter() + "");
        }
        for (GADDAG child : tree.getChildren()) {
            for (String str : getAllStrings(child)) {
                if (tree.getLetter() != null) {
                    list.add(tree.getLetter() + "" + str);
                } else {
                    list.add(str);
                }
            }
        }
        return list;
    }

    // Inner class to represent a GADDAG data structure
    private static class GADDAG {
        private final Character letter;
        private final ArrayList<GADDAG> children;
        private boolean endOfWord;

        GADDAG(Character letter) {
            this.letter = letter;
            children = new ArrayList<>();
            endOfWord = false;
        }

        // Inserts a given item of the form rev(x)+y into the GADDAG
        void insertFormatted(String item) {
            if (!item.equals("")) {
                if (!hasPathFrom(item.charAt(0))) {
                    children.add(new GADDAG(item.charAt(0)));
                }
                getSubTree(item.charAt(0)).insertFormatted(item.substring(1));
            } else {
                endOfWord = true;
            }
        }

        // Checks if this level of the GADDAG has a path starting from given letter
        boolean hasPathFrom(Character letter) {
            for (GADDAG tree : children) {
                if (letter == tree.getLetter()) {
                    return true;
                }
            }
            return false;
        }

        // Returns the sub-tree at this level starting from given letter
        GADDAG getSubTree(Character letter) {
            for (GADDAG tree : children) {
                if (letter == tree.getLetter()) {
                    return tree;
                }
            }
            return null;
        }

        Character getLetter() {
            return letter;
        }

        ArrayList<GADDAG> getChildren() {
            return children;
        }

        boolean isEndOfWord() {
            return endOfWord;
        }
    }

}