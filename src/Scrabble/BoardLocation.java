package Scrabble;

public class BoardLocation {
    private int row;
    private int column;

    public BoardLocation() {
        // Construct NULL location
        this.row = -1;
        this.column = -1;
    }
    public BoardLocation(int row, int column) {
        this.row = row;
        this.column = column;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public boolean isLinearWith(BoardLocation other, Orientation o) {
        boolean res = false;

        switch (o) {
            case ACROSS -> {
                res = (this.row == other.getRow());
            }
            case DOWN -> {
                res = (this.column == other.getColumn());
            }
            default -> throw new IllegalStateException("Unexpected value: " + o);
        }

        return res;
    }

    @Override
    public int hashCode() {
        return (2^this.row * 3^this.column);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (!(o instanceof BoardLocation)) {
            return false;
        } else {
            return ((this.row == ((BoardLocation) o).row) && (this.column == ((BoardLocation) o).column));
        }
    }

    public String toString() {
        return "(" + this.row + ", " + this.column + ")";
    }
}
