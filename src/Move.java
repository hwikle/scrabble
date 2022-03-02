import java.util.ArrayList;

public class Move {
    //private ArrayList<LetterTile> word;
    private String word;
    private BoardLocation location;
    private Orientation orientation;

    public Move(String word, BoardLocation loc, Orientation o) {
        this.word = word;
        this.location = loc;
        this.orientation = o;
        /*
        for (char c: word.toCharArray()) {
            this.word.add(new LetterTile(c));
        }

         */
    }

    public String getWord() {
        return this.word;
    }

    public BoardLocation getLocation() {
        return this.location;
    }

    public Orientation getOrientation() {
        return orientation;
    }
}
