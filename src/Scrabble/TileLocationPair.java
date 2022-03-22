package Scrabble;

import Scrabble.BoardLocation;

public class TileLocationPair {
    private LetterTile tile;
    private BoardLocation location;

    public TileLocationPair(LetterTile t, BoardLocation loc) {
        this.tile = t;
        this.location = loc;
    }

    public LetterTile getTile() {
        return this.tile;
    }

    public BoardLocation getLocation() {
        return this.location;
    }
}
