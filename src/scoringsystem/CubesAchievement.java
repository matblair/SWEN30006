package scoringsystem;

public class CubesAchievement extends Achievement{

	private int cubesPickedUp;
	private int levelId;


	public CubesAchievement(String name, String description, boolean unlocked,
			String imgID, int levelid, int cubeTarget) {
		super(name, description, unlocked, imgID);
		cubesPickedUp = cubeTarget;
		levelId = levelid;
	}


	@Override
	public boolean checkUnlock(LevelStats stats) {
		boolean isUnlocked=false;
		
		if(levelId==-1){
			if(stats.getCubesPickedUp()>=cubesPickedUp){
				unlockAchievement();
				isUnlocked=true;
			}
		} else if (levelId==stats.getLevelID() && stats.getCubesPickedUp()>=cubesPickedUp){
			unlockAchievement();
			isUnlocked=true;
		}	
		
		return isUnlocked;
	}

}
