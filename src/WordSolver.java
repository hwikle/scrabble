import Scrabble.*;

import java.util.ArrayList;

public class WordSolver {
    public static void main(String[] args) {
        Board b = new Board(7);
        String boardsString = "3. .. .. 2. .. .. 3.\n" + ".. .3 .. .. .. .3 ..\n"
                + ".. ..  a  d .2 .. ..\n" + "2. ..  u  h .. .. 2.\n" + ".. ..  l  o .2 .. ..\n"
                + "..  m  a  t .. .3 ..\n" + " r  e  S  i  d .. 3.";
        b.populateFromString(boardsString);

        LetterTray t = new LetterTray("toloeri");
        //Dictionary d = new Dictionary("/Users/hank/IdeaProjects/cs351/scrabble/resources/sowpods.txt");
        WordTree tr = new WordTree();
        tr.populateFromFile("/Users/hank/IdeaProjects/cs351/scrabble/resources/sowpods.txt");
        LetterScores scores = new LetterScores("/Users/hank/IdeaProjects/cs351/scrabble/resources/letterscores.txt");

        BoardLocation loc;
        SquareSequence seq;
        Orientation o = Orientation.DOWN;
        ArrayList<String> words;

        int score;
        MoveScore ms;
        int bestScore = 0;
        Move bestMove = new Move();

        for (int i=0; i<b.getRows(); i++) {
            for (int j=0; j<b.getColumns(); j++) {
                loc = new BoardLocation(i, j);

                for (Move m: b.getPossibleMoves(loc, o, t, tr, tr)) {
                    ms = b.scoreAllWords(m, scores, 7);

                    if (ms.getScore() > bestScore) {
                        bestScore = ms.getScore();
                        bestMove = m;
                    }
                }

                /*
                for (int k = 2; k < b.columns + 1; k++) {
                    seq = b.getSlice(loc, k, o);
                    if (seq.hasAnyTiles()) {
                        words = d.Query(seq, t);

                        for (String w: words) {

                            score = b.score(new Scrabble.Move(w, loc, o), scores);


                            if (score > maxScore) {
                                bestWord = w;
                                maxScore = score;
                                bestLoc = loc;
                            }
                        }
                    }
                }
                */
            }
        }
        System.out.println(bestMove + " for " + bestScore + " points");
    }
}
