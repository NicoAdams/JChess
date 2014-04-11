package dtadams.chess.piece;

import dtadams.chess.*;
import java.util.ArrayList;

public class Queen extends Piece {
	
	public Queen(PieceColor _color) {
		super(PieceType.QUEEN, _color, false);
	}

	public ArrayList<Position> getMoveListIgnoreCheck(Position current, Board b) {
		int[] rowDeltas = new int[]{1, 0, -1, -1, -1, 0, 1, 1};
		int[] colDeltas = new int[]{1, 1, 1, 0, -1, -1, -1, 0};

		return Mover.getMoves(current,
								b,
								rowDeltas,
								colDeltas,
								color,
								Mover.Type.STRAIGHT,
								Mover.Capture.ALLOWED);
	}

	public Piece copy() {
		return new Queen(color);
	}
}