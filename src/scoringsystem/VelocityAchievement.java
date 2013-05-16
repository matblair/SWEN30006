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
	public void checkUnlock(LevelStats stats) {
		if(levelId==-1){
			if(stats.getMaxVelocity()>=targetVel){
				unlockAchievement();
			}
		} else if (levelId==stats.getLevelID() && stats.getMaxVelocity()>=targetVel){
			unlockAchievement();
		}			
				
	}

}
