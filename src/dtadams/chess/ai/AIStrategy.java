package dtadams.chess.ai;

import dtadams.chess.*;
import dtadams.chess.piece.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.stream.Collectors;

public class AIStrategy {

	public enum Field {
		MATERIAL,
		ROOKS_IN_LINE,
		ATTACK_TOTAL,
		CENTER_ATTACK_TOTAL,
		CAN_CASTLE,
		CHECK_BONUS;
	}

	static HashMap<PieceType, Double> scores = new HashMap<>();

	HashMap<Field, Double> weights;

	// TODO Add "determinism" field

	public AIStrategy(AIDataParser parser) {
		this.weights = new HashMap<>();
		for(Field f: Field.values()) {
			weights.put(f, parser.get(f.name()));
		}

		setupScores();
	}

	public AIStrategy(HashMap<Field, Double> _weights) {
		this.weights = _weights;

		setupScores();
	}

	static void setupScores() {
		if(scores.size() == 0) {
			scores.put(PieceType.PAWN, 		1d);
			scores.put(PieceType.BISHOP,	3d);
			scores.put(PieceType.KNIGHT,	3d);
			scores.put(PieceType.ROOK,		5d);
			scores.put(PieceType.QUEEN,		9d);
		}
	}

	public double score(Board b, PieceColor color) {
		if(b.inCheckmate(color)) return Double.NEGATIVE_INFINITY;
		if(b.inCheckmate(PieceColor.opposite(color))) return Double.POSITIVE_INFINITY;

		double score = 0;
		score += materialScore(b, color);
		score += rooksScore(b, color);
		score += attackTotal(b, color);
		score += centerAttackTotal(b, color);
		score += canCastle(b, color);
		score += checkBonus(b, color);

		return score;
	}

	double materialScore(Board b, PieceColor color) {
		double materialScore = 0;

		ArrayList<Piece> capturedPieces = b.getCapturedPieces();
		for(Piece piece: capturedPieces) {
			materialScore += scores.get(piece.getType()) *
						(piece.getColor() == color ? -1 : 1);
		}

		return weights.get(Field.MATERIAL) * materialScore;
	}

	double rooksScore(Board b, PieceColor color) {
		double rooksScore = 0;

		ArrayList<Position> rookPositions = b.locatePiece(PieceType.ROOK, color);
		if(rookPositions.size() != 2) return 0;

		Position pos1 = rookPositions.get(0);
		Position pos2 = rookPositions.get(1);

		int rowDelta = 0;
		int colDelta = 0;
		if(pos1.row == pos2.row) colDelta = (int) Math.signum(pos2.col - pos1.col);
		else if(pos1.col == pos2.col) rowDelta = (int) Math.signum(pos2.row - pos1.row);
		else return 0;

		Position p = pos1.copy();
		while(true) {
			p = new Position(p.row + rowDelta, p.col + colDelta);
			
			if(p.equals(pos2)) break;
			if(!b.getPiece(p).isNone()) return 0;
		}

		return weights.get(Field.ROOKS_IN_LINE);
	}

	double attackTotal(Board b, PieceColor color) {
		double attackScore = 0;

		Iterator<Position> positions = b.getIterator();
		while(positions.hasNext()) {
			Position pos = positions.next();

			if(b.getPiece(pos).getColor() == color) {
				attackScore += b.getPiece(pos).getMoveList(pos, b).size();
			}
		}

		return weights.get(Field.ATTACK_TOTAL) * attackScore;
	}

	double centerAttackTotal(Board b, PieceColor color) {
		double centerAttackScore = 0;

		Iterator<Position> positions = b.getIterator();
		while(positions.hasNext()) {
			Position pos = positions.next();

			if(b.getPiece(pos).getColor() == color) {
				centerAttackScore += b.getPiece(pos)
										.getMoveList(pos, b)
										.stream()
										.filter(p -> inCenter(p))
										.collect(Collectors.toList())
										.size();
			}
		}

		return weights.get(Field.CENTER_ATTACK_TOTAL) * centerAttackScore;
	}

	double canCastle(Board b, PieceColor color) {
		Position kingPos = b.locatePiece(PieceType.KING, color).get(0);
		King king = (King) b.getPiece(kingPos);

		if(king.hasCastled()) return 0;
		return (king.hasMoved()
				? 0
				: weights.get(Field.CAN_CASTLE));
	}

	double checkBonus(Board b, PieceColor color) {
		return weights.get(Field.CHECK_BONUS) * (b.inCheck(PieceColor.opposite(color)) ? 1 : 0);
	}

	boolean inCenter(Position p) {
		return (p.row == 4 || p.row == 5)
			&& (p.col == 4 || p.col == 5);
	}

}