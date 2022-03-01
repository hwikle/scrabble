import java.util.ArrayList;
import java.util.Optional;

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

    public boolean hasAnyTiles() {
        for (BoardSquare sq: this) {
            if (sq.hasTile()) {
                return true;
            }
        }
        return false;
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
}
