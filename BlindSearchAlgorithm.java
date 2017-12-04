import java.awt.Color;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Implementation of Blind Search Algorithms,
 * It contains Breath-First-Search and Depth-First-Search.
 * @author aleksandar_manev
 */
public class BlindSearchAlgorithm extends Algorithm{
	private Node _currentNode;
	private SearchType _searchType;
	private List<Node> _childrenNodes;
	private long _sleepTime;
	public BlindSearchAlgorithm(String fileName,SearchType searchAlgoritm,String sleepTime)
	{
		gui = new GUI(fileName);				//GUI
		_sleepTime = Long.parseLong(sleepTime);	//delay time
		_childrenNodes = null;
		_searchType = searchAlgoritm;			//BFS or DFS
		_currentNode = new Node (new Grid(fileName,gui.cellPanes),"blindSearch");		//initial state
		_frontier.add(_currentNode);			//add initial state in open list
	}
	
	@Override
	public  void StartSearch()
	{
		boolean solutionFound = false;
		
		if(_currentNode.AreYouFinalNode())
		{
			System.out.println(super._stateCount);
		}else
		{
			while(!solutionFound)
			{
				//check if frontier is empty
				if(super._frontier.isEmpty())
				{
					System.out.println("Solution is impossible.");
					break;
				}
				//Assign the first one in the frontier to be examined
				_currentNode = super._frontier.get(0); 	
				
				//find child nodes
				_childrenNodes = _currentNode.FindChildrenNodes();
				
				if(_searchType == SearchType.DFS)
					Collections.reverse(_childrenNodes);

				for(Node child : _childrenNodes)
				{
					//repetitive state check
					if(super.IsNotRepetive(child) && super.IsNotRepetiveInUnsortedList(child,_frontier))
					{
						
						child.getState().blankTile.getCell().setBackground(Color.GREEN);
						
						//checks if this child node is a goal state
						if(child.AreYouFinalNode())
						{
							child.getState().blankTile.getCell().setBackground(Color.ORANGE);
							super.OnFinalNode(child);
							solutionFound = true;
							break;
						}
						if(_searchType == SearchType.BFS)
							super._frontier.add(child);		//add to the back of the frontier
						else if (_searchType == SearchType.DFS)
							super._frontier.add(0,child);	//add in front of the frontier
						super._stateCount++;		
					}
				}
				
				//add it to past states list
				super.AddNodeToSortedList(_previousNodes, _currentNode, CostType.H);
				if(!solutionFound)
					_currentNode.getState().blankTile.getCell().setBackground(Color.CYAN);
				//remove the explored node from the frontier
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
