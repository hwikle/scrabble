package Scrabble;

import java.util.ArrayList;
import java.util.Optional;
import java.util.Random;

/**

 * @author Hank Wikle
 */
public class ComputerPlayer extends Player {
    private WordTree dict;

    public ComputerPlayer() {
        super();
    }

    public ComputerPlayer(LetterTray t) {
        super(t);
    }

    public ComputerPlayer(String name, LetterTray t) {
        super(name, t);
    }

    public void setDictionary(WordTree dict) {
        this.dict = dict;
    }

    public Optional<Move> getMove(Board b) {
        ArrayList<Move> moves = b.getGlobalPossibleMoves(this.tray, this.dict);
        Random rand = new Random();

        if (moves.size() > 0) {
            return Optional.of(moves.get(rand.nextInt(moves.size())));
        } else {
            return Optional.empty();
        }
    }


}
