package Scrabble;

public class Word {
    private SquareSequence seq;
    private Move move;

    public Word(SquareSequence seq, Move m) {
        this.seq = seq;
        assert m.isLinear();
        this.move = m;
    }

    public Move getMove() {
        return this.move;
    }

    public SquareSequence getBoardSquares() {
        return this.seq;
    }

    public int length() {
        // TODO: This might have a bug
        return this.seq.size();
    }

    public BoardSquare get(int i, Board b) {
        BoardSquare res = new BoardSquare();
        BoardSquare sq = this.seq.get(i);

        if (sq.hasTile()) {
            res.addTile(this.seq.get(i).getTile().get());
        } else {
            res.setLetterMultiplier(sq.getLetterMultiplier());
            res.setWordMultiplier(sq.getWordMultiplier());
            res.addTile(this.move.tileByLocation(b.locationFromSquare(sq)).get());
        }

        return res;
    }

    public String toString(Board b) {
        String s = "";
        BoardLocation loc;

        for (BoardSquare sq: this.seq) {
            LetterTile t;
            if (sq.hasTile()) {
                t = sq.getTile().get();
                if (t.isBlank()) {
                    s += t.getLetter();
                } else {
                    s += Character.toLowerCase(t.getLetter());
                }
            } else {
                loc = b.locationFromSquare(sq);
                t = this.move.tileByLocation(loc).get();
                if (t.isBlank()) {
                    s += t.getLetter();
                } else {
                    s += Character.toLowerCase(t.getLetter());
                }
            }
        }
        return s;
    }
}

