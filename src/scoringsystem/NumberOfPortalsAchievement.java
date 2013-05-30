package scoringsystem;

public class NumberOfPortalsAchievement extends Achievement{

	/** The ac type for this particular achievement **/
	private final static String actype="portals";

	/** The constructor for this particular achievement, passes on achievement 
	 * type and the information from the resource loader 
	 * @param name
	 * @param description
	 * @param unlocked
	 * @param imgID
	 * @param levelId
	 * @param portals
	 * @param persistant
	 */
	public NumberOfPortalsAchievement(String name, String description,
			boolean unlocked, String imgID, int levelId, float portals,boolean persistant) {
		super(name, description, unlocked, imgID,actype,levelId,portals,persistant);
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
			if(stats.getNumberPortals()>=target){
				unlockAchievement();
				isUnlocked=true;

			}
		} else if (levelId==stats.getLevelID() && stats.getNumberPortals()>=target){
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
		setDiff(stats.getNumberPortals()-getDiff());
		this.target=this.target-getDiff();
		setDiff(stats.getNumberPortals()+getDiff());
	}

}
