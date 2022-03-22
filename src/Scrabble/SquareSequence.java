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

    public String toRegex() {
        String s = "";
        int repeatedCharacters = 0;

        for (BoardSquare sq: this) {
            if (!sq.hasTile()) {
                repeatedCharacters++;
            } else {
                if (repeatedCharacters >= 1) {
                    s += ".";
                }
                if (repeatedCharacters > 1) {
                    s += "{" + repeatedCharacters + "}";
                }

                s += sq.getTile().get().getLetter();
            }
        }


        return "^" + s + "$";
     }

    public String toRegex(LetterTray tray) {
        String s = "";
        int repeatedChars = 0;

        for (BoardSquare sq: this) {
            if (!sq.hasTile()) {
                repeatedChars++;
            } else {
                if (repeatedChars >= 1) {
                    if (tray.hasBlank()) {
                        s += "[a-z]";
                    } else {
                        s += tray.toRegexRange();
                    }
                }
                if (repeatedChars > 1) {
                    s += "{" + repeatedChars + "}";
                }

                s += sq.getTile().get().getLetter();
                repeatedChars = 0;
            }
        }

        if (repeatedChars >= 1) {
            s += tray.toRegexRange();
        }
        if (repeatedChars > 1) {
            s += "{" + repeatedChars + "}";
        }

        return "^" + s + "$";
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
}