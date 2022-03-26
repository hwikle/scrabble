import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import Scrabble.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Optional;
import java.util.Scanner;

public class Scrabble extends Application {

    public static void main(String[] args) {launch(args);}

    @Override
    public void start(Stage primaryStage) {
        File boardCfg = new File("resources/scrabble_board.txt");
        final Board board;
        WordScorer ws = new WordScorer("resources/scrabble_tiles.txt");
        WordTree dict = new WordTree();
        dict.populateFromFile("resources/sowpods.txt");

        Board b;

        try {
            Scanner s = new Scanner(boardCfg);
            b = new Board(Integer.valueOf(s.nextLine()));
            b.populateFromScanner(s);
        } catch (FileNotFoundException e) {
            b = new Board(15); // No multipliers
        }

        board = b;

        GameManager game = new GameManager(board, ws);
        game.setDictionary(dict);

        ComputerPlayer cp;

        for (int i=0; i<2; i++) {
            cp = new ComputerPlayer(new LetterTray(7), ws);
            cp.setDictionary(dict);
            game.addPlayer(cp);
        }

        game.setup();

        GridPane gp = new GridPane();
        gp.setHgap(5);
        gp.setVgap(5);

        drawBoard(gp, board);

        primaryStage.setScene(new Scene(gp));
        primaryStage.show();

        AnimationTimer a = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if(!game.playTurn()) {
                    this.stop();
                }
                drawTiles(gp, board);
            }
        };

        a.start();



    }

    public static void drawBoard(GridPane gp, Board board) {
        Rectangle rect;

        for (int i=0; i<board.getRows(); i++) {
            for (int j=0; j<board.getColumns(); j++) {

                Color fill = switch (board.getSquareAt(i, j).get().getLetterMultiplier()) {
                    case 2 -> Color.LIGHTBLUE;
                    case 3 -> Color.BLUE;
                    default -> Color.BURLYWOOD;
                };

                fill = switch (board.getSquareAt(i, j).get().getWordMultiplier()) {
                    case 2 -> Color.PINK;
                    case 3 -> Color.RED;
                    default -> fill;
                };

                rect = new Rectangle(50, 50);
                rect.setFill(fill);
                gp.add(new StackPane(rect), i, j);
            }
        }
    }

    public void drawTiles(GridPane gp, Board b) {
        Optional<LetterTile> t;
        Rectangle rect;
        Text l;

        for (int i=0; i<b.getRows(); i++) {
            for (int j=0; j<b.getColumns(); j++) {
                t = b.getSquareAt(i, j).get().getTile();

                if (t.isPresent()) {
                    rect = new Rectangle(50, 50);
                    rect.setFill(Color.BISQUE);
                    StackPane sq = (StackPane) gp.getChildren().get(i*b.getColumns()+j);
                    l = new Text(Character.toString(t.get().getLetter()));
                    l.setFont(new Font(30));
                    l.setStroke(Color.BLACK);

                    if (t.get().isBlank()) {
                        l.setFill(Color.BISQUE);
                    } else {
                        l.setFill(Color.BLACK);
                    }
                    sq.getChildren().addAll(rect, l);
                }
            }
        }

    }
}
