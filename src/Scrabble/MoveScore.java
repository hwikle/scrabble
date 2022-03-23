package Scrabble;

import java.util.ArrayList;

public class MoveScore extends ArrayList<WordScore> {
    public int getScore() {
        int score = 0;

        for (WordScore ws: this) {
            score += ws.getScore();
        }

        return score;
    }

    public String toString() {
        String s = "Total: " + this.getScore();

        for (WordScore ws: this) {
            s += "\n\t" + ws.toString();
        }

        return s;
    }
}
