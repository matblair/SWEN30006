package scoringsystem;

public class VelocityAchievement extends Achievement{

	private int levelId;
	private final static String actype="velocity";

	
	public VelocityAchievement(String name, String description,
			boolean unlocked, String imgID, int levelId, float targetvel) {
		super(name, description, unlocked, imgID,actype,levelId,targetvel);
	}

	@Override
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

}
