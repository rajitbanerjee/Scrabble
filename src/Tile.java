public class Tile {

    public static final char BLANK = '_';
    private static final int BLANK_VALUE = 0;
    private static final int[] TILE_VALUE = {1,3,3,2,1,4,2,4,1,8,5,1,3,1,1,3,10,1,1,1,1,4,4,8,4,10};

    private boolean blank;
    private char letter;
    private int value;

    // Tile precondition: must be uppercase letter
    Tile(char letter) {
        if (letter == BLANK) {
            this.blank = true;
            this.letter = BLANK;
            this.value = BLANK_VALUE;
        } else {
            this.blank = false;
            this.letter = letter;
            this.value = TILE_VALUE[(int) letter - (int) 'A'];
        }
    }

    Tile(Tile tile) {
        this.blank = tile.blank;
        this.letter = tile.letter;
        this.value = tile.value;
    }

    public boolean isBlank() {
        return blank;
    }

    // getLetter precondition isBlank() = false;
    public char getLetter() {
        return letter;
    }

    // getValue precondition isBlank() = false;
    public int getValue() {
        return value;
    }

    // equals is used by the contains method to find matching objects in an ArrayList
    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Tile)) {
            return false;
        } else {
            return (this.letter == ((Tile) object).letter) || (this.isBlank() && ((Tile) object).isBlank());
        }
    }

    public void designate(char letter) {
        this.letter = letter;
    }

    public void removeDesignation() {
        this.letter = BLANK;
    }

    @Override
    public String toString() {
        return Character.toString(letter);
    }

}
