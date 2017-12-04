import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
/**
 * This documents defines functions shared between algorithms.
 * @author aleksandar_manev
 */
public abstract class Algorithm {
	public List<Node> _frontier;
	public int _stateCount;
	public List<Node> _previousNodes;
	public GUI gui;
	public boolean solutionFound;

	public Algorithm() {
		_frontier = new ArrayList<Node>(); // priority queue ordered by path
		_stateCount = 0; // path cost = 0
		_previousNodes = new ArrayList<Node>(); // explored states - empty set;
		solutionFound = false;
	}
	
	/**
	 * Abstract class that need to be defined in each search algorithm.
	 */
	public abstract void StartSearch();
	
	/**
	 * Outputs result strings in the console.
	 * @param solutionNode
	 */
	public void OnFinalNode(Node solutionNode) {
		System.out.print(_stateCount + " ");
		solutionFound = true;
		List<String> paths = new ArrayList<String>();
		do {
			paths.add(solutionNode.getParentDirection());
			solutionNode = solutionNode.getParentNode();
			solutionNode.getState().blankTile.getCell().setBackground(Color.YELLOW);
		} while (solutionNode.getParentDirection() != "");
		solutionNode.getState().blankTile.getCell().setBackground(Color.ORANGE);
		System.out.println(paths.size() + " moves:");
		for (int i = paths.size() - 1; i >= 0; i--) {
			System.out.print(paths.get(i));

		}
	}

	/**
	 * (Used for Bidirectional Searches)
	 * Outputs result strings in the console.
	 * @param state1
	 * @param state2
	 * @param type
	 */
	public void OnFinalNode(Node state1, Node state2, String type) {
		System.out.print(_stateCount + " ");
		solutionFound = true;
		List<String> paths = new ArrayList<String>();
		Node firstPart;
		Node secondPart;
		if (type == "s") {
			firstPart = state2;
			secondPart = state1;
		} else {
			firstPart = state1;
			secondPart = state2;
		}
		state1.getState().blankTile.getCell().setBackground(Color.YELLOW);
		state2.getState().blankTile.getCell().setBackground(Color.YELLOW);
		do {
			paths.add(ReverseDir(firstPart.getParentDirection()));
			firstPart = firstPart.getParentNode();
			firstPart.getState().blankTile.getCell().setBackground(Color.YELLOW);
		} while (firstPart.getParentDirection() != "");
		firstPart.getState().blankTile.getCell().setBackground(Color.ORANGE);
		Collections.reverse(paths);
		do {
			paths.add(secondPart.getParentDirection());
			secondPart = secondPart.getParentNode();
			secondPart.getState().blankTile.getCell().setBackground(Color.YELLOW);
		} while (secondPart.getParentDirection() != "");
		secondPart.getState().blankTile.getCell().setBackground(Color.ORANGE);

		System.out.println(paths.size() + " moves:");
		for (int i = paths.size() - 1; i >= 0; i--) {
			System.out.print(paths.get(i));
		}
	}
	
	/**
	 * Used to Reverse direction
	 * @param dir
	 * @return
	 */
	private String ReverseDir(String dir) {
		switch (dir) {
		case "Left;":
			return "Right;";
		case "Right;":
			return "Left;";
		case "Up;":
			return "Down;";
		case "Down;":
			return "Up;";
		default:
			return null;
		}
	}

	/**
	 * Adds state inside a sorted list using binary search
	 * @param thisList
	 * @param newNode
	 * @param sortBy
	 */
	public void AddNodeToSortedList(List<Node> thisList, Node newNode, CostType sortBy) {
		int index = binarySearch(thisList, newNode, sortBy);
		if (sortBy == CostType.F) {
			List<Node> nodes = new ArrayList<Node>();

			for (int i = index; i < thisList.size(); i++) {
				if (thisList.get(i).getCost(sortBy) == newNode.getCost(sortBy)) {
					nodes.add(thisList.get(i));
				}
			}
			int j = binarySearch(nodes, newNode, CostType.H);
			index += j;
		}
		thisList.add(index, newNode);
	}
 
	/**
	 * Using Binary Search it returns int position where the node should stay.
	 * @param list
	 * @param newNode
	 * @param sortBy
	 * @return index position
	 */
	private int binarySearch(List<Node> list, Node newNode, CostType sortBy) {
		int low = 0;
		int high = list.size() - 1;
		int value = newNode.getCost(sortBy);

		while (low <= high) {
			int mid = (low + high) >>> 1;
			Node comp = list.get(mid);
			int midVal = comp.getCost(sortBy);

			if (midVal < value)
				low = mid + 1;
			else if (midVal > value)
				high = mid - 1;
			else if (low != mid)
				high = mid;
			else
				return mid;
		}
		return low;
	}


	/**
	 * In sorted previous states list using binary search checks if node is not repetitive
	 * @param state
	 * @return  true if node it is not repetitive in _previousNodes
	 */
	public boolean IsNotRepetive(Node state) {
		int startFrom = binarySearch(_previousNodes, state, CostType.H);
		for (int i = startFrom; i < _previousNodes.size(); i++) {
			if (_previousNodes.get(i).AreYouTheSame(state))
				return false;
		}
		return true;
	}

	/**
	 * In unsorted list using linear search checks if node is not repetitive
	 * @param state
	 * @param inThisList
	 * @return true if node it is not repetitive inThisList
	 */
	public boolean IsNotRepetiveInUnsortedList(Node state, List<Node> inThisList) {
		for (Node node : inThisList) {
			if (node.AreYouTheSame(state))
				return false;
		}
		return true;
	}

	/**
	 *  In sorted list using binary search checks if node is not repetitive
	 * @param node
	 * @param inThisList
	 * @return true if node it is not repetitive inThisList
	 */
	public boolean IsNotRepetive(Node node, List<Node> inThisList) {
		int startFrom = 0;

		startFrom = binarySearch(inThisList, node, CostType.H);

		for (int i = startFrom; i < inThisList.size(); i++) {
			if (inThisList.get(i).AreYouTheSame(node))
				return false;
		}
		return true;
	}

	/**
	 * (Used for Bidirectional Searches)
	 * Looks if the given node has already occurred in
	 * the previous states of the opposite search direction
	 * @param node
	 * @param inThisList
	 * @param type ("e" if you are checking form the end node, "s" if you are checking form start node)
	 * @return false if the node haven't occurred and calls OnFinalNode if it finds match.
	 */
	public boolean IsItRepetive(Node node, List<Node> inThisList, String type) {
		for (int i = 0; i < inThisList.size(); i++) {
			if (inThisList.get(i).getState().blankTile.M == node.getState().blankTile.M)
				if (inThisList.get(i).getState().blankTile.N == node.getState().blankTile.N) {
					this.OnFinalNode(node, inThisList.get(i), type);
					return true;
				}
		}
		return false;
	}
}
