package dtadams.gui;

import dtadams.chess.*;

public class GUIGame extends Game {

	ChessGUI gui;

	public GUIGame(Player white, Player black, ChessGUI _gui) {
		super(white, black);
		this.gui = _gui;
	}

	protected void display(Board b) {
		gui.update(b);
	}
}