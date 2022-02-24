public class BoardSquare {
    private int letterMultiplier = 1;
    private int wordMultiplier = 1;
    private LetterTile tile = new LetterTile('0');

    public BoardSquare() {}

    public BoardSquare(int letterMultiplier, int wordMultiplier) {
        this.letterMultiplier = letterMultiplier;
        this.wordMultiplier = wordMultiplier;
    }

    public void addTile(LetterTile t) {
        this.tile = t;
    }

    public LetterTile getTile() {
        return this.tile;
    }
}
