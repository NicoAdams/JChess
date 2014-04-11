package dtadams.chess.piece;

import dtadams.chess.*;
import java.util.ArrayList;

public class NonePiece extends Piece {

	public NonePiece() {
		super(PieceType.NONE, PieceColor.NONE, true);
	}

	public ArrayList<Position> getMoveListIgnoreCheck(Position current, Board b) {
		return new ArrayList<Position>();
	}

	public Piece copy() {
		return new NonePiece();
	}
}