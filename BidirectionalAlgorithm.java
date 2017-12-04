import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Implementation of Bidirectional Blind Search Algorithms
 * Includes Bidirectional Breath-first-search and Bidirectional Depth-first-search
 * @author aleksandar_manev
 *
 */
public class BidirectionalAlgorithm extends Algorithm {

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
	
	public BidirectionalAlgorithm(String fileName, SearchType searchType, String sleepTime) {
		super.gui = new GUI(fileName);
		_searchType = searchType;
		_sleepTime = Long.parseLong(sleepTime);
		_StartNodeFrontier = new ArrayList<Node>();
		_GoalNodeFrontier = new ArrayList<Node>();
		_StartNodePastNodes = new ArrayList<Node>();
		_GoalNodePastNodes = new ArrayList<Node>();
		_forwardSearchNode = new Node(new Grid(fileName, gui.cellPanes), "bd");
		_goalNode = new Node(_forwardSearchNode.getState().getClonedGoalNode(), "bd");
		_StartNodeFrontier.add(_forwardSearchNode);
		_GoalNodeFrontier.add(_goalNode);
	}

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
				
				//reverse the order so when we add them to the frontier they 
				//will be just like when we discovered them
				if (_searchType == SearchType.BDDFS) {
					Collections.reverse(_forwardChildren);
					Collections.reverse(_backwardChildren);
				}
				// find all child nodes form forward-searching node
				for (Node forwardChild : _forwardSearchNode.FindChildrenNodes()) {
					// child is not repetitive in past nodes and is not in the frontier
					if (super.IsNotRepetive(forwardChild, _StartNodePastNodes)
							&& super.IsNotRepetiveInUnsortedList(forwardChild, _StartNodeFrontier)) {
						forwardChild.getState().blankTile.getCell().setBackground(Color.GREEN);
						// add child to the frontier
						if (_searchType == SearchType.BDBFS)
							_StartNodeFrontier.add(forwardChild);		//if search type is BDBFS add child in queue order
						else if (_searchType == SearchType.BDDFS)
							_StartNodeFrontier.add(0, forwardChild);	//if search type is BDDFS add child in stack order
						super._stateCount++;
					}
					// check if child matches with any node form the opposite frontier.
					if (super.IsItRepetive(forwardChild, _GoalNodeFrontier, "s"))
						break;
				}
				if (!solutionFound) {
					// find all child nodes form backwards-searching node
					for (Node childFromBack : _goalNode.FindChildrenNodes()) {
						// child is not repetitive in past nodes and is not in the frontier
						if (super.IsNotRepetive(childFromBack, _GoalNodePastNodes)
								&& super.IsNotRepetiveInUnsortedList(childFromBack, _GoalNodeFrontier)) {
							childFromBack.getState().blankTile.getCell().setBackground(Color.GREEN);
							// add child to the frontier
							if (_searchType == SearchType.BDBFS)
								_GoalNodeFrontier.add(childFromBack);	//if search type is BDBFS add child in queue order
							else if (_searchType == SearchType.BDDFS)
								_GoalNodeFrontier.add(0, childFromBack);//if search type is BDDFS add child in stack order
							super._stateCount++;
						}
						// check if child matches with any node form the
						// opposite frontier.
						if (super.IsItRepetive(childFromBack, _StartNodeFrontier, "e"))
							break;
					}
				}
				// add to sorted past-nodes list.
				// (this list is sorted by Heuristic Cost because its easier to
				// search through sorter list)
				super.AddNodeToSortedList(_StartNodePastNodes, _forwardSearchNode, CostType.H);
				super.AddNodeToSortedList(_GoalNodePastNodes, _goalNode, CostType.H);
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