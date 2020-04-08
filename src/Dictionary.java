import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Dictionary implements DictionaryAPI {

    private static String inputFileName = "csw.txt";
    private Node root;

    Dictionary() throws FileNotFoundException {
        root = new Node();
        File inputFile = new File(inputFileName);
        Scanner in = new Scanner(inputFile);
        while (in.hasNextLine()) {
            String word = in.nextLine();
            Node currentNode = root;
            for (int i=0; i<word.length(); i++) {
                char currentLetter = word.charAt(i);
                if (currentNode.isChild(currentLetter)) {
                    currentNode = currentNode.getChild(currentLetter);
                }
                else {
                    currentNode = currentNode.addChild(currentLetter);
                }
            }
            currentNode.setEndOfWord();
        }
        in.close();
    }

    public boolean areWords (ArrayList<Word> words) {
        boolean found = true;
        for (Word word : words) {
            Node currentNode = root;
            for (int i=0; (i<word.length()) && found; i++) {
                char currentLetter = word.getDesignatedLetter(i);
                if (currentNode.isChild(currentLetter)) {
                    currentNode = currentNode.getChild(currentLetter);
                }
                else {
                    found = false;
                }
            }
            if (!currentNode.isEndOfWord()) {
                found = false;
            }
        }
        return(found);
    }


}
