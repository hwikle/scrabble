package Scrabble;

import Scrabble.BoardLocation;
import Scrabble.Orientation;

import java.util.ArrayList;
import java.util.Optional;

public class Move extends ArrayList<TileLocationPair> {

    public Move() {}

    public Move(TileLocationPair pair) {
        this.add(pair);
    }

    public ArrayList<LetterTile> getTiles() {
        ArrayList<LetterTile> tiles = new ArrayList<>();

        for (TileLocationPair pair: this) {
            tiles.add(pair.getTile());
        }

        return tiles;
    }

    public ArrayList<BoardLocation> getLocations() {
        ArrayList<BoardLocation> locs = new ArrayList<>();

        for (TileLocationPair pair: this) {
            locs.add(pair.getLocation());
        }

        return locs;
    }

    public boolean isSingleTile() {
        return (this.size() == 1);
    }

    public Optional<Orientation> getOrientation() {
        // must be Optional since single letter plays have no defined orientation
        Optional<Orientation> o = Optional.empty();

        if (this.size() > 1) {
            if (this.get(0).getLocation().getRow() == this.get(1).getLocation().getRow()) {
                o = Optional.of(Orientation.ACROSS);
            } else {
                o = Optional.of(Orientation.DOWN);
            }
        }

        return o;
    }

    public String toString() {
        String s = "";

        for (TileLocationPair pair: this) {
            s += pair.getTile().getLetter();
        }

        return s;
    }
}
