import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
/**
 * Implementation of Bidirectional Informed Search Algorithms
 * Includes Bidirectional Greedy-best-first-search and Bidirectional A* search
 * @author aleksandar_manev
 *
 */
public class BidirecionalInformedSearch extends Algorithm {
	private List<Node> _StartNodeFrontier;
	private List<Node> _GoalNodeFrontier;
	private List<Node> _StartNodePastNodes;
	private List<Node> _GoalNodePastNodes;
	private List<Node> _forwardChildren;
	private List<Node> _backwardChildren;
	private Node _forwardSearchNode;
	private Node _goalNode;
	private long _sleepTime;
	private SearchType _searchType;
	private CostType _sortBy;

	public BidirecionalInformedSearch(String fileName, SearchType searchType, String sleepTime) {
		super.gui = new GUI(fileName);
		_searchType = searchType;
		_sleepTime = Long.parseLong(sleepTime);
		if(_searchType == SearchType.BDAS){
			_sortBy = CostType.F;
			_forwardSearchNode = new Node(new Grid(fileName, gui.cellPanes), "as");
			_goalNode = new Node(_forwardSearchNode.getState().getClonedGoalNode(), "as");
		}
		else{
			_sortBy = CostType.H;
			_forwardSearchNode = new Node(new Grid(fileName, gui.cellPanes), "gbfs");
			_goalNode = new Node(_forwardSearchNode.getState().getClonedGoalNode(), "gbfs");
		}
		
		_StartNodeFrontier = new ArrayList<Node>();
		_GoalNodeFrontier = new ArrayList<Node>();
		_StartNodePastNodes = new ArrayList<Node>();
		_GoalNodePastNodes = new ArrayList<Node>();
		_StartNodeFrontier.add(_forwardSearchNode);
		_GoalNodeFrontier.add(_goalNode);
	}

	@Override
	public void StartSearch() {
		// Check if we start with goal state.
		if (_forwardSearchNode.AreYouFinalNode()) {
			System.out.println(super._stateCount + "0 moves:");
		} else {
			while (!super.solutionFound) {
				// if any of the frontiers is empty solution is impossible.
				if (_StartNodeFrontier.isEmpty() || _GoalNodeFrontier.isEmpty()) {
					System.out.println("Solution is impossible.");
					break;
				}
				// get the least cost node from the frontier.
				_forwardSearchNode = _StartNodeFrontier.get(0);
				_goalNode = _GoalNodeFrontier.get(0);
				
				_forwardChildren = _forwardSearchNode.FindChildrenNodes();
				_backwardChildren = _goalNode.FindChildrenNodes();
				
				// find all child nodes form forward-searching node
				for(Node forwardChild : _forwardChildren) {
					// child is not repetitive in past nodes and is not in the frontier
					if(super.IsNotRepetive(forwardChild,_StartNodePastNodes) 
							&& super.IsNotRepetive(forwardChild, _StartNodeFrontier))
					{
						forwardChild.getState().blankTile.getCell().setBackground(Color.GREEN);
						// add child to the frontier
						super.AddNodeToSortedList(_StartNodeFrontier, forwardChild,_sortBy);
						super._stateCount++;
					}
					// check if child matches with any node form the opposite frontier.
					if (super.IsItRepetive(forwardChild, _GoalNodeFrontier, "s"))
						break;
				}
				if(!solutionFound)
				{
					// find all child nodes form backwards-searching node
					for (Node childFromBack : _backwardChildren) {
						// child is not repetitive in past nodes and is not in the frontier
						if(super.IsNotRepetive(childFromBack,_GoalNodePastNodes) 
								&& super.IsNotRepetive(childFromBack, _GoalNodeFrontier))
						{
							childFromBack.getState().blankTile.getCell().setBackground(Color.GREEN);
							// add child to the frontier
							super.AddNodeToSortedList(_GoalNodeFrontier, childFromBack,_sortBy);
							super._stateCount++;
						}
						// check if child matches with any node form the opposite frontier.
						if (super.IsItRepetive(childFromBack, _StartNodeFrontier, "e"))
							break;
					}
				}
				
				// add to sorted past-nodes list.
				super.AddNodeToSortedList(_StartNodePastNodes, _forwardSearchNode, _sortBy);
				super.AddNodeToSortedList(_GoalNodePastNodes, _goalNode, _sortBy);
				// remove the current nodes form the frontiers
				_StartNodeFrontier.remove(_forwardSearchNode);
				_GoalNodeFrontier.remove(_goalNode);
				
				if (!solutionFound) {
					_forwardSearchNode.getState().blankTile.getCell().setBackground(Color.CYAN);
					_goalNode.getState().blankTile.getCell().setBackground(Color.CYAN);
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

}
