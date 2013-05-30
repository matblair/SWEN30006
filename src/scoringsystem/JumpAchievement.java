package scoringsystem;

public class JumpAchievement extends Achievement {

	/** The ac type for this particular achievement **/
	private final static String actype="jumps";

	/** The constructor for this particular achievement, passes on achievement 
	 * type and the information from the resource loader 
	 * @param name
	 * @param description
	 * @param unlocked
	 * @param imgID
	 * @param levelId
	 * @param target
	 * @param persistant
	 */
	public JumpAchievement(String name, String description, boolean unlocked,
			String imgID, int levelId, float target,boolean persistant) {
		super(name, description, unlocked, imgID,actype,levelId, target,persistant);
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
			if(stats.getJumps()>=target){
				unlockAchievement();
				isUnlocked=true;
			}
		} else if (levelId==stats.getLevelID() && stats.getJumps()>=target){
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
		setDiff(stats.getJumps()-getDiff());
		this.target=this.target-getDiff();
		setDiff(stats.getJumps()+getDiff());
	}


}
