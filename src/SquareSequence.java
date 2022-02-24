import java.util.ArrayList;

public class SquareSequence extends ArrayList<BoardSquare> {

    public String toRegex() {
        String s = "";
        char letter;

        for (BoardSquare sq: this) {
            letter = sq.getTile().getLetter();

            switch (letter) {
                case '0':
                case ' ':
                    s += '.';
                    break;
                default:
                    s += letter;
            }
        }

        return "^" + s + "$";
     }

    public String toRegex(LetterTray tray) {
        String s = "";
        char letter;
        int repeatedChars = 0;

        for (BoardSquare sq: this) {
            letter = sq.getTile().getLetter();

            switch (letter) {
                case '0':
                case ' ':
                    repeatedChars++;
                    break;
                default:
                    if (repeatedChars >= 1) {
                        s += tray.toRegexRange();
                    }
                    if (repeatedChars > 1) {
                        s += "{" + repeatedChars + "}";
                    }
                    repeatedChars = 0;
                    s += letter;
            }
        }

        if (repeatedChars >= 1) {
            s += tray.toRegexRange();
        }
        if (repeatedChars > 1) {
            s += "{" + repeatedChars + "}";
        }

        return "^" + s + "$";
    }
}
