package scoringsystem;

public class TimingAchievement extends Achievement{

	private int levelId;
	private final static String actype="timelevel";

	
	public TimingAchievement(String name, String description, boolean unlocked,
			String imgID, int levelId, float timetarget) {
		super(name, description, unlocked, imgID,actype,levelId,timetarget);
	}
	
	@Override
	public boolean checkUnlock(LevelStats stats) {
		boolean isUnlocked=false;
		
		if(levelId==-1){
			if(stats.getTimeInLevel()>=target){
				unlockAchievement();
				isUnlocked=true;

			}
		} else if (levelId==stats.getLevelID() && stats.getTimeInLevel()>=target){
			unlockAchievement();
			isUnlocked=true;

		}		
		
		return isUnlocked;
		
	}

}
