package Scrabble;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Scanner;

public class LetterScores extends HashMap<Character,Integer> {
    public LetterScores() {}

    public LetterScores(String fname) {
        File infile = new File(fname);
        Scanner s;

        try {
            s = new Scanner(infile);
        } catch (FileNotFoundException e) {
            s = new Scanner(System.in);
        }

        // TODO: Rewrite for json file

        String[] pair;

        while (s.hasNextLine()) {
            pair = s.nextLine().split(" ");
            this.put(pair[0].toUpperCase().charAt(0), Integer.valueOf(pair[1]));
        }
    }

    public int score(String word) {
        int score = 0;

        for (char c: word.toCharArray()) {
            score += this.get(c);
        }

        return score;
    }
}
