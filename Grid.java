import java.io.File;
/**
 * This is the grid class, also know as the state or environment.
 * In this a grid is being initialized, cloned and manipulated.
 * @author aleksandar_manev
 */
import java.util.*;

public class Grid implements Cloneable {
	public Tile blankTile;
	private int _n;
	private int _m;
	private String _blankIndex;
	private List<Tile> _tiles;
	private List<CellPane> _cells; 	//used for the GUI

	public Grid(String file,List<CellPane> cells) {
		_n = 0;
		_m = 0;
		_cells = cells;
		_tiles = new ArrayList<Tile>();
		_blankIndex = "";
		InitFromFile(file);
	}

	/**
	 * Read the input file and initialize the grid. 
	 * @param fileName
	 */
	private void InitFromFile(String fileName) {
		List<String> input = new ArrayList<String>();
		try {
			File file = new File(fileName);
			Scanner scanner = new Scanner(file);
			while (scanner.hasNextLine()) {
				input.add(scanner.nextLine());
			}
			scanner.close();
		} catch (Exception e) {
			System.out.println("Error: Couldn't locate file '" + fileName + "'.");
		}
		String[] NxM = input.get(0).substring(1, input.get(0).length() - 1).split(",");
		_n = Integer.parseInt(NxM[0]);
		_m = Integer.parseInt(NxM[1]);
		String[] blankNxM = input.get(1).substring(1, input.get(1).length() - 1).split(",");
		String[] goalNxM = input.get(2).substring(1, input.get(2).length() - 1).split(",");
		
		//Create tiles
		int i = 0;
		for (int n = 0; n <= _n - 1; n++) {
			for (int m = 0; m <= _m - 1; m++) {
				try {
					Thread.yield();
				_tiles.add(new Tile((Integer.toString(i)), n, m, _n, _m, false,_cells.get(i)));
				}catch(Exception e) {
					System.out.println(i);
				}
				i++;
			}
		}
		blankTile = WhatTileIsOn(Integer.parseInt(blankNxM[1]), Integer.parseInt(blankNxM[0]));
		blankTile.setGoalPosition(Integer.parseInt(goalNxM[1]), Integer.parseInt(goalNxM[0]));
		_blankIndex = blankTile.getId();
		
		//Set Walls
		String[] wallInfo;
		int wallN;
		int wallM;
		int wallW;
		int wallH;
		for (int index = 3; index < input.size(); index++) {
			wallInfo = input.get(index).substring(1, input.get(index).length() - 1).split(",");
			wallM = Integer.parseInt(wallInfo[0]);
			wallN = Integer.parseInt(wallInfo[1]);
			wallW = Integer.parseInt(wallInfo[2]);
			wallH = Integer.parseInt(wallInfo[3]);
			WhatTileIsOn(wallN, wallM).setIsWall(true);
			for (int h = wallN; h <= (wallN + wallH - 1); h++) {
				for (int w = wallM; w <= (wallM + wallW - 1); w++) {
					WhatTileIsOn(h, w).setIsWall(true);
				}
			}
		}
	}
	
	/**
	 * Swaps the position of two tiles on the grid.
	 * @param newPos
	 * @param blank
	 */
	private void SwapTiles(Tile newPos, Tile blank) {
		if (newPos != null) {
			int newN = newPos.N;
			int newM = newPos.M;
			int oldN = blank.N;
			int oldM = blank.M;
			CellPane newCell = newPos.getCell();
			CellPane oldCell = blank.getCell();

			newPos.N = oldN;
			newPos.M = oldM;
			blank.N = newN;
			blank.M = newM;
			blank.setCell(newCell);
			newPos.setCell(oldCell);
		}
	}

	public List<Tile> getTiles() {
		return _tiles;
	}

	public int MisplacedTiles() {
		return blankTile.HCost();
	}
	
	/**
	 * This function creates state form particular direction of the blank tile.
	 * @param dir
	 * @return clone state from direction
	 */
	public Grid StateFromDirecion(Direction dir) {
		Grid clone = this.clone();
		Tile temp = clone.WhatTileIsOn(dir);
		if (temp.getIsWall())
			return null;
		SwapTiles(temp, clone.blankTile);
		return clone;
	}
	
	/**
	 * Looks for tiles around the blank tile.
	 * @param dir
	 * @return tile that seats next to the blank tile.
	 */
	public Tile WhatTileIsOn(Direction dir) {
		Tile defaultResult = null;
		switch (dir) {
		case Up:
			for (Tile tile : _tiles)
				if ((tile.M == (blankTile.M)) && (tile.N == blankTile.N - 1))
					return tile;
			break;
		case Down:
			for (Tile tile : _tiles)
				if ((tile.M == (blankTile.M)) && (tile.N == blankTile.N + 1))
					return tile;
			break;
		case Left:
			for (Tile tile : _tiles)
				if ((tile.M == blankTile.M - 1) && (tile.N == (blankTile.N)))
					return tile;
			break;
		case Right:
			for (Tile tile : _tiles)
				if ((tile.M == blankTile.M + 1) && (tile.N == (blankTile.N)))
					return tile;
			break;
		default:
			break;

		}
		return defaultResult;
	}

	//returns tile that seats on coordinates n m
	/**
	 * Looks for tile that sits on NxM coordinates in the grid.
	 * @param n
	 * @param m
	 * @return Tile
	 */
	public Tile WhatTileIsOn(int n, int m) {
		Tile result = null;
		for (Tile tile : _tiles) {
			if (tile.N == n)
				if (tile.M == m) {
					result = tile;
					break;
				}
		}
		return result;
	}

	//returns cloned goal node with set goal position to the start node
	/**
	 * Swaps the goal and start position (Useful only for Bidirectional search algorithms)
	 * @return clone of goal node
	 */
	public Grid getClonedGoalNode() {
		Grid clone = this.clone();
		Tile tempStart = clone.WhatTileIsOn(blankTile.getGoalN(), blankTile.getGoalM());
		clone.blankTile = tempStart;
		clone.blankTile.setGoalPosition(this.blankTile.N, this.blankTile.M);
		clone._blankIndex = tempStart.getId();
		return clone;
	}
	/**
	 * Use this function to clone this grid
	 * @return clone of this grid
	 */
	public Grid clone() {
		Grid copy = null;
		try {
			copy = (Grid) super.clone();
			copy._tiles = new ArrayList<Tile>();
			for (Tile t : this._tiles) {
				copy._tiles.add(t.clone());
			}
			for (Tile tile : copy._tiles) {
				if (tile.AreYou(_blankIndex)) {
					copy.blankTile = tile;
				}
			}
		} catch (CloneNotSupportedException e) {
			System.out.println("Unable to clone the puzzle.");
			return null;
		}

		return copy;
	}
}
