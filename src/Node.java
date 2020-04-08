public class Node {
    private static final int NUM_LETTERS = 26;

    private Node[] children = new Node[NUM_LETTERS];
    private boolean endOfWord;

    Node () {
        for (int i=0; i<NUM_LETTERS-1; i++) {
            children[i] = null;
        }
        endOfWord = false;
    }

    public Node addChild (char letter) {
        int index = ((int) letter) - ((int) 'A');
        if (children[index] == null) {
            children[index] = new Node();
        }
        return(children[index]);
    }

    public void setEndOfWord () {
        endOfWord = true;
    }

    public Node getChild (char letter) {
        int index = ((int) letter) - ((int) 'A');
        return children[index];
    }

    public boolean isChild (char letter) {
        int index = ((int) letter) - ((int) 'A');
        return children[index]!=null;
    }

    public boolean isEndOfWord () {
        return endOfWord;
    }
}
