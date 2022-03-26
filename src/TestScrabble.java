import Scrabble.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class TestScrabble {
    public static void main(String[] args) {
        Board b = new Board(15);
        WordTree dict = new WordTree();
        dict.populateFromFile("resources/sowpods.txt");
        System.out.println("Made dictionary");

        File boardCfg = new File("resources/scrabble_board.txt");

        try {
            Scanner s = new Scanner(boardCfg);
            b = new Board(Integer.valueOf(s.nextLine()));
            b.populateFromScanner(s);
            System.out.println("Made board");
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
            System.exit(1);
        }


        GameManager game = new GameManager(b);
        game.setDictionary(dict);

        ComputerPlayer cp;

        for (int i=0; i<2; i++) {
            cp = new ComputerPlayer(new LetterTray(7));
            cp.setDictionary(dict);
            game.addPlayer(cp);
        }

        game.setUp();

        System.out.println(b);

        for (Player p: game.getPlayers()) {
            System.out.println(p.getName() + " " + p.getTray());
        }

        game.run();

    }
}
