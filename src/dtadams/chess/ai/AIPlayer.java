package dtadams.chess.ai;

import dtadams.chess.Board;
import dtadams.chess.InvalidMoveException;
import dtadams.chess.Move;
import dtadams.chess.Piece;
import dtadams.chess.PieceColor;
import dtadams.chess.Player;
import dtadams.chess.Position;
import dtadams.chess.piece.PieceType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.lang.Comparable;

public class AIPlayer extends Player {
	
	static HashMap<PieceType, Integer> scores = new HashMap<>();

	int recurDepth;
	AIStrategy strategy;
	int movesAnalyzed;

	public AIPlayer(PieceColor _color, int _recurDepth, AIStrategy _strategy) {
		super(_color);
		this.recurDepth = _recurDepth;
		this.strategy = _strategy;
		this.movesAnalyzed = 0;
	}

	public Move move(Board b) {
		this.movesAnalyzed = 0;
		double initAlpha = Integer.MIN_VALUE;
		double initBeta = Integer.MAX_VALUE;
		Move move = findMove(this.color, this.recurDepth, b, initAlpha, initBeta).move;

		System.out.println("Moves analyzed: "+this.movesAnalyzed);
		System.out.println("Move:"+move);

		return move;
	}

	class ScoreData {
		Move move;
		double score;
		public ScoreData(Move _move, double _score) {
			this.move = _move;
			this.score = _score;
		}
		public String toString(){return ""+move;}
	}

	ScoreData findMove(PieceColor moveColor,
						int recurDepth,
						Board b,
						double alpha,
						double beta) {

//		System.out.println("findMove("+recurDepth+", "+alpha+", "+beta+")");

		ArrayList<ScoreData> moveSet = new ArrayList<>();
		
		Iterator<Position> positions = b.getIterator();
		while(positions.hasNext()) {
			// Iterates over board positions

			Position currPos = positions.next();
			Piece piece = b.getPiece(currPos);			
			if(piece.getColor() == moveColor) {
				// Iterates over piece positions

				ArrayList<Position> pieceMoveList = piece.getMoveList(currPos, b);
				for(Position movePos: pieceMoveList) {
					// Iterates over moves

					// Makes the move on a virtual board
					Board b1 = null;
					Move move = null;
					try {
						move = b.getMove(currPos, movePos);
						b1 = b.move(move);
					} catch(InvalidMoveException e) {}

					// Scores the move
					double score = (
						(recurDepth == 0
							|| b1.inCheckmate(PieceColor.opposite(moveColor)))
						? score(b1)
						: findMove(PieceColor.opposite(moveColor),
											recurDepth-1,
											b1,
											alpha,
											beta)
									.score
					);

					// Adds the move to the list
					this.movesAnalyzed++;
					moveSet.add(new ScoreData(move, score));

					// Updates alpha and beta scores
					if(moveColor == this.color)
						alpha = Math.max(alpha, score);
					else
						beta = Math.min(beta, score);
					if(beta <= alpha) break; // Alpha-beta break condition
				}
			}
		}

		return getMinimax(moveSet, moveColor, recurDepth == this.recurDepth);
	}

	double score(Board b) {
		return strategy.score(b, this.color);
	} 

	ScoreData getMinimax(ArrayList<ScoreData> moveSet, PieceColor moveColor, boolean print) {
		ArrayList<ScoreData> best = new ArrayList<>();
		
		int bestFactor = (moveColor == this.color ? 1 : -1);
		double bestScore = (double)Integer.MIN_VALUE * bestFactor;
		
		for(ScoreData data: moveSet) {
			if(data.score * bestFactor > bestScore * bestFactor)  {
				best = new ArrayList<>();
				best.add(data);
				bestScore = data.score;
			}
			else if(data.score == bestScore) {
				best.add(data);
			}
		}

		if(print) {
			System.out.print(""+bestScore+" point moves: ");
			for(ScoreData bestElement: best) {
				System.out.print(bestElement.move+", ");
			}
			System.out.println();
		}

		// Deterministic
		return best.get(0); //(int) (Math.random() * best.size()));
	}
}