package Scrabble;

import java.util.ArrayList;

public class PlayerList extends ArrayList<Player> {
    int currentIdx;

    public PlayerList() {
        currentIdx = 0;
    }

    Player next() {
        Player nextPlayer = this.get(currentIdx);

        if (currentIdx < this.size() - 1) {
            currentIdx++;
        } else {
            // start back at the beginning once the end is reached
            currentIdx = 0;
        }

        return  nextPlayer;
    }
}