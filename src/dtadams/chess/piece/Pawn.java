package dtadams.chess.piece;

import dtadams.chess.*;
import java.util.ArrayList;

public class Pawn extends Piece {
	
	boolean canEnPassent;

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
		this.canEnPassent = false;
	}
	
	public void onMove(Position current, Position movePos, Board b) {
		if(Math.abs(movePos.row - current.row) == 2) {
			this.canEnPassent = true;
		}
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

		// En Passent capture
		for(int i=0; i<rowDeltas.length; i++) {
			if(canEnPassentCapture(current, rowDeltas[i], colDeltas[i], b)) {
				moveList.addAll(Mover.getMoves(current,
												b,
												new int[]{rowDeltas[i]},
												new int[]{colDeltas[i]},
												color,
												Mover.Type.SINGLE,
												Mover.Capture.OFF));
			}
		}

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

	boolean canEnPassentCapture(Position current, int rowDelta, int colDelta, Board b) {
		int rowDeltaCheck = 0;
		Position checkPos = new Position(current.row + rowDeltaCheck, current.col + colDelta);

		if(b.onBoard(checkPos)) {
			Piece piece = b.getPiece(checkPos);
			if(piece.getType() == PieceType.PAWN
			&& ((Pawn)piece).canEnPassent())

				return true;
		}
		return false;
	}

	public Piece copy() {
		return new Pawn(color, canEnPassent);
	}
}