import java.util.ArrayList;

public class LetterTray extends ArrayList<LetterTile> {
    public String toRegexRange() {
        String s = "";

        for (LetterTile t: this) {
            if (t.getLetter() != ' ') {
                s += t.getLetter();
            }
        }

        return "[" + s + "]";
    }
}
