package scoringsystem;

public class TimingAchievement extends Achievement{

	private float timeTarget;
	private int levelId;
	
	public TimingAchievement(String name, String description, boolean unlocked,
			String imgID, int levelid, float timetarget) {
		super(name, description, unlocked, imgID);
		this.timeTarget=timetarget;
		this.levelId=levelid;
	}
	
	@Override
	public boolean checkUnlock(LevelStats stats) {
		boolean isUnlocked=false;
		
		if(levelId==-1){
			if(stats.getTimeInLevel()>=timeTarget){
				unlockAchievement();
				isUnlocked=true;

			}
		} else if (levelId==stats.getLevelID() && stats.getTimeInLevel()>=timeTarget){
			unlockAchievement();
			isUnlocked=true;

		}		
		
		return isUnlocked;
		
	}

}
