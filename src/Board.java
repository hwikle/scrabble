import javafx.geometry.Point2D;
import java.util.ArrayList;

enum Orientation {
    ACROSS,
    DOWN
}

public class Board {
    int rows;
    int columns;
    BoardSquare[][] squares;

    public Board(int rows, int cols) {
        this.rows = rows;
        this.columns = cols;

        BoardSquare[][] squares = new BoardSquare [rows][cols];

        for (int i=0; i<rows; i++) {
            for (int j=0; j<cols; j++) {
                squares[i][j] = new BoardSquare();
            }
        }
    }

    public Board(int width) {
        this(width, width);
    }

    public BoardSquare getSquareAt(BoardLocation loc) {
        return this.squares[loc.getRow()][loc.getColumn()];
    }

    public ArrayList<BoardSquare> getSquares(BoardLocation loc, int numSquares, Orientation o) {
        ArrayList<BoardSquare> squares = new ArrayList<>();

        for (int i=0; i<numSquares; i++) {
            if (this.isOnBoard(loc)) {
                squares.add(this.getSquareAt(loc));
                loc = this.getNextLocation(loc, o);
            } else {
                break;
            }
        }

        return squares;
    }

    public BoardLocation getNextLocation(BoardLocation loc, Orientation o) {
        BoardLocation next;

        if (o == Orientation.ACROSS) {
            next = new BoardLocation(loc.getRow(), loc.getColumn() + 1);
        } else {
            next = new BoardLocation(loc.getRow() + 1, loc.getColumn());
        }

        if (this.isOnBoard(next)) {
            return next;
        } else {
            // NULL Location
            return new BoardLocation(-1, -1);
        }
    }

    public boolean isOnBoard(BoardLocation loc) {
        if (loc.getRow() < 0 || loc.getRow() + 1 > this.rows) {
            return false;
        } else if (loc.getColumn() < 0 || loc.getColumn() + 1 > this.columns) {
            return false;
        }

        return true;
    }

    public void setLetterMultipliers(int[][] multipliers) {}

    public void setWordMultipliers(int[][] multipliers) {}

    public void playWord(ArrayList<LetterTile> word, Point2D location, Orientation o) {}
}
