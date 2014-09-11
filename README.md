What is "JChess"?
-----------------
JChess is a Java based chess engine designed for modification. Modifications so far include a simple minimax AI as well as a new piece: "Monk".

What is "Monk"?
---------------
"Castle" (n.) An object that is extremely attached to its surroundings

"Monk" (n.) An object that is extremely unattached to its surroundings

As a beginning chess player, I began to wonder what it would be like to change the action of the rooks. Currently, they are extremely difficult to extricate and typically do not become important until very late in the game.

The "monk" is an alternate version of the rook which can move in a straight line in any direction, modulated by two rules:
a) It cannot move more than three spaces at a time
b) It can jump pieces in its way

The hope is to give rooks more power in the game's beginning for a trade-off of less at the end. The bigger hope is to break chess and see if anything interesting comes out.

Known Bugs
----------

- Pawn promotion not enabled
- Flash effect upon mouse entering board area

Enhancements
------------

- Add captured pieces display
- Extract GameLogic class from Board class
- Return list of moves instead of positions as move list for full generality

Chess piece art credit:
http://www.wpclipart.com/recreation/games/chess/chess_set_1

License
-------

The MIT License (MIT)

Copyright (c) <2014> <Dominic Adams>

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.