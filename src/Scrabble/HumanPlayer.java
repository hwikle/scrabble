package Scrabble;

import java.util.ArrayList;
import java.util.Optional;

public class HumanPlayer extends Player {
    private Move selected;
    private boolean readyToPlay;

    public HumanPlayer() {
        super();
    }

    public HumanPlayer(String name, LetterTray tray) {
        super(name, tray);
    }

    public Optional<Move> getMove(Board b) {
        readyToPlay = false;
        return Optional.of(this.selected);
    }

    public boolean isReady() {
        return this.readyToPlay;
    }

    public void setSelected(ArrayList<LetterTile> tiles, ArrayList<BoardSquare> squares, Board b) {
        ArrayList<BoardLocation> locs = new ArrayList<>();

        for (BoardSquare sq: squares) {
            locs.add(b.locationFromSquare(sq));
        }

        this.selected = new Move(tiles, locs);
        readyToPlay = true;
    }

    public boolean canPlay(Board b) {
        return true;
    }
}
