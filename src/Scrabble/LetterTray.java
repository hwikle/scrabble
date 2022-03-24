package Scrabble;

import org.w3c.dom.ls.LSOutput;

import java.util.ArrayList;
import java.util.Optional;

public class LetterTray extends ArrayList<LetterTile> implements Cloneable {
    public LetterTray() {}

    public LetterTray(String letters) {
        for (int i=0; i<letters.length(); i++) {
            this.add(new LetterTile(letters.charAt(i)));
        }
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
            if (t.isBlank()) {
                return true;
            }
        }

        return false;
    }

    public Optional<LetterTile> getBlank() {
        for (LetterTile t: this) {
            if (t.isBlank()) {
                return Optional.of(t);
            }
        }

        return Optional.empty();
    }

    @Override
    public String toString() {
        String s = "";

        for (LetterTile t: this) {
            if (t.isBlank()) {
                s += t.getLetter();
            } else {
                s += Character.toLowerCase(t.getLetter());
            }
        }
        return s;
    }
}
