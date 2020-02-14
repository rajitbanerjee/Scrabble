public class Square {
    enum Multiplier {DOUBLE_LS, TRIPLE_LS, DOUBLE_WS, TRIPLE_WS, NORMAL, CENTRE}

    Multiplier multiplier;
    Tile tile = null;

    Square(Multiplier multiplier) {
        this.multiplier = multiplier;
    }

    public void setMultiplier(Multiplier multiplier) {
        this.multiplier = multiplier;
    }

    public void setTile(Tile tile) {
        this.tile = tile;
    }

    public Multiplier getMultiplier() {
        return multiplier;
    }

    public Tile getTile() {
        return tile;
    }

    public String toString() {
        if (tile == null) {
            switch (multiplier) {
                case DOUBLE_LS:
                    return "2x_LS";
                case TRIPLE_LS:
                    return "3x_LS";
                case DOUBLE_WS:
                    return "2x_WS";
                case TRIPLE_WS:
                    return "3x_WS";
                case CENTRE:
                    return "  *  ";
                default:
                    return "     ";
            }
        } else {
            return tile.toString();
        }
    }
}
