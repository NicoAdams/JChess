package dtadams.chess.piece;

import dtadams.chess.*;
import java.util.ArrayList;

public class King extends Piece {

	boolean hasMoved;

	public King(PieceColor _color) {
		super(PieceType.KING, _color, false);
		hasMoved = false;
	}

	King(PieceColor _color, boolean _hasMoved) {
		super(PieceType.KING, _color, false);
		hasMoved = _hasMoved;
	}

	public void onMove(Board b) {
		hasMoved = true;
	}

	public ArrayList<Position> getMoveListIgnoreCheck(Position current, Board b) {
		int[] rowDeltas = new int[]{1, 0, -1, -1, -1, 0, 1, 1};
		int[] colDeltas = new int[]{1, 1, 1, 0, -1, -1, -1, 0};

		ArrayList<Position> moveList = Mover.getMoves(current,
														b,
														rowDeltas,
														colDeltas,
														color,
														Mover.Type.SINGLE,
														Mover.Capture.ALLOWED);

		// Castling

		if(!hasMoved) {

			for(int row=1; row<=b.getRows(); row++)
			for(int col=1; col<=b.getCols(); col++) {
				Position pos = new Position(row, col);
				Piece piece = b.getPiece(pos);

				if(piece.getType() == PieceType.ROOK
				&& ((Rook)piece).canCastle()) {
					
					// TODO implement further check rules

					if(canCastle(current, pos, b)) {
						if(pos.col > current.col) {
							//Kingside castle
							moveList.add(new Position(current.row, current.col+2));
						} else {
							//Queenside castle
							moveList.add(new Position(current.row, current.col-2));
						}
					}
				}
			}
		}

		return moveList;
	}

	public Piece copy() {
		return new King(color, hasMoved);
	}

	public Move move(Position current, Position move, Board b) {

		if(Math.abs(current.col - move.col) == 2) {
			return new Move(current, move, MoveType.CASTLE);
		}

		return super.move(current, move, b);
	}

	boolean canCastle(Position current, Position pos, Board b) {
		if(current.row != pos.row) return false;
		if(current.equals(pos)) return false;

		// if(b.inCheck(this.getColor())) return false;

		int row = current.row;
		int delta = (int) Math.signum(pos.col - current.col);
		for(int col=current.col+delta; delta*col < delta*pos.col; col+=delta) {

			Position testPos = new Position(row, col);

			if(!b.getPiece(testPos).isNone()) return false;

			// Checks for check in each space
			// TODO
			// Board b1 = b.copy();
			// b1.setPiece(current, new NonePiece());
			// b1.setPiece(testPos, this);
			// if(b1.inCheck(this.getColor())) return false;
		}

		return true;
	}
}
