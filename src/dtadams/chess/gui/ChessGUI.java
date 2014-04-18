package dtadams.chess.gui;

import dtadams.chess.*;
import dtadams.chess.piece.*;
import java.awt.Color;
import javax.swing.JComponent;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import javax.imageio.ImageIO;

public class ChessGUI
	extends JComponent {

	int width,
		height,
		boardWidth,
		boardHeight,
		horzBorder,
		vertBorder;

	PieceColor view;
	PieceColor currentPlayer;

	ChessBoard cb;

	public ChessGUI(int _width, int _height, int _squareSize, Board _b) {
		super();
		setPreferredSize(new Dimension(_width, _height));

		this.width = _width;
		this.height = _height;

		this.boardWidth = _squareSize * _b.getCols();
		this.boardHeight = _squareSize * _b.getRows();
		this.horzBorder = (width - boardWidth) / 2;
		this.vertBorder = (height - boardHeight) / 2;

		this.view = PieceColor.WHITE;
		this.currentPlayer = PieceColor.NONE;

		this.cb = new ChessBoard(_squareSize, _b, horzBorder, vertBorder);
		cb.setBounds(horzBorder, vertBorder, boardWidth, boardHeight);
		cb.setView(view);
		cb.setCurrentPlayer(currentPlayer);
		this.add(cb);

		repaint();
	}

	public ChessBoard getBoard() {
		return cb;
	}

	public Move getMove(PieceColor color) {

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

	public void update(Board b) {
		cb.update(b);
	}

	public void paintComponent(Graphics g0) {
		Graphics2D g = (Graphics2D)g0;

		g.drawImage(ImageLoader.getBackground(),
					0,
					0,
					width,
					height,
					null);
	}
}
