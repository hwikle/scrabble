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
        this.tile = Optional.ofNullable(t);
    }

    public Optional<LetterTile> getTile() {
        return this.tile;
    }

    public String toString() {
        if (this.tile.isPresent()) {
            return " " + Character.toString(this.tile.get().getLetter());
        } else {
            String s = Integer.toString(wordMultiplier) + Integer.toString(letterMultiplier);
            s.replaceAll("1", ".");
            return " ";
        }
    }
}
