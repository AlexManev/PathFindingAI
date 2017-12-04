import java.awt.Color;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
/**
 * Implementation of Iterative Deepening A* Search Algorithm
 * @author aleksandar_manev
 */
public class IDA extends Algorithm {
	private Node _currentNode;
	private Grid _initState;
	private long _sleepTime;
	private CostType _sortBy;
	private boolean solutionFound = false;

	public IDA(String fileName, SearchType searchAlgorithm, String sleepTime) {
		gui = new GUI(fileName);
		_sleepTime = Long.parseLong(sleepTime);
		_sortBy = CostType.F;
		_currentNode = new Node(new Grid(fileName, gui.cellPanes), "as");
		_currentNode.getState().blankTile.getCell().setBackground(Color.ORANGE);
		_initState = _currentNode.getState().clone();
		_frontier.add(_currentNode);
	}

	@Override
	public void StartSearch() {
		if (_currentNode.AreYouFinalNode()) {
			System.out.println(super._stateCount);
		} else {
			//set the bound really low.
			int bound = 1;
			while (!solutionFound) {
				if (super._frontier.isEmpty()) {
					System.out.println("Solution is impossible.");
					break;
				}
				//get new bound
				bound = SearchIteration(bound);
				//reset search
				super._frontier = new ArrayList<Node>();
				super._previousNodes = new ArrayList<Node>();
				_currentNode = new Node(_initState.clone(), "as");
				_frontier.add(_currentNode);
			}
		}
	}
	/**
	 * Do an A* Star search with a set bound cutoff on f-cost
	 * @param bound
	 * @return new bound value
	 */
	public int SearchIteration(int bound) {
		boolean finished = false;
		int newBound = Integer.MAX_VALUE;
		while (!finished) {
			//check if you are done exploring on this bound.
			if (super._frontier.isEmpty()) {
				CleanGUIGrid();
				super._stateCount = 0;
				break;
			}
			// chooses the lowest-cost node in frontier
			_currentNode = super._frontier.get(0);

			// checks if this node is a goal state
			if (_currentNode.AreYouFinalNode()) {
				_currentNode.getState().blankTile.getCell().setBackground(Color.ORANGE);
				super.OnFinalNode(_currentNode);
				solutionFound = true;
				return 0;
			}
			for (Node child : _currentNode.FindChildrenNodes()) {
				//repetitive state check
				if (super.IsNotRepetive(child) && super.IsNotRepetiveInUnsortedList(child, _frontier)) {
					if (child.getCost(CostType.F) <= bound) {
						child.getState().blankTile.getCell().setLabelText(Integer.toString(child.getCost(CostType.F)));
						child.getState().blankTile.getCell().setBackground(Color.GREEN);
						//add child node to frontier
						super.AddNodeToSortedList(super._frontier, child, _sortBy);
						super._stateCount++;
					}else if(child.getCost(CostType.F) > bound && child.getCost(CostType.F) < newBound) {
							newBound = child.getCost(CostType.F);	//prepare new bound
					}
				}
			}
			
			//add to sorted past-nodes list.
			super.AddNodeToSortedList(_previousNodes, _currentNode, CostType.H);
			_currentNode.getState().blankTile.getCell().setBackground(Color.CYAN);
			// remove the current nodes form the frontier
			super._frontier.remove(_currentNode);
			try {
				TimeUnit.MILLISECONDS.sleep(_sleepTime);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return newBound;
	}
	/**
	 * Reset GUI back to all bank tiles.
	 */
	private void CleanGUIGrid() {
		for (Node n : super._frontier) {
			n.getState().blankTile.getCell().setBackground(Color.WHITE);
			n.getState().blankTile.getCell().setLabelText("");
		}
		for (Node n : super._previousNodes) {
			n.getState().blankTile.getCell().setBackground(Color.WHITE);
			n.getState().blankTile.getCell().setLabelText("");
		}
	}
}
