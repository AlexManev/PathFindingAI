import java.util.ArrayList;
import java.util.List;

public class Node {
	private Grid _state;				//Have its own unique state
	private Node _parentNode;			//Remember parent node
	private String _parentDirection;	//Remember parent direction
	private String _algorithm;
	private int _costG;
	private int _costH;
	private int _costF;
	
	public Node(Grid puzzle, String algorithm)
	{
		_algorithm = algorithm.toLowerCase();
		_parentNode = null;
		_parentDirection = "";
		_costG = 0;
		InitNode(puzzle);
	}
	public Node(Grid puzzle, String algorithm,String parentDirection, Node parentNode )
	{
		_algorithm = algorithm;
		_parentNode = parentNode;
		_parentDirection = parentDirection;
		if(algorithm == "as" || algorithm == "ids")
			_costG = parentNode._costG + 1;
		else
			_costG  = 0;
		InitNode(puzzle);
	}
	/**
	 * Initializes default values for state class.
	 * @param state
	 */
	private void InitNode(Grid state)
	{
		_state = state;
		_costH = _state.MisplacedTiles();
		_costF = _costG+_costH;
	}

	/**
	 * Used to translate enumeration to a string
	 * @param direction
	 * @return String form direction 
	 */
	private String DirectionToString(Direction direction)
	{
		switch(direction){
		case Left:
			return "Left;";
		case Right:
			return "Right;";
		case Up:
			return "Up;";
		case Down:
			return "Down;";
		default:
			return "";
		}
	}

	public int getCost(CostType type)
	{
		if (type == CostType.G)
			return _costG;
		else if (type == CostType.H)
			return _costH;
		else
			return _costF;
	}
	public String getParentDirection()
	{
		return _parentDirection;
	}
	public String getAlgorithm()
	{
		return _algorithm;
	}
	public Node getParentNode()
	{
		return _parentNode;
	}
	public Grid getState()
	{
		return _state;
	}

	/**
	 * Compares the current state with another state if the
	 * tiles are positioned on the same location. This is used to
	 * avoid repetitive states.
	 * @param otherNode
	 * @return true if otherNode is same as this node
	 */
	public boolean AreYouTheSame(Node otherNode)
	{
		if(this._algorithm!="blindSearch") {
			//if h cost is is not the same, the blank tile is on different position
			if(otherNode.getCost(CostType.H)!=this.getCost(CostType.H))
				return false;
		}
		//check blank is on the same position.
		if(otherNode._state.blankTile.M != _state.blankTile.M)
			return false;
		else if(otherNode._state.blankTile.N != _state.blankTile.N)
			return false;
		
		if(this._algorithm!="blindSearch")
			if(otherNode.getCost(CostType.F) < this.getCost(CostType.F))
				return false;
		
		return true;

	}
	
	//
	/**
	 * Finds the children states for the current state.
	 * It looks through all possible directions where it can move.
	 * @return list of children noes.
	 */
	public List<Node> FindChildrenNodes()
	{
		List<Direction> directions = _state.blankTile.WhereCanYouMove();
		List<Node> childrenNodes = new ArrayList<Node>();
		Grid cloneState;
		for(Direction direction : directions)
		{
			cloneState = _state.StateFromDirecion(direction);
			if(cloneState != null)
				childrenNodes.add(new Node(cloneState,_algorithm,DirectionToString(direction),this));
		}
		return childrenNodes;
	}
	/**
	 * @return true if the cost H is 0 (which indicates the blank tile is on its goal position.)
	 */
	public boolean AreYouFinalNode()
	{
		return _costH == 0;
	}
}
