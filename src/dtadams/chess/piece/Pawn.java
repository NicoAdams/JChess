package dtadams.chess.piece;

import dtadams.chess.*;
import java.util.ArrayList;

public class Pawn extends Piece {
	
	boolean canEnPassent = false;

	public Pawn(PieceColor _color) {
		super(PieceType.PAWN, _color, false);
		this.canEnPassent = false;
	}

	Pawn(PieceColor _color, boolean _canEnPassent) {
		super(PieceType.PAWN, _color, false);
		this.canEnPassent = _canEnPassent;
	}

	public boolean canEnPassent() {
		return canEnPassent;
	}

	public void onTurn(Board b) {
		canEnPassent = false;
	}

	public Move move(Position current, Position move, Board b) {

		// Promoting the pawn
		if(move.row == 1
		|| move.row == b.getCols())
			return new Move(current, move, MoveType.PROMOTION);

		// En Passent
		if(move.row != current.row
		&& move.col != current.col
		&& b.getPiece(move).isNone()) {

			Position capturePos = new Position(current.row, move.col);
			return new MoveCapture(current, move, capturePos, MoveType.NORMAL);
		}

		return super.move(current, move, b);
	}

	public ArrayList<Position> getMoveListIgnoreCheck(Position current, Board b) {
		
		ArrayList<Position> moveList = new ArrayList<>();

		// Determines row delta
		int rowDelta = 1;
		if(color == PieceColor.BLACK) rowDelta = -1;

		// Single forward space
		int[] rowDeltas = new int[]{rowDelta};
		int[] colDeltas = new int[]{0};
		moveList.addAll(Mover.getMoves(current,
										b,
										rowDeltas,
										colDeltas,
										color,
										Mover.Type.SINGLE,
										Mover.Capture.OFF));
		
		// Diagonal capture
		rowDeltas = new int[]{rowDelta, rowDelta};
		colDeltas = new int[]{1, -1};
		moveList.addAll(Mover.getMoves(current,
										b,
										rowDeltas,
										colDeltas,
										color,
										Mover.Type.SINGLE,
										Mover.Capture.ONLY));

		// Advancing the pawn
		if((rowDelta == 1 && current.row == 2
			|| rowDelta == -1 && current.row == b.getRows()-1)
		&& b.getPiece(new Position(current.row + rowDelta, current.col)).isNone()) {
			rowDeltas = new int[]{2 * rowDelta};
			colDeltas = new int[]{0};
			moveList.addAll(Mover.getMoves(current,
											b,
											rowDeltas,
											colDeltas,
											color,
											Mover.Type.SINGLE,
											Mover.Capture.OFF));
		}

		return moveList;
	}


	public Piece copy() {
		return new Pawn(color, canEnPassent);
	}
}