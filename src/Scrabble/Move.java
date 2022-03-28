package Scrabble;

import Scrabble.BoardLocation;
import Scrabble.Orientation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Optional;

public class Move extends ArrayList<TileLocationPair> {
    public Move() {}

    public Move(TileLocationPair pair) {
        this.add(pair);
    }

    public Move(ArrayList<LetterTile> tiles, ArrayList<BoardLocation> locs) {
        for (int i=0; i<tiles.size(); i++) {
            this.add(new TileLocationPair(tiles.get(i), locs.get(i)));
        }

        //this.sort();
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

    public Optional<LetterTile> tileByLocation(BoardLocation loc) {
        Optional<LetterTile> tile = Optional.empty();

        for (TileLocationPair p: this) {
            if (p.getLocation().equals(loc)) {
                return Optional.of(p.getTile());
            }
        }

        return tile;
    }

    public boolean isLinear() {
        ArrayList<BoardLocation> locs = this.getLocations();
        Orientation o;

        if (locs.size() < 2) {
            return true;
        } else if (locs.get(0).getRow() == locs.get(1).getRow()) {
            o = Orientation.ACROSS;
        } else if (locs.get(0).getColumn() == locs.get(1).getColumn()) {
            o = Orientation.DOWN;
        } else {
            return false;
        }

        for (int i=2; i<locs.size(); i++) {
            if (!(locs.get(0).isLinearWith(locs.get(i), o))) {
                return false;
            }
        }
        return true;
    }

    public Orientation getOrientation() {
        Orientation o;

        if (this.size() > 1) {
            if (this.get(0).getLocation().getRow() == this.get(1).getLocation().getRow()) {
                o = Orientation.ACROSS;
            } else {
                o = Orientation.DOWN;
            }
        } else {
            o = Orientation.ACROSS; // By convention, define single-letter moves as ACROSS
        }

        return o;
    }

    private void sort() {
        Orientation o = this.getOrientation();

        Collections.sort(this, new Comparator<TileLocationPair>() {
            @Override
            public int compare(TileLocationPair o1, TileLocationPair o2) {
                if (o.equals(Orientation.ACROSS)) {
                    return Integer.compare(o1.getLocation().getColumn(), o2.getLocation().getColumn());
                } else {
                    return Integer.compare(o1.getLocation().getRow(), o2.getLocation().getRow());
                }
            }
        });
    }

    public String toString() {
        String s = "";

        for (TileLocationPair pair: this) {
            s += pair.getTile().getLetter();
        }

        return s;
    }
}
