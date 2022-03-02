import java.util.Optional;

public class BoardSquare {
    private int letterMultiplier = 1;
    private int wordMultiplier = 1;
    private Optional<LetterTile> tile = Optional.ofNullable(null);

    public BoardSquare() {}

    public BoardSquare(int letterMultiplier, int wordMultiplier) {
        this.letterMultiplier = letterMultiplier;
        this.wordMultiplier = wordMultiplier;
    }

    public int getLetterMultiplier() {
        return letterMultiplier;
    }

    public int getWordMultiplier() {
        return wordMultiplier;
    }

    public void setLetterMultiplier(int i) {
        this.letterMultiplier = i;
    }

    public void setWordMultiplier(int i) {
        this.wordMultiplier = i;
    }

    public boolean hasTile() {
        return this.tile.isPresent();
    }

    public void addTile(LetterTile t) {
        this.tile = Optional.of(t);
        this.resetMultipliers();
    }

    private void resetMultipliers() {
        this.letterMultiplier = 1;
        this.wordMultiplier = 1;
    }

    public Optional<LetterTile> getTile() {
        return this.tile;
    }

    public String toString() {
        if (this.tile.isPresent()) {
            return " " + Character.toString(this.tile.get().getLetter());
        } else {
            String s = String.valueOf(wordMultiplier) + String.valueOf(letterMultiplier);
            s = s.replaceAll("1", ".");

            return s;
        }
    }
}
