import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
/**
 * Implementation of Iterative Deepening DFS Search Algorithm
 * @author aleksandar_manev
 *
 */
public class IDS extends Algorithm {
	private Node _currentNode;
	private Grid _initState;
	private long _sleepTime;
	private boolean solutionFound = false;
	private SearchType _searchType;
	private List<Node> _childrenNodes;

	public IDS(String fileName, SearchType searchAlgorithm, String sleepTime) {
		gui = new GUI(fileName);
		_searchType = searchAlgorithm;
		_sleepTime = Long.parseLong(sleepTime);
		//_sortBy = CostType.H;
		_currentNode = new Node(new Grid(fileName, gui.cellPanes), "ids");
		_currentNode.getState().blankTile.getCell().setBackground(Color.RED);
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
				//Perform DFS with a depth cutoff (bound)
				SearchIteration(bound);
				//increase bound by 1
				bound++;
				//reset search
				super._stateCount = 0;
				super._frontier = new ArrayList<Node>();
				super._previousNodes = new ArrayList<Node>();
				_currentNode = new Node(_initState.clone(), "as");
				_frontier.add(_currentNode);
			}
		}
	}
	/**
	 * Do an DFS search with a set bound cutoff
	 * @param bound
	 * @return
	 */
	public boolean SearchIteration(int bound) {
		boolean finished = false;
		while (!finished) {
			//check if you are done exploring on this bound.
			if(super._frontier.isEmpty()){
				CleanGUIGrid();
				super._stateCount = 0;
				break;
			}
			// chooses the lowest-cost node in frontier
			_currentNode = super._frontier.get(0); 	
			
			//find children states form the current state
			_childrenNodes = _currentNode.FindChildrenNodes();
			Collections.reverse(_childrenNodes);

			for(Node child : _childrenNodes)
			{
				if(super.IsNotRepetive(child) && super.IsNotRepetiveInUnsortedList(child,_frontier))
				{
					if (child.getCost(CostType.G) <= bound) {
					child.getState().blankTile.getCell().setBackground(Color.GREEN);
					
					//checks if this node is a goal state
					if(child.AreYouFinalNode())
					{
						child.getState().blankTile.getCell().setBackground(Color.ORANGE);
						super.OnFinalNode(child);
						solutionFound = true;
						return true;
					}
						super._frontier.add(0,child);
					super._stateCount++;
					}
				}
			}
			
			//add to sorted past-nodes list.
			super.AddNodeToSortedList(_previousNodes, _currentNode, CostType.H);
			if(!solutionFound)
				_currentNode.getState().blankTile.getCell().setBackground(Color.CYAN);
			// remove the current nodes form the frontier
			super._frontier.remove(_currentNode);
			
			try {
				TimeUnit.MILLISECONDS.sleep(_sleepTime);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return false;
	}

	private void CleanGUIGrid() {
		for (Node n : super._frontier) {
			n.getState().blankTile.getCell().setBackground(Color.WHITE);
		}
		for (Node n : super._previousNodes) {
			n.getState().blankTile.getCell().setBackground(Color.WHITE);
		}
	}
}
