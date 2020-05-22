package bot;

import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

// TODO javadoc comments
public class GADDAG implements Serializable {
    private final Character letter;
    private final List<GADDAG> children;
    private boolean isEndOfWord;

    public GADDAG(Character letter) {
        this.letter = letter;
        children = new ArrayList<>();
        isEndOfWord = false;
    }

    public static GADDAG getGADDAG() {
        GADDAG tree;
//        try {
//            ClassLoader classLoader = ClassLoader.getSystemClassLoader();
//            InputStream is = classLoader.getResourceAsStream("gaddag.ser");
//            FileInputStream fileIn = new FileInputStream("src/main/resources/gaddag.ser");
//            ObjectInputStream in = new ObjectInputStream(fileIn);
//            tree = (GADDAG) in.readObject();
//            in.close();
//        } catch (NullPointerException | IOException | ClassNotFoundException e) {
        tree = new GADDAG(null);
        tree.build();
//            try {
//                FileOutputStream fileOut = new FileOutputStream("src/main/resources/gaddag.ser");
//                ObjectOutputStream out = new ObjectOutputStream(fileOut);
//                out.writeObject(tree);
//                out.close();
//                fileOut.close();
//            } catch (IOException ex) {
//                ex.printStackTrace();
//            }
//        }
        return tree;
    }

    // Check if a word is valid (present in the dictionary)
    public boolean inDictionary(String word) {
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
    public void build() {
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        InputStream in = classLoader.getResourceAsStream("sowpods.txt");
        Scanner sc = new Scanner(Objects.requireNonNull(in));
        while (sc.hasNextLine()) {
            String word = sc.nextLine();
            insert(word);
        }
        sc.close();
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
    public boolean hasPathFrom(Character letter) {
        for (GADDAG tree : children) {
            if (letter == tree.letter) {
                return true;
            }
        }
        return false;
    }

    // Returns the sub-tree at this level starting from given letter
    public GADDAG getSubTree(Character letter) {
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