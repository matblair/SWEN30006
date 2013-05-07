package gameengine;

public class HighScore {

	private float score;
	private int levelid;
	private String name;
	
	public HighScore (String setname, float setscore, int setlevelid) {
		setScore(setscore);
		setLevelid(setlevelid);
		setName(setname);
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the levelid
	 */
	public int getLevelid() {
		return levelid;
	}

	/**
	 * @param levelid the levelid to set
	 */
	public void setLevelid(int levelid) {
		this.levelid = levelid;
	}

	/**
	 * @return the score
	 */
	public float getScore() {
		return score;
	}

	/**
	 * @param score the score to set
	 */
	public void setScore(float score) {
		this.score = score;
	}
}
