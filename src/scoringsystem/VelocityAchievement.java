package scoringsystem;

public class VelocityAchievement extends Achievement{

	private float targetVel;
	private int levelId;
	
	public VelocityAchievement(String name, String description,
			boolean unlocked, String imgID, int levelid, int targetvel) {
		super(name, description, unlocked, imgID);
		this.levelId=levelid;
		this.targetVel=targetvel;
	}

	@Override
	public boolean checkUnlock(LevelStats stats) {
		boolean isUnlocked=false;
		
		if(levelId==-1){
			if(stats.getMaxVelocity()>=targetVel){
				unlockAchievement();
				isUnlocked=true;

			}
		} else if (levelId==stats.getLevelID() && stats.getMaxVelocity()>=targetVel){
			unlockAchievement();
			isUnlocked=true;
		}	
		
		return isUnlocked;
	}

}
