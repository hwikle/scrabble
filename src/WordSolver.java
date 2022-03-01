import java.util.ArrayList;

public class WordSolver {
    public static void main(String[] args) {
        Board b = new Board(7);
        String boardsString = "3. .. .. 2. .. .. 3.\n" + ".. .3 .. .. .. .3 ..\n"
                + ".. ..  a  d .2 .. ..\n" + "2. ..  u  h .. .. 2.\n" + ".. ..  l  o .2 .. ..\n"
                + "..  m  a  t .. .3 ..\n" + " r  e  S  i  d .. 3.";
        b.populateFromString(boardsString);

        LetterTray t = new LetterTray("toloeri");
        Dictionary d = new Dictionary("/Users/hank/IdeaProjects/cs351/scrabble/sowpods.txt");

        BoardLocation loc;
        SquareSequence seq;
        Orientation o = Orientation.ACROSS;
        ArrayList<String> words = new ArrayList<>();

        for (int i=0; i<b.rows; i++) {
            for (int j=0; j<b.columns; j++) {
                loc = new BoardLocation(i, j);

                for (int k = 2; k < b.columns + 1; k++) {
                    seq = b.getSquares(loc, k, o);
                    if (seq.hasAnyTiles()) {
                        words.addAll(d.Query(seq, t));
                    }
                }
            }
        }

        System.out.println(words);
        System.out.println("Found " + words.size() + " valid words");
    }
}
