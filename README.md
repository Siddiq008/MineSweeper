# MohammedMinesweeper Game - README

## Game Flow
1. The game board is a grid, and your goal is to flag all mines and expose safe cells.
2. Enter the row and column of the cell to Dig or Flag.
3. Safe cells show numbers indicating nearby mines.
4. Use flags to mark suspected mines and avoid revealing them.
5. Continue until all safe cells are revealed, or you expose a mine.

## Win and Loss
- Win: Expose all safe cells without hitting a mine.
- Loss: Expose a mine and end the game.

## Notes
1. The board uses 0-based indexing (first cell is (0, 0) NOT (1, 1) ).
2. You cannot reverse actions on revealed cells.
3. Flags are limited to the number of mines.

HAPPY GAME
