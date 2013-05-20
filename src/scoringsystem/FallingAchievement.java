package scoringsystem;

public class FallingAchievement extends Achievement{
	private final static String actype="distfallen";

	
	public FallingAchievement(String name, String description,
			boolean unlocked, String imgID, int levelId, float distFallen) {
		super(name, description, unlocked, imgID,actype,levelId, distFallen);
	}

	@Override
	public boolean checkUnlock(LevelStats stats) {
		boolean isUnlocked=false;

		
		if(levelId==-1){
			if(stats.getDistFallen()>=target){
				unlockAchievement();
				isUnlocked=true;

			}
		} else if (levelId==stats.getLevelID() && stats.getDistFallen()>=target){
			unlockAchievement();
			isUnlocked=true;

		}		
		return isUnlocked;

	}
	

}
