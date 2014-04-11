package dtadams.chess;

public class MoveCapture extends Move {

	Position capture;

	public MoveCapture(Position _current, Position _move, Position _capture, MoveType _type) {
		super(_current, _move, _type);
		this.isCapture = true;
		this.capture = _capture; 
	}

	public Position capture() {
		return capture;
	}

	public String toString() {
		return super.toString()+"("+capture+")";
	}
}