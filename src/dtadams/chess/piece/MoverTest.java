package dtadams.chess;

import dtadams.chess.*;
import dtadams.chess.piece.*;
import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;

public class MoverTest {

	private int value1;
	private int value2;

	public MoverTest() {}

	@Before
	public void setUp() throws Exception {
		value1 = 3;
		value2 = 5;
	}
	
	@After
	public void tearDown() throws Exception {
		value1 = 0;
		value2 = 0;
	}
	
	@Test
	public void testStraight() {

		Piece[][] pieces = new Piece[][]{
			new Piece[]{new NonePiece(), new NonePiece(), new NonePiece(), new NonePiece()},
			new Piece[]{new NonePiece(), new NonePiece(), new NonePiece(), new NonePiece()},
			new Piece[]{new NonePiece(), new NonePiece(), new NonePiece(), new NonePiece()},
			new Piece[]{new NonePiece(), new NonePiece(), new NonePiece(), new NonePiece()},
		};

		Board emptyBoard = new Board(pieces);
		ArrayList<Position> expected = new ArrayList<>();

		// Test 1

		expected.add(new Position(2,1));
		expected.add(new Position(3,1));
		expected.add(new Position(4,1));

		Position startPos = new Position(1,1);
		int[] rowDeltas = new int[]{1};
		int[] colDeltas = new int[]{0};
		PieceColor color = PieceColor.WHITE;
		ArrayList<Position> actual = 
			Mover.getMoves(startPos,
							emptyBoard,
							rowDeltas,
							colDeltas,
							color,
							Mover.Type.STRAIGHT,
							Mover.Capture.ALLOWED);

		System.out.println(listsEqual(expected, actual));

		// Test 2

		expected.add(new Position(1,2));
		expected.add(new Position(1,3));
		expected.add(new Position(1,4));

		startPos = new Position(1,1);
		rowDeltas = new int[]{1,-1};
		colDeltas = new int[]{0,0};
		color = PieceColor.WHITE;
		actual = 
			Mover.getMoves(startPos,
							emptyBoard,
							rowDeltas,
							colDeltas,
							color,
							Mover.Type.STRAIGHT,
							Mover.Capture.ALLOWED);

		assertTrue(listsEqual(expected, actual));

		// Test 3

		startPos = new Position(1,1);
		rowDeltas = new int[]{1,-1};
		colDeltas = new int[]{0,0};
		color = PieceColor.WHITE;
		actual = 
			Mover.getMoves(startPos,
							emptyBoard,
							rowDeltas,
							colDeltas,
							color,
							Mover.Type.STRAIGHT,
							Mover.Capture.OFF);

		assertTrue(listsEqual(expected, actual));
	}

	<E> boolean listsEqual(ArrayList<E> list, ArrayList<E> list2) {

		if(list.size() != list2.size()) return false;

		ArrayList<E> list1 = new ArrayList<>();
		list1.addAll(list);

		for(int i=0; i<list.size(); i++) {
			E e = list.get(i);
			if(list1.contains(e) && list2.contains(e)) {
				list1.remove(e);
				list2.remove(e);
			} else {
				return false;
			}
		}

		return true;
	}
}
