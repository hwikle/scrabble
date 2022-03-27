import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import Scrabble.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Scanner;

public class Scrabble extends Application {
    final static int TILE_WIDTH = 40;

    public static void main(String[] args) {launch(args);}

    @Override
    public void start(Stage primaryStage) {
        final Board board;
        final Stage modal = new Stage();

        File boardCfg = new File("resources/scrabble_board.txt");
        final ArrayList<BoardSquare> boardSelected = new ArrayList<BoardSquare>();
        final ArrayList<LetterTile> traySelected = new ArrayList<LetterTile>();

        // Initialize endgame modal
        modal.initModality(Modality.APPLICATION_MODAL);
        modal.initOwner(primaryStage);
        VBox endgameVbox = new VBox(20);
        Scene endgameScene = new Scene(endgameVbox, 300, 200);

        VBox invalidMoveVBox = new VBox();
        Button invalidMoveButton = new Button("Try again");

        invalidMoveVBox.getChildren().addAll(new Text("Invalid Move!"), invalidMoveButton);
        Scene invalidMove = new Scene(invalidMoveVBox, 300, 200);

        // Initialize scorer and dictionary
        WordScorer ws = new WordScorer("resources/scrabble_tiles.txt");
        WordTree dict = new WordTree();
        dict.populateFromFile("resources/sowpods.txt");

        Board b;

        // Read in board from file
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

        ComputerPlayer cp = new ComputerPlayer(new LetterTray(7), ws);
        cp.setDictionary(dict);

        HumanPlayer hp = new HumanPlayer();
        game.addPlayer(cp);
        game.addPlayer(hp);

        game.setup();

        VBox root = new VBox();
        root.setSpacing(TILE_WIDTH/10.0);

        GridPane gp = setupGraphicalBoard(board, boardSelected);
        GridPane tray = setupGraphicalTray(hp.getTray(), traySelected);

        Button submitButton = new Button("Submit Move");

        submitButton.setAlignment(Pos.CENTER);

        HBox playerControls = new HBox(tray, submitButton);
        playerControls.setAlignment(Pos.CENTER);
        playerControls.setSpacing(20);

        VBox scores = new VBox();

        for (Player p: game.getPlayers()) {
            scores.getChildren().add(new Text(p.getName() + ": " + p.getScore()));
        }

        root.getChildren().addAll(scores, gp, playerControls);
        System.out.println(root.getChildren().size());

        endgameVbox.getChildren().addAll(new Text("Game Over!"));

        drawBoard(gp, board, boardSelected);

        int sceneWidth = (int) ((b.getColumns()*1.1 - 0.1) * TILE_WIDTH);
        int sceneHeight = (int) (((b.getRows()+2)*1.1-0.1) * TILE_WIDTH);
        //int sceneHeight = 1000;

        primaryStage.setScene(new Scene(root, sceneWidth, sceneHeight));

        System.out.println(root.getChildren().size());
        primaryStage.show();

        AnimationTimer a = new AnimationTimer() {
            @Override
            public void handle(long now) {
                Player currentPlayer = game.getPlayers().getCurrent();

                if (game.gameIsOver()) {
                    Player winner = game.getWinner();
                    endgameVbox.getChildren().add(new Text(winner.getName() + " wins!"));
                    endgameVbox.getChildren().add(new VBox(scores));
                    modal.setScene(endgameScene);
                    modal.show();
                    this.stop();
                }

                if (currentPlayer.isReady()) {
                    if (!board.isValidMove(currentPlayer.getMove(board).get())) {
                        modal.setScene(invalidMove);
                        modal.show();
                        this.stop();
                    } else {
                        game.playTurn();
                        updateScores(scores, game.getPlayers());
                    }
                }

                drawBoard(gp, board, boardSelected);
                drawTray(tray, hp.getTray(), traySelected);
            }
        };

        submitButton.setOnMouseClicked((event) -> {
            if (traySelected.size() == boardSelected.size()) {
                hp.setSelected(traySelected, boardSelected, board);
            } else {
                a.stop();
                modal.setScene(invalidMove);
                invalidMoveVBox.getChildren().add(1, new Text(tray.toString()));
                modal.show();
            }
            traySelected.clear();
            boardSelected.clear();
        });

        invalidMoveButton.setOnMouseClicked((event) -> {
            modal.close();
            a.start();
        });

        a.start();



    }

    public static void drawBoard(GridPane gp, Board board, ArrayList<BoardSquare> selected) {
        BoardSquare sq;
        boolean inSelected;

        for (int i=0; i<board.getRows(); i++) {
            for (int j = 0; j < board.getColumns(); j++) {

                sq = board.getSquareAt(i, j).get();
                inSelected = selected.contains(sq);

                drawBoardSquare(gp, sq, i, j, inSelected);
            }
        }
    }

    public static void drawBoardSquare(GridPane gp, BoardSquare sq, int row, int col, boolean selected) {
        StackPane square = (StackPane) (gp.getChildren().get(row*15 + col));
        Rectangle rect;
        Color fill;
        Text letter;
        LetterTile t;

        square.getChildren().clear();

        if (sq.hasTile()) {
            fill = Color.BISQUE;
        } else {
            fill = switch (sq.getLetterMultiplier()) {
                case 2 -> Color.LIGHTBLUE;
                case 3 -> Color.BLUE;
                default -> Color.BURLYWOOD;
            };

            fill = switch (sq.getWordMultiplier()) {
                case 2 -> Color.PINK;
                case 3 -> Color.RED;
                default -> fill;
            };
        }

        rect = new Rectangle(TILE_WIDTH, TILE_WIDTH);
        rect.setFill(fill);
        rect.setStrokeType(StrokeType.INSIDE);

        if (selected) {
            rect.setStrokeWidth(3);
            rect.setStroke(Color.DARKTURQUOISE);
        } else {
            rect.setStrokeWidth(0);
        }

        square.getChildren().add(rect);

        if (sq.hasTile()) {
            t = sq.getTile().get();
            letter = new Text(Character.toString(t.getLetter()));

            letter.setStroke(Color.BLACK);

            if (t.isBlank()) {
                letter.setFill(Color.BISQUE);
            } else {
                letter.setFill(Color.BLACK);
            }
            letter.setFont(new Font(TILE_WIDTH*3.0/5.0));

            square.getChildren().add(letter);
        }
    }

    public static void drawTray(GridPane gp, LetterTray tray, ArrayList<LetterTile> selected) {
        LetterTile t;
        boolean inSelected;
        StackPane sp;

        for (int i=0; i<tray.getCapacity(); i++) {
            if (i < tray.size()) {
                t = tray.get(i);
                inSelected = selected.contains(t);
                drawTile(gp, t, i, inSelected);
            } else {
                sp = (StackPane) (gp.getChildren().get(i));
                sp.getChildren().clear();
            }
        }
    }

    public static void drawTile(GridPane gp, LetterTile t, int idx, boolean selected) {
        StackPane tile = (StackPane) (gp.getChildren().get(idx));
        Rectangle rect;
        Text letter;

        tile.getChildren().clear();

        rect = new Rectangle(TILE_WIDTH, TILE_WIDTH);
        rect.setFill(Color.BISQUE);

        rect.setStrokeType(StrokeType.INSIDE);
        rect.setStroke(Color.DARKTURQUOISE);

        if (selected) {
            rect.setStrokeWidth(3);
        } else {
            rect.setStrokeWidth(0);
        }

        letter = new Text(Character.toString(t.getLetter()));

        Color stroke;
        Color fill;

        if (t.isBlank()) {
            stroke = Color.BISQUE;
            fill = Color.BISQUE;
        } else {
            stroke = Color.BLACK;
            fill = Color.BLACK;
        }

        letter.setStroke(stroke);
        letter.setFill(fill);
        letter.setFont(new Font(3.0/5.0*TILE_WIDTH));

        tile.getChildren().addAll(rect, letter);
    }

    public void drawTiles(GridPane gp, Board b, ArrayList<BoardSquare> selected) {
        Optional<LetterTile> t;
        BoardSquare sq;
        Rectangle rect;
        Text l;
        StackPane gsq;

        for (int i=0; i<b.getRows(); i++) {
            for (int j=0; j<b.getColumns(); j++) {
                sq = b.getSquareAt(i, j).get();

                if (selected.contains(sq)) {
                    gsq = (StackPane) gp.getChildren().get(i*b.getColumns()+j);
                    rect = new Rectangle(TILE_WIDTH, TILE_WIDTH);
                    rect.setFill(Color.TRANSPARENT);
                    rect.setStrokeType(StrokeType.INSIDE); // Prevents tiles from changing size
                    rect.setStrokeWidth(3);
                    rect.setStroke(Color.DARKTURQUOISE);
                    gp.add(rect, i, j);
                }
                t = sq.getTile();

                if (t.isPresent()) {
                    rect = new Rectangle(TILE_WIDTH, TILE_WIDTH);
                    rect.setFill(Color.BISQUE);
                    gsq = (StackPane) gp.getChildren().get(i*b.getColumns()+j);
                    l = new Text(Character.toString(t.get().getLetter()));
                    l.setFont(new Font(3.0/5.0 * TILE_WIDTH));
                    l.setStroke(Color.BLACK);

                    if (t.get().isBlank()) {
                        l.setFill(Color.BISQUE);
                    } else {
                        l.setFill(Color.BLACK);
                    }
                    gsq.getChildren().addAll(rect, l);
                }
            }
        }

    }

    public void drawTray(GridPane gp, LetterTray tray) {
        Rectangle rect;
        StackPane tile;
        Text letter;

        gp.getChildren().clear();

        for (int i=0; i<tray.size(); i++) {
            rect = new Rectangle(TILE_WIDTH, TILE_WIDTH);
            rect.setFill(Color.BISQUE);
            tile = new StackPane(rect);

            if (!tray.get(i).isBlank()) {
                letter = new Text(Character.toString(tray.get(i).getLetter()));
                letter.setFont(new Font(3.0/5.0 * TILE_WIDTH));
                tile.getChildren().add(letter);
            }

            gp.add(tile, i, 0);
        }
    }

    private static GridPane setupGraphicalBoard(Board board, ArrayList<BoardSquare> selected) {
        StackPane sp;
        GridPane gp = new GridPane();

        for (int i=0; i<board.getRows(); i++) {
            for (int j=0; j<board.getColumns(); j++) {
                sp = new StackPane();

                final BoardSquare sq = board.getSquareAt(i, j).get();

                sp.setOnMouseClicked(event -> {
                    if (selected.contains(sq)) {
                        selected.remove(sq);
                    } else {
                        selected.add(sq);
                    }
                    //System.out.println(selected);
                });

                gp.add(sp, i, j);
            }
        }

        gp.setHgap(TILE_WIDTH/10.0);
        gp.setVgap(TILE_WIDTH/10.0);

        return gp;
    }

    private static GridPane setupGraphicalTray(LetterTray tray, ArrayList<LetterTile> selected) {
        StackPane sp;
        GridPane gp = new GridPane();

        for (int i=0; i<tray.size(); i++) {
            sp = new StackPane();

            final int idx = i;
            final LetterTray tr = tray;

            sp.setOnMouseClicked(event -> {
                if (idx > tr.size()-1) {
                    return;
                }
                if (selected.contains(tr.get(idx))) {
                    selected.remove(tr.get(idx));
                } else {
                    selected.add(tr.get(idx));
                }
            });

            gp.add(sp, i, 0);
        }

        gp.setHgap(TILE_WIDTH/10.0);

        return gp;
    }

    public static void updateScores(VBox scores, PlayerList players) {
        scores.getChildren().clear();

        for (Player p: players) {
            scores.getChildren().add(new Text(p.getName() + " " + p.getScore()));
        }
    }
}
