import java.awt.Color;
import java.util.concurrent.TimeUnit;
/**
 * Implementation of Best-First-Search Algorithms
 * Includes Bidirectional Greedy-Best-First-Search and A* Search
 * @author aleksandar_manev
 *
 */
public class BestFirstAlgorithm extends Algorithm{
	private Node _currentNode;
	private long _sleepTime;
	private CostType _sortBy;
	public BestFirstAlgorithm(String fileName, SearchType searchAlgorithm, String sleepTime)
	{
		gui = new GUI(fileName);
		_sleepTime = Long.parseLong(sleepTime);
		if(searchAlgorithm == SearchType.AS){
			_sortBy = CostType.F;
			_currentNode = new Node (new Grid(fileName,gui.cellPanes),"as");
		}
		else{
			_sortBy = CostType.H;
			_currentNode = new Node (new Grid(fileName,gui.cellPanes),"gbfs");
		}
		_currentNode.getState().blankTile.getCell().setBackground(Color.ORANGE);
		_frontier.add(_currentNode);
	}
	
	@Override
	public  void StartSearch()
	{
		boolean solutionFound = false;
		// Check if we start with goal state.
		if(_currentNode.AreYouFinalNode())
		{
			System.out.println(super._stateCount);
		}else
		{
			while(!solutionFound)
			{
				// if the frontier is empty solution is impossible.
				if(super._frontier.isEmpty())
				{
					System.out.println("Solution is impossible.");
					break;
				}
				//get the least cost node from the frontier
				_currentNode = super._frontier.get(0); 
				
				//checks if the current node is a goal state
				if(_currentNode.AreYouFinalNode())
				{
					_currentNode.getState().blankTile.getCell().setBackground(Color.ORANGE);
					super.OnFinalNode(_currentNode);
					break;
				}
				// find all possible child nodes form the current node
				for(Node child : _currentNode.FindChildrenNodes())
				{
					// child is not repetitive in past nodes and is not in the frontier
					if(super.IsNotRepetive(child) && super.IsNotRepetiveInUnsortedList(child, _frontier))
					{
						child.getState().blankTile.getCell().setLabelText(Integer.toString(child.getCost(CostType.F)));
						child.getState().blankTile.getCell().setBackground(Color.GREEN);
						// add child to the frontier
						super.AddNodeToSortedList(super._frontier, child,_sortBy);
						super._stateCount++;
					}
				}
				// add to sorted past-nodes list.
				super.AddNodeToSortedList(_previousNodes, _currentNode, CostType.H);
				if(!_currentNode.getState().blankTile.getCell().getBackground().equals(Color.RED));
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
		}
	}
}
