package scoringsystem;

public class VelocityAchievement extends Achievement{

	/** The ac type for this particular achievement **/
	private final static String actype="velocity";

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
	public VelocityAchievement(String name, String description,
			boolean unlocked, String imgID, int levelId, float targetvel,boolean persistant) {
		super(name, description, unlocked, imgID,actype,levelId,targetvel,persistant);
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
			if(stats.getMaxVelocity()>=target){
				unlockAchievement();
				isUnlocked=true;

			}
		} else if (levelId==stats.getLevelID() && stats.getMaxVelocity()>=target){
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
	 */
	public void decrementStats(LevelStats stats) {
		setDiff(stats.getMaxVelocity()-getDiff());
		this.target=this.target-getDiff();
		setDiff(stats.getMaxVelocity()+getDiff());
	}
}
