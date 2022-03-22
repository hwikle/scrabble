import Scrabble.*;

import java.util.ArrayList;

public class GetMovesTest {
    public static void main(String[] args) {
        Board b = new Board(7);
        String boardString = "3. .. .. 2. .. .. 3.\n" + ".. .3 .. .. .. .3 ..\n"
                + ".. ..  a  d .2 .. ..\n" + "2. ..  u  h .. .. 2.\n" + ".. ..  l  o .2 .. ..\n"
                + "..  m  a  t .. .3 ..\n" + " r  e  S  i  d .. 3.";
        b.populateFromString(boardString);

        LetterTray t = new LetterTray("toloeri");
        WordTree tr = new WordTree();
        tr.populateFromFile("/Users/hank/IdeaProjects/cs351/scrabble/resources/sowpods.txt");

        System.out.println(tr.getAllWords().size() + " words in word tree");

        String testWord = "TROOLIE";
        BoardLocation testLoc = new BoardLocation(0, 6);
        assert tr.contains(testWord): "Not in dictionary";

        LetterScores scores = new LetterScores("/Users/hank/IdeaProjects/cs351/scrabble/resources/letterscores.txt");

        ArrayList<Move> moves = b.getPossibleMoves(testLoc, Orientation.DOWN, t, tr);
        System.out.println(moves);
        System.out.println(moves.size());

        boolean inMoves = false;

        for (Move m: moves) {
            if (m.toString().equals(testWord)) {
                inMoves = true;
            }
        }

        if (inMoves) {
            System.out.println(testWord + " in possible moves");
        } else {
            System.out.println(testWord + " not in possible moves");
        }

        System.out.println(b);
        System.out.println(b.getSquareAt(new BoardLocation(0, 6)).get().getWordMultiplier());
        System.out.println(moves.size() + " moves found");

        int score = 0;
        int bestScore = 0;
        Move bestMove = new Move();
        int maxLength = 0;
        Move longest = new Move();

        for (Move m: moves) {
            if (m.size() > maxLength) {
                maxLength = m.size();
                longest = m;
            }
            score = b.score(m, scores);

            if (score > bestScore) {
                bestScore = score;
                bestMove = m;
            }
        }

        System.out.println("Best move: " + bestMove);
        System.out.println("Score: " + bestScore);
        System.out.println("Longest: " + longest.toString());
        b.score(bestMove, scores);
    }
}
