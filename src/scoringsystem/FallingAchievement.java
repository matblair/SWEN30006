package scoringsystem;

public class FallingAchievement extends Achievement{

	private float distFallen;
	private int levelId;
	
	public FallingAchievement(String name, String description,
			boolean unlocked, String imgID, int levelid, float distFallen) {
		super(name, description, unlocked, imgID);
		this.distFallen=distFallen;
		this.levelId=levelid;
	}

	@Override
	public boolean checkUnlock(LevelStats stats) {
		boolean isUnlocked=false;

		
		if(levelId==-1){
			if(stats.getDistFallen()>=distFallen){
				unlockAchievement();
				isUnlocked=true;

			}
		} else if (levelId==stats.getLevelID() && stats.getDistFallen()>=distFallen){
			unlockAchievement();
			isUnlocked=true;

		}		
		return isUnlocked;

	}
	

}
