package dtadams.chess.gui;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.WindowEvent;
import java.awt.event.WindowAdapter;
import java.lang.InterruptedException;
import dtadams.chess.*;

public class ChessGUI extends Frame {

	ChessPanel cp;

	public ChessGUI(String title, Board _b) {
		super(title);

		int width = 600;
		int height = 700;
		this.setSize(new Dimension(width, height));
		this.setResizable(false);

		this.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent we){
				System.exit(0);
			}
		});

		this.cp = new ChessPanel(width, height, 50, _b);
		this.add(cp);
	}

	public Move getMove(PieceColor color) {

		ChessBoard cb = cp.getBoard();
		cb.setCurrentPlayer(color);
		cb.setView(color);
		
		while(cb.getMove() == null) {
			try {
				synchronized(cb) {
					cb.wait();
				}
			} catch (InterruptedException e) {}
		}
		Move move = cb.getMove();

		cb.resetMove();
		cb.setCurrentPlayer(PieceColor.NONE);
		return move;
	}

	public void update(Board _b) {
		cp.getBoard().update(_b);
	}
}