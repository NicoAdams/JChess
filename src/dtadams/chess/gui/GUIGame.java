package dtadams.chess.gui;

import dtadams.chess.*;

public class GUIGame extends Game {

	ChessGUI gui;

	public GUIGame(Player _white, Player _black, Board _b, ChessGUI _gui) {
		super(_white, _black, _b);
		this.gui = _gui;
	}

	protected void display(Board b) {
		gui.update(b);
	}
}