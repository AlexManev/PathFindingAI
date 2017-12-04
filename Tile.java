import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
/**
 * This document defines a Tile and every function that a tile can do.
 * @author aleksandar_manev
 *
 */
public class Tile implements Cloneable {
	public int N;
	public int M;
	private String _id;
	private boolean _isWall;
	private int _puzzleN;
	private int _puzzleM;
	private int _goalN;
	private int _goalM;
	private CellPane _cell;

	public Tile(String id, int positionN, int positionM, int puzzleN, int puzzleM, boolean isWall,CellPane cell) {
		_id = id;
		N = positionN;
		M = positionM;
		_puzzleN = puzzleN;
		_puzzleM = puzzleM;
		_isWall = isWall;
		_cell = cell;
			
	}

	public void setIsWall(boolean value) {
		_isWall = value;
		_cell.setBackground(Color.GRAY);
	}

	public boolean getIsWall() {
		return _isWall;
	}
	public int getGoalN() {
		return _goalN;
	}
	public int getGoalM() {
		return _goalM;
	}
	public CellPane getCell() {
		return _cell;
	}
	
	/**
	 * Calculate Heuristic Cost.
	 * @param tileN
	 * @param tileM
	 * @return Heuristic
	 */
	private int HowFarFromPosition(int tileN, int tileM) {
		int result;
		int nSteps = (int) Math.sqrt((tileN - N) * (tileN - N));
		int mSteps = (int) Math.sqrt((tileM - M) * (tileM - M));
		result = nSteps + mSteps;
		return result;
	}


	public void setGoalPosition(int goalN, int goalM) {
		_goalN = goalN;
		_goalM = goalM;
	}

	public boolean AreYou(String id) {
		return id.equals(_id);
	}

	public String getId() {
		return _id;
	}
	
	public void setCell(CellPane value) {
		_cell = value;
	}

	/**
	 * Used for Bidirectional Informed Searches
	 * @param blank
	 * @return distance from the start point.
	 */
	public int CostToBlank(Tile blank) {
		return HowFarFromPosition(blank.N, blank.M);
	}

	/**
	 * Calculate Heuristic Cost, 
	 * Used for Informed Search
	 * @return distance from the end point.
	 */
	public int HCost() {
		return HowFarFromPosition(_goalN, _goalM);
	}

	/**
	 * @return List of Directions where this tile can move.
	 */
	public List<Direction> WhereCanYouMove() {
		List<Direction> result = new ArrayList<Direction>();
		if (N > 0)
			result.add(Direction.Up);
		if (M > 0)
			result.add(Direction.Left);
		if (N < _puzzleN-1)
			result.add(Direction.Down);
		if (M < _puzzleM-1)
			result.add(Direction.Right);
		
		
		
		return result;
	}

	public Tile clone() {
		try {
			return (Tile) super.clone();
		} catch (CloneNotSupportedException e) {
			System.out.println("Failed to create tile clone!");
			return null;
		}
	}

}