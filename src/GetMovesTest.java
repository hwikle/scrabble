import Scrabble.*;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.HashMap;

public class GetMovesTest {
    public static void main(String[] args) {
        Board b = new Board(7);
        String boardString = "3. .. .. 2. .. .. 3.\n" + ".. .3 .. .. .. .3 ..\n"
                + ".. ..  a  d .2 .. ..\n" + "2. ..  u  h .. .. 2.\n" + ".. ..  l  o .2 .. ..\n"
                + "..  m  a  t .. .3 ..\n" + " r  e  S  i  d .. 3.";
        b.populateFromString(boardString);

        LetterTray t = new LetterTray(7, "toloeri");
        WordTree tr = new WordTree();
        tr.populateFromFile("/Users/hank/IdeaProjects/cs351/scrabble/resources/sowpods.txt");

        System.out.println(tr.getAllWords().size() + " words in word tree");

        String testWord = "TROOLIE";
        BoardLocation testLoc = new BoardLocation(0, 5);
        assert tr.contains(testWord): "Not in dictionary";

        LetterScores scores = new LetterScores("/Users/hank/IdeaProjects/cs351/scrabble/resources/letterscores.txt");

        ArrayList<Move> moves = b.getGlobalPossibleMoves(t, tr);
        //System.out.println(moves);
        System.out.println(moves.size() + " possible moves found");

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
        //System.out.println(b.getSquareAt(new BoardLocation(1, 6)).get().getWordMultiplier());
        System.out.println(moves.size() + " moves found");

        //int score = 0;
        MoveScore ms;
        int bestScore = 0;
        Move bestMove = new Move();
        int maxLength = 0;
        Move longest = new Move();

        for (Move m: moves) {
            if (m.size() > maxLength) {
                maxLength = m.size();
                longest = m;
            }
            ms = b.scoreAllWords(m, scores, 7);

            if (ms.getScore() > bestScore) {
                bestScore = ms.getScore();
                bestMove = m;
            }
        }

        try {
            System.out.println("Best move: " + bestMove + " at " + bestMove.getLocations().get(0));
            System.out.println("Score: " + bestScore);

            ArrayList<Word> allWords = b.getAllWords(bestMove);

            System.out.println("Individual word scores: ");
            for (Word w: allWords) {
                System.out.println(w.toString(b) + ": " + b.score(w, scores));
            }

            System.out.println("Word length frequencies: ");
            HashMap<Integer, Integer> freqs = new HashMap<>();

            for (int i=0; i<8; i++) {
                freqs.put(i, 0);
            }

            int count;

            for (Move m: moves) {
                count = freqs.get(m.size());
                freqs.put(m.size(), count+1);
            }

            System.out.println(freqs);
        } catch (IndexOutOfBoundsException e) {
            System.out.println("No moves found.");
        }
    }
}
