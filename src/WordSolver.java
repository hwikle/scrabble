import Scrabble.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Scanner;

public class WordSolver {
    public static void main(String[] args) {
        String dictFile = args[0];
        String boardCfgFile = args[1];

        System.out.println(dictFile);
        System.out.println(boardCfgFile);

        // Set up dictionary
        WordTree dict = new WordTree();
        dict.populateFromFile(dictFile);
        LetterScores scores = new LetterScores("/Users/hank/IdeaProjects/cs351/scrabble/resources/letterscores.txt");

        Board b;
        Scanner s;
        LetterTray tray;
        Optional<String> l;

        try {
            s = new Scanner(new File(boardCfgFile));
            while (s.hasNextLine()) {
                l = getNextNonemptyLine(s);

                if (l.isPresent()) {
                    b = new Board(Integer.valueOf(l.get()));
                    b.populateFromScanner(s);

                    l = getNextNonemptyLine(s);

                    if (l.isPresent()) {
                        tray = new LetterTray(7, l.get());
                        solve(b, tray, dict, scores);
                    }
                }
            }
        } catch(FileNotFoundException e) {
            System.out.println("File not found");
        }
    }

    private static void solve(Board b, LetterTray tray, WordTree dict, LetterScores ls) {
        ArrayList<Move> moves = b.getGlobalPossibleMoves(tray, dict);

        System.out.println("Input Board:");
        System.out.println(b);
        System.out.println("Tray:" + tray);

        Move bestMove = new Move();
        int bestScore = 0;

        MoveScore ms;
        MoveScore bestMS = new MoveScore();

        for (Move m: moves) {
            ms = b.scoreAllWords(m, ls, 7);
            if (ms.getScore() > bestScore) {
                bestScore = ms.getScore();
                bestMS = ms;
                bestMove = m;
            }
        }

        System.out.println("Solution " + b.getWord(bestMove, bestMove.getOrientation()).toString(b) + " has " + bestScore + " points ");

        b.play(bestMove);
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
