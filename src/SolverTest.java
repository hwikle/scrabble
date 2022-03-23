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
                trays.add(new LetterTray(freader.nextLine()));
            }

        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        }

        int[] expectedScores = {81, 67, 24, 83};
        String[] expectedWords = {"lemoned", "troolie", "tinpot", "bodgiest"};

        int passed = 0;
        int failed = 0;
        boolean result;

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
        System.out.println("Found " + moves.size() + " moves");
        Move bestMove = new Move();
        int bestScore = 0;

        int score;

        for (Move m: moves) {
            score = b.scoreAllWords(m, scores, 7);
            if (score > bestScore) {
                bestScore = score;
                bestMove = m;
            }
        }

        System.out.println("Expected: " + expectedWord + " (" + expectedScore +")");
        System.out.println("Actual: " + b.getPrimaryWord(bestMove).toString(b) + " (" + bestScore +")");

        if (bestScore == expectedScore && expectedWord.equals(b.getPrimaryWord(bestMove).toString(b))) {
            return true;
        } else {
            return false;
        }
    }
}
