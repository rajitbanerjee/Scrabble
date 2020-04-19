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
        switch (turnCount) {
            case 0:
//                command = "NAME DarkMode";
                if (me.getPrintableId() == 1) {
                    command = "NAME DarkMode1";
                } else {
                    command = "NAME DarkMode2";
                }
                break;
            case 1:
                command = "PASS";
                break;
            case 2:
                command = "HELP";
                break;
            case 3:
                command = "SCORE";
                break;
            case 4:
                command = "POOL";
                break;
            default:
                command = "H8 A AN";
                break;
        }
        turnCount++;
        return command;
    }

    private void setUpDictionaryTrie() {
        try (Scanner reader = new Scanner(new File("csw.txt"))) {
            while (reader.hasNextLine()) {
                String word = reader.nextLine();
                trie.insert(word);
            }
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
    }

    private static class Trie {
        private final TrieNode head;

        public Trie() {
            // creates a sentinel node
            head = new TrieNode();
        }

        public void insert(String word) {
            // only supports uppercase alphabetical characters
            if (word == null || !word.matches("^[a-zA-Z]+$")) {
                throw new IllegalArgumentException("Invalid word.");
            }
            word = word.toUpperCase();
            TrieNode pointer = head;
            // go through each character in string
            for (int i = 0; i < word.length(); i++) {
                char ch = word.charAt(i);
                // no children, create new node and follow new child
                if (pointer.children[ch - 'A'] == null) {
                    pointer.children[ch - 'A'] = new TrieNode();
                }
                // update pointer to point to child
                pointer = pointer.children[ch - 'A'];
                // set end of word if it's the last character
                if (i == word.length() - 1) {
                    pointer.isEndOfWord = true;
                }
            }
        }

        /* uses arrays instead of HashMaps since there's only 26 uppercase characters
           (HashMap resizing trade off isn't worth it) */
        private static class TrieNode {
            private final TrieNode[] children;
            private boolean isEndOfWord;

            public TrieNode() {
                children = new TrieNode[ALPHABET_SIZE];
                isEndOfWord = false;
            }
        }
    }
}