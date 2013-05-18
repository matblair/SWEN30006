package scoringsystem;

public class LevelStats {

	private int timeInLevel=0;
	private int levelID;
	private int numberPortals=0;
	private float distFallen=0;
	private float timeFallen=0;
	private float jumps=0;
	private float maxVelocity=0;
	private float distWalked=0;
	private float cubesPickedUp=0;
	private int achievementsUnlocked=0;
	
	public LevelStats(int levelid) {
		this.levelID=levelid;
	}

	public int getTimeInLevel() {
		return timeInLevel;
	}

	public void setTimeInLevel(int timeInLevel) {
		this.timeInLevel = timeInLevel;
	}

	public int getLevelID() {
		return levelID;
	}

	public void setLevelID(int levelID) {
		this.levelID = levelID;
	}

	public void setNumberPortals(int numberPortals) {
		this.numberPortals = numberPortals;
	}

	public float getDistFallen() {
		return distFallen;
	}

	public void setDistFallen(float distFallen) {
		this.distFallen = distFallen;
	}

	public float getMaxVelocity() {
		return maxVelocity;
	}

	public void setMaxVelocity(float maxVelocity) {
		this.maxVelocity = maxVelocity;
	}

	public float getDistWalked() {
		return distWalked;
	}

	public void setDistWalked(float distWalked) {
		this.distWalked = distWalked;
	}

	public float getCubesPickedUp() {
		return cubesPickedUp;
	}

	public void setCubesPickedUp(float cubesPickedUp) {
		this.cubesPickedUp = cubesPickedUp;
	}
	
	public int getNumberPortals() {
		return numberPortals;
	}

	public int getAchievementsUnlocked() {
		return achievementsUnlocked;
	}

	public void setAchievementsUnlocked(int i) {
		achievementsUnlocked=i;
	}

	public float getJumps() {
		return jumps;
	}

	public void setJumps(float jumps) {
		this.jumps = jumps;
	}

	public float getTimeFallen() {
		return timeFallen;
	}

	public void setTimeFallen(float timeFallen) {
		this.timeFallen = timeFallen;
	}
}
