package dtadams.chess;

public class Move {

	Position current;
	Position move;
	MoveType type;
	boolean isCapture;

	public Move(Position _current, Position _move, MoveType _type) {
		this.current = _current;
		this.move = _move;
		this.type = _type;
		isCapture = false;
	}

	public Position current() {
		return current;
	}

	public Position move() {
		return move;
	}

	public MoveType type() {
		return type;
	}

	public boolean isCapture() {
		return isCapture;
	}

	public String toString() {
		return ""+current+"->"+move;
	}
}