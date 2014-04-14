package dtadams.chess.gui;

import java.awt.Dimension;
import java.applet.Applet;
import java.lang.InterruptedException;
import dtadams.chess.*;

public class ChessApplet extends Applet {

	public void init() {
		int width = 600;
		int height = 700;
		int squareSize = 50;
		Board b = new Board(Board.Setup.MONK);

		ChessGUI gui = new ChessGUI(width, height, squareSize, b);
		
		GUIPlayer white = new GUIPlayer(PieceColor.WHITE, gui);
		GUIPlayer black = new GUIPlayer(PieceColor.BLACK, gui);

		Game game = new GUIGame(white, black, b, gui);
		PieceColor winner = game.play();

		this.add(gui);
	}
}