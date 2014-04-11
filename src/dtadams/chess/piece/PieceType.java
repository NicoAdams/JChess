package dtadams.chess.piece;

public enum PieceType {
	PAWN("Pawn"),
	ROOK("Rook"),
	KNIGHT("Knight"),
	BISHOP("Bishop"),
	QUEEN("Queen"),
	KING("King"),
	NONE("(none)");

	String name;
	PieceType(String _name) {
		this.name = _name;
	}

	public String getName() {
		return name;
	}

	public String toString() {
		return getName();
	}
}