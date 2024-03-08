
/*
 * Class for cells
 */
public class Cell {
	public int row;
	public int col;
	public char cellType;
	public boolean visited;

	public Cell() {
	}
	
	public Cell(int row, int col, char cellType, boolean visited) {
		this.row = row;
		this.col = col;
		this.cellType = cellType;
		this.visited = visited;
	}
}
