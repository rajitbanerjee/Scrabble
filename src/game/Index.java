package game;

/**
 * An Index is a pair composed of row and column index.
 * It is useful to store the last covered indices after a move.
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

    /**
     * Accessor for row.
     *
     * @return row
     */
    public int getRow() {
        return row;
    }

    /**
     * Setter for row.
     *
     * @param row integer 0 - 14
     */
    public void setRow(int row) {
        this.row = row;
    }

    /**
     * Accessor for column.
     *
     * @return column
     */
    public int getColumn() {
        return column;
    }

    /**
     * Setter for column.
     *
     * @param column integer 0 - 14
     */
    public void setColumn(int column) {
        this.column = column;
    }

    /**
     * Index equality checker.
     *
     * @param obj to be compared to calling Index object
     * @return {@code true} if two Index objects are equal
     */
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