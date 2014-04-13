package dtadams.chess.gui;

import dtadams.chess.Board;
import dtadams.chess.Move;
import dtadams.chess.Player;
import dtadams.chess.PieceColor;


public class GUIPlayer extends Player {

	ChessGUI g;

	public GUIPlayer(PieceColor _color, ChessGUI _g) {
		super(_color);
		this.g = _g;
	}

	public Move move(Board b) {
		return g.getMove(this.color);
	}
}