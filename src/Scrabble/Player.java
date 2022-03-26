package Scrabble;

import Scrabble.*;

import java.awt.geom.AffineTransform;
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
    protected boolean canPlay;
    protected static int playerNumber = 1;

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

    /**
     * Get the player's hand.
     *
     * @return Hand
     */
    public LetterTray getTray() {
        return this.tray;
    }

    /**
     * Includes choosing and playing a move, and drawing from the boneyard
     *  where necessary.
     *
     * @return Move to be played. Contains a "null domino" with negative values
     *  if no valid moves can be made
     *
     */
    abstract Optional<Move> getMove(Board b);

    /**
     * Draw a card from the boneyard and add it to the player's hand.
     *
     * @return boolean Success value indicating whether the player was able to
     *  successfully draw.
     */
    public boolean draw(TileBag bag) {
        if (bag.isEmpty()) {
            return false;
        } else {
            this.tray.add(bag.draw().get());
            return true;
        }
    }

    /**
     * Draw dominoes until the initial hand size is reached. Used for game
     * setup.
     */
    public void drawToCapacity(TileBag bag) {
        while (this.tray.size() < this.tray.getCapacity() && this.draw(bag));
    }

    /**
     * Determine whether the player's hand is empty. Used to check whether the
     * game is over.
     *
     * @return boolean
     */
    public boolean trayIsEmpty() {
        return this.tray.isEmpty();
    }

    /**
     * Return the string representation of the player, which includes the
     * player's name and the current state of their hand.
     *
     * @return String
     */
    @Override
    public String toString() {
        return this.name + ": " + this.tray;
    }
}
