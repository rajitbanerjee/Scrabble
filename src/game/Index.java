package game;

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
