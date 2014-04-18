package dtadams.chess.gui;

import dtadams.chess.*;
import dtadams.chess.piece.*;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import javax.swing.JComponent;

// TODO How do I handle the colors?

public class CapturedPiecesDisplay
	extends JComponent {

	PieceColor color;
	ArrayList<Piece> pieces;
	int width, height, pieceSize;

	public CapturedPiecesDisplay(PieceColor _color, int _width, int _height, int _pieceSize) {
		super();

		this.pieces = new ArrayList<>();
		this.color = _color;

		this.width = _width;
		this.height = _height;
		this.pieceSize = _pieceSize;
	}

	public void setPieces(ArrayList<Piece> _pieces) {
		this.pieces = _pieces;
	}

	public void addPiece(Piece _piece) {
		this.pieces.add(_piece);
	}

	public void PaintComponent(Graphics g0) {
		Graphics2D g = (Graphics2D)g0;

		for(Piece piece: this.pieces) {
			if(piece.getColor() == this.color) {
//				g.drawImage(
//					ImageLoader.getPiece(piece.getType(), piece.getColor()),


			}
		}
	}
}