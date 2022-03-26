import Scrabble.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class SolverTest {
    private static WordTree dictionary = new WordTree();
    private static LetterScores scores = new LetterScores("/Users/hank/IdeaProjects/cs351/scrabble/resources/letterscores.txt");

    public static void main(String[] args) {
        dictionary.populateFromFile("/Users/hank/IdeaProjects/cs351/scrabble/resources/sowpods.txt");
        String fname = "/Users/hank/IdeaProjects/cs351/scrabble/resources/example_input.txt";
        File testFile = new File(fname);
        ArrayList<Board> boards = new ArrayList<>();
        ArrayList<LetterTray> trays = new ArrayList<>();
        String boardString;
        Board b;
        int boardSize;

        try {
            Scanner freader = new Scanner(testFile);

            while (freader.hasNextLine()) {
                boardSize = freader.nextInt();
                freader.nextLine();
                b = new Board(boardSize);
                boardString = "";

                for (int i=0; i<boardSize; i++) {
                    boardString += freader.nextLine();
                    if (i != boardSize-1) {
                        boardString += '\n';
                    }
                }
                //System.out.println(boardSize);
                //System.out.println(boardString);
                b.populateFromString(boardString);

                boards.add(b);
                trays.add(new LetterTray(7, freader.nextLine()));
            }

        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        }

        int[] expectedScores = {81, 67, 24, 83};
        String[] expectedWords = {"lemoned", "troolie", "tinpot", "bodgiest"};

        int passed = 0;
        int failed = 0;
        boolean result;
        BoardLocation loc = new BoardLocation(2, 8);

        //result = testAtLoc(boards.get(0), trays.get(0), loc, expectedWords[0], expectedScores[0]);

        //LetterTray tray = new LetterTray("d");
        //assert tray.hasBlank();
        //WordTree sub = dictionary.query("lemone");
        //ArrayList<String> subwords = sub.getAllWords();

        /*
        for (String w: subwords) {
            System.out.println(w);
        }
         */

        /*
        ArrayList<Move> submoves = boards.get(0).getPossibleMoves(new BoardLocation(2, 14), Orientation.ACROSS, tray, sub, sub);

        System.out.println(submoves.size());

        for (Move m: submoves) {
            System.out.println(m);
        }

         */

        for (int i=0; i<4; i++) {

            result = test(boards.get(i), trays.get(i), expectedWords[i], expectedScores[i]);

            if (result) {
                passed++;
            } else {
                failed++;
            }
        }

        System.out.println("Passed " + passed + "/4 tests " + "(" + (int)(100*passed/4.0) + "%)");
    }

    public static boolean test(Board b, LetterTray tray, String expectedWord, int expectedScore) {
        expectedWord = expectedWord.toUpperCase();
        ArrayList<Move> moves = b.getGlobalPossibleMoves(tray, dictionary);
        System.out.println(b);
        System.out.println("Found " + moves.size() + " moves");

        Word w;

        for (Move m: moves) {
            w = b.getWord(m, m.getOrientation());
            //System.out.println(w.toString(b));
            if (w.toString().equals(expectedWord)) {
                System.out.println(expectedWord + " in moves");
                System.out.println(m.getLocations().get(0) + " " + m.getOrientation());
            }
        }

        Move bestMove = new Move();
        int bestScore = 0;

        MoveScore ms;
        MoveScore bestMS = new MoveScore();

        for (Move m: moves) {
            ms = b.scoreAllWords(m, scores, 7);
            if (ms.getScore() > bestScore) {
                bestScore = ms.getScore();
                bestMS = ms;
                bestMove = m;
            }
        }

        System.out.println("Expected: " + expectedWord + " (" + expectedScore +")");
        System.out.println("Actual: " + b.getPrimaryWord(bestMove).toString(b) + " (" + bestScore +")");

        assert dictionary.containsWord(b.getPrimaryWord(bestMove).toString(b));

        System.out.println(bestMS);
        System.out.println(b.getWordStart(b.getPrimaryWord(bestMove)) + ", " + b.getWordEnd(b.getPrimaryWord(bestMove)));

        if (bestScore == expectedScore && expectedWord.equals(b.getPrimaryWord(bestMove).toString(b))) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean testAtLoc(Board b, LetterTray tray, BoardLocation loc, String expectedWord, int expectedScore) {
        expectedWord = expectedWord.toUpperCase();
        ArrayList<Move> moves = b.getPossibleMoves(loc, Orientation.ACROSS, tray, dictionary, dictionary);
        moves.addAll(b.getPossibleMoves(loc, Orientation.DOWN, tray, dictionary, dictionary));

        System.out.println(b);
        System.out.println("Found " + moves.size() + " moves");

        for (Move m: moves) {
            if (m.size() == 0) {
                continue;
            }
            Word w = b.getWord(m, m.getOrientation());
            System.out.println(w.toString(b));
            if (m.toString().equals(expectedWord)) {
                System.out.println(expectedWord + " in moves");
                System.out.println(m.getLocations().get(0) + " " + m.getOrientation());
            }
        }

        Move bestMove = new Move();
        int bestScore = 0;

        MoveScore ms;
        MoveScore bestMS = new MoveScore();

        for (Move m: moves) {
            ms = b.scoreAllWords(m, scores, 7);
            if (ms.getScore() > bestScore) {
                bestScore = ms.getScore();
                bestMS = ms;
                bestMove = m;
            }
        }

        System.out.println("Expected: " + expectedWord + " (" + expectedScore +")");
        System.out.println("Actual: " + b.getPrimaryWord(bestMove).toString(b) + " (" + bestScore +")");


        assert dictionary.containsWord(b.getPrimaryWord(bestMove).toString(b));

        System.out.println(bestMS);
        System.out.println(bestMove.getLocations().get(0) + " " + bestMove.getOrientation());

        if (bestScore == expectedScore && expectedWord.equals(b.getPrimaryWord(bestMove).toString(b))) {
            return true;
        } else {
            return false;
        }
    }
}
