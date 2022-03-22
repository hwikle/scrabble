package Scrabble;

public class LetterTile {
    private char letter;
    private int value;
    private boolean isBlank = false;

    public LetterTile(char letter) {
        this.letter = Character.toUpperCase(letter);
    }

    public void setBlank() {
        this.isBlank = true;
    }

    public char getLetter() {
        return this.letter;
    }

    public boolean isBlank() {
        return this.isBlank;
    }
}
