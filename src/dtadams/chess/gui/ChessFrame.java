package dtadams.chess.gui;

import dtadams.chess.*;
import dtadams.chess.ai.*;
import java.awt.Dimension;
import java.awt.event.WindowEvent;
import java.awt.event.WindowAdapter;
import java.lang.InterruptedException;
import javax.swing.JFrame;

public class ChessFrame extends JFrame {

	ChessGUI cp;

	public static void main(String[] args) {
		int width = 600;
		int height = 700;
		int squareSize = 50;
		Board b = new Board(Board.Setup.NORMAL);

		ChessGUI gui = new ChessGUI(width, height, squareSize, b);
		ChessFrame frame = new ChessFrame(AdjectiveGenerator.random()+" CHESS - a Dominic Adams production",
											width,
											height,
											gui);
		frame.setVisible(true);

		String aiDataDir = "lib/aidata/";
		String aiDataFile = "1.txt"; // "2.txt";
		int recurDepth = 2;

		Player white = new GUIPlayer(PieceColor.WHITE, gui);
		AIPlayer black = new AIPlayer(PieceColor.BLACK,
										recurDepth,
										new AIStrategy(new AIDataParser(aiDataDir + aiDataFile)));

		Game game = new GUIGame(white, black, b, gui);
		PieceColor winner = game.play();

		System.out.println("Winner: "+winner);
	}

	public ChessFrame(String _title, int _width, int _height, ChessGUI _gui) {
		super(_title);
		this.setSize(new Dimension(_width, _height));
		this.setResizable(false);
		this.add(_gui);

		this.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent we){
				System.exit(0);
			}
		});
	}
}