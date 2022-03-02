import java.util.ArrayList;

public class WordSolver {
    public static void main(String[] args) {
        Board b = new Board(7);
        String boardsString = "3. .. .. 2. .. .. 3.\n" + ".. .3 .. .. .. .3 ..\n"
                + ".. ..  a  d .2 .. ..\n" + "2. ..  u  h .. .. 2.\n" + ".. ..  l  o .2 .. ..\n"
                + "..  m  a  t .. .3 ..\n" + " r  e  S  i  d .. 3.";
        b.populateFromString(boardsString);

        LetterTray t = new LetterTray("toloeri");
        Dictionary d = new Dictionary("/Users/hank/IdeaProjects/cs351/scrabble/resources/sowpods.txt");
        LetterScores scores = new LetterScores("/Users/hank/IdeaProjects/cs351/scrabble/resources/letterscores.txt");

        BoardLocation loc;
        SquareSequence seq;
        Orientation o = Orientation.DOWN;
        ArrayList<String> words;

        int score;
        int maxScore = 0;
        String bestWord = "";
        BoardLocation bestLoc = new BoardLocation(0, 0);

        for (int i=0; i<b.rows; i++) {
            for (int j=0; j<b.columns; j++) {
                loc = new BoardLocation(i, j);

                for (int k = 2; k < b.columns + 1; k++) {
                    seq = b.getSquares(loc, k, o);
                    if (seq.hasAnyTiles()) {
                        words = d.Query(seq, t);

                        for (String w: words) {
                            score = b.score(new Move(w, loc, o), scores);
                            if (score > maxScore) {
                                bestWord = w;
                                maxScore = score;
                                bestLoc = loc;
                            }
                        }
                    }
                }
            }
        }
        System.out.println(bestWord + " at " + bestLoc + " for " + maxScore + " points");
    }
}
