package dtadams.chess;

import dtadams.gui.ChessGUI;
import dtadams.gui.GUIGame;
import dtadams.gui.GUIPlayer;
import dtadams.gui.AdjectiveGenerator;

/* TO DO
 * ---------------
 * 
 * Pieces
 * - Fix check conditions (not limiting piece movements) 
 * - Fix checkmate conditions (accounts only for king)
 * - Fix castling conditions
 * - Fix En Passent rules
 * - Enable pawn promotion
 *
 * Display
 * - Extract ChessBoard display class from ChessPanel class
 * - Fix flash effect upon mouse entering board area
 * - Add captured pieces display
 *
 * Game
 * - Extract GameLogic class from Board class
 * - Return list of moves instead of positions for full generality
 */

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
		game.play();

		System.out.println("YOLO #wooooot");
	}
}