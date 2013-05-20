package scoringsystem;

public class TimeFallingAchievement extends Achievement{

	private int levelId;
	private final static String actype="timefallen";


	public TimeFallingAchievement(String name, String description, boolean unlocked,
			String imgID, int levelId, float fallTarget) {
		super(name, description, unlocked, imgID,actype,levelId,fallTarget);
	}


	@Override
	public boolean checkUnlock(LevelStats stats) {
		boolean isUnlocked=false;
		
		if(levelId==-1){
			if(stats.getTimeFallen()>=target){
				unlockAchievement();
				isUnlocked=true;
			}
		} else if (levelId==stats.getLevelID() && stats.getTimeFallen()>=target){
			unlockAchievement();
			isUnlocked=true;
		}	
		
		return isUnlocked;
	}

}
