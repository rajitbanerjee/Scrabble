public class Square {

    private final int letterMultiplier;
    private final int wordMultiplier;
    private boolean isOccupied;
    private Tile tile;

    Square(int letterMultiplier, int wordMultiplier) {
        isOccupied = false;
        this.letterMultiplier = letterMultiplier;
        this.wordMultiplier = wordMultiplier;
    }

    Square(Square square) {
        this.letterMultiplier = square.letterMultiplier;
        this.wordMultiplier = square.wordMultiplier;
        this.isOccupied = square.isOccupied;
        if (square.isOccupied) {
            this.tile = new Tile(square.tile);
        }
    }

    public int getLetterMultiplier() {
        return letterMultiplier;
    }

    public int getWordMultiplier() {
        return wordMultiplier;
    }

    public boolean isDoubleLetter() {
        return letterMultiplier == 2;
    }

    public boolean isTripleLetter() {
        return letterMultiplier == 3;
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