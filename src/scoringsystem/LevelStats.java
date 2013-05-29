package scoringsystem;

/** A basic container for the level stats
 * that can be passed between various game objects
 * @author mat
 */

public class LevelStats {

	/** Time spent in the level so far **/
	private int timeInLevel=0;
	/** The level id these stats pertain to **/
	private int levelID;
	/** The number of portals created in the level **/
	private int numberPortals=0;
	/** The total distance fallen so far **/
	private float distFallen=0;
	/** The total time spent falling so far **/
	private float timeFallen=0;
	/** The number of jumps we have made so far **/
	private float jumps=0;
	/** The max velocity we have reached **/
	private float maxVelocity=0;
	/** The total distance we have walked so far **/
	private float distWalked=0;
	/** The number of cubes we have picked up so far **/
	private float cubesPickedUp=0;
	/** The number of achievements we have unlocked so far **/
	private int achievementsUnlocked=0;
	
	/** Initialise the level stats by setting the correct level id
	 * 
	 * @param levelid
	 */
	public LevelStats(int levelid) {
		this.levelID=levelid;
	}

	/** Get the time in level so far
	 * 
	 * @return timeInLevel
	 */
	public int getTimeInLevel() {
		return timeInLevel;
	}

	/** Set the time in level to update to current interval
	 * 
	 * @param timeInLevel
	 */
	public void setTimeInLevel(int timeInLevel) {
		this.timeInLevel = timeInLevel;
	}

	/** Get teh level id for achievement checking
	 * 
	 * @return
	 */
	public int getLevelID() {
		return levelID;
	}

	/** Set the level id 
	 * 
	 * @param levelID
	 */
	public void setLevelID(int levelID) {
		this.levelID = levelID;
	}

	/** Set the number of poratls used so far 
	 * 
	 * @param numberPortals
	 */
	public void setNumberPortals(int numberPortals) {
		this.numberPortals = numberPortals;
	}

	/** Get the distance fallen so far
	 * 
	 * @return
	 */
	public float getDistFallen() {
		return distFallen;
	}

	/** Set the distance fallen so far
	 * 
	 * @param distFallen
	 */
	public void setDistFallen(float distFallen) {
		this.distFallen = distFallen;
	}

	/** Get the maximum velocity reached so far
	 * 
	 * @return maxVelocity
	 */
	public float getMaxVelocity() {
		return maxVelocity;
	}

	/** Set the maximum velocity reached so far
	 * 
	 * @param maxVelocity
	 */
	public void setMaxVelocity(float maxVelocity) {
		this.maxVelocity = maxVelocity;
	}

	/** Get the distance walked so far 
	 * 
	 * @return distWalked
	 */
	public float getDistWalked() {
		return distWalked;
	}

	/** Set the distance walked so far
	 * 
	 * @param distWalked
	 */
	public void setDistWalked(float distWalked) {
		this.distWalked = distWalked;
	}

	/** Get teh number of cubes picked up so far 
	 * 
	 * @return cubesPickedUp
	 */
	public float getCubesPickedUp() {
		return cubesPickedUp;
	}

	/** Set the number of cubes picked up so far
	 * 
	 * @param cubesPickedUp
	 */
	public void setCubesPickedUp(float cubesPickedUp) {
		this.cubesPickedUp = cubesPickedUp;
	}
	
	/** Get the number of portals created so far
	 * 
	 * @return numberPortals
	 */
	public int getNumberPortals() {
		return numberPortals;
	}

	/** Get the number of achievements unlocked so far
	 * 
	 * @return achievementsUnlocked
	 */
	public int getAchievementsUnlocked() {
		return achievementsUnlocked;
	}

	/** Set the number of achievements unlocked so far
	 * 
	 * @param i
	 */
	public void setAchievementsUnlocked(int i) {
		achievementsUnlocked=i;
	}

	/** Get the number of jumps made so far
	 * 
	 * @return jumps
	 */
	public float getJumps() {
		return jumps;
	}

	/** Set the number of jumps made so far
	 * 
	 * @param jumps
	 */
	public void setJumps(float jumps) {
		this.jumps = jumps;
	}

	/** Get the total time fallen so far in level
	 * 
	 * @return
	 */
	public float getTimeFallen() {
		return timeFallen;
	}

	/** Set the total time spent falling in level so far
	 * 
	 * @param timeFallen
	 */
	public void setTimeFallen(float timeFallen) {
		this.timeFallen = timeFallen;
	}
}
