package scoringsystem;

public class CubesAchievement extends Achievement{
	
	/** The ac type for this particular achievement **/
	private final static String actype="cubesused";
	
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
	public CubesAchievement(String name, String description, boolean unlocked,
			String imgID, int levelid, float cubeTarget, boolean persistant) {
		super(name, description, unlocked, imgID,actype,levelid, cubeTarget,persistant);
	}


	@Override
	public boolean checkUnlock(LevelStats stats) {
		boolean isUnlocked=false;
		
		if(getLevelId()==-1){
			if(stats.getCubesPickedUp()>=target){
				unlockAchievement();
				isUnlocked=true;
			}
		} else if (getLevelId()==stats.getLevelID() && stats.getCubesPickedUp()>=target){
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
		setDiff(stats.getCubesPickedUp()-getDiff());
		this.target=this.target-getDiff();
		setDiff(stats.getCubesPickedUp()+getDiff());
	}

}
