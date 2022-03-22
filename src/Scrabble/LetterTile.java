package Scrabble;

public class LetterTile {
    private char letter;
    private int value;

    public LetterTile(char letter) {
        this.letter = Character.toUpperCase(letter);
    }

    public char getLetter() {
        return this.letter;
    }

    public int getValue() {
        return this.value;
    }
}
