package scoringsystem;

public class CubesAchievement extends Achievement{
	
	private final static String actype="cubesused";
	public CubesAchievement(String name, String description, boolean unlocked,
			String imgID, int levelid, float cubeTarget) {
		super(name, description, unlocked, imgID,actype,levelid, cubeTarget);
		setLevelId(levelid);
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

}
