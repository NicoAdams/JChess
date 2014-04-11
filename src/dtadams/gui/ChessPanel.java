package dtadams.gui;

import dtadams.chess.*;
import dtadams.chess.piece.*;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import javax.imageio.ImageIO;

public class ChessPanel
	extends Component
	implements MouseListener, MouseMotionListener {

	Board b;

	int width,
		height,
		squareSize,
		boardWidth,
		boardHeight,
		horzBorder,
		vertBorder;

	Position currentHover;
	ArrayList<Position> highlights;

	PieceColor currentPlayer;
	Piece selectedPiece;
	Position selectedPiecePosition;
	Move playerMove;

	public ChessPanel(int _width, int _height, int _squareSize, Board _b) {
		super();
		setPreferredSize(new Dimension(_width, _height));

		this.width = _width;
		this.height = _height;
		this.squareSize = _squareSize;

		this.b = _b;
		this.boardWidth = squareSize * b.getCols();
		this.boardHeight = squareSize * b.getRows();
		this.horzBorder = (width - boardWidth) / 2;
		this.vertBorder = (height - boardHeight) / 2;

		this.currentHover = new Position(0, 0);
		this.highlights = new ArrayList<>();

		this.addMouseListener(this);
		this.addMouseMotionListener(this);

		this.currentPlayer = PieceColor.NONE;
		this.selectedPiece = new NonePiece();
		this.selectedPiecePosition = new Position(0, 0);
		this.playerMove = null;
	}

	public int getSquareSize() {
		return squareSize;
	}

	public int getBoardWidth() {
		return boardWidth;
	}

	public int getBoardHeight() {
		return boardHeight;
	}

	public Position getSquare(int x, int y) {
		if((x < horzBorder || x > horzBorder + boardWidth)
		|| (y < vertBorder || y > vertBorder + boardWidth))
			return new Position(0,0);

		return new Position(b.getCols() - ((y - vertBorder) / squareSize),
							(x - horzBorder) / squareSize + 1);
	}

	public Move getMove() {
		return this.playerMove;
	}

	public void resetMove() {
		this.playerMove = null;
	}

	public void setCurrentPlayer(PieceColor color) {
		this.currentPlayer = color;
	}

	public void update(Board _b) {
		this.b = _b;
		updateHighlights();
		repaint();
	}

	public void paint(Graphics g0) {

		Graphics2D g = (Graphics2D)g0;
		HashMap<RenderingHints.Key, Object> hints = new HashMap<>();
		g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		// Draws the background image
		g.drawImage(ImageLoader.getBackground(), 0, 0, width, height, null);

		// Draw the squares
		for(int row = b.getRows(); row >= 1; row--)
		for(int col = 1; col <= b.getCols(); col++) {
			BufferedImage image = (((row + col) % 2 == 0)
										? ImageLoader.getWhiteSquare()
										: ImageLoader.getBlackSquare()
									);
			g.drawImage(image,
						horzBorder + squareSize * (col - 1),
						vertBorder + (squareSize * (b.getRows() - row)),
						squareSize,
						squareSize,
						null);
		}

		// Adds the highlights
		for(Position p: this.highlights) {
			int row = p.row;
			int col = p.col;
			g.drawImage(ImageLoader.getHighlightSquare(),
						horzBorder + squareSize * (col - 1),
						vertBorder + (squareSize * (b.getRows() - row)),
						squareSize,
						squareSize,
						null);
		}

		// Adds the selected square
		if(b.onBoard(this.selectedPiecePosition)) {
			int row = this.selectedPiecePosition.row;
			int col = this.selectedPiecePosition.col;
			g.drawImage(ImageLoader.getSelectedSquare(),
						horzBorder + squareSize * (col - 1),
						vertBorder + (squareSize * (b.getRows() - row)),
						squareSize,
						squareSize,
						null);
		}

		// Adds the pieces
		for(int row = b.getRows(); row >= 1; row--)
		for(int col = 1; col <= b.getCols(); col++) {
			Position pos = new Position(row, col);
			Piece piece = b.getPiece(pos);
			BufferedImage image = ImageLoader.getPiece(piece.getType(), piece.getColor());
			g.drawImage(image,
						horzBorder + squareSize * (col - 1),
						vertBorder + (squareSize * (b.getRows() - row)),
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

	boolean outOfBounds(Position p) {
		return p.row < 1
			|| p.row > b.getRows()
			|| p.col < 1
			|| p.col > b.getCols();
	}
}
