# ChessAI

## How to run

Download all the files in the same folder and run it through "Chess.java".

If you want to run it in cmd, then type ```java Chess``` to run.



## How to operate
White is CAPITAL, black is lowercase. Upon running the program, it will ask you if you want to play with another player or with the computer.
- Type two if you want the two-player mode.
- Type ai if you want to play with the computer.
If you type ai , then you will be prompted how much depth is required.
- Type an integer number. It works at 3 and really good at 5.
- Depth 5 takes anywhere from 2 seconds to 50 seconds at max.
It then asks you to type which piece do you want to move.
- Type something like a2 or b2, etc.
- Make sure to give a cell which contains the piece of your own colour. Else, you will be prompted to make selection again.
- After giving the correct input, you will be shown the possible moves for the selected piece.
- If no moves are possible, you will have to select again. Else, you can select from the given possible moves.

The same goes on till someone wins the game or a draw occurs.

## Implemented heuristic
The heuristic score function includes different type of scores inside it.
- First, we have the score of all the pieces present on the board.
- The number of queens each player has.
- How many players surround each player’s king.
- The position of the pawn on the board. The score for each pawn increases as it moves to the next position. (We keep in mind the piece promotion) .
- The position of the knight on the board. We know that a knight is the best if it is in the middle rather than the sides.
WE HAVE A MATRIX FOR THE KNIGHT POSITION SCORE AND THE BLACK AND WHITE PAWN POSITION SCORES.
