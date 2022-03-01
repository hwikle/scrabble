import java.util.ArrayList;
import java.util.Optional;

public class Constraint {
    private BoardLocation anchor;
    private Orientation orientation;
    private ArrayList<LetterTile> tiles;
    private BoardLocation firstPossible;
    private BoardLocation lastPossible;

    public Constraint(BoardLocation anchor, Orientation o, LetterTray t) {
        this.anchor = anchor;
        this.orientation = o;
        this.tiles = t;
    }

    private void getFirstPossibleLocation(Board b) {
        int numTiles = this.tiles.size();
        Optional<BoardLocation> loc = Optional.of(this.anchor);

        while (numTiles > 0) {
            loc = b.getPreviousLocation(this.anchor, this.orientation);

            if (loc.isPresent() && !b.getSquareAt(loc.get()).get().hasTile()) {
                numTiles--;
            }
        }

        this.firstPossible = loc.get();
        System.out.println(this.firstPossible);
    }

    private void getLastPossibleLocation(Board b) {
        int numTiles = this.tiles.size();
        Optional<BoardLocation> loc = Optional.of(this.anchor);

        while (numTiles > 0) {
            loc = b.getNextLocation(this.anchor, this.orientation);

            if (loc.isPresent() && !b.getSquareAt(loc.get()).get().hasTile()) {
                numTiles--;
            }
        }

        this.lastPossible = loc.get();
        System.out.println(this.lastPossible);
    }

    public SquareSequence getSquareSequence(Board b) {
        SquareSequence seq = new SquareSequence();

        this.getFirstPossibleLocation(b);
        this.getLastPossibleLocation(b);

        Optional<BoardLocation> loc = Optional.of(this.firstPossible);

        do {
            seq.add(b.getSquareAt(loc.get()).get());
            loc = b.getNextLocation(loc.get(), this.orientation);
            System.out.println(seq.size());
        } while (loc.isPresent() && loc.get() != this.lastPossible);

        return seq;
    }
}
