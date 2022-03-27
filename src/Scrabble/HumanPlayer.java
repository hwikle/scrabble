package Scrabble;

import java.util.Optional;

public class HumanPlayer extends Player {

    public HumanPlayer() {
        super();
    }

    public HumanPlayer(String name, LetterTray tray) {
        super(name, tray);
    }

    public Optional<Move> getMove(Board b) {
        return null;
    }
}
