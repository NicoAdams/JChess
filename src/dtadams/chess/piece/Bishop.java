package dtadams.chess.piece;

import dtadams.chess.*;
import java.util.ArrayList;

public class Bishop extends Piece {
	
	public Bishop(PieceColor _color) {
		super(PieceType.BISHOP, _color, false);
	}

	public ArrayList<Position> getMoveListIgnoreCheck(Position current, Board b) {
		int[] rowDeltas = new int[]{1, 1, -1, -1};
		int[] colDeltas = new int[]{1, -1, 1, -1};

		return Mover.getMoves(current,
								b,
								rowDeltas,
								colDeltas,
								color,
								Mover.Type.STRAIGHT,
								Mover.Capture.ALLOWED);
	}

	public Piece copy() {
		return new Bishop(color);
	}
}