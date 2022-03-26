package Scrabble;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class TileBag extends Stack<LetterTile> {
    private ArrayList<LetterTile> tiles;

    public TileBag(){}

    public TileBag(String fpath) {
        try {
            File inFile = new File(fpath);
            Scanner s = new Scanner(inFile);

            String[] l;

            while (s.hasNextLine()) {
                l = s.nextLine().split(" ");

                for (int i=0; i<Integer.valueOf(l[2]); i++) {
                    this.add(new LetterTile(l[0].charAt(0)));
                }
            }
        } catch (FileNotFoundException e) {
            return;
        }
    }

    public void shuffle() {
        Collections.shuffle(this);
    }

    public Optional<LetterTile> draw() {
        try {
            return Optional.of(this.pop());
        } catch (EmptyStackException e) {
            return Optional.empty();
        }
    }
}
