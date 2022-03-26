package Scrabble;

import java.util.ArrayList;
import java.util.Optional;

public class LetterTray extends ArrayList<LetterTile> implements Cloneable {
    private int capacity;
    public LetterTray(int capacity) {}

    public LetterTray(int capacity, String letters) {

        for (int i=0; i<Math.min(letters.length(), capacity); i++) {
            this.add(new LetterTile(letters.charAt(i)));
        }
    }

    public int getCapacity() {
        return this.capacity;
    }

    public boolean hasLetter(char c) {
        for (LetterTile t: this) {
            if (t.getLetter() == c) {
                return true;
            }
        }
        return false;
    }

    public Optional<LetterTile> getTileByLetter(char c) {
        for (LetterTile t: this) {
            if (t.getLetter() == c) {
                return Optional.of(t);
            }
        }

        return null;
    }

    public boolean hasBlank() {
        for (LetterTile t: this) {
            if (t.getLetter() == ' ') {
                return true;
            }
        }

        return false;
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

    public LetterTray clone() {
        // TODO: Implement
        return this;
    }
}
