package dtadams.chess.piece;

import dtadams.chess.*;
import java.util.ArrayList;

public class Mover {
	
	public enum Type {
		STRAIGHT, SINGLE;
	}

	public enum Capture {
		OFF, ALLOWED, ONLY;
	}

	public static ArrayList<Position> getMoves(Position current,
												Board b,
												int[] rowDeltas,
												int[] colDeltas,
												PieceColor color,
												Type t,
												Capture c) {

		ArrayList<Position> moves = new ArrayList<>();

		for(int i=0; i<rowDeltas.length; i++) {
			int rowDelta = rowDeltas[i];
			int colDelta = colDeltas[i];

			if(t == Type.STRAIGHT) moves.addAll(getMovesStraight(current, b, rowDelta, colDelta, color, c));
			if(t == Type.SINGLE) moves.addAll(getMovesSingle(current, b, rowDelta, colDelta, color, c));
		}
		return moves;
	}

	static ArrayList<Position> getMovesStraight(Position current,
												Board b,
												int rowDelta,
												int colDelta,
												PieceColor color,
												Capture c) {

		ArrayList<Position> moveList = new ArrayList<>();

		Position pos = new Position(current.row, current.col);

		while(true) {
			pos = new Position(pos.row + rowDelta, pos.col + colDelta);

			if(b.onBoard(pos)) {

				if(b.getPiece(pos).isNone()
				&& c != Capture.ONLY) {
					moveList.add(pos); // Unoccupied

				} else {

					if(b.getPiece(pos).getColor() != color
					&& c != Capture.OFF) {
						moveList.add(pos); // Occupied by opposite-color piece

					}
					break; // Further moves blocked by piece
				}
			}
			else break; // Further moves blocked by end of board
		}

		return moveList;
	}

	static ArrayList<Position> getMovesSingle(Position current,
												Board b,
												int rowDelta,
												int colDelta,
												PieceColor color,
												Capture c) {

		ArrayList<Position> moveList = new ArrayList<>();
		
		Position pos = new Position(current.row + rowDelta, current.col + colDelta);

		if (b.onBoard(pos)
		&& ((b.getPiece(pos).isNone() 
				&& c != Capture.ONLY)
			|| (!b.getPiece(pos).isNone()
				&& b.getPiece(pos).getColor() != color
				&& c != Capture.OFF))) {

			moveList.add(pos);
		}

		return moveList;
	}
}
