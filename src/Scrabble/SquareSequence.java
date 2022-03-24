package Scrabble;

import java.util.ArrayList;
import java.util.Optional;
import java.util.Collections;

public class SquareSequence extends ArrayList<BoardSquare> {

    public static SquareSequence fromBoard(Board b, BoardLocation loc, Orientation o, int numSquares) {
        SquareSequence seq = new SquareSequence();
        Optional<BoardSquare> square = b.getSquareAt(loc);

        while (square.isPresent() && numSquares > 0) {
            seq.add(square.get());
            loc = b.getNextLocation(loc, o).get();
            numSquares--;
        }

        return seq;
    }

    private Optional<BoardSquare> getFirstTile() {
        for (BoardSquare sq: this) {
            if (sq.hasTile()) {
                return Optional.of(sq);
            }
        }
        return null;
    }

    private Optional<BoardSquare> getLastTile() {
        SquareSequence reverse = new SquareSequence();
        Collections.copy(reverse, this);
        Collections.reverse(reverse);

        return reverse.getFirstTile();
    }

    public boolean hasAnyTiles() {
        for (BoardSquare sq: this) {
            if (sq.hasTile()) {
                return true;
            }
        }
        return false;
    }

    public int score(LetterScores ls) {
        int wordMultiplier = 1;
        int score = 0;
        LetterTile t;

        for (BoardSquare sq: this) {
            if (sq.hasTile()) {
                t = sq.getTile().get();
                score += sq.getLetterMultiplier()*ls.get(t.getLetter());
                wordMultiplier *= sq.getWordMultiplier();
            }
        }

        return score * wordMultiplier;
    }

    public String toVariableLengthRegex(Board b, LetterTray tray) {
        String s = "";
        BoardSquare sq;
        int repeatedBlanks = 0;
        boolean anchorEncountered = false;

        int i = 0;

        while (!anchorEncountered) {
            sq = this.get(i);
            anchorEncountered = b.isAnchor(sq);

            if (!sq.hasTile()) {
                repeatedBlanks++;
            } else {

            }
        }

        return s;
    }

    public boolean fitsWith(Board b, Move m) {
        BoardLocation loc;
        for (BoardSquare sq: this) {
            loc = b.locationFromSquare(sq);
            if (sq.hasTile() && m.getLocations().contains(loc)) {
                return false;
            } else if (!sq.hasTile() && !m.getLocations().contains(loc)) {
                return false;
            }
        }

        return true;
    }
}
