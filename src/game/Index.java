package game;

/**
 * An Index is a pair composed of row and column index.
 * It is useful to store the last covered indices after a move.
 * TODO javadoc comments
 *
 * @author Rajit Banerjee, 18202817
 * @author Katarina Cvetkovic, 18347921
 * @author Tee Chee Guan, 18202044
 * @team DarkMode
 */
public class Index {
    private int row;
    private int column;

    public Index(int row, int column) {
        setRow(row);
        setColumn(column);
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Index) {
            Index index = (Index) obj;
            return index.getRow() == getRow() && index.getColumn() == getColumn();
        } else {
            return false;
        }
    }

}