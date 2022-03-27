package Scrabble;

import java.util.ArrayList;
import java.util.Optional;

/**
 * Abstract class that serves as the parent of both the ComputerPlayer and
 * HumanPlayer derived classes.
 *
 * @author Hank Wikle
 */
public abstract class Player {
    protected String name;
    protected LetterTray tray;
    protected static int playerNumber = 1;
    private int score = 0;
    private boolean readyToPlay = true;

    public Player() {
        this(new LetterTray(7));
    }

    public Player(String name, LetterTray t) {
        this.name = name;
        this.tray = t;
        playerNumber++;
    }

    public Player(LetterTray t) {
        this.name = "Player " + playerNumber;
        this.tray = t;
        playerNumber++;
    }

    public String getName() {
        return this.name;
    }

    public int getScore() {
        return this.score;
    }

    public void addScore(int score) {
        this.score += score;
    }

    public LetterTray getTray() {
        return this.tray;
    }

    public abstract Optional<Move> getMove(Board b);

    public boolean draw(TileBag bag) {
        if (bag.isEmpty()) {
            return false;
        } else {
            this.tray.add(bag.draw().get());
            return true;
        }
    }

    public void drawToCapacity(TileBag bag) {
        while (this.tray.size() < this.tray.getCapacity() && this.draw(bag));
    }

    public void tradeIn(ArrayList<LetterTile> tiles, TileBag bag) {
        this.tray.removeAll(tiles);
        this.drawToCapacity(bag);
    }

    public void tradeInAll(TileBag bag) {
        this.tray.clear();
        this.drawToCapacity(bag);
    }

    public boolean trayIsEmpty() {
        return this.tray.isEmpty();
    }

    public boolean isReady() {
        return this.readyToPlay;
    }

    public abstract boolean canPlay(Board b);

    @Override
    public String toString() {
        return this.name + ": " + this.tray;
    }
}
