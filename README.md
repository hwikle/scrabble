# General Usage

## Word Solver

Word solver is meant to be run from the command line. Usage is:

`java -jar wordsolver.jar <dictfile> <boardconfig>`

## Scrabble Game

Play begins with the human player. On their turn, the player may perform one of three actions:

1. Play a move
2. Trade in one or more tiles
3. Forfeit their turn

All three actions may be performed via the buttons at the bottom of the screen.

### Tile Selection

To play a move, the player must select a set of tiles from their tray and a corresponding set of squares on the board. The number of tiles must match the number of board squares selected. The player may select the tiles first or the board squares first; the results are the same. Note, however, that the order in which the player selects tiles in the tray or squares on the board is important. For instance, the first tile selected will be matched with the first board square selected. Once the player has finished selecting tiles and board squares, they must press the "Submit" button to finalize the move.

Tile trade-in is performed similarly, but in this case, the selected board squares are ignored. Challenges to moves, either by the computer player or the human player, are not currently supported.

# Algorithm Details

Algorithm and data structure are based on conversations with classmate Christopher "Bo" Gilbert rather than on the provided Scrabble paper or any other resource.

## Dictionary Structure

The dictionary is stored as a tree structure, implemented as a `Hashmap` from `char` to `Hashmap`. The subtree associated with each character represents the dictionary of "subwords" that can legally follow the given character in the dictionary from which the tree is initialized. Ends of words are marked by the null terminator, `\0`.

## Wordfinding Algorithm

The wordfinding algorithm begins with a location on the board, an orientation (either `ACROSS` or `DOWN`), and a tray of letters.
If there is a tile to the left of the location (for `ACROSS` words) or above it (for `DOWN` words), then the algorithm backs up to what would be the first tile in the word (i.e. to the first tile that has a blank square above or to the left).

If the starting location already has a tile on it (this will be the case if the algorithm backed up at all), then the algorithm recurses using the dictionary subtree associated with that tile's letter, and moving forward by a single board square. If there is not a tile at that location, it will try each tile in the tray one at a time, recursing on the with the subtree associated with that tile's letter and a copy of the tray, less that particular tile. With each recursive call:

1. The location advances a single square.
2. The tray shrinks by a single tile (if the current location has no tile).
3. The depth of the tree is reduced by one (since it is a subtree of the parent call's tree).

In addition, a tile is immediately discarded as a possibility if it would produce invalid crosswords.

Each recursive call returns a list of valid submoves, each of which reaches to the end of a possible word.

As the recursive calls return, and the algorithm moves back up the call tree towards the root,
these submoves are then appended to the move-location pairs that they can legally follow, so that they form larger submoves.
At the final (top) level of the call tree, they are appended to candidates for the starting tile to create full moves, which are then
passed off to the scorer.

## Choosing the Best Move

The above algorithm is run for all squares on the board (225 squares for a standard board).
This is tenable since it will fail quickly on most squares.
This yields a list of all possible valid moves, given the player's tray and the current state of the board.
From this point, the best move is determined simply by iterating over the moves, calculating the score of each,
and choosing whichever move has the maximum score.

# Known Issues

1. When the computer player has both blanks in their tray, the game will hang for longer than five sections while the computer considers their move.
2. When playing blank tiles, player is not currently prompted to declare a letter for the blank tile.

# Planned Features

1. Subtraction of leftover tiles from score at end of game is not yet implemented.
2. Challenges to moves are not currently implemented.
3. Difficulty levels for computer player based on partial knowledge of dictionary.
4. Ability to set random seed (for reproducibility) via command line argument is planned.
5. Ability to show computer player's tray via command line option is planned