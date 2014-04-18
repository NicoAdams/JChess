package dtadams.chess;

// Base class for a chess game with no display

public abstract class Game {

	Player white, black;
	PieceColor currentColor;
	Board b;

	public Game(Player _white, Player _black, Board _b) {
		this.white = _white;
		this.black = _black;
		this.b = _b;
	}

	public PieceColor play() {
		this.currentColor = PieceColor.WHITE;

		while(!b.inCheckmate(currentColor)) {
			playTurn();
			display(b);
		}

		return PieceColor.opposite(currentColor); // The winner
	}

	protected void playTurn() {
		Player toMove = (currentColor == PieceColor.WHITE ? white : black);
		Move move = toMove.move(b);
		b = b.move(move);

		currentColor = PieceColor.opposite(currentColor);
	}

	protected void display(Board b) {}
}