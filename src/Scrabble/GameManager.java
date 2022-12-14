package Scrabble;

import java.util.Optional;

public class GameManager {
    private Board board;
    private PlayerList players;
    private TileBag bag;
    private String tileFpath = "resources/scrabble_tiles.txt";
    private WordTree dict = new WordTree();
    private WordScorer scorer;

    public GameManager(Board b, WordScorer ws) {
        this.board = b;
        this.scorer = ws;
        this.players = new PlayerList();
    }

    public void addPlayer(Player p) {
        players.add(p);
    }

    public void setDictionary(WordTree dict) {
        this.dict = dict;
    }

    public void setup() {
        this.bag = new TileBag(tileFpath);
        this.bag.shuffle();
        System.out.println("Bag has " + this.bag.size() + " tiles");

        for (Player p: this.players) {
            p.drawToCapacity(this.bag);
        }
    }

    public PlayerList getPlayers() {
        return this.players;
    }

    public boolean playTurn() {
        Player p = this.players.next();
        System.out.println(p.getName() + ": " + p.getTray());
        Optional<Move> m = p.getMove(this.board);

        MoveScore ms;

        if (m.isPresent()) {
            ms = scorer.scoreMove(this.board, m.get(), p.getTray().getCapacity());
            System.out.println(ms);

            for (WordScore ws: ms) {
                if (!dict.containsWord(ws.getWord())) {
                    System.out.println(ws.getWord() + " not in dictionary");
                }

            }
            this.board.play(m.get());

            p.addScore(ms.getScore());

            p.getTray().removeTiles(m.get());
            p.drawToCapacity(this.bag);

            return true;
        } else {
            return false;
        }
    }

    public TileBag getBag() {
        return this.bag;
    }

    public void skipTurn() {
        this.players.next();
    }

    public Player getWinner() {
        int bestScore = 0;
        Player winner = this.players.get(0);

        for (Player p: this.players) {
            if (p.getScore() > bestScore) {
                bestScore = p.getScore();
                winner = p;
            }
        }

        return winner;
    }

    public void run() {
        while (playTurn()) {
            System.out.println(this.board.toString(false) + "\n");
        }

        System.out.println("Game Over!");
        System.out.println("Scores: ");

        for (Player p: this.getPlayers()) {
            System.out.println(p.getName() + ": " + p.getScore());
        }

        System.out.println(this.getWinner().getName() + " wins!");

    }

    public boolean gameIsOver() {
        return this.bag.isEmpty();
        //return (this.bag.isEmpty() && this.players.getCurrent().canPlay());
    }

}
