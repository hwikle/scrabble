import Scrabble.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.security.spec.RSAOtherPrimeInfo;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Scanner;

public class WordSolver {
    public static void main(String[] args) {
        String dictFile;
        Scanner s = new Scanner(System.in);

        System.out.println("Enter board configuration file path: ");
        String boardCfgFile = s.nextLine().strip();

        try {
            dictFile = args[0];
        } catch (IndexOutOfBoundsException e) {
            dictFile = "resources/sowpods.txt";
        }

        WordTree dict = new WordTree();
        //String dictFile = "/Users/hank/IdeaProjects/cs351/scrabble/resources/sowpods.txt";
        //String boardCfgFile = "/Users/hank/IdeaProjects/cs351/scrabble/resources/example_input.txt";
        dict.populateFromFile(dictFile);
        //LetterScores scores = new LetterScores("scrabble_tiles.txt");

        //System.out.println("Creating word scorer...");
        WordScorer scorer = new WordScorer("/Users/hank/IdeaProjects/cs351/scrabble/resources/scrabble_tiles.txt");
        Board b;
        Scanner freader;
        LetterTray tray;
        Optional<String> l;

        //System.out.println("Configuring board...");
        try {
            freader = new Scanner(new File(boardCfgFile));
            while (freader.hasNextLine()) {
                l = getNextNonemptyLine(freader);

                if (l.isPresent()) {
                    b = new Board(Integer.valueOf(l.get()));
                    b.populateFromScanner(freader);

                    l = getNextNonemptyLine(freader);

                    if (l.isPresent()) {
                        tray = new LetterTray(7, l.get());
                        solve(b, tray, dict, scorer);
                    }
                }
            }
        } catch(FileNotFoundException e) {
            System.out.println("File not found");
        }
    }

    private static void solve(Board b, LetterTray tray, WordTree dict, WordScorer ws) {
        ArrayList<Move> moves = b.getGlobalPossibleMoves(tray, dict);

        System.out.println("Input Board:");
        System.out.println(b);
        System.out.println("Tray:" + tray);

        Move best = ws.getBestMove(b, moves, tray.getCapacity());
        //System.out.println(best);
        MoveScore ms = ws.scoreMove(b, best, tray.getCapacity());
        //System.out.println(ms);
        //System.out.println(ms.getBonus());
        //System.out.println(best.size());
        //System.out.println(tray.getCapacity());
        int bestScore = ms.getScore();

        System.out.println("Solution " + b.getWord(best, best.getOrientation()).toString(b) + " has " + bestScore + " points ");

        b.play(best);
        System.out.println("Solution Board:");
        System.out.println(b);
    }

    private static Optional<String> getNextNonemptyLine(Scanner s) {
        String l;
        while (s.hasNextLine()) {
            l = s.nextLine();

            if (!l.strip().equals("")) {
                return Optional.of(l);
            }
        }

        return Optional.empty();
    }
}
