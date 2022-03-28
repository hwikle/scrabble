package Scrabble;

import java.util.ArrayList;

public class MoveScore extends ArrayList<WordScore> {
    private int bonus = 0;

    public int getScore() {
        int score = 0;

        for (WordScore ws: this) {
            score += ws.getScore();
        }

        return score + bonus;
    }

    public int getBonus() {
        return this.bonus;
    }

    public void setBonus(int bonus) {
        this.bonus = bonus;
    }

    public String toString() {
        String s = "Total: " + this.getScore();

        for (WordScore ws: this) {
            s += "\n\t" + ws.toString();
        }

        if (bonus > 0) {
            s += "\n\tBONUS: " + this.bonus;
        }

        return s;
    }
}
