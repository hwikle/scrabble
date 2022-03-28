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
            System.out.println("File not found");
            s = new Scanner(System.in);
        }

        String[] l;

        while (s.hasNextLine()) {
            l = s.nextLine().split(" ");
            this.put(l[0].toUpperCase().charAt(0), Integer.valueOf(l[1]));
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
