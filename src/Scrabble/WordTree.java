package Scrabble;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class WordTree extends HashMap<Character, WordTree> {
    private final char TERM = '\0';

    public char getTerminator() {return this.TERM;}

    public void populateFromFile(String fname) {
        Scanner freader;
        File infile = new File(fname);

        try {
            freader = new Scanner(infile);
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
            freader = new Scanner(System.in);
        }

        while (freader.hasNextLine()) {
            this.add(freader.nextLine());
        }

    }

    public void add(String word) {
        word = word.toUpperCase();
        if (word.length() == 0) {
            this.put(TERM, new WordTree());
        } else if (this.containsKey(word.charAt(0))) {
            this.get(word.charAt(0)).add(word.substring(1));
        } else {
            WordTree tr = new WordTree();
            this.put(word.charAt(0), tr);
            tr.add(word.substring(1));
        }
    }

    public WordTree query(String s) {
        s = s.toUpperCase();
        WordTree result = this;
        //System.out.println("Query: " + s);

        while (s.length() > 0 && !result.keySet().isEmpty()) {
            result = result.getOrDefault(s.charAt(0), new WordTree());
            s = s.substring(1);
        }

        return result;
    }

    public boolean contains(String s) {
        s = s.toUpperCase();
        return !this.query(s).keySet().isEmpty();
    }

    public boolean containsWord(String word) {
        return this.query(word).keySet().contains(this.TERM);
    }

    public boolean isEmpty() {
        return this.keySet().isEmpty();
    }
/*
    public boolean contains(char c) {return this.keySet().contains(c);}
 */
    
    public ArrayList<String> getAllWords() {
        ArrayList<String> words = new ArrayList<>();

        for (Character c: this.keySet()) {
            if (c == TERM) {
                words.add("");
            } else {
                for (String s : this.get(c).getAllWords()) {
                    words.add(c + s);
                }
            }
        }

        return words;
    }
}
