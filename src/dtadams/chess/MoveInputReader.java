package dtadams.chess;

import java.io.Console;

public class MoveInputReader {
	
	public static Position readPosition(String request, String failure) {
		Console console = System.console();

		Position pos;

		while(true) {
			System.out.print(request);
			String inputString = console.readLine();
			
			try {
				pos = parsePosition(inputString);
				break;
			} catch(IllegalArgumentException e) {
				System.out.println(failure);
				continue;
			}
		}

		return pos;
	}

	public static Position parsePosition(String posString)
		throws IllegalArgumentException {

		if(posString.length()!=2) throw new IllegalArgumentException();

		String columns = "ABCDEFGH";

		int row;
		try {
			row = Integer.parseInt(""+posString.charAt(1));
		} catch(NumberFormatException e) {
			throw new IllegalArgumentException();
		}
		int col = columns.indexOf(posString.charAt(0)+1);
		return new Position(row, col);
	}
}