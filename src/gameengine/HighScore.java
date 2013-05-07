package gameengine;

public class HighScore {

	private float score;
	private int levelid;
	private String name;
	
	public HighScore (String setname, float setscore, int setlevelid) {
		score = setscore;
		levelid = setlevelid;
		name = setname;
	}
}
