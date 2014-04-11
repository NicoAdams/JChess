package dtadams.chess;

public abstract class Player {

	protected PieceColor color;

	public Player(PieceColor _color) {
		this.color = _color;
	}

	public PieceColor getColor() {
		return this.color;
	}

	public abstract Move move(Board b);
}