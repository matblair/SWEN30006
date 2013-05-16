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
	public void checkUnlock(LevelStats stats) {
		if(levelId==-1){
			if(stats.getNumberPortals()>=portalsCreated){
				unlockAchievement();
			}
		} else if (levelId==stats.getLevelID() && stats.getNumberPortals()>=portalsCreated){
			unlockAchievement();
		}			
	}

}
