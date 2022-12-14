package Scrabble;

import java.util.*;

public class Board {
    int rows;
    int columns;
    BoardSquare[][] squares;
    private boolean isFirstPlay = true;

    public Board(int rows, int cols) {
        this.rows = rows;
        this.columns = cols;

        squares = new BoardSquare[rows][cols];

        for (int i=0; i<rows; i++) {
            for (int j=0; j<cols; j++) {
                this.squares[i][j] = new BoardSquare();
            }
        }
    }

    public Board(int width) {
        this(width, width);
    }

    public int getRows() {
        return this.rows;
    }

    public int getColumns() {
        return this.columns;
    }

    public void populateFromString(String s) {
        String[] rows = s.split("\n");
        ArrayList<String> tileStrings;
        String tileStr;
        BoardSquare sq;
        LetterTile newTile;

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
                    newTile = new LetterTile(tileStr.charAt(0));

                    if (Character.isUpperCase(tileStr.charAt(0))) {
                        newTile.setBlank();
                    }
                    sq.addTile(newTile);
                } else {
                    if (Character.isDigit(tileStr.charAt(0))) {
                        //System.out.println(Character.digit(tileStr.charAt(0), 10));
                        sq.setWordMultiplier(Character.digit(tileStr.charAt(0), 10));
                    } else if (Character.isDigit(tileStr.charAt(1))) {
                        sq.setLetterMultiplier(Character.digit(tileStr.charAt(1), 10));
                    }
                }
            }
        }
    }

    public void populateFromScanner(Scanner s) {
        ArrayList<String> tileStrings;
        String tileStr;
        BoardSquare sq;
        LetterTile newTile;

        for (int i=0; i<this.rows; i++) {
            tileStrings = new ArrayList<String>(Arrays.asList(s.nextLine().split(" ")));

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
                    newTile = new LetterTile(tileStr.charAt(0));

                    if (Character.isUpperCase(tileStr.charAt(0))) {
                        newTile.setBlank();
                    }
                    sq.addTile(newTile);
                } else {
                    if (Character.isDigit(tileStr.charAt(0))) {
                        //System.out.println(Character.digit(tileStr.charAt(0), 10));
                        sq.setWordMultiplier(Character.digit(tileStr.charAt(0), 10));
                    } else if (Character.isDigit(tileStr.charAt(1))) {
                        sq.setLetterMultiplier(Character.digit(tileStr.charAt(1), 10));
                    }
                }
            }
        }
    }

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

    public Optional<BoardSquare> getSquareAt(int row, int col) {
        return getSquareAt(new BoardLocation(row, col));
    }

    public ArrayList<Move> getGlobalPossibleMoves(LetterTray tray, WordTree tree) {
        ArrayList<Move> moves = new ArrayList<>();
        BoardLocation loc;

        for (int i=0; i<this.rows; i++) {
            for (int j=0; j<this.columns; j++) {
                loc = new BoardLocation(i, j);
                moves.addAll(this.getPossibleConnectingMoves(loc, Orientation.ACROSS, tray, tree, tree));
                moves.addAll(this.getPossibleConnectingMoves(loc, Orientation.DOWN, tray, tree, tree));
            }
        }

        return moves;
    }

    public ArrayList<Move> getPossibleConnectingMoves(BoardLocation loc, Orientation o, LetterTray tray, WordTree tree, WordTree sub) {
        ArrayList<Move> moves = getPossibleMoves(loc, o, tray, tree, sub);
        ArrayList<Move> toRemove = new ArrayList<>();

        for (Move m: moves) {
            if (m.size() == 0 || !this.connects(m, this.isFirstPlay)) {
                toRemove.add(m);
            }
        }

        moves.removeAll(toRemove);

        return moves;
    }

    public ArrayList<Move> getPossibleMoves(BoardLocation loc, Orientation o, LetterTray tray, WordTree tree, WordTree sub) {
        ArrayList<Move> moves = new ArrayList<>();
        char c;

        //assert this.isOnBoard(loc);

        if (tree == sub) {
            loc = this.getSequenceStart(loc, o);
        }

        if (tray.size() >= 0) {
            Optional<LetterTile> t = this.getTileAt(loc);
            Optional<BoardLocation> next = this.getNextLocation(loc, o);
            String cross;

            if (t.isPresent()) { // Tile already exists in location
                if (sub.keySet().contains(t.get().getLetter())) {
                    c = t.get().getLetter();

                    if (next.isPresent()) {
                        //System.out.println("Descending tree with letter: " + c + " at " + next.get());
                        ArrayList<Move> submoves = this.getPossibleMoves(next.get(), o, tray, tree, sub.get(c));
                        //System.out.println(sub.get(c).getAllWords().size() + " subwords in sub");
                        //System.out.println(submoves.size() + " submoves found");
                        moves.addAll(submoves);
                    } else if (tree.get(c).keySet().contains(sub.getTerminator())) {
                        moves.add(new Move());
                    }
                }
            } else { // no tile at location
                if (next.isPresent() && tray.hasBlank()) { // Incorrectly excludes edge tiles
                    LetterTile blank = tray.getBlank().get();
                    LetterTray blankless = new LetterTray(tray.getCapacity());
                    blankless.addAll(tray);
                    blankless.remove(blank);

                    for (char ch: sub.keySet()) {
                        if (ch != sub.getTerminator()) {
                            //System.out.println("Descending tree with letter: " + ch + " at " + next.get());
                            ArrayList<Move> submoves = this.getPossibleMoves(next.get(), o, blankless, tree, sub.get(ch));
                            //System.out.println(sub.get(ch).getAllWords().size() + " subwords in sub");
                            //System.out.println(blankless);
                            //System.out.println(submoves.size() + " submoves found");
                            for (Move m : submoves) {
                                blank = new LetterTile(ch);
                                blank.setBlank();

                                cross = this.getCrossword(new TileLocationPair(blank, loc), o);

                                if (cross.length() > 1) {
                                    if (!tree.containsWord(cross)) {
                                        continue;
                                    }
                                }

                                Move newMove = new Move(new TileLocationPair(blank, loc));
                                newMove.addAll(m);
                                moves.add(newMove);
                            }
                        }
                    }
                }
                for (char ch : sub.keySet()) {
                    if (ch == sub.getTerminator()) {
                        moves.add(new Move());
                    } else if (tray.hasLetter(ch)) {

                        // What to do if at edge?
                        if (!next.isPresent() && sub.get(ch).keySet().contains(sub.getTerminator())) {
                            Move newMove = new Move(new TileLocationPair(tray.getTileByLetter(ch).get(), loc));

                            LetterTile tile = tray.getTileByLetter(ch).get();

                            cross = this.getCrossword(new TileLocationPair(tile, loc), o);

                            if (cross.length() > 1) {
                                if (tree.containsWord(cross)) {
                                    moves.add(newMove);
                                }
                            } else {
                                moves.add(newMove);
                            }
                        }

                        LetterTray trayCopy = new LetterTray(tray.getCapacity());
                        trayCopy.addAll(tray);
                        LetterTile tile = trayCopy.getTileByLetter(ch).get();

                        cross = this.getCrossword(new TileLocationPair(tile, loc), o);

                        if (cross.length() > 1) {
                            if (!tree.containsWord(cross)) {
                                continue;
                            }
                        }

                        trayCopy.remove(tile); // prevents mutations to original tray

                        if (next.isPresent()) {
                            // Append partial moves (obtained recursively)
                            //System.out.println("Descending tree with letter: " + ch + " at " + next.get());
                            ArrayList<Move> submoves = this.getPossibleMoves(next.get(), o, trayCopy, tree, sub.get(ch));
                            //System.out.println(sub.get(ch).getAllWords().size() + " subwords in sub");
                            //System.out.println(submoves.size() + " submoves found");

                            for (Move m : submoves) {
                                Move newMove = new Move(new TileLocationPair(tile, loc));
                                newMove.addAll(m);
                                moves.add(newMove);
                            }
                        }
                    } else if (tray.hasLetter(ch)) {
                        if (sub.get(ch).keySet().contains(sub.getTerminator())) {
                            LetterTray trayCopy = new LetterTray(tray.getCapacity());
                            trayCopy.addAll(tray);
                            LetterTile tile = trayCopy.getTileByLetter(ch).get();

                            cross = this.getCrossword(new TileLocationPair(tile, loc), o);

                            if (cross.length() > 1 && tree.containsWord(cross)) {
                                //System.out.println(cross);
                                moves.add(new Move(new TileLocationPair(tile, loc)));
                            }
                        }
                    }
                }
            }
        }

        return moves;
    }

    public boolean connects(Move m, boolean isFirstPlay) {
        for (BoardLocation loc: m.getLocations()) {
            if (isFirstPlay && m.getLocations().contains(this.getCenter())) {
                return true;
            }
            if (this.neighborHasTile(loc)) {
                return true;
            }
        }

        return false;
    }

    public boolean collides(Move m) {
        for (BoardLocation loc: m.getLocations()) {
            if (this.getSquareAt(loc).get().hasTile()) {
                return true;
            }
        }

        return false;
    }

    public boolean isContinuous(Move m) {
        if (m.size() == 0) {
            return true;
        }

        BoardLocation loc = m.getLocations().get(0);
        BoardLocation last = m.getLocations().get(m.size()-1);

        while (!loc.equals(last)) {
            if (!m.getLocations().contains(loc) && !this.getSquareAt(loc).get().hasTile()) {
                return false;
            }
            loc = this.getNextLocation(loc, m.getOrientation()).get();
        }

        return true;
    }

    public boolean isValidMove(Move m) {
        if (!m.isLinear()) {
            System.out.println("Move is not linear");
            return false;
        } else if (!this.isContinuous(m)) {
            System.out.println("Move is not continuous");
            return false;
        } else if (this.collides(m)) {
            System.out.println("Move collides");
            return false;
        } else if (!this.connects(m, this.isFirstPlay)) {
            System.out.println("Move does not connect");
            return false;
        }

        return true;
    }

    public BoardLocation locationFromSquare(BoardSquare sq) {
        for (int row=0; row<this.rows; row++) {
            for (int col=0; col<this.columns; col++) {
                if (this.squares[row][col] == sq) {
                    return new BoardLocation(row, col);
                }
            }
        }
        return null;
    }

    private Optional<LetterTile> getTileAt(BoardLocation loc) {
        if (this.isOnBoard(loc)) {
            return this.getSquareAt(loc).get().getTile();
        } else {
            return Optional.empty();
        }
    }

    private Optional<BoardSquare> getNextSquare (BoardLocation loc, Orientation o) {
        Optional<BoardLocation> next = this.getNextLocation(loc, o);

        if (next.isPresent()) {
            return this.getSquareAt(next.get());
        } else {
            return Optional.empty();
        }
    }

    private Optional<BoardSquare> getPreviousSquare(BoardLocation loc, Orientation o) {
        return this.getNextSquare(loc, o.reverse());
    }

    public boolean neighborHasTile(BoardLocation loc) {
        ArrayList<BoardLocation> neighbors = this.getNeighbors(loc);

        for (BoardLocation e: neighbors) {
            if (this.getTileAt(e).isPresent()) {
                return true;
            }
        }
        return false;
    }

    public SquareSequence getSlice(BoardLocation loc, int numSquares, Orientation o) {
        SquareSequence squares = new SquareSequence();
        Optional<BoardLocation> next = Optional.of(loc);

        for (int i=0; i<numSquares; i++) {
            if (next.isPresent()) {
                squares.add(this.getSquareAt(next.get()).get());
                next = this.getNextLocation(next.get(), o);
            } else {
                break;
            }
        }

        return squares;
    }

    public SquareSequence getSlice(BoardLocation start, BoardLocation end) {
        SquareSequence squares = new SquareSequence();
        BoardLocation loc = start;
        Orientation o;

        if (start.getRow() == end.getRow()) {
            o = Orientation.ACROSS;
        } else {
            o = Orientation.DOWN;
        }

        while (!loc.equals(end)) {
            squares.add(this.getSquareAt(loc).get());

            if (this.getNextLocation(loc, o).isPresent()) {
                loc = this.getNextLocation(loc, o).get();
            }
        }

        squares.add(this.getSquareAt(end).get());

        return squares;
    }

    public String getCrossword(TileLocationPair p, Orientation o) {
        String cross = "";

        Orientation perp = o.getPerpendicular();
        BoardLocation start = this.getSequenceStart(p.getLocation(), new Move(p), perp).get();
        BoardLocation end = this.getSequenceEnd(p.getLocation(), new Move(p), perp).get();

        for (BoardSquare sq: this.getSlice(start, end)) {
            if (sq.hasTile()) {
                cross += sq.getTile().get().getLetter();
            } else {
                cross += p.getTile().getLetter();
            }
        }

        if (!start.equals(end)) {
            //System.out.println(start + " " + end);
            //System.out.println(cross);
        }

        return cross;
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

        Optional<BoardLocation> validatedNext = Optional.empty();

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

    private BoardLocation getSequenceStart(BoardLocation loc, Orientation o) {
        BoardLocation start = loc;
        Optional<BoardLocation> current = this.getPreviousLocation(loc, o);

        while (current.isPresent() && this.getTileAt(current.get()).isPresent()) {
            start = current.get();
            current = this.getPreviousLocation(current.get(), o);
        }

        return start;

    }

    private Optional<BoardLocation> getSequenceStart(BoardLocation loc, Move m, Orientation o) {
        Optional<BoardLocation> start = Optional.empty();
        Optional<BoardLocation> current;

        if (this.isOnBoard(loc) && (this.getSquareAt(loc).get().hasTile() || m.getLocations().contains(loc))) {
            current = Optional.of(loc);
            //System.out.println(current.get().getRow() + " " + current.get().getColumn());

            while (current.isPresent() && (this.getSquareAt(current.get()).get().hasTile() || m.getLocations().contains(current.get()))) {
                start = current;
                current = this.getPreviousLocation(current.get(), o);
            }
        }

        return start;
    }

    private Optional<BoardLocation> getSequenceEnd(BoardLocation loc, Move m, Orientation o) {
        o = o.reverse();

        return this.getSequenceStart(loc, m, o);
    }

    public Word getWord(Move m, Orientation o) {
        BoardLocation start, end;

        start = this.getSequenceStart(m.getLocations().get(0), m, o).get();
        end = this.getSequenceEnd(m.getLocations().get(m.getLocations().size() - 1), m, o).get();

        return new Word(this.getSlice(start, end), m);
    }

    public Word getPrimaryWord(Move m) {
        BoardLocation start, end;

        start = this.getSequenceStart(m.getLocations().get(0), m, m.getOrientation()).get();
        end = this.getSequenceEnd(m.getLocations().get(0), m, m.getOrientation()).get();

        return new Word(this.getSlice(start, end), m);
    }

    public ArrayList<Word> getCrossWords(Move m) {
        // TODO: This may be broken, too!!!
        ArrayList<Word> crosswords = new ArrayList<>();
        Orientation o = m.getOrientation().getPerpendicular();
        Word w;

        for (TileLocationPair p: m) {
            w = this.getWord(new Move(p), o);
            if (w.length() > 1) {
                crosswords.add(w);
            }
        }

        return crosswords;
    }

    public ArrayList<Word> getAllWords(Move m) {
        ArrayList<Word> words = new ArrayList<>();

        if (m.size() == 0) {
            return words;
        }

        Word primaryWord = this.getPrimaryWord(m);

        if (primaryWord.length() > 1) {
            words.add(this.getPrimaryWord(m));
        }

        for (Word w: this.getCrossWords(m)) {
            words.add(w);
        }

        return words;
    }

    public BoardLocation getWordStart(Word w) {
        return this.locationFromSquare(w.getBoardSquares().get(0));
    }

    public BoardLocation getWordEnd(Word w) {
        BoardSquare lastSquare = w.getBoardSquares().get(w.getBoardSquares().size()-1);
        return  this.locationFromSquare(lastSquare);
    }

    public boolean play(Move m) {
        int numTiles = m.getTiles().size();
        Optional<BoardSquare> sq;

        for (int i=0; i<numTiles; i++) {
            sq = this.getSquareAt(m.getLocations().get(i));

            if (sq.isPresent()) {
                sq.get().addTile(m.getTiles().get(i));
            } else {
                // May result in half-played words
                return false;
            }
        }
        this.isFirstPlay = false;
        return true;
    }

    public String toString(boolean showMultipliers) {
        if (showMultipliers) {
            return this.toString();
        } else {
            String s = "";

            for (int i=0; i<this.rows; i++) {
                for (int j=0; j<this.columns; j++) {
                    if (this.squares[i][j].hasTile()) {
                        if (this.squares[i][j].getTile().get().isBlank()) {
                            s += "*";
                        } else {
                            s += this.squares[i][j].getTile().get().getLetter();
                        }
                    } else
                        s += " ";

                    if (j != this.columns) {
                        s += " ";
                    }
                }
                if (i != this.rows-1) {
                    s += "\n";
                }
            }

            return s;
        }
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
            if (i != this.rows-1) {
                s += "\n";
            }
        }

        return s;
    }
}
