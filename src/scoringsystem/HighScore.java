package scoringsystem;

public class HighScore implements Comparable<HighScore>{

	/** The score for this high score **/
	private Float score;
	/** The level on which it occured **/
	private int levelid;
	/** The name of the player **/
	private String name;
	
	/** Construct the high score **/
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

	@Override
	/** Comparing high scores for use in array comparisons
	 *  @param hs The high score to compare to;
	 */
	public int compareTo(HighScore hs) {
		Float score2 = hs.getScore();
		return score.compareTo(score2);
	}
}
