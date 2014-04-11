package dtadams.chess.piece;

import dtadams.chess.*;
import java.util.ArrayList;

public class Knight extends Piece {
	
	public Knight(PieceColor _color) {
		super(PieceType.KNIGHT, _color, false);
	}

	public ArrayList<Position> getMoveListIgnoreCheck(Position current, Board b) {
		int[] rowDeltas = new int[]{1, 2, 2, 1, -1, -2, -2, -1};
		int[] colDeltas = new int[]{2, 1, -1, -2, -2, -1, 1, 2};

		return Mover.getMoves(current,
								b,
								rowDeltas,
								colDeltas,
								color,
								Mover.Type.SINGLE,
								Mover.Capture.ALLOWED);
	}

	public Piece copy() {
		return new Knight(color);
	}
}