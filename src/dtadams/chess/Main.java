package dtadams.chess;

import dtadams.chess.gui.ChessGUI;
import dtadams.chess.gui.GUIGame;
import dtadams.chess.gui.GUIPlayer;
import dtadams.chess.gui.AdjectiveGenerator;

public class Main {
	public static void main(String[] args) {

		Board b = new Board();
		ChessGUI gui = new ChessGUI(AdjectiveGenerator.random()+" CHESS - a Dominic Adams production",
									b);
		gui.setVisible(true);

		GUIPlayer white = new GUIPlayer(PieceColor.WHITE, gui);
		GUIPlayer black = new GUIPlayer(PieceColor.BLACK, gui);

		Player currentPlayer = white;
		Player otherPlayer = black;
		
		Game game = new GUIGame(white, black, gui);
		PieceColor winner = game.play();

		System.out.println("Winner: "+winner);
	}
}