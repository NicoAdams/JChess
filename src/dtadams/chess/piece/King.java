package dtadams.chess.piece;

import dtadams.chess.*;
import java.util.ArrayList;

public class King extends Piece {

	boolean hasMoved;

	public King(PieceColor _color) {
		super(PieceType.KING, _color, false);
		this.hasMoved = false;
	}

	King(PieceColor _color, boolean _hasMoved) {
		super(PieceType.KING, _color, false);
		this.hasMoved = _hasMoved;
	}

	protected void onMove(Position current, Position movePos, Board b) {
		this.hasMoved = true;
	}

	public boolean hasMoved() {
		return this.hasMoved;
	}

	public ArrayList<Position> getMoveList(Position current, Board b) {
		ArrayList<Position> moveList = super.getMoveList(current, b);

		// Castling
		if(!this.hasMoved) {

			ArrayList<Position> rookPositions = b.locatePiece(PieceType.ROOK, color);
			for(Position rookPos: rookPositions) {

				Rook rook = (Rook) b.getPiece(rookPos);
				if(this.canCastle(current, rookPos, b, rook)) {
					if(rookPos.col > current.col) {
						//Kingside castle
						moveList.add(new Position(current.row, current.col+2));
					} else {
						//Queenside castle
						moveList.add(new Position(current.row, current.col-2));
					}
				}
			}
		}

		return moveList;

	} 

	public ArrayList<Position> getMoveListIgnoreCheck(Position current, Board b) {
		int[] rowDeltas = new int[]{1, 0, -1, -1, -1, 0, 1, 1};
		int[] colDeltas = new int[]{1, 1, 1, 0, -1, -1, -1, 0};

		return Mover.getMoves(current,
								b,
								rowDeltas,
								colDeltas,
								color,
								Mover.Type.SINGLE,
								Mover.Capture.ALLOWED);
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

	boolean canCastle(Position current, Position pos, Board b, Rook rook) {
		if(!rook.canCastle()) return false;
		if(current.row != pos.row) return false;
		if(current.equals(pos)) return false;

		// if(b.inCheck(this.getColor())) return false;

		int row = current.row;
		int delta = (int) Math.signum(pos.col - current.col);
		for(int col=current.col; delta*col < delta*pos.col; col+=delta) {

			Position testPos = new Position(row, col);

			if(!b.getPiece(testPos).isNone()) return false;

//			Checks for check in each space
			Board b1 = b.copy();
			b1.setPiece(current, new NonePiece());
			b1.setPiece(testPos, this);
			if(b1.inCheck(this.getColor())) return false;
		}

		return true;
	}
}
