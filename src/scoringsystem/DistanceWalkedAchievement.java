package scoringsystem;

public class DistanceWalkedAchievement extends Achievement{
	
	/** The ac type for this particular achievement **/
	private final static String actype="distwalked";

	/** The constructor for this particular achievement, passes on achievement 
	 * type and the information from the resource loader 
	 * @param name
	 * @param description
	 * @param unlocked
	 * @param imgID
	 * @param levelid
	 * @param cubeTarget
	 * @param persistant
	 */
	public DistanceWalkedAchievement(String name, String description,
			boolean unlocked, String imgID, int levelId, float distWalked,boolean persistant) {
		super(name, description, unlocked, imgID, actype,levelId,distWalked,persistant);
	}

	@Override
	/** Check if the achievement has reached it's particular target
	 * by reading from the level stats
	 * 
	 * @param stats The level stats from glados
	 */
	public boolean checkUnlock(LevelStats stats) {
		boolean isUnlocked=false;
		
		if(levelId==-1){
			if(stats.getDistWalked()/1000>=target){
				unlockAchievement();
				isUnlocked=true;

			}
		} else if (levelId==stats.getLevelID() && stats.getDistWalked()/1000>=target){
			unlockAchievement();
			isUnlocked=true;

		}
		
		return isUnlocked;
	}

	@Override
	/** Handle the stats decrementation specific to the target of this
	 *  particular achievement
	 *  @param stats The level stats to check agains
	 *   
	 */	public void decrementStats(LevelStats stats) {
		setDiff(stats.getDistWalked()-getDiff());
		this.target=this.target-getDiff();
		setDiff(stats.getDistWalked()+getDiff());
	}
}
