import java.util.ArrayList;

public class LetterTray extends ArrayList<LetterTile> {
    public LetterTray() {}

    public LetterTray(String letters) {
        for (int i=0; i<letters.length(); i++) {
            this.add(new LetterTile(letters.charAt(i)));
        }
    }

    public String toRegexRange() {
        String s = "";

        for (LetterTile t: this) {
            if (t.getLetter() != ' ') {
                s += t.getLetter();
            }
        }

        return "[" + s + "]";
    }

    public boolean hasBlank() {
        for (LetterTile t: this) {
            if (t.getLetter() == ' ') {
                return true;
            }
        }

        return false;
    }
}
