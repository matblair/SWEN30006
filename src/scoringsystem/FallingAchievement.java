package scoringsystem;

public class FallingAchievement extends Achievement{
	private final static String actype="distfallen";

	
	public FallingAchievement(String name, String description,
			boolean unlocked, String imgID, int levelId, float distFallen,boolean persistant) {
		super(name, description, unlocked, imgID,actype,levelId, distFallen,persistant);
	}

	@Override
	public boolean checkUnlock(LevelStats stats) {
		boolean isUnlocked=false;

		
		if(levelId==-1){
			if(stats.getDistFallen()/1000>=target){
				unlockAchievement();
				isUnlocked=true;

			}
		} else if (levelId==stats.getLevelID() && stats.getDistFallen()/1000>=target){
			unlockAchievement();
			isUnlocked=true;

		}		
		return isUnlocked;

	}
	
	@Override
	public void decrementStats(LevelStats stats) {
		setDiff(stats.getDistFallen()-getDiff());
		this.target=this.target-getDiff();
		setDiff(stats.getDistFallen()+getDiff());
	}
	

}
