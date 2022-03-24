package Scrabble;

public class WordScore {
    private String word;
    private int score;

    public WordScore(String word, int score) {
        this.word = word;
        this.score = score;
    }

    public String getWord() {
        return this.word;
    }

    public int getScore() {
        return score;
    }

    @Override
    public String toString() {
        return this.word + ": "+ this.score;
    }
}
