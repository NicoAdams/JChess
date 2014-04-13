package dtadams.chess.gui;

import dtadams.chess.*;
import dtadams.chess.piece.*;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.Iterator;

public class ChessBoard
	extends Container
	implements MouseListener, MouseMotionListener {

	Board b;

	int squareSize,
		width,
		height,
		horzBorder,
		vertBorder;

	Position currentHover;
	ArrayList<Position> highlights;

	PieceColor view;
	PieceColor currentPlayer;

	Piece selectedPiece;
	Position selectedPiecePosition;
	Move playerMove;

	public ChessBoard(int _squareSize, Board _b, int _horzBorder, int _vertBorder) {
		super();

		this.squareSize = _squareSize;
		this.b = _b;

		this.horzBorder = _horzBorder;
		this.vertBorder = _vertBorder;

		this.width = b.getCols() * this.squareSize;
		this.height = b.getRows() * this.squareSize;

		this.currentHover = new Position(0, 0);
		this.highlights = new ArrayList<>();

		this.view = PieceColor.WHITE;
		this.currentPlayer = PieceColor.NONE;

		this.selectedPiece = new NonePiece();
		this.selectedPiecePosition = new Position(0, 0);
		this.playerMove = null;
		
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
	}

	public int getSquareSize() {
		return this.squareSize;
	}

	public int getBoardWidth() {
		return this.width;
	}

	public int getBoardHeight() {
		return this.height;
	}

	public Position getSquare(int x, int y) {
		int row = (this.view == PieceColor.WHITE
					? b.getRows() - (y / squareSize)
					: (y / squareSize) + 1);
		int col = (this.view == PieceColor.WHITE
					? (x / squareSize) + 1
					: b.getCols() - (x / squareSize));
		return new Position(row, col);
	}

	int[] getCoords(Position pos) {
		return new int[]{
			(this.view == PieceColor.WHITE
			? squareSize * (pos.col - 1)
			: squareSize * (b.getCols() - pos.col))
			+ this.horzBorder
			,
			(this.view == PieceColor.WHITE 
			? squareSize * (b.getRows() - pos.row)
			: squareSize * (pos.row - 1))
			+ this.vertBorder
		};
	}

	public Move getMove() {
		return this.playerMove;
	}

	public void resetMove() {
		this.playerMove = null;
	}

	public void setView(PieceColor color) {
		if(color == PieceColor.NONE) return;
		this.view = color;
		repaint();
	}

	public void setCurrentPlayer(PieceColor color) {
		this.currentPlayer = color;
	}

	public void update(Board _b) {
		this.b = _b;
		updateHighlights();
		repaint();
	}

	boolean outOfBounds(Position p) {
		return p.row < 1
			|| p.row > b.getRows()
			|| p.col < 1
			|| p.col > b.getCols();
	}

	boolean posListContains(ArrayList<Position> list, Position pos) {
		for(Position listPos: list) {
			if(listPos.equals(pos)) return true;
		}
		return false;
	}

	void updateHighlights() {
		if(outOfBounds(currentHover)
		|| !(b.getPiece(currentHover).getColor() == this.currentPlayer
			|| b.getPiece(currentHover).getColor() == PieceColor.NONE)) {
			highlights = new ArrayList<>();
		} else {
			highlights = (b.getPiece(currentHover)
				.getMoveList(currentHover, b)
			);
		}
	}

	void updateCurrentHover(int x, int y) {
		this.currentHover = getSquare(x, y);
	}

	public void paint(Graphics g0) {
		Graphics2D g = (Graphics2D)g0;

		Iterator<Position> positions = b.getIterator();

		while(positions.hasNext()) {
			Position pos = positions.next();
			int[] coords = getCoords(pos);
			
			BufferedImage squareImage = (((pos.row + pos.col) % 2 == 1)
											? ImageLoader.getWhiteSquare()
											: ImageLoader.getBlackSquare()
											);

			g.drawImage(squareImage,
						coords[0],
						coords[1],
						squareSize,
						squareSize,
						null);

			if(pos.equals(selectedPiecePosition))
				g.drawImage(ImageLoader.getSelectedSquare(),
							coords[0],
							coords[1],
							squareSize,
							squareSize,
							null);

			if(posListContains(highlights, pos))
				g.drawImage(ImageLoader.getHighlightSquare(),
							coords[0],
							coords[1],
							squareSize,
							squareSize,
							null);

			Piece piece = b.getPiece(pos);
			g.drawImage(ImageLoader.getPiece(piece.getType(), piece.getColor()),
						coords[0],
						coords[1],
						squareSize,
						squareSize,
						null);
		}
	}

	public void mouseEntered (MouseEvent e) {}
	public void mousePressed (MouseEvent e) {}
	public void mouseReleased (MouseEvent e) {}
	public void mouseExited (MouseEvent e) {}
	public void mouseDragged(MouseEvent e) {}

	public void mouseClicked(MouseEvent e) {
		Position clickPosition = getSquare(e.getX(), e.getY());

		// Ignores any clicks not on chessboard
		if(outOfBounds(clickPosition)) return;

		if(this.selectedPiece.getType() == PieceType.NONE) {
			// Attempts to select the piece that was just clicked
			Piece piece = b.getPiece(clickPosition);

			if(piece.getType() != PieceType.NONE
			&& piece.getColor() == this.currentPlayer) {
				this.selectedPiece = b.getPiece(clickPosition);
				this.selectedPiecePosition = clickPosition;
			}
		} else {
			// Attempts to move the selected piece
			try {
				this.playerMove = b.getMove(selectedPiecePosition, clickPosition);
				synchronized(this) {
					this.notify();
				}
			} catch(InvalidMoveException ex) {

			}
			this.selectedPiece = new NonePiece();
			this.selectedPiecePosition = new Position(0, 0);
			
			updateCurrentHover(e.getX(), e.getY());
			updateHighlights();
		}

		repaint();
	}

	public void mouseMoved(MouseEvent e) {
		Position prevHover = currentHover.copy();
		updateCurrentHover(e.getX(), e.getY());
		
		if(this.selectedPiece.getType() == PieceType.NONE
		&& !currentHover.equals(prevHover)) {
			updateHighlights();
			repaint();
		}
	}
}
