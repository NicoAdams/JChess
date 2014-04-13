package dtadams.chess;

// Base class for a chess game with no display

public abstract class Game {

	Player white, black;
	PieceColor currentColor, oppositeColor;
	Board b;

	public Game(Player _white, Player _black, Board _b) {
		this.white = _white;
		this.black = _black;
		this.b = _b;
	}

	public PieceColor play() {
		this.currentColor = PieceColor.WHITE;
		this.oppositeColor = PieceColor.BLACK;

		while(!b.inCheckmate(currentColor)) {
			playTurn();
			display(b);
		}

		return oppositeColor; // The winner
	}

	void playTurn() {
		Player toMove = (currentColor == PieceColor.WHITE ? white : black);
		Move move = toMove.move(b);

		b = b.move(move);

		PieceColor tempColor = currentColor;
		currentColor = oppositeColor;
		oppositeColor = tempColor;
	}

	protected void display(Board b) {}
}