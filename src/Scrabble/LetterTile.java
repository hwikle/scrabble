package Scrabble;

public class LetterTile implements Cloneable {
    private char letter;
    private boolean isBlank = false;

    public LetterTile(char letter) {
        this.letter = Character.toUpperCase(letter);
        if (this.letter == '*') {
            this.setBlank();
        }
    }

    public void setBlank() {
        this.isBlank = true;
    }

    public void setLetter(char l) {
        this.letter = l;
    }

    public char getLetter() {
        return this.letter;
    }

    public boolean isBlank() {
        return this.isBlank;
    }

    public String toString() {
        if (this.isBlank()) {
            return String.valueOf(this.letter);
        } else {
            return String.valueOf(Character.toLowerCase(this.letter));
        }
    }
}
