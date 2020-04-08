public class Square {

    private int letterMuliplier;
    private int wordMultiplier;
    private boolean isOccupied;
    private Tile tile;

    Square(int letterMultiplier, int wordMultiplier) {
        isOccupied = false;
        this.letterMuliplier = letterMultiplier;
        this.wordMultiplier = wordMultiplier;
    }

    Square(Square square) {
        this.letterMuliplier = square.letterMuliplier;
        this.wordMultiplier = square.wordMultiplier;
        this.isOccupied = square.isOccupied;
        this.tile = new Tile(tile);
    }

    public int getLetterMuliplier() {
        return letterMuliplier;
    }

    public int getWordMultiplier() {
        return wordMultiplier;
    }

    public boolean isDoubleLetter() {
        return letterMuliplier == 2;
    }

    public boolean isTripleLetter() {
        return letterMuliplier == 3;
    }

    public boolean isDoubleWord() {
        return wordMultiplier == 2;
    }

    public boolean isTripleWord() {
        return wordMultiplier == 3;
    }

    public void add(Tile tile) {
        isOccupied = true;
        this.tile = tile;
    }

    public Tile removeTile() {
        isOccupied = false;
        return tile;
    }

    public boolean isOccupied() {
        return isOccupied;
    }

    // getTile pre-condition: isOccupied must be true
    public Tile getTile() {
        return tile;
    }

}
