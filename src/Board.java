import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;

enum Orientation {
    ACROSS,
    DOWN,
    BACKWARDS,
    UP,
    DIAG_ACROSS_DOWN,
    DIAG_ACROSS_UP,
    DIAG_BACK_DOWN,
    DIAG_BACK_UP;

    public Orientation reverse() {
        return switch (this) {
            case ACROSS -> BACKWARDS;
            case DOWN -> UP;
            case BACKWARDS -> ACROSS;
            case UP -> DOWN;
            case DIAG_ACROSS_DOWN -> DIAG_BACK_UP;
            case DIAG_ACROSS_UP -> DIAG_BACK_DOWN;
            case DIAG_BACK_DOWN -> DIAG_ACROSS_UP;
            case DIAG_BACK_UP -> DIAG_ACROSS_DOWN;
        };
    }
}

public class Board {
    int rows;
    int columns;
    BoardSquare[][] squares;

    public Board(int rows, int cols) {
        this.rows = rows;
        this.columns = cols;

        squares = new BoardSquare [rows][cols];

        for (int i=0; i<rows; i++) {
            for (int j=0; j<cols; j++) {
                this.squares[i][j] = new BoardSquare();
            }
        }
    }

    public Board(int width) {
        this(width, width);
    }

    public void populateFromString(String s) {
        String[] rows = s.split("\n");
        ArrayList<String> tileStrings;
        String tileStr;
        BoardSquare sq;
        int idx;

        for (int i=0; i<this.rows; i++) {
            tileStrings = new ArrayList<String>(Arrays.asList(rows[i].split(" ")));

            ArrayList<String> toRemove = new ArrayList<>();

            // Remove empty strings
            for (String ts: tileStrings) {
                if (ts.equals("")) {
                    toRemove.add(ts);
                }
            }

            tileStrings.removeAll(toRemove);

            for (int j=0; j<this.columns; j++) {
                tileStr = tileStrings.get(j);
                sq = this.getSquareAt(new BoardLocation(i, j)).get();

                if (Character.isLetter(tileStr.charAt(0))) {
                    sq.addTile(new LetterTile(tileStr.charAt(0)));
                } else {
                    if (Character.isDigit(tileStr.charAt(0))) {
                        sq.setWordMultiplier(Character.digit(tileStr.charAt(0), 10));
                    } else if (Character.isDigit(tileStr.charAt(1))) {
                        sq.setWordMultiplier(Character.digit(tileStr.charAt(1), 10));
                    }
                }
            }
        }
    }

    public void setLetterMultipliers(int[][] multipliers) {}

    public void setWordMultipliers(int[][] multipliers) {}

    public BoardLocation getCenter() {
        // NOTE: If board dimensions are even, will return location to right of
        // and below center
        return new BoardLocation(this.rows/2, this.columns/2);
    }

    public Optional<BoardSquare> getSquareAt(BoardLocation loc) {
        Optional<BoardSquare> square = null;

        if (this.isOnBoard(loc)) {
            square = Optional.of(this.squares[loc.getRow()][loc.getColumn()]);
        }

        return square;
    }

    public SquareSequence getSquares(BoardLocation loc, int numSquares, Orientation o) {
        SquareSequence squares = new SquareSequence();
        Optional<BoardLocation> next = Optional.of(loc);

        for (int i=0; i<numSquares; i++) {
            if (next.isPresent()) {
                squares.add(this.getSquareAt(loc).get());
                next = this.getNextLocation(loc, o);
            } else {
                break;
            }
        }

        return squares;
    }

    public Optional<BoardLocation> getNextLocation(BoardLocation loc, Orientation o) {
        BoardLocation next;

        next = switch (o){
            case ACROSS -> new BoardLocation(loc.getRow(), loc.getColumn() + 1);
            case DOWN -> new BoardLocation(loc.getRow() + 1, loc.getColumn());
            case BACKWARDS -> new BoardLocation(loc.getRow(), loc.getColumn() - 1);
            case UP -> new BoardLocation(loc.getRow() - 1, loc.getColumn());
            case DIAG_ACROSS_DOWN -> new BoardLocation(loc.getRow() + 1, loc.getColumn() + 1);
            case DIAG_ACROSS_UP -> new BoardLocation(loc.getRow() - 1, loc.getColumn() + 1);
            case DIAG_BACK_DOWN -> new BoardLocation(loc.getRow() + 1, loc.getColumn() - 1);
            case DIAG_BACK_UP -> new BoardLocation(loc.getRow() - 1, loc.getColumn() - 1);
        };

        Optional<BoardLocation> validatedNext = Optional.ofNullable(null);

        if (this.isOnBoard(next)) {
            validatedNext = Optional.of(next);
        }

        return validatedNext;
    }

    public Optional<BoardLocation> getPreviousLocation(BoardLocation loc, Orientation o) {
        return getNextLocation(loc, o.reverse());
    }

    public ArrayList<BoardLocation> getNeighbors(BoardLocation loc) {
        ArrayList<BoardLocation> neighbors = new ArrayList<>();

        neighbors.add(new BoardLocation(loc.getRow(), loc.getColumn()+1));
        neighbors.add(new BoardLocation(loc.getRow(), loc.getColumn()-1));
        neighbors.add(new BoardLocation(loc.getRow()+1, loc.getColumn()));
        neighbors.add(new BoardLocation(loc.getRow()-1, loc.getColumn()));

        return neighbors;
    }

    public boolean isOnBoard(BoardLocation loc) {
        if (loc.getRow() < 0 || loc.getRow() + 1 > this.rows) {
            return false;
        } else if (loc.getColumn() < 0 || loc.getColumn() + 1 > this.columns) {
            return false;
        }

        return true;
    }

    public ArrayList<BoardLocation> getAnchors() {
        HashSet<BoardLocation> anchors = new HashSet<>();
        BoardLocation loc;
        int letterTiles = 0;

        for (int i=0; i<this.rows; i++) {
            for (int j=0; j<this.columns; j++) {
                loc = new BoardLocation(i, j);
                if (this.getSquareAt(loc).get().hasTile()) {
                    anchors.add(loc);
                    anchors.addAll(this.getNeighbors(loc));
                    letterTiles++;
                }
            }
        }

        System.out.println(letterTiles + " letter tiles");
        System.out.println((anchors.size() - letterTiles) + " tile-adjacent anchors");

        ArrayList<BoardLocation> anchorList = new ArrayList<>();
        anchorList.addAll(anchors);

        return anchorList;
    }

    public boolean play(ArrayList<LetterTile> word, BoardLocation loc, Orientation o) {
        Optional<BoardSquare> sq;
        Optional<BoardLocation> next = Optional.of(loc);

        for (int i=0; i<word.size(); i++) {
            if (!next.isPresent()) {
                return false;
            }

            sq = this.getSquareAt(next.get());
            sq.get().addTile(word.get(i));
            next = this.getNextLocation(next.get(), o);
        }
            return true;
    }

    public boolean play(String word, BoardLocation loc, Orientation o) {
        ArrayList<LetterTile> tiles = new ArrayList<>();

        for (char c: word.toCharArray()) {
            tiles.add(new LetterTile(c));
        }

        return this.play(tiles, loc, o);
    }

    private boolean wordFits(ArrayList<LetterTile> word, BoardLocation loc, Orientation o) {
        int wordLength = word.size();
        Optional<BoardLocation> next = Optional.of(loc);

        for (int i=0; i<wordLength; i++) {
            if (!next.isPresent()) {
                return false;
            }
            next = this.getNextLocation(loc, o);
        }

        return true;
    }

    public String toString() {
        String s = "";

        for (int i=0; i<this.rows; i++) {
            for (int j=0; j<this.columns; j++) {
                s += this.squares[i][j].toString();

                if (j != this.columns) {
                    s += " ";
                }
            }
            s += "\n";
        }

        return s;
    }
}
