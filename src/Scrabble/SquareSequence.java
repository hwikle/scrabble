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

}
