package Scrabble;

import java.util.Optional;

public class GameManager {
    private Board board;
    private PlayerList players;
    private TileBag bag;
    private String tileFpath = "../resources/scrabble_tiles.txt";
    private WordTree dict;
    private boolean prevCouldPlay;

    public GameManager(Board b) {
        this.board = b;
        this.players = new PlayerList();
        this.prevCouldPlay = true;
    }

    public void addPlayer(Player p) {
        players.add(p);
    }

    public void setDictionary(WordTree dict) {
        this.dict = dict;
    }

    public void setUp() {
        this.bag = new TileBag(tileFpath);
        this.bag.shuffle();

        for (Player p: this.players) {
            p.drawToCapacity(this.bag);
        }
    }

    public boolean playTurn() {
        Player p = this.players.next();
        Optional<Move> m = p.getMove(this.board);

        if (m.isPresent()) {
            this.board.play(m.get());
            return true;
        } else {
            return false;
        }
    }

}
