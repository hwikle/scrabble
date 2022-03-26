package Scrabble;

import java.util.ArrayList;
import java.util.Optional;
import java.util.Random;

/**

 * @author Hank Wikle
 */
public class ComputerPlayer extends Player {
    private WordTree dict;
    private WordScorer scorer;

    public ComputerPlayer() {
        super();
    }

    public ComputerPlayer(LetterTray t, WordScorer scorer) {
        super(t);
        this.scorer = scorer;
    }

    public ComputerPlayer(String name, LetterTray t) {
        super(name, t);
    }

    public void setDictionary(WordTree dict) {
        this.dict = dict;
    }

    public Optional<Move> getMove(Board b) {
        ArrayList<Move> moves = b.getGlobalPossibleMoves(this.tray, this.dict);

        if (moves.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(this.scorer.getBestMove(b, moves, this.tray.getCapacity()));
    }


}
