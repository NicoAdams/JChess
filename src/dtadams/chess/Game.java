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
		b.onTurn(currentColor);

		if(b.inCheck(currentColor))
			System.out.println("Check");

		Player toMove = (currentColor == PieceColor.WHITE ? white : black);
		Move move = toMove.move(b);

		System.out.println(move);

		b = b.move(move);

		PieceColor tempColor = currentColor;
		currentColor = oppositeColor;
		oppositeColor = tempColor;
	}

	protected void display(Board b) {}
}