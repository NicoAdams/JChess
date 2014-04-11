package dtadams.chess.piece;

import dtadams.chess.*;
import java.util.ArrayList;

public class Rook extends Piece {
	
	boolean hasMoved;

	public Rook(PieceColor _color) {
		super(PieceType.ROOK, _color, false);
		hasMoved = false;
	}

	Rook(PieceColor _color, boolean _hasMoved) {
		super(PieceType.ROOK, _color, false);
		hasMoved = _hasMoved;
	}
	
	public boolean canCastle() {
		return !hasMoved;
	}

	public void onMove(Board b) {
		hasMoved = true;
	}

	public ArrayList<Position> getMoveListIgnoreCheck(Position current, Board b) {
		int[] rowDeltas = new int[]{1,0,-1,0};
		int[] colDeltas = new int[]{0,1,0,-1};

		return Mover.getMoves(current,
								b,
								rowDeltas,
								colDeltas,
								color,
								Mover.Type.STRAIGHT,
								Mover.Capture.ALLOWED);
	}

	public Piece copy() {
		return new Rook(color, hasMoved);
	}
}