package Scrabble;

import java.util.ArrayList;

public class WordScorer {
    private LetterScores ls;

    public WordScorer(String fname) {
        this.ls = new LetterScores(fname);
    }

    private int scoreWord(Board b, Word w) {
        int score = 0;
        int wordMult = 1;
        BoardSquare sq;

        for (
                int i = 0; i < w.length(); i++) {
            sq = w.get(i, b);
            if (!sq.getTile().get().isBlank()) {
                score += ls.get(sq.getTile().get().getLetter()) * sq.getLetterMultiplier();
            }
            wordMult *= sq.getWordMultiplier();
        }

        return score * wordMult;
    }

    public MoveScore scoreMove(Board b, Move m, int trayCapacity) {
        ArrayList<Word> words = b.getAllWords(m);
        MoveScore ms = new MoveScore();

        int score;

        for (Word w: words) {
            score = this.scoreWord(b, w);
            ms.add(new WordScore(w.toString(b), score));
        }

        if (m.size() == trayCapacity) {
            ms.setBonus(50);
        }
        return ms;
    }

    public Move getBestMove(Board b, ArrayList<Move> allMoves, int trayCapacity) {
        int bestScore = 0;
        Move bestMove = new Move();

        int score;

        for (Move m: allMoves) {
            score = this.scoreMove(b, m, trayCapacity).getScore();

            if (score > bestScore) {
                bestScore = score;
                bestMove = m;
            }
        }

        return bestMove;
    }
}
