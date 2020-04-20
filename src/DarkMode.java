import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

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
    private int turnCount;
    private final Trie trie;

    DarkMode(PlayerAPI me, OpponentAPI opponent, BoardAPI board, UserInterfaceAPI ui, DictionaryAPI dictionary) {
        this.me = me;
        this.opponent = opponent;
        this.board = board;
        this.info = ui;
        this.dictionary = dictionary;
        trie = new Trie();
        turnCount = 0;
        setUpDictionaryTrie();
    }

    @Override
    public String getCommand() {
        // Add your code here to input your commands
        // Your code must give the command NAME <botname> at the start of the game
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
        for (char ch : me.getFrameAsString().toCharArray()) {
            if (Character.isLetter(ch) || ch == '_') {
                letters.append(ch);
            }
        }
        return letters.toString();
    }

    // Dictionary processing ---------------------------------------------

    private void setUpDictionaryTrie() {
        try (Scanner sc = new Scanner(new File("csw.txt"))) {
            while (sc.hasNextLine()) {
                String word = sc.nextLine();
                trie.insert(word);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static class Trie {
        private static final int ALPHABET_SIZE = 26;
        private static final TrieNode ROOT = new TrieNode();

        /**
         * Inserts a word into the Trie.
         *
         * @param word to be inserted into Trie.
         */
        public void insert(String word) {
            // Only supports uppercase alphabetical characters
            if (word == null || !word.matches("^[a-zA-Z]+$")) {
                throw new IllegalArgumentException("Invalid word.");
            }
            word = word.toUpperCase();
            TrieNode curr = ROOT;
            for (char letter : word.toCharArray()) {
                // If letter not in Trie, create child node with letter
                if (curr.getChild(letter) == null) {
                    curr.createChild(letter);
                }
                curr = curr.getChild(letter);
            }
            curr.setEndOfWord(true);
        }

        /**
         * Search for a word in the Trie.
         *
         * @param word to be searched
         * @return {@code true}, if word is present in Trie
         */
        public boolean search(String word) {
            TrieNode curr = ROOT;
            for (char letter : word.toCharArray()) {
                // Return false at any point a child letter is not found
                if (curr.getChild(letter) == null) {
                    return false;
                }
                curr = curr.getChild(letter);
            }
            // Check that final child letter is found and end of word is reached
            return curr != null && curr.getIsEndOfWord();
        }

        private static class TrieNode {
            private final TrieNode[] children = new TrieNode[ALPHABET_SIZE];
            private boolean isEndOfWord; // Checks if node is leaf node (end of a word)

            TrieNode() {
                setEndOfWord(false);
                for (int i = 0; i < ALPHABET_SIZE; i++)
                    children[i] = null;
            }

            TrieNode getChild(char letter) {
                return children[letter - 'A'];
            }

            void createChild(char letter) {
                children[letter - 'A'] = new TrieNode();
            }

            boolean getIsEndOfWord() {
                return isEndOfWord;
            }

            void setEndOfWord(boolean isEndOfWord) {
                this.isEndOfWord = isEndOfWord;
            }
        }
    }

}