public class Coordinates {

    int row, col;

    Coordinates(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Coordinates)) {
            return false;
        } else {
            return (this.row == ((Coordinates) object).row) && (this.col == ((Coordinates) object).col);
        }
    }
}
