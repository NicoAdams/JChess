package dtadams.chess;

import dtadams.chess.piece.PieceType;
import java.util.ArrayList;

public abstract class Piece {
	
	protected PieceType type;
	protected PieceColor color;
	protected boolean isNone;

	public Piece(PieceType _type, PieceColor _color, boolean _isNone) {
		this.type = _type;
		this.color = _color;
		this.isNone = _isNone;
	}

	public PieceType getType() {
		return type;
	}

	public String getName() {
		return type.getName();
	}

	public PieceColor getColor() {
		return color;
	}

	public boolean isNone() {
		return isNone;
	}

	public boolean canMove(Position current, Position move, Board b) {
		ArrayList<Position> moveList = getMoveList(current, b);
		for (Position pos: moveList) {
			if(move.equals(pos)) return true;
		}

		return false;
	}

	public String toString() {
		String toReturn = "";
		if(color == PieceColor.WHITE) toReturn += "W ";
		else if(color == PieceColor.BLACK) toReturn += "B ";
		toReturn += getName();
		return toReturn;
	}

	public Move move(Position current, Position movePos, Board b) {

		Move move;

		if(!b.getPiece(movePos).isNone()) {
			move = new MoveCapture(current, movePos, movePos, MoveType.NORMAL);
		} else move = new Move(current, movePos, MoveType.NORMAL);

		onMove(current, movePos, b);
		return move;
	}

	// Called on Piece moved
	protected void onMove(Position current, Position movePos, Board b) {}

	// Called at the beginning of each turn
	public void onTurn(Board b) {}

	public ArrayList<Position> getMoveList(Position current, Board b) {
		ArrayList<Position> moveList = getMoveListIgnoreCheck(current, b);
		ArrayList<Position> newMoveList = new ArrayList<>();
		for(Position move: moveList) {
			Board newBoard = b.move(this.copy().move(current, move, b));
			if(!newBoard.inCheck(this.color)) newMoveList.add(move);
		}

		return newMoveList;
	}

	public abstract ArrayList<Position> getMoveListIgnoreCheck(Position current, Board b);

	public abstract Piece copy();
}