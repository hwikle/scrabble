import Scrabble.LetterTray;
import Scrabble.SquareSequence;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.util.regex.*;

public class Dictionary {
    private ArrayList<String> words = new ArrayList<>();

    public Dictionary(String fname, double addChance) {
        Scanner freader;
        File infile = new File(fname);
        Random r = new Random();

        try{
            freader = new Scanner(infile);
        } catch (FileNotFoundException e) {
            freader = new Scanner(System.in);
        }

        while (freader.hasNextLine()) {
            if (addChance >= 1.0 || r.nextDouble() > addChance) {
                this.words.add(freader.nextLine());
            }
        }
    }

    public Dictionary(String fname) {
        this(fname, 2.0);
    }

    public ArrayList<String> search(Constraint c) {
        ArrayList<String> a = new ArrayList<>();
        return a;
    }

    public boolean isValidWord(String word) {
        return true;
    }

    /*
    public ArrayList<String> Query(SquareSequence seq, LetterTray tray) {
        ArrayList<String> words = new ArrayList<>();

        String regex = seq.toRegex(tray);
        Pattern r = Pattern.compile(regex);

        System.out.println("Querying with regex: " + regex + "...");

        for (int i=0; i<this.words.size(); i++) {
            Matcher m = r.matcher(this.words.get(i));

            if (m.find()) {
                words.add(this.words.get(i));
            }
        }

        return words;
    }

     */

}
