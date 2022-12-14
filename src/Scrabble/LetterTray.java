package Scrabble;

import org.w3c.dom.ls.LSOutput;

import java.util.ArrayList;
import java.util.Optional;

public class LetterTray extends ArrayList<LetterTile> implements Cloneable {
    private int capacity;

    public LetterTray(int capacity) {
        this.capacity = capacity;
    }

    public LetterTray(int capacity, String letters) {
        this.capacity = capacity;

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

    public void removeTiles(Move m) {
        LetterTile blankTile;

        for (LetterTile t: m.getTiles()) {
            if (t.isBlank()) {
                blankTile = this.getBlank().get();
                this.remove(blankTile);
            } else {
                this.remove(t);
            }
        }
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
