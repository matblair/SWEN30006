package scoringsystem;

public class NumberOfPortalsAchievement extends Achievement{
	private final static String actype="portals";

	
	public NumberOfPortalsAchievement(String name, String description,
			boolean unlocked, String imgID, int levelId, float portals,boolean persistant) {
		super(name, description, unlocked, imgID,actype,levelId,portals,persistant);
	}


	@Override
	public boolean checkUnlock(LevelStats stats) {
		boolean isUnlocked=false;

		
		if(levelId==-1){
			if(stats.getNumberPortals()>=target){
				unlockAchievement();
				isUnlocked=true;

			}
		} else if (levelId==stats.getLevelID() && stats.getNumberPortals()>=target){
			unlockAchievement();
			isUnlocked=true;

		}
		
		return isUnlocked;
	}
	
	@Override
	public void decrementStats(LevelStats stats) {
		setDiff(stats.getNumberPortals()-getDiff());
		this.target=this.target-getDiff();
		setDiff(stats.getNumberPortals()+getDiff());
	}

}
