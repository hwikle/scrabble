package Scrabble;

import java.util.Optional;

public class GameManager {
    private Board board;
    private PlayerList players;
    private TileBag bag;
    private String tileFpath = "resources/scrabble_tiles.txt";
    private WordTree dict = new WordTree();
    private LetterScores ls;
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
        this.ls = new LetterScores(tileFpath);
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
            ms = board.scoreAllWords(m.get(), this.ls, p.tray.getCapacity());
            System.out.println(ms);

            for (WordScore ws: ms) {
                if (!dict.containsWord(ws.getWord())) {
                    System.out.println(ws.getWord() + " not in dictionary");
                }

            }
            this.board.play(m.get());
            p.getTray().removeAll(m.get().getTiles());
            p.drawToCapacity(this.bag);
            return true;
        } else {
            return false;
        }
    }

    public void run() {
        while (playTurn()) {
            System.out.println(this.board.toString(false) + "\n");
        }
    }

}
