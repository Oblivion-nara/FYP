package mokapot2;

import java.util.ArrayList;

public class ASearch {

	private Track track;
	private ArrayList<PointHeuristic> moves;
	
	private
	
	public ASearch(Track track){
		this.track = track;
		
	}
	
	public ArrayList<PointHeuristic> getMoves(){
		return moves;
	}
	
}
