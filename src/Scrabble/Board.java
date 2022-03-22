package Scrabble;

import java.util.*;

public class Board {
    int rows;
    int columns;
    BoardSquare[][] squares;

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
                        //System.out.println(Character.digit(tileStr.charAt(0), 10));
                        sq.setWordMultiplier(Character.digit(tileStr.charAt(0), 10));
                    } else if (Character.isDigit(tileStr.charAt(1))) {
                        sq.setLetterMultiplier(Character.digit(tileStr.charAt(1), 10));
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

    public ArrayList<Move> getPossibleMoves(BoardLocation loc, Orientation o, LetterTray tray, WordTree tree) {
        ArrayList<Move> moves = new ArrayList<>();
        char c;

        assert this.isOnBoard(loc);

        if (tray.size() >= 0) {
            Optional<LetterTile> t = this.getTileAt(loc); // Empty could mean multiple things; need check that loc is on board
            Optional<BoardLocation> next = this.getNextLocation(loc, o); // Could be empty
            Optional<String> cross;
            BoardLocation start, end;

            /*
            if (!next.isPresent()) {
                if (!t.isEmpty()) {
                    return new ArrayList<Move>();
                } else {

                }
            }

             */

            if (t.isPresent()) {
                if (tree.keySet().contains(t.get().getLetter())) {
                    c = t.get().getLetter();

                    // I think edge-of-board bug is here
                    if (next.isPresent()) {
                        moves.addAll(this.getPossibleMoves(next.get(), o, tray, tree.get(c)));
                    } else if (tree.get(c).keySet().contains(tree.getTerminator())) {
                        moves.add(new Move());
                    }
                }
            } else {
                for (char ch : tree.keySet()) {
                    if (ch == tree.getTerminator()) {
                        moves.add(new Move());
                        // Possibly here as well
                    } else if (next.isPresent() && tray.hasLetter(ch)) {
                    //} else if (tray.hasLetter(ch)) {

                        LetterTray trayCopy = new LetterTray();
                        trayCopy.addAll(tray);
                        LetterTile tile = trayCopy.getTileByLetter(ch).get();

                        // Look for crossword
                        start = this.getSequenceStart(loc, new Move(new TileLocationPair(tile, loc)), o.getPerpendicular()).get();
                        end = this.getSequenceEnd(loc, new Move(new TileLocationPair(tile, loc)), o.getPerpendicular()).get();

                        if (!start.equals(end)) {
                            cross = this.wordFromMove(start, end, new Move(new TileLocationPair(tile, loc)));

                            if (!tree.keySet().contains(cross.get())) {
                                continue;
                            }
                        }

                        trayCopy.remove(tile);

                        for (Move m : this.getPossibleMoves(next.get(), o, trayCopy, tree.get(ch))) {
                            Move newMove = new Move();
                            newMove.add(new TileLocationPair(tile, loc));
                            newMove.addAll(m);
                            moves.add(newMove);
                        }
                    } else if (tray.hasLetter(ch)) {
                        if (tree.get(ch).keySet().contains(tree.getTerminator())) {
                            LetterTray trayCopy = new LetterTray();
                            trayCopy.addAll(tray);
                            LetterTile tile = trayCopy.getTileByLetter(ch).get();
                            moves.add(new Move(new TileLocationPair(tile, loc)));
                        }
                    }
                }
            }
        }

        return moves;
    }

    /*
    public ArrayList<Move> getPossibleMoves2(BoardLocation loc, Orientation o, LetterTray tray, WordTree tree) {
        ArrayList<Move> moves = new ArrayList<>();
        Optional<BoardLocation> next = this.getNextLocation(loc, o);
        BoardSquare sq = this.getSquareAt(loc).get();

        if (sq.hasTile()) {
            char letter = sq.getTile().get().getLetter();
            if (tree.keySet().contains(letter)) {
                if (next.isPresent()) {

                }
            } else {
                return moves;
            }

        } else {

        }

        if (!this.getNextLocation(loc, o).isPresent()) {
            // Do something
        } else {
            // Do something
        }
    }

     */

    public boolean isAnchor(BoardLocation loc) {
        if (!this.isOnBoard(loc)) {
            return false;
        } else if (this.getTileAt(loc).isPresent()) {
            return true;
        } else {
            return this.neighborHasTile(loc);
        }
    }

    public boolean isAnchor(BoardSquare sq) {
        return this.isAnchor(this.locationFromSquare(sq));
    }

    private BoardLocation locationFromSquare(BoardSquare sq) {
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
        Orientation o = Orientation.ACROSS;
        ArrayList<BoardLocation> neighbors = this.getNeighbors(loc);

        for (BoardLocation e: neighbors) {
            if (this.getTileAt(e).isPresent()) {
                return true;
            }
        }
        return false;
    }

    public SquareSequence reachableFrom(BoardLocation loc, Orientation o, int traySize) {
        SquareSequence squares = new SquareSequence();

        if (!this.isOnBoard(loc)) {
            return squares;
        }

        Optional<BoardLocation> current = Optional.of(loc);
        BoardSquare sq;

        while (traySize > 0 && current.isPresent()) {
            sq = this.getSquareAt(current.get()).get();
            squares.add(sq);

            if (!sq.hasTile()) {
                traySize--;
            }

            current = this.getNextLocation(current.get(), o);
        }

        return squares;
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

        //System.out.println("Start: " + start);
        //System.out.println("End: " + end);

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

    public Optional<String> wordFromMove(BoardLocation start, BoardLocation end, Move m) {
        Optional<String> word = Optional.empty();
        String s = "";
        BoardLocation loc = start;
        LetterTile t;
        Orientation o;

        if (start.getRow() == end.getRow()) {
            o = Orientation.ACROSS;
        } else {
            o = Orientation.DOWN;
        }

        while (loc != end) {
            if (!this.getSquareAt(loc).get().hasTile() && !m.getLocations().contains(loc)) {
                return word;
            }
            if (this.getSquareAt(loc).get().hasTile()) {
                t = this.getSquareAt(loc).get().getTile().get();
            } else {
                t = m.getTiles().get(m.getLocations().indexOf(loc));
            }
            s += t.getLetter();

            if (this.getNextLocation(loc, o).isPresent()) {
                loc = this.getNextLocation(loc, o).get();
            }
        }

        word = Optional.of(s);

        return word;

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

    public SquareSequence getPrimaryWord(Move m) {
        Orientation o;
        SquareSequence word = new SquareSequence();
        BoardLocation start, end;

        if (m.size() == 0) {
            return word;
        }

        if (!m.isSingleTile()) {
            if (m.getOrientation().isPresent()) {
                o = m.getOrientation().get();
            } else {
                o = Orientation.ACROSS;
            }
            start = this.getSequenceStart(m.getLocations().get(0), m, o).get();
            end = this.getSequenceEnd(m.getLocations().get(0), m, o).get();

            return this.getSlice(start, end);

        } else {
            start = m.getLocations().get(0);
        }

        return word;
    }

    public ArrayList<SquareSequence> getCrossWords(Move m) { return  new ArrayList<SquareSequence>();}

    public ArrayList<SquareSequence> getAllWords(Move m) {
        ArrayList<SquareSequence> words = new ArrayList<>();
        words.add(this.getPrimaryWord(m));
        words.addAll(this.getCrossWords(m));

        return words;
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

        return true;
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

    public int score(Move m, LetterScores ls) {
        // Needs to take into account new words created by crossing
        int score = 0;
        int wordMultiplier = 1;
        int moveIdx = 0;

        SquareSequence primary = this.getPrimaryWord(m);

        for (BoardSquare sq: primary) {
            if (sq.hasTile()) {
                score += ls.get(sq.getTile().get().getLetter());
            } else {
                score += ls.get(m.get(moveIdx).getTile().getLetter()) * sq.getLetterMultiplier();
                wordMultiplier *= sq.getWordMultiplier();
                moveIdx++;
            }
        }

        score *= wordMultiplier;
        /*
        ArrayList<Scrabble.SquareSequence> words = this.getAllWords(m);

        for (Scrabble.SquareSequence w: words) {
            score += w.score(ls);
        }
         */

        return score;
    }

    private SquareSequence getMaxSquareSequence(BoardLocation loc, Orientation o, int traySize) {
        SquareSequence seq = new SquareSequence();
        Optional<BoardLocation> current = Optional.of(loc);
        BoardSquare square;

        while (traySize > 0 && current.isPresent()) {
            square = this.getSquareAt(current.get()).get();
            seq.add(square);
            current = this.getNextLocation(current.get(), o);
            traySize--;
        }

        return seq;
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
