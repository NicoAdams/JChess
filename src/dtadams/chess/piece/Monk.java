package dtadams.chess.piece;

import dtadams.chess.*;
import java.util.ArrayList;

public class Monk extends Rook {

	/* Experimental replacement of the Rook. Can move only up to 3 spaces, but
	 * can jump pieces
	 */
	
	boolean hasMoved;

	public Monk(PieceColor _color) {
		super(_color);
	}

	Monk(PieceColor _color, boolean _hasMoved) {
		super(_color);
	}
	
	public ArrayList<Position> getMoveListIgnoreCheck(Position current, Board b) {
		ArrayList<Position> moveList = new ArrayList<Position>();

		int[] rowDeltas = new int[]{1,0,-1,0};
		int[] colDeltas = new int[]{0,1,0,-1};

		moveList.addAll(Mover.getMoves(current,
										b,
										rowDeltas,
										colDeltas,
										color,
										Mover.Type.SINGLE,
										Mover.Capture.ALLOWED));

		rowDeltas = new int[]{2,0,-2,0};
		colDeltas = new int[]{0,2,0,-2};

		moveList.addAll(Mover.getMoves(current,
										b,
										rowDeltas,
										colDeltas,
										color,
										Mover.Type.SINGLE,
										Mover.Capture.ALLOWED));

		rowDeltas = new int[]{3,0,-3,0};
		colDeltas = new int[]{0,3,0,-3};

		moveList.addAll(Mover.getMoves(current,
										b,
										rowDeltas,
										colDeltas,
										color,
										Mover.Type.SINGLE,
										Mover.Capture.ALLOWED));

		return moveList;
	}

	public Piece copy() {
		return new Monk(color, hasMoved);
	}
}