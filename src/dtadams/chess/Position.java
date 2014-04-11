package dtadams.chess;

public class Position {
	
	public int row, col;
	static char[] colNames = {'A','B','C','D','E','F','G','H'};

	public Position(int _row, int _col) {
		this.row = _row;
		this.col = _col;
	}

	public String toString() {
		if(col < 1 || col > colNames.length) return "("+col+")"+row;
		return ""+colNames[col-1]+row;
	}

	public boolean equals(Position other) {
		return this.row == other.row && this.col == other.col;
	}

	public Position copy() {
		return new Position(this.row, this.col);
	}
}