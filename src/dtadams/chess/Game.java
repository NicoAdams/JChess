package dtadams.chess;

// Base class for a chess game with no display

public abstract class Game {

	Player white, black;
	PieceColor currentColor, oppositeColor;
	Board b;

	public Game(Player _white, Player _black) {
		this.white = _white;
		this.black = _black;
	}

	public void play() {
		b = new Board();
		this.currentColor = PieceColor.WHITE;
		this.oppositeColor = PieceColor.BLACK;

		while(!b.inCheckmate(currentColor)) {
			playTurn();
			display(b);
		}
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