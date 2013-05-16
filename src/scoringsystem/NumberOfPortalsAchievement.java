package scoringsystem;

public class NumberOfPortalsAchievement extends Achievement{

	private int portalsCreated;
	private int levelId;
	
	public NumberOfPortalsAchievement(String name, String description,
			boolean unlocked, String imgID, int portals, int levelid) {
		super(name, description, unlocked, imgID);
		this.levelId=levelid;
		this.portalsCreated=portals;
	}


	@Override
	public boolean checkUnlock(LevelStats stats) {
		boolean isUnlocked=false;

		
		if(levelId==-1){
			if(stats.getNumberPortals()>=portalsCreated){
				unlockAchievement();
				isUnlocked=true;

			}
		} else if (levelId==stats.getLevelID() && stats.getNumberPortals()>=portalsCreated){
			unlockAchievement();
			isUnlocked=true;

		}
		
		return isUnlocked;
	}
	

}