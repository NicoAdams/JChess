package dtadams.chess;

import java.io.InputStreamReader;

public class HumanPlayer extends Player {

	public HumanPlayer() {}

	public Move move(Board b) {
		
		Move move;
		while(true) {
			Position from = MoveInputReader.readPosition("Move from: ", "Invalid position!");
			Position to   = MoveInputReader.readPosition("Move to:   ", "Invalid position!");

			try {
				move = b.getMove(from, to);
			} catch(InvalidMoveException e) {
				System.out.println("Invalid move!");
				continue;
			}
			break;
		}

		return move;
	}
}