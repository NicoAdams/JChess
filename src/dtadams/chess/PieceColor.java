package dtadams.chess;

public enum PieceColor {
	WHITE("White"), BLACK("Black"), NONE("(none)");

	String name;
	PieceColor(String _name) {
		this.name = _name;
	}

	public String toString() {
		return name;
	}
}