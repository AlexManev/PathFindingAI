
public class Search {
	public Algorithm _searchAlgorithm;
	public Search(String[] args ){
		_searchAlgorithm = null;
		Init(args);
	}
	private void Init(String[] inTerminal)
	{
		String speed = "20";
		if(inTerminal.length > 2)
			speed = inTerminal[2];
			
		System.out.print(inTerminal[0].toLowerCase()+ " " +inTerminal[1].toUpperCase() +" ");
		if(inTerminal[1].toLowerCase().equals("as"))
			_searchAlgorithm = new BestFirstAlgorithm(inTerminal[0],SearchType.AS,speed);
		else if (inTerminal[1].toLowerCase().equals("gbfs"))
			_searchAlgorithm = new BestFirstAlgorithm(inTerminal[0],SearchType.GBFS,speed);
		else if (inTerminal[1].toLowerCase().equals("bfs"))
			_searchAlgorithm = new BlindSearchAlgorithm(inTerminal[0],SearchType.BFS,speed);
		else if (inTerminal[1].toLowerCase().equals("dfs"))
			_searchAlgorithm = new BlindSearchAlgorithm(inTerminal[0],SearchType.DFS,speed);
		else if (inTerminal[1].toLowerCase().equals("bdbfs")) {
			System.out.print("(Bi-Directional BFS) ");
			_searchAlgorithm = new BidirectionalAlgorithm(inTerminal[0],SearchType.BDBFS,speed);
		}else if(inTerminal[1].toLowerCase().equals("bddfs")){
			System.out.print("(Bi-Directional DFS) ");
			_searchAlgorithm = new BidirectionalAlgorithm(inTerminal[0],SearchType.BDDFS,speed);
		}else if(inTerminal[1].toLowerCase().equals("bdas")){
			System.out.print("(Bi-Directional A*) ");
			_searchAlgorithm = new BidirecionalInformedSearch(inTerminal[0],SearchType.BDAS,speed);
		}else if(inTerminal[1].toLowerCase().equals("bdgbfs")){
			System.out.print("(Bi-Directional Greedy-best-first) ");
			_searchAlgorithm = new BidirecionalInformedSearch(inTerminal[0],SearchType.BDGBFS,speed);
		}else if(inTerminal[1].toLowerCase().equals("ids")){
			System.out.print("(Iterative Deepening DFS) ");
			_searchAlgorithm = new IDS(inTerminal[0],SearchType.DFS,speed);
		}else if(inTerminal[1].toLowerCase().equals("ida")){
			System.out.print("(Iterative Deepening A*) ");
			_searchAlgorithm = new IDA(inTerminal[0],SearchType.AS,speed);
		}
			
	}
	
	public void Start(){
		if(_searchAlgorithm != null)
			_searchAlgorithm.StartSearch();
		else
			System.out.println("Incorrect Search Algorithm. Try again.");
	}

}
