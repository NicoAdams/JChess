package dtadams.chess;

import dtadams.chess.piece.*;
import java.util.ArrayList;
import java.util.Iterator;

public class Board {
	
	int rows;
	int cols;
	Piece[][] pieces;
	ArrayList<Piece> capturedPieces;

	public enum Setup {
		NORMAL, MONK;
	}

	static Piece[][] generatePieces(Setup setup) {
		PieceColor w = PieceColor.WHITE;
		PieceColor b = PieceColor.BLACK;

		Piece whiteCornerPiece = (setup == Setup.NORMAL
									? new Rook(w)
									: new Monk(w));
		Piece blackCornerPiece = (setup == Setup.NORMAL
									? new Rook(b)
									: new Monk(b));

		Piece[][] pieces = new Piece[][]{
			new Piece[]{whiteCornerPiece, new Knight(w), new Bishop(w), new Queen(w), new King(w), new Bishop(w), new Knight(w), whiteCornerPiece},
			new Piece[]{new Pawn(w), new Pawn(w), new Pawn(w), new Pawn(w), new Pawn(w), new Pawn(w), new Pawn(w), new Pawn(w)},
			new Piece[]{new NonePiece(), new NonePiece(), new NonePiece(), new NonePiece(), new NonePiece(), new NonePiece(), new NonePiece(), new NonePiece()},
			new Piece[]{new NonePiece(), new NonePiece(), new NonePiece(), new NonePiece(), new NonePiece(), new NonePiece(), new NonePiece(), new NonePiece()},
			new Piece[]{new NonePiece(), new NonePiece(), new NonePiece(), new NonePiece(), new NonePiece(), new NonePiece(), new NonePiece(), new NonePiece()},
			new Piece[]{new NonePiece(), new NonePiece(), new NonePiece(), new NonePiece(), new NonePiece(), new NonePiece(), new NonePiece(), new NonePiece()},
			new Piece[]{new Pawn(b), new Pawn(b), new Pawn(b), new Pawn(b), new Pawn(b), new Pawn(b), new Pawn(b), new Pawn(b)},
			new Piece[]{blackCornerPiece, new Knight(b), new Bishop(b), new Queen(b), new King(b), new Bishop(b), new Knight(b), blackCornerPiece},
		};

		return pieces;
	}

	public Board(Setup setup) {
		this(generatePieces(setup));
	}

	public Board(Piece[][] _pieces) {
		this.rows = _pieces.length;
		this.cols = _pieces[0].length;
		this.pieces = _pieces;
		capturedPieces = new ArrayList<>();
	}

	public boolean onBoard(Position pos) {
		return pos.row > 0
			&& pos.row <= rows
			&& pos.col > 0
			&& pos.col <= cols;
	}

	public int getRows() {
		return rows;
	}

	public int getCols() {
		return cols;
	}

	public Piece[][] getPieces() {
		return pieces;
	}

	public Piece getPiece(Position pos) {
		return pieces[pos.row-1][pos.col-1];
	}

	public void setPiece(Position pos, Piece p) {
		pieces[pos.row-1][pos.col-1] = p;
	}

	public ArrayList<Position> locatePiece(PieceType type, PieceColor color) {
		ArrayList<Position> locations = new ArrayList<>();

		for(int row=1; row<=rows; row++)
		for(int col=1; col<=cols; col++) {
			Position pos = new Position(row, col);
			Piece piece = getPiece(pos);

			if((piece.getType() == type) && (piece.getColor() == color)) {
				locations.add(pos);
			}
		}

		return locations;
	}

	public ArrayList<Piece> getCapturedPieces() {
		return capturedPieces;
	}

	public void setCapturedPieces(ArrayList<Piece> _capturedPieces) {
		this.capturedPieces = _capturedPieces;
	}

	public void addCapturedPiece(Piece piece) {
		capturedPieces.add(piece);
	}

	public Iterator<Position> getIterator() {		
		Board currentBoard = this;

		return new Iterator<Position>() {
			Position current = new Position(1, 1);

			Position virtualNext() {
				Position nextPos = new Position(current.row, current.col + 1);
				if(!currentBoard.onBoard(nextPos))
					nextPos = new Position(current.row + 1, 1);
				return nextPos;
			}

			public Position next() {
				Position last = current.copy();
				current = virtualNext();
				return last;
			}

			public boolean hasNext() {
				return currentBoard.onBoard(current);
			}
		};
	}

	public Board copy() {

		Piece[][] piecesCopy = new Piece[rows][cols];
		for(int row=0; row<rows; row++)
		for(int col=0; col<cols; col++) {
			piecesCopy[row][col] = pieces[row][col].copy();
		}

		Board b = new Board(piecesCopy);
		b.setCapturedPieces(new ArrayList<>(capturedPieces));
		return b;
	}

	public Move getMove(Position from, Position to)
		throws InvalidMoveException {

		Piece toMove = getPiece(from);
		
		if(!toMove.canMove(from, to, this)) throw new InvalidMoveException();
		if(getPiece(to).getType() == PieceType.KING) throw new InvalidMoveException();
		return toMove.move(from, to, this);
	}

	// Creates a new Board and performs the given move
	public Board move(Move move) {

		// Creates a board copy
		Board b = this.copy();

		// Gets the move data
		Position current = move.current();
		Position movePos = move.move();
		Piece piece = b.getPiece(current).copy();

		// Performs the onTurn update
		this.onTurn(piece.getColor());
 
		// Removes the old piece
		b.setPiece(current, new NonePiece());

		// Handles piece capture
		if(move.isCapture()) {
			Position capture = ((MoveCapture) move).capture();
			b.addCapturedPiece(b.getPiece(capture));
			b.setPiece(capture, new NonePiece());
		}

		// Handles castling
		if(move.type == MoveType.CASTLE) {
			if(movePos.col > current.col) {
				// Kingside castle
				Piece rook = b.getPiece(new Position(current.row, cols)).copy();
				b.setPiece(new Position(current.row, cols), new NonePiece());
				b.setPiece(new Position(current.row, cols-2), rook);
			} else {
				// Queenside castle
				Piece rook = b.getPiece(new Position(current.row, 1)).copy();
				b.setPiece(new Position(current.row, 1), new NonePiece());
				b.setPiece(new Position(current.row, 4), rook);
			}
		}
		
		// Adds the new piece
		b.setPiece(movePos, piece);

		// Performs the onMove update on the new piece
		piece.onMove(current, movePos, this);

		return b;
	}

	// Updates piece states at start of each turn cycle
	public void onTurn(PieceColor color) {
		for(int row=1; row<=rows; row++)
		for(int col=1; col<=cols; col++) {
			Position pos = new Position(row, col);
			Piece piece = getPiece(pos);
			if(piece.getColor() == color) {
				piece.onTurn(this);
			}
		}
	}

	public boolean inCheck(PieceColor color) {
		if(color == PieceColor.NONE) return false;

		Position kingPos = locatePiece(PieceType.KING, color).get(0);

		for(int row=1; row<=rows; row++)
		for(int col=1; col<=cols; col++) {
			Position pos = new Position(row, col);
			if(pos.equals(kingPos)) continue;

			Piece piece = getPiece(pos);

			if(piece.getType() != PieceType.NONE
			&& piece.getColor() != color) {

				ArrayList<Position> moveList = piece.getMoveListIgnoreCheck(pos, this);

				for(Position movePos: moveList) {
					if(movePos.equals(kingPos)) return true;
				}
			}
		}

		return false;
	}

	public boolean inCheckmate(PieceColor color) {
		for(int row=1; row<=rows; row++)
		for(int col=1; col<=cols; col++) {
			Position pos = new Position(row, col);
			Piece piece = getPiece(pos);
			if(piece.getColor() == color) {
				for(Position movePos: piece.getMoveList(pos, this)) {
					Board newBoard = this.move(piece.copy().move(pos, movePos, this));
					if(!newBoard.inCheck(color)) return false; 
				}
			}
		}

		return true;
	}

	public boolean canMove(PieceColor color) {
		for(int row=1; row<=rows; row++)
		for(int col=1; col<=cols; col++) {
			Position pos = new Position(row, col);
			Piece piece = getPiece(pos);

			if(piece.getColor() == color
			&& piece.getMoveList(pos, this).size() > 0)
				return true;
		}

		return false;
	}

	public String toString() {
		String toReturn = "";

		int maxSpace = 11;
		int textSpace = 9;

		toReturn += "\n";
		for (int i=0; i<maxSpace*(cols+1); i++) toReturn += "-";
		toReturn += "\n";
		
		// Iterates over the rows
		for(int rowNum=rows; rowNum>0; rowNum--) {
			
			// Adds the current row number
			toReturn += ""+rowNum;
			int spaceUsed = 1;

			for(int i=0; i<textSpace-spaceUsed; i++) toReturn += " ";
			toReturn += "| ";

			// Adds the pieces
			for(int colNum=1; colNum<=cols; colNum++) {
				spaceUsed = 0;

				Piece p = getPiece(new Position(rowNum, colNum));
				if(!p.isNone()) {
					toReturn += p+" ";
					spaceUsed += p.toString().length() + 1;
				}

				for(int i=0; i<textSpace-spaceUsed; i++) toReturn += " ";
				toReturn+="| ";
			}
			toReturn += "\n";
			for (int i=0; i<maxSpace*(cols+1); i++) toReturn += "-";
			toReturn += "\n";
		}

		// Prints the column names
		char[] columns = new char[]{'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H'};

		for(int i=0; i<textSpace; i++) toReturn += " ";
		toReturn += "| ";

		for(int colNum=0; colNum<cols; colNum++) {
			toReturn += columns[colNum];
			int spaceUsed = 1;

			for(int i=0; i<textSpace-spaceUsed; i++) toReturn += " ";
			toReturn += "| ";
		}

		toReturn += "\n";

		return toReturn;
	}
}